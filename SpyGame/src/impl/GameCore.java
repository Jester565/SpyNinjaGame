package impl;

import java.util.ArrayList;

import edu.cpp.cs.cs141.final_proj.GameEngine;
import jgraphic.Core;
import jgraphic.DisplayManager;
import jgraphic.FPSLogger;
import jgraphic.Music;
import jphys.CollisionManager;
import jphys.Vector2D;

public class GameCore extends Core {
	private Music backgroundMusic;
	
	protected static void SetImage(GameImage gameImg, GameImage gameHorzImg, String fileName)
	{
		if (!gameImg.init(fileName))
		{
			System.out.println("Unable to load " + fileName);
		}
		int periodIdx = fileName.lastIndexOf(".");
		String fileExtension = fileName.substring(periodIdx, fileName.length());
		fileName = fileName.substring(0, periodIdx);
		fileName += "Horz" + fileExtension;
		if (!gameHorzImg.init(fileName))
		{
			System.out.println("Unable to load " + fileName);
		}
	}
	
	protected static boolean IsFacing(Player player, float x1, float y1, float x2, float y2)
	{
		float playerX = player.getX() + player.getW()/2;
		float playerY = player.getY() + player.getH()/2;
		float rad1 = NormalizeRadian((float)Math.atan2(y1 - playerY, x1 - playerX));
		float rad2 = NormalizeRadian((float)Math.atan2(y2 - playerY, x2 - playerX));
		float playerRads = player.getRads();
		if (RadiansCrossZero(rad1, rad2))
		{
			if (rad1 > rad2)
			{
				rad1 -= 2 * Math.PI;
			}
			else
			{
				rad2 -= 2 * Math.PI;
			}
			if (playerRads > Math.PI)
			{
				playerRads -= 2 * Math.PI;
			}
		}
		if (rad1 > rad2)
		{
			float temp = rad1;
			rad1 = rad2;
			rad2 = temp;
		}
		return (playerRads >= rad1 && playerRads <= rad2);
	}
	
	protected static float GetFacingOff(Player player, float x1, float y1, float x2, float y2)
	{
		float playerX = player.getX() + player.getW()/2;
		float playerY = player.getY() + player.getH()/2;
		float rad1 = NormalizeRadian((float)Math.atan2(y1 - playerY, x1 - playerX));
		float rad2 = NormalizeRadian((float)Math.atan2(y2 - playerY, x2 - playerX));
		float playerRads = player.getRads();
		if (RadiansCrossZero(rad1, rad2))
		{
			if (rad1 > rad2)
			{
				rad1 -= 2 * Math.PI;
			}
			else
			{
				rad2 -= 2 * Math.PI;
			}
			if (playerRads > Math.PI)
			{
				playerRads -= 2 * Math.PI;
			}
		}
		return Math.abs((rad1 + rad2)/2 - playerRads);
	}
	
	protected static float NormalizeRadian(float radian)
	{
		if (radian < 0)
		{
			radian += 2 * Math.PI;	//normalize the radians
		}
		return radian;
	}
	
	protected static boolean RadiansCrossZero(float radian1, float radian2)
	{
		if (radian1 < radian2)
		{
			return (radian2 - Math.PI > radian1);
		}
		return (radian1 - Math.PI > radian2);
	}
	
	static final String WINDOW_NAME = "CS141 Final";
	private Player player;
	private CollisionManager collisionManager;
	private FPSLogger fpsLogger;
	private RoomManager roomManager;
	private GameEngine gameEngine;
	private HUD hud;
	private ArrayList <Star> stars;
	private boolean playerMovementEnabled = false;
	private NotificationManager notificationManager;
	
	public static float GAME_SCALE_DEFAULT_X = .35f;
	public static float GAME_SCALE_DEFAULT_Y = .35f;
	
	public static float GAME_MENU_SCALE_X = .017f;
	public static float GAME_MENU_SCALE_Y = .017f; 
	
	public float gameScaleX = GAME_MENU_SCALE_X;
	public float gameScaleY = GAME_MENU_SCALE_Y;
	
	public float gameX = 0;
	public float gameY = 0;
	
	private MainMenu mainMenu;
	private boolean inMenu = true;
	
	public GameCore()
	{
		super();
	}
	
	@Override
	protected boolean init(String windowName) {
		if (super.init(WINDOW_NAME))
		{
			boolean fail = false;
			notificationManager = new NotificationManager(this, 5);
			String consolasName = this.getTextRenderer().loadFont("./resources/Fonts/Consolas.ttf");
			fail |= consolasName == null;
			fail |= !this.getTextRenderer().setFont(consolasName);
			getDisplayManager().setBackgroundColor(0, 0, 0, 1);
			fpsLogger = new FPSLogger(this);
			mainMenu = new MainMenu(this);
			collisionManager = new CollisionManager();
			gameEngine = new GameEngine();
			gameEngine.reset();
			roomManager = new RoomManager(this);
			player = new Player(this, collisionManager, roomManager.getLastPlayerRoomColumn() * Room.ROOM_W + Room.ROOM_W/2, 
					roomManager.getLastPlayerRoomRow() * Room.ROOM_W + Room.ROOM_W/2, 200, 200);
			backgroundMusic = new Music("./resources/Sounds/backgroundMusic.wav");
			backgroundMusic.setVolume(.7d);
			backgroundMusic.setLoop(true);
			backgroundMusic.play();
			hud = new HUD(this);
			stars = new ArrayList <Star>();
			for (int i = 0; i < 100; i++)
			{
				stars.add(new Star(this, (float)(Math.random() * 100000 - 50000), (float)(Math.random() * 100000 - 50000)));
			}
			this.setFrameRateCap(200);
			return !fail;
		}
		return false;
	}
	
	public void zoomIn(float deltaZoom)
	{
		deltaZoom = Math.abs(deltaZoom);
		gameScaleX += deltaZoom * rate;
		gameScaleY += deltaZoom * rate;
	}
	
	public RoomManager getRoomManager()
	{
		return roomManager;
	}
	
	public void setPlayerMovement(boolean mode)
	{
		playerMovementEnabled = mode;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public CollisionManager getCollisionManager()
	{
		return collisionManager;
	}
	
	public HUD getHUD()
	{
		return hud;
	}
	
	public GameEngine getGameEngine()
	{
		return gameEngine;
	}
	
	public NotificationManager getNotificationManager()
	{
		return notificationManager;
	}
	
	public boolean isPlayerMovementEnabled()
	{
		return playerMovementEnabled;
	}
	
	public void exitToMenu()
	{
		gameEngine.reset();
		reset();
		inMenu = true;
	}
	
	public boolean save(String file)
	{
		return gameEngine.save(file);
	}
	
	public boolean load(String file)
	{
		if (gameEngine.load(file))
		{
			reset();
			if (GameEngine.DebugMode)
			{
				notificationManager.addNotification("Debug Mode Is Active... Press X to zoom in or V to disable debug mode");
			}
			notificationManager.addNotification("Load Successful!");
			return true;
		}
		return false;
	}
	
	private void reset()
	{
		collisionManager = new CollisionManager();
		roomManager = new RoomManager(this);
		player = new Player(this, collisionManager, roomManager.getLastPlayerRoomColumn() * Room.ROOM_W + Room.ROOM_W/2, 
				roomManager.getLastPlayerRoomRow() * Room.ROOM_W + Room.ROOM_W/2, 200, 200);
		hud = new HUD(this);
	}
	
	public void setSpyToInitialState()
	{
		gameScaleX = GAME_SCALE_DEFAULT_X;
		gameScaleY = GAME_SCALE_DEFAULT_Y;
		gameEngine.resetVisibility();
		gameEngine.enemyMove();
		gameEngine.setSpyBackToInitialState();
		player = new Player(this, collisionManager, 0 * Room.ROOM_W + Room.ROOM_W/2, 
				RoomManager.ROOMS_SIZE * Room.ROOM_W - Room.ROOM_W/2, 200, 200);
		roomManager.refreshLastPlayerRoomIndex();
		roomManager.updateAliens();
		roomManager.updateVisibility();
		setPlayerMovement(true);
	}

	@Override
	protected void draw() {
		if (gameScaleX < GAME_SCALE_DEFAULT_X)
		{
			for (Star star : stars)
			{
				star.draw();
			}
		}
		Vector2D moveVector = new Vector2D(0, 0);
		if (playerMovementEnabled)
		{
			if (this.getInputManager().isKeyPressed('a'))
			{
				moveVector.vX -= Player.SPEED;
			}
			if (this.getInputManager().isKeyPressed('s'))
			{
				moveVector.vY += Player.SPEED;
			}
			if (this.getInputManager().isKeyPressed('w'))
			{
				moveVector.vY -= Player.SPEED;
			}
			if (this.getInputManager().isKeyPressed('d'))
			{
				moveVector.vX += Player.SPEED;
			}
			player.setAngleToPointTo((float)this.getInputManager().getScaleMouseX(), (float)this.getInputManager().getScaleMouseY());
		}
		if (this.getInputManager().isKeyTyped('F') && !hud.inMenu() && !mainMenu.inTextField())
		{
			if (this.getDisplayManager().isFullScreen())
			{
				this.getDisplayManager().setWindowed();
			}
			else
			{
				if (!this.getDisplayManager().setFullScreen())
				{
					notificationManager.addNotification("OS does not support full screen");
				}
			}
		}
		if (GameEngine.DebugMode)
		{
			if (this.getInputManager().isKeyPressed('z') && !hud.inMenu())
			{
				gameScaleX -= .01f * rate;
				gameScaleY -= .01f * rate;
				if (gameScaleX < .01f)
				{
					gameScaleX = .01f;
				}
				if (gameScaleY < .01f)
				{
					gameScaleY = .01f;
				}
			}
			if (this.getInputManager().isKeyPressed('x') && !hud.inMenu())
			{
				gameScaleX += .01f * rate;
				gameScaleY += .01f * rate;
			}
		}
		player.move(moveVector.vX, moveVector.vY);
		gameX = (player.getX() + player.getW()/2) - (float)(DisplayManager.DISPLAY_DEFAULT_W/2) / gameScaleX;
		gameY = (player.getY() + player.getH()/2) - (float)(DisplayManager.DISPLAY_DEFAULT_H/2) / gameScaleY;
		roomManager.draw();
		if (!inMenu)
		{
			if (this.getInputManager().isKeyTyped('v') && !hud.inMenu())
			{
				GameEngine.SetDebugMode(!GameEngine.DebugMode);
				if (!GameEngine.DebugMode)
				{
					gameScaleX = GAME_SCALE_DEFAULT_X;
					gameScaleY = GAME_SCALE_DEFAULT_Y;
					notificationManager.addNotification("Debug Mode Deactivated");
				}
				else
				{
					notificationManager.addNotification("Warning: Death animations will not look as intended");
					notificationManager.addNotification("Press X to zoom in   Press Z to zoom out");
					notificationManager.addNotification("Debug Mode Activated");
				}
				roomManager.updateVisibility();
			}
			if (!GameEngine.DebugMode)
			{
				if (gameScaleX < GAME_SCALE_DEFAULT_X)
				{
					roomManager.drawRoof();
					gameScaleX += rate * .003f;
					if (gameScaleX > GAME_SCALE_DEFAULT_X)
					{
						gameScaleX = GAME_SCALE_DEFAULT_X;
					}
				}
				if (gameScaleY < GAME_SCALE_DEFAULT_Y)
				{
					gameScaleY += rate * .003f;
					if (gameScaleY > GAME_SCALE_DEFAULT_Y)
					{
						gameScaleY = GAME_SCALE_DEFAULT_Y;
					}
				}
			}
			hud.draw();
		}
		else
		{
			setPlayerMovement(false);
			gameScaleX = GAME_MENU_SCALE_X;
			gameScaleY = GAME_MENU_SCALE_Y;
			roomManager.drawRoof();
			if (mainMenu.checkButtons())
			{
				inMenu = false;
				setPlayerMovement(true);
			}
		}
		fpsLogger.draw(20, 40, 40, 0, 1, 0, 1);
		notificationManager.draw((float)(DisplayManager.DISPLAY_DEFAULT_W/2), (float)(DisplayManager.DISPLAY_DEFAULT_H - 50), 30);
	}
}
