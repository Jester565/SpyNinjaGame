package impl;

import java.awt.Color;
import java.util.ArrayList;

import edu.cpp.cs.cs141.final_proj.Grid;
import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.GameEngine;
import edu.cpp.cs.cs141.final_proj.GameEngine.GAME_OBJECT_TYPE;
import edu.cpp.cs.cs141.final_proj.GameEngine.GAME_STATE;
import impl.Room.WALL_TYPES;
import jgraphic.Sound;

public class RoomManager {
	public static int ROOMS_SIZE = Grid.GRID_SIZE;
	private static int ROOM_RENDER_DISTANCE = 3;
	
	private static final float BOUNDS_OFF = Room.ROOM_W;
	private Color boundColor = new Color(.4f, .4f, .4f, 1);
	private static final float ENGINE_W = 1500;
	
	private static GameImage EngineImg1;
	private float engineTimer = 0;
	private static final float MAX_ENGINE_TIME = 180;
	
	
	private boolean moveTurn = false;
	private float winRoomX = 0;
	private float winRoomY = 0;
	private ArrayList <ForceWave> forceWaves;
	private ArrayList <Alien> aliens;
	
	private static Sound LightSound;

	private ArrayList <ArrayList<Room>> rooms;
	private GameCore core;
	private int lastPlayerCol;
	private int lastPlayerRow;
	private boolean isShooting = false;
	private float engineTimerChange = 1;
	
	public RoomManager(GameCore core)
	{
		this.core = core;
		if (LightSound == null)
		{
			LightSound = new Sound("./resources/Sounds/light.wav");
			EngineImg1 = new GameImage(core);
			EngineImg1.init("./resources/Imgs/Structure/stationEngine1.png");
		}
		reset();
	}
	
	public void reset()
	{
		moveTurn = false;
		isShooting = false;
		aliens = new ArrayList <Alien>();
		forceWaves = new ArrayList <ForceWave>();
		GAME_OBJECT_TYPE[][] gameTypesMatrix = core.getGameEngine().getGridTypes();
		float x = 0;
		rooms = new ArrayList<ArrayList<Room>>();
		for (int i = 0; i < ROOMS_SIZE; i++)
		{
			float y = 0;
			rooms.add(new ArrayList<Room>());
			for (int j = 0; j < ROOMS_SIZE; j++)
			{
				if (gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.PLAYER)
				{
					lastPlayerCol = i;
					lastPlayerRow = j;
				}
				Room room = null;
				if (gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.INVINCIBILITY)
				{
					room = new ItemRoom(core, new ShieldGenerator(core), x, y, 0, 0, 1);
				}
				else if (gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.BULLET)
				{
					room = new ItemRoom(core, new Bullet(core), x, y, 0, 1, 0);
				}
				else if (gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.RADAR)
				{
					room = new ItemRoom(core, new RadarDish(core), x, y, 1, 0, 0);
				}
				else if (gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.ROOM || gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.BRIEFCASE)
				{
					if (gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.BRIEFCASE)
					{
						winRoomX = i * Room.ROOM_W + Room.ROOM_W/2;
						winRoomY = j * Room.ROOM_W + Room.ROOM_W/2;
					}
					room = new ServerRoom(core, x, y);
				}
				else
				{
					room = new Room(core, x, y);
				}
				//Handles creating the left wall
				if (i == 0)
				{
					room.setLeftWall(WALL_TYPES.SOLID);
				}
				else
				{
					room.setLeftWall(rooms.get(i - 1).get(j).getRightWall());
				}
				
				//Handles creating the upper wall
				if (j == 0)
				{
					room.setUpWall(WALL_TYPES.SOLID);
				}
				else
				{
					room.setUpWall(rooms.get(i).get(j - 1).getDownWall());
				}
				
				//Handles creating the right wall
				if (i == ROOMS_SIZE - 1 || gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.BRIEFCASE || gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.ROOM
						|| gameTypesMatrix[i + 1][j] == GAME_OBJECT_TYPE.BRIEFCASE || gameTypesMatrix[i + 1][j] == GAME_OBJECT_TYPE.ROOM)
				{
					room.setRightWall(WALL_TYPES.SOLID);
				}
				else
				{
					room.setRightWall(WALL_TYPES.DOOR);
				}
				
				//Handles creating the down wall
				if (j == ROOMS_SIZE - 1 || gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.BRIEFCASE || gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.ROOM)
				{
					room.setDownWall(WALL_TYPES.SOLID);
				}
				else if (gameTypesMatrix[i][j + 1] == GAME_OBJECT_TYPE.BRIEFCASE || gameTypesMatrix[i][j + 1] == GAME_OBJECT_TYPE.ROOM)
				{
					room.setDownWall(WALL_TYPES.ROOM_DOOR);
				}
				else
				{
					room.setDownWall(WALL_TYPES.DOOR);
				}
				
				rooms.get(i).add(room);
				y += Room.ROOM_W;
			}
			
			x += Room.ROOM_W;
		}
		updateAliens();
		updateVisibility();
	}
	
	public void refreshLastPlayerRoomIndex()
	{
		lastPlayerCol = getPlayerRoomColumn();
		lastPlayerRow = getPlayerRoomRow();
	}
	
	public float getLastPlayerRoomColumn()
	{
		return lastPlayerCol;
	}
	
	public float getLastPlayerRoomRow()
	{
		return lastPlayerRow;
	}
	public Room getRoom(float x, float y)
	{
		int column = (int)((x) / Room.ROOM_W);
		if (column < 0)
		{
			return null;
		}
		else if (column >= ROOMS_SIZE)
		{
			return null;
		}
		int row = (int)((y) / Room.ROOM_W);
		if (row < 0)
		{
			return null;
		}
		else if (row >= ROOMS_SIZE)
		{
			return null;
		}
		return rooms.get(column).get(row);
	}
	
	public int getPlayerRoomColumn()
	{
		int column = (int)((core.getPlayer().getX() + core.getPlayer().getW() / 2) / Room.ROOM_W);
		if (column < 0)
		{
			column = 0;
		}
		else if (column >= ROOMS_SIZE)
		{
			column = ROOMS_SIZE - 1;
		}
		return column;
	}
	
	public boolean playerOnLeftSide()
	{
		return (core.getPlayer().getX() + core.getPlayer().getW()/2) % Room.ROOM_W <= Room.ROOM_W/2.0f;
	}
	
	public boolean playerOnUpSide()
	{
		return (core.getPlayer().getY() + core.getPlayer().getH()/2) % Room.ROOM_W <= Room.ROOM_W/2.0f;
	}
	
	public int getPlayerRoomRow()
	{
		int row = (int)((core.getPlayer().getY() + core.getPlayer().getH()/2) / Room.ROOM_W);
		if (row < 0)
		{
			row = 0;
		}
		else if (row >= ROOMS_SIZE)
		{
			row = ROOMS_SIZE - 1;
		}
		return row;
	}
	
	public void draw()
	{
		if (GameEngine.DebugMode)
		{
			renderAll();
		}
		else
		{
			render();
		}
		if (core.getGameEngine().getSpy().isAlive())
		{
			if (!isShooting)
			{
				if (!moveTurn)
				{
					handleLookTurn();
					rooms.get(lastPlayerCol).get(lastPlayerRow).drawPlayerRoom(true);
				}
				else
				{
					handleMoveTurn();
					rooms.get(lastPlayerCol).get(lastPlayerRow).drawPlayerRoom(false);
				}
			}
			else
			{
				rooms.get(lastPlayerCol).get(lastPlayerRow).drawPlayerRoom(true);
				ForceWave forceWave = rooms.get(lastPlayerCol).get(lastPlayerRow).createForceWave();
				if (forceWave != null)
				{
					isShooting = false;
					core.getGameEngine().playerShoot(forceWave.getWaveDirection());
					setDeadAlienBodys();
					core.getHUD().checkItems();
					core.getGameEngine().resetVisibility();
					forceWaves.add(forceWave);
					precedeMoveTurnCall();
					checkPlayerDeath();
				}
			}
		}
		else
		{
			rooms.get(lastPlayerCol).get(lastPlayerRow).drawPlayerRoom(true);
		}
		core.getPlayer().draw(0, 0);
		if (GameEngine.DebugMode)
		{
			renderBlockAll();
		}
		else
		{
			renderBlock();
		}
		drawForceWaves();
		
		core.getShapeRenderer().drawRect((-BOUNDS_OFF + 30 - core.gameX) * core.gameScaleX, (-BOUNDS_OFF - core.gameY) * core.gameScaleY, (BOUNDS_OFF) * core.gameScaleX, (Room.ROOM_W * ROOMS_SIZE + BOUNDS_OFF * 2) * core.gameScaleY, boundColor);
		core.getShapeRenderer().drawRect((Room.ROOM_W * ROOMS_SIZE - core.gameX) * core.gameScaleX, (-BOUNDS_OFF - core.gameY) * core.gameScaleY, (BOUNDS_OFF) * core.gameScaleX, (Room.ROOM_W * ROOMS_SIZE + BOUNDS_OFF * 2) * core.gameScaleY, boundColor);
		core.getShapeRenderer().drawRect((0 - core.gameX) * core.gameScaleX, (-BOUNDS_OFF - core.gameY) * core.gameScaleY, (Room.ROOM_W * ROOMS_SIZE) * core.gameScaleX, (BOUNDS_OFF) * core.gameScaleY, boundColor);
		core.getShapeRenderer().drawRect((0 - core.gameX) * core.gameScaleX, (Room.ROOM_W * ROOMS_SIZE - core.gameY) * core.gameScaleY, (Room.ROOM_W * ROOMS_SIZE) * core.gameScaleX, (BOUNDS_OFF) * core.gameScaleY, boundColor);
		
		//RightStationImg.draw(Room.ROOM_W * ROOMS_SIZE, -Room.ROOM_W, STATION_WALL_W, Room.ROOM_W * ROOMS_SIZE + Room.ROOM_W * 2);
		//LeftStationImg.draw(-STATION_WALL_W, -Room.ROOM_W, STATION_WALL_W, Room.ROOM_W * ROOMS_SIZE + Room.ROOM_W * 2);
		//UpStationImg.draw(-Room.ROOM_W, -STATION_WALL_W, Room.ROOM_W * ROOMS_SIZE + Room.ROOM_W * 2, STATION_WALL_W);
		//DownStationImg.draw(-Room.ROOM_W, Room.ROOM_W * ROOMS_SIZE, Room.ROOM_W * ROOMS_SIZE + Room.ROOM_W * 2, STATION_WALL_W);
		
		engineTimer += core.rate * engineTimerChange;
		if (engineTimer > MAX_ENGINE_TIME)
		{
			engineTimer = MAX_ENGINE_TIME;
			engineTimerChange *= -1;
		}
		if (engineTimer < 0)
		{
			engineTimer = 0;
			engineTimerChange *= -1;
		}
		drawStationEngine((Room.ROOM_W * ROOMS_SIZE) + ENGINE_W/2, (Room.ROOM_W * ROOMS_SIZE) + ENGINE_W/2);
		drawStationEngine(-ENGINE_W/2, (Room.ROOM_W * ROOMS_SIZE) + ENGINE_W/2);
		drawStationEngine((Room.ROOM_W * ROOMS_SIZE) + ENGINE_W/2, -ENGINE_W/2);
		drawStationEngine(-ENGINE_W/2, -ENGINE_W/2);
	}
	
	private void drawStationEngine(float x, float y)
	{
		EngineImg1.draw(x - ENGINE_W/2, y - ENGINE_W/2, ENGINE_W, ENGINE_W);
		core.getShapeRenderer().drawCircle((x - core.gameX) * core.gameScaleX, (y - core.gameY) * core.gameScaleY, ((engineTimer/MAX_ENGINE_TIME) * ENGINE_W/3) * (core.gameScaleX + core.gameScaleY)/2, 1, 0, 0, 1);
		core.getShapeRenderer().drawCircle((x - core.gameX) * core.gameScaleX, (y - core.gameY) * core.gameScaleY, ((1 - engineTimer/MAX_ENGINE_TIME) * ENGINE_W/3) * core.gameScaleX, .5f, 0, 0, 1);
	}
	
	private void setDeadAlienBodys()
	{
		boolean alienKilled = false;
		GAME_OBJECT_TYPE[][] gameTypesMatrix = core.getGameEngine().getGridTypes();
		for (int i = 0; i < aliens.size(); i++)
		{
			int gridI = (int) (aliens.get(i).getRoomOccupied().x / Room.ROOM_W);
			int gridJ = (int) (aliens.get(i).getRoomOccupied().y / Room.ROOM_W);
			if (gameTypesMatrix[gridI][gridJ] != GAME_OBJECT_TYPE.ENEMY)
			{
				AlienDeadBody alienBody = new AlienDeadBody(core, aliens.get(i).getRoomOccupied());
				alienBody.setXY(aliens.get(i).getX(), aliens.get(i).getY());
				aliens.get(i).getRoomOccupied().addOccupant(alienBody);
				alienKilled = true;
			}
		}
		if (alienKilled)
		{
			Alien.PlayDeathSound();
		}
	}
	
	private void drawForceWaves()
	{
		for (int i = 0; i < forceWaves.size(); i++)
		{
			forceWaves.get(i).draw();
			if (forceWaves.get(i).checkCollision())
			{
				forceWaves.remove(i);
				i--;
			}
		}
	}
	
	private void handleLookTurn()
	{
		int playerCol = getPlayerRoomColumn();
		int playerRow = getPlayerRoomRow();
		DIRECTION dir = rooms.get(playerCol).get(playerRow).manageButtons();
		if (dir != null)
		{
			LightSound.stopAndReset();
			LightSound.play();
			core.getGameEngine().playerLook(dir);
			updateVisibility();
			moveTurn = true;
		}
	}
	
	private void precedeMoveTurnCall()
	{
		updateVisibility();
		moveTurn = false;
		if (core.getGameEngine().useSpyPowerup() && rooms.get(lastPlayerCol).get(lastPlayerRow) instanceof ItemRoom)
		{
			((ItemRoom)rooms.get(lastPlayerCol).get(lastPlayerRow)).setUsed(true);
		}
		core.getGameEngine().enemyAttack();
		if (core.getGameEngine().getSpy().isAlive())
		{
			core.getGameEngine().enemyMove();
		}
		updateAliens();
		core.getGameEngine().updateSpyPowerups();
	}
	
	private void handleMoveTurn()
	{
		int playerCol = getPlayerRoomColumn();
		int playerRow = getPlayerRoomRow();
		if (core.getGameEngine().getSpy().getGun().getNumRounds() <= 0 && !core.getGameEngine().playerMoveable())
		{
			core.getGameEngine().resetVisibility();
			precedeMoveTurnCall();
			checkPlayerDeath();
		}
		DIRECTION terminalDirection = rooms.get(lastPlayerCol).get(lastPlayerRow).checkTerminalUsed();
		if (terminalDirection != null)
		{
			core.getGameEngine().resetVisibility();
			core.getHUD().initiateTerminal();
			core.getGameEngine().playerMove(terminalDirection);
			precedeMoveTurnCall();
		}
		else if (playerCol == lastPlayerCol && playerRow == lastPlayerRow)
		{
			if (core.getGameEngine().getSpy().getGun().getNumRounds() > 0 && !core.getHUD().inMenu())
			{
				DIRECTION shootDirection = rooms.get(playerCol).get(playerRow).manageShoot();
				if (shootDirection != null)
				{
					isShooting = true;
					core.getPlayer().setShoot(true);
				}
			}
		}
		else
		{
			if (!rooms.get(playerCol).get(playerRow).isLeaveable())
			{
				core.getGameEngine().resetVisibility();
				DIRECTION moveDir = null;
				if (playerCol < lastPlayerCol)
				{
					moveDir = DIRECTION.LEFT;
				}
				else if (playerCol > lastPlayerCol)
				{ 
					moveDir = DIRECTION.RIGHT;
				}
				else if (playerRow < lastPlayerRow)
				{
					moveDir = DIRECTION.UP;
				}
				else if (playerRow > lastPlayerRow)
				{
					moveDir = DIRECTION.DOWN;
				}
				else
				{
					System.out.println("INVALID MOVE");
				}
				core.getGameEngine().playerMove(moveDir);
				lastPlayerCol = playerCol;
				lastPlayerRow = playerRow;
				precedeMoveTurnCall();
				checkPlayerDeath();
			}
		}
	}
	
	public void checkPlayerDeath()
	{
		if (!core.getGameEngine().getSpy().isAlive())
		{
			rooms.get(lastPlayerCol).get(lastPlayerRow).addOccupant(new PlayerDeadBody(core, rooms.get(lastPlayerCol).get(lastPlayerRow)));
		}
	}
	
	public void updateVisibility()
	{
		boolean[][] visibleGrid = core.getGameEngine().getGridVisibility();
		for (int i = 0; i < ROOMS_SIZE; i++)
		{
			for (int j = 0; j < ROOMS_SIZE; j++)
			{
				rooms.get(i).get(j).setVisibility(visibleGrid[i][j]);
			}
		}
	}
	
	private void renderBlockAll()
	{
		for (int i = 0; i < ROOMS_SIZE; i++)
		{
			for (int j = 0; j < ROOMS_SIZE; j++)
			{
				rooms.get(i).get(j).drawBlockLayer();
			}
		}
	}
	
	private void renderAll()
	{
		for (int i = 0; i < ROOMS_SIZE; i++)
		{
			for (int j = 0; j < ROOMS_SIZE; j++)
			{
				rooms.get(i).get(j).draw();
			}
		}
	}
	
	private void render()
	{
		int playerColumn = getPlayerRoomColumn();
		int playerColumnLowBound = playerColumn - ROOM_RENDER_DISTANCE + 1;
		int playerColumnHighBound = playerColumn + ROOM_RENDER_DISTANCE;
		if (playerOnLeftSide())
		{
			playerColumnLowBound--;
		}
		else
		{
			playerColumnHighBound++;
		}
		if (playerColumnLowBound < 0)
		{
			playerColumnLowBound = 0;
		}
		if (playerColumnHighBound > ROOMS_SIZE)
		{
			playerColumnHighBound = ROOMS_SIZE;
		}
		
		int playerRow = getPlayerRoomRow();
		int playerRowLowBound = playerRow - ROOM_RENDER_DISTANCE + 1;
		int playerRowHighBound = playerRow + ROOM_RENDER_DISTANCE;
		if (playerOnUpSide())
		{
			playerRowLowBound--;
		}
		else
		{
			playerRowHighBound++;
		}
		if (playerRowLowBound < 0)
		{
			playerRowLowBound = 0;
		}
		if (playerRowHighBound > ROOMS_SIZE)
		{
			playerRowHighBound = ROOMS_SIZE;
		}
		//int playerRowUpBound = 
		for (int i = playerColumnLowBound; i < playerColumnHighBound; i++)
		{
			for (int j = playerRowLowBound; j < playerRowHighBound; j++)
			{
				rooms.get(i).get(j).draw();
			}
		}
	}
	
	public float getWinRoomX()
	{
		return winRoomX;
	}
	
	public float getWinRoomY()
	{
		return winRoomY;
	}
	
	public void drawRoof()
	{
		for (int i = 0; i < ROOMS_SIZE; i++)
		{
			for (int j = 0; j < ROOMS_SIZE; j++)
			{
				rooms.get(i).get(j).drawRoof();
			}
		}
	}
	
	public void updateAliens()
	{
		for (int i = 0; i < aliens.size(); i++)
		{
			aliens.get(i).getRoomOccupied().removeOccupant(aliens.get(i));
		}
		aliens.clear();
		GAME_OBJECT_TYPE[][] gameTypesMatrix = core.getGameEngine().getGridTypes();
		for (int i = 0; i < ROOMS_SIZE; i++)
		{
			for (int j = 0; j < ROOMS_SIZE; j++)
			{
				Room room = rooms.get(i).get(j);
				if (gameTypesMatrix[i][j] == GAME_OBJECT_TYPE.ENEMY)
				{
					Alien alien = new Alien(core, room);
					alien.setXY(room.getX() + Room.ROOM_W/2, room.getY() + Room.ROOM_W/2);
					rooms.get(i).get(j).addOccupant(alien);
					aliens.add(alien);
				}
			}
		}
	}
	
	private void renderBlock()
	{
		int playerColumn = getPlayerRoomColumn();
		int playerColumnLowBound = playerColumn - ROOM_RENDER_DISTANCE + 1;
		int playerColumnHighBound = playerColumn + ROOM_RENDER_DISTANCE;
		if (playerOnLeftSide())
		{
			playerColumnLowBound--;
		}
		else
		{
			playerColumnHighBound++;
		}
		if (playerColumnLowBound < 0)
		{
			playerColumnLowBound = 0;
		}
		if (playerColumnHighBound > ROOMS_SIZE)
		{
			playerColumnHighBound = ROOMS_SIZE;
		}
		
		int playerRow = getPlayerRoomRow();
		int playerRowLowBound = playerRow - ROOM_RENDER_DISTANCE + 1;
		int playerRowHighBound = playerRow + ROOM_RENDER_DISTANCE;
		if (playerOnUpSide())
		{
			playerRowLowBound--;
		}
		else
		{
			playerRowHighBound++;
		}
		if (playerRowLowBound < 0)
		{
			playerRowLowBound = 0;
		}
		if (playerRowHighBound > ROOMS_SIZE)
		{
			playerRowHighBound = ROOMS_SIZE;
		}
		//int playerRowUpBound = 
		for (int i = playerColumnLowBound; i < playerColumnHighBound; i++)
		{
			for (int j = playerRowLowBound; j < playerRowHighBound; j++)
			{
				rooms.get(i).get(j).drawBlockLayer();
			}
		}
	}
}
