package impl;

import java.util.ArrayList;

import edu.cpp.cs.cs141.final_proj.GameEngine;
import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

public class Room {
	
	public static enum WALL_TYPES {
		SOLID, DOOR, ROOM_DOOR
	}
	public static DIRECTION[] WallDirections = new DIRECTION[]{DIRECTION.LEFT, DIRECTION.RIGHT, DIRECTION.UP, DIRECTION.DOWN};
	public static float ROOM_W = 700;
	public static float WALL_W = ROOM_W / 8;
	public static float WALL_H = ROOM_W;
	protected static GameImage FloorImg = null;
	private static GameImage FloorInvisibleImg = null;
	private static GameImage RoofImg = null;
	protected float x, y;
	protected boolean visible = false;
	private ArrayList <RoomOccupant> occupants;

	protected Wall[] walls = new Wall[4];
	
	protected GameCore core;
	
	public Room(GameCore core, float x, float y)
	{
		occupants = new ArrayList <RoomOccupant>();
		this.core = core;
		this.x = x;
		this.y = y;
		if (FloorImg == null)
		{
			FloorImg = new GameImage(core);
			FloorImg.init("./resources/Imgs/Structure/floor.png");
			FloorInvisibleImg = new GameImage(core);
			FloorInvisibleImg.init("./resources/Imgs/Structure/floorInvisible.png");
			RoofImg = new GameImage(core);
			RoofImg.init("./resources/Imgs/Structure/roof.png");
		}
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public Wall getLeftWall()
	{
		return walls[0];
	}
	
	public Wall getRightWall()
	{
		return walls[1];
	}
	
	public Wall getUpWall()
	{
		return walls[2];
	}
	
	public Wall getDownWall()
	{
		return walls[3];
	}
	
	public void setLeftWall(Wall wall)
	{
		walls[0] = wall;
	}
	
	public void setRightWall(Wall wall)
	{
		walls[1] = wall;
	}
	
	public void setUpWall(Wall wall)
	{
		walls[2] = wall;
	}
	
	public void setDownWall(Wall wall)
	{
		walls[3] = wall;
	}
	
	public void setLeftWall(WALL_TYPES wallType)
	{
		float wallX = x - WALL_W/2;
		float wallY = y;
		float wallW = WALL_W;
		float wallH = WALL_H;
		setWall(wallType, 0, wallX, wallY, wallW, wallH, true);
	}
	
	public void setRightWall(WALL_TYPES wallType)
	{
		float wallX = x + ROOM_W - WALL_W/2;
		float wallY = y;
		float wallW = WALL_W;
		float wallH = WALL_H;
		setWall(wallType, 1, wallX, wallY, wallW, wallH, true);
	}
	
	public void setUpWall(WALL_TYPES wallType)
	{
		float wallX = x;
		float wallY = y - WALL_W/2;
		float wallW = WALL_H;
		float wallH = WALL_W;
		setWall(wallType, 2, wallX, wallY, wallW, wallH, false);
	}
	
	public void setDownWall(WALL_TYPES wallType)
	{
		float wallX = x;
		float wallY = y + WALL_H - WALL_W/2;
		float wallW = WALL_H;
		float wallH = WALL_W;
		setWall(wallType, 3, wallX, wallY, wallW, wallH, false);
	}
	
	private void setWall(WALL_TYPES wallType, int wallIdx, float wallX, float wallY, float wallW, float wallH, boolean vertical)
	{
		switch (wallType)
		{
		case SOLID:
			walls[wallIdx] = new SolidWall(core, wallX, wallY, wallW, wallH, vertical);
			break;
		case DOOR:
			walls[wallIdx] = new DoorWall(core, wallX, wallY, wallW, wallH, vertical);
			break;
		case ROOM_DOOR:
			walls[wallIdx] = new TerminalSolidWall(core, wallX, wallY, wallW, wallH, vertical);
			break;
		}
	}
	
	public DIRECTION manageButtons()
	{
		DIRECTION dir = null;
		int numButtonsPressed = 0;
		for (int i = 0; i < walls.length; i++)
		{
			if (walls[i] instanceof DoorWall && ((DoorWall)walls[i]).checkButton())
			{
				dir = WallDirections[i];
				numButtonsPressed++;
			}
		}
		if (numButtonsPressed > 1)
		{
			return null;
		}
		return dir;
	}
	
	public ForceWave createForceWave()
	{
		for (int i = 0; i < walls.length; i++)
		{
			if (walls[i] instanceof DoorWall)
			{
				ForceWave forceWave = ((DoorWall)walls[i]).createForceWave();
				if (forceWave != null)
				{
					forceWave.setDirection(WallDirections[i]);
					return forceWave;
				}
			}
		}
		return null;
	}
	
	public DIRECTION manageShoot()
	{
		DIRECTION dir = null;
		int numButtonsPressed = 0;
		int shotI = 0;
		for (int i = 0; i < walls.length; i++)
		{
			if (walls[i] instanceof DoorWall)
			{
				if (((DoorWall)walls[i]).isLeaveable())
				{
					return null;
				}
				if (((DoorWall)walls[i]).checkShoot())
				{
					dir = WallDirections[i];
					shotI = i;
					numButtonsPressed++;
				}
			}
		}
		if (numButtonsPressed > 1)
		{
			return null;
		}
		else if (numButtonsPressed == 1)
		{
			((DoorWall)walls[shotI]).shoot();
		}
		return dir;
	}
	
	public void drawRoof()
	{
		RoofImg.draw(x, y, ROOM_W, ROOM_W);
	}
	
	public DIRECTION checkTerminalUsed()
	{
		for (int i = 0; i < walls.length; i++)
		{
			if (walls[i] != null && walls[i] instanceof TerminalSolidWall && ((TerminalSolidWall)walls[i]).checkTerminalUsed())
			{
				return WallDirections[i];
			}
		}
		return null;
	}
	
	public void setVisibility(boolean visible)
	{
		this.visible = visible;
	}
	
	public boolean isLeaveable()
	{
		for (int i = 0; i < walls.length; i++)
		{
			if (walls[i] instanceof DoorWall && ((DoorWall)walls[i]).isLeaveable())
			{
				return true;
			}
		}
		return false;
	}
	
	public void draw()
	{
		if (visible || GameEngine.DebugMode)
		{
			FloorImg.draw(x, y, ROOM_W, ROOM_W);
			drawOccupants();
			for (int i = 0; i < walls.length; i++)
			{
				if (walls[i] != null)
				{
					walls[i].draw();
				}
			}
		}
	}
	
	public void drawBlockLayer()
	{
		if (!visible && !GameEngine.DebugMode)
		{
			drawBlockLayerNoCheck();
		}
		for (int i = 0; i < occupants.size(); i++)
		{
			occupants.get(i).drawBlocker();
		}
	}
	
	public void drawBlockLayerNoCheck()
	{
		FloorInvisibleImg.draw(x, y, ROOM_W, ROOM_W);
	}
	
	public void drawPlayerRoom(boolean doorLocked)
	{
		for (int i = 0; i < walls.length; i++)
		{
			if (walls[i] instanceof DoorWall && core.getGameEngine().checkPlayerMove(WallDirections[i]).moveResult == MOVE_RESULT.LEGAL)
			{
				((DoorWall)walls[i]).manageDoor(doorLocked);
			}
			else if (walls[i] instanceof DoorWall)
			{
				((DoorWall)walls[i]).manageDoor(true);
			}
		}
	}
	
	public void addOccupant(RoomOccupant occupant)
	{
		occupants.add(occupant);
	}
	
	public void removeOccupant(RoomOccupant occupant)
	{
		occupants.remove(occupant);
	}
	
	public ArrayList <RoomOccupant> getOccupants()
	{
		return occupants;
	}
	
	protected void drawOccupants()
	{
		for (int i = 0; i < occupants.size(); i++)
		{
			occupants.get(i).draw();
		}
	}
}
