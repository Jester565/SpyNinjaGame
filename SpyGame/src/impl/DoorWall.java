package impl;

import jgraphic.Sound;
import jphys.Blocker;

public class DoorWall implements Wall {
	private static GameImage DoorWallImg;
	private static GameImage DoorWallHorzImg;
	
	private static GameImage DoorWallUnlockImg;
	private static GameImage DoorWallUnlockHorzImg;
	
	private static GameImage LightningImg;
	
	private static Sound ShotSound;
	private static Sound BlowSound;
	
	private static Sound DoorOpenSound;
	private static Sound DoorCloseSound;
	
	private Blocker side1;
	private Blocker side2;
	private Door door;
	
	private static float HALLWAY_DECM = .3f;
	private static float DOOR_OFF_X = -4;
	private static float DOOR_OPEN_OFF = 140;
	private static float DOOR_THICK_OFF = .1f;
	private static float DOOR_BUTTON_OFF = 140;
	private static float SHOOT_TIME = 200;
	private static float SHOCK_SHOOT_TIME = 120;
	
	private float x;
	private float y;
	private float w;
	private float h;
	
	private static final int MAX_LIGHTNING_COUNT = 10;
	private boolean vertical;
	private boolean openSoundPlaying = false;
	private boolean closeSoundPlaying = false;
	private boolean isShot = false;
	private float shotTimer = 0;
	
	private GameImage wallImg;
	private GameImage wallUnlockImg;
	
	private GameCore core;
	
	public enum BUTTON_RESULT {
		PRESSED, NOT_FACING, TOO_FAR
	}
	
	public DoorWall(GameCore core, float x, float y, float w, float h, boolean vertical)
	{
		this.core = core;
		this.vertical = vertical;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		if (vertical)
		{
			side1 = new Blocker(x, y, w, h * HALLWAY_DECM);
			side2 = new Blocker(x, y + (1 - HALLWAY_DECM) * h, w, h * HALLWAY_DECM);
			door = new Door(core, x + DOOR_THICK_OFF * w, y + h * HALLWAY_DECM, w - DOOR_THICK_OFF * w * 2, h * HALLWAY_DECM * 2, true);
		}
		else
		{
			side1 = new Blocker(x, y, w * HALLWAY_DECM, h);
			side2 = new Blocker(x + (1 - HALLWAY_DECM) * w, y, w * HALLWAY_DECM, h);
			door = new Door(core, x + (w * HALLWAY_DECM) + DOOR_OFF_X, y + DOOR_THICK_OFF * h, w * HALLWAY_DECM * 2, h - DOOR_THICK_OFF * h * 2, false);
		}
		core.getCollisionManager().addBlocker(side1);
		core.getCollisionManager().addBlocker(side2);
		if (DoorWallImg == null)
		{
			DoorWallImg = new GameImage(core);
			DoorWallHorzImg = new GameImage(core);
			GameCore.SetImage(DoorWallImg, DoorWallHorzImg, "./resources/Imgs/Structure/doorFrame.png");
			DoorWallUnlockImg = new GameImage(core);
			DoorWallUnlockHorzImg = new GameImage(core);
			GameCore.SetImage(DoorWallUnlockImg, DoorWallUnlockHorzImg, "./resources/Imgs/Structure/doorFrameOpen.png");
			LightningImg = new GameImage(core);
			LightningImg.init("./resources/Imgs/Structure/lightning.png");
			ShotSound = new Sound("./resources/Sounds/scavengerFire.wav");
			BlowSound = new Sound("./resources/Sounds/scavengerBlowup.wav");
			DoorOpenSound = new Sound("./resources/Sounds/doorOpen.wav");
			DoorCloseSound = new Sound("./resources/Sounds/doorClose.wav");
		}
		if (vertical)
		{
			wallImg = DoorWallImg;
			wallUnlockImg = DoorWallUnlockImg;
		}
		else
		{
			wallImg = DoorWallHorzImg;
			wallUnlockImg = DoorWallUnlockHorzImg;
		}
	}

	@Override
	public void draw() {
		door.draw();
		wallImg.draw(x, y, w, h);
		if (isShot)
		{
			if (shotTimer < SHOCK_SHOOT_TIME)
			{
				int lightningCount = (int) (Math.random() * MAX_LIGHTNING_COUNT);
				for (int i = 0; i < lightningCount; i++)
				{
					float lightningX = (float) (Math.random() * w) + x;
					float lightningY = (float) (Math.random() * h) + y;
					float lightningW = (float) (Math.random() * (w));
					float lightningH = (float) (Math.random() * (h));
					LightningImg.draw(lightningX, lightningY, lightningW, lightningH);
				}
			}
		}
	}
	
	public ForceWave createForceWave()
	{
		if (isShot)
		{
			if (shotTimer < SHOOT_TIME)
			{
				shotTimer += core.rate;
			}
			else
			{
				shotTimer = 0;
				isShot = false;
				return new ForceWave(core, x, y);
			}
		}
		return null;
	}
	
	public BUTTON_RESULT checkButton()
	{
		if (playerInButtonRange())
		{
			if (playerFacingButton())
			{
				return BUTTON_RESULT.PRESSED;
			}
			else
			{
				return BUTTON_RESULT.NOT_FACING;
			}
		}
		return BUTTON_RESULT.TOO_FAR;
	}
	
	private boolean playerInButtonRange()
	{
		float playerX = core.getPlayer().getX()  + core.getPlayer().getW()/2;
		float playerY = core.getPlayer().getY()  + core.getPlayer().getH()/2;
		float wallX = x;
		float wallW = w;
		float wallY = y;
		float wallH = h;
		if (vertical)
		{
			wallX -= DOOR_BUTTON_OFF;
			wallW += DOOR_BUTTON_OFF * 2;
		}
		else
		{
			wallY -= DOOR_BUTTON_OFF;
			wallH += DOOR_BUTTON_OFF * 2;
		}
		return (playerX > wallX && playerX < wallX + wallW && playerY > wallY && playerY < wallY + wallH);
	}
	
	private boolean playerFacingButton()
	{
		return GameCore.IsFacing(core.getPlayer(), x, y, x + w, y + h);
	}
	
	public void manageDoor(boolean doorsLocked)
	{
		if (!doorsLocked)
		{
			wallUnlockImg.draw(x, y, w, h);
		}
		if (!doorsLocked && (vertical && core.getPlayer().getX() + core.getPlayer().getW()/2 > door.getX() - DOOR_OPEN_OFF && core.getPlayer().getX() + core.getPlayer().getW()/2 < door.getX() + door.getW() + DOOR_OPEN_OFF
		 && core.getPlayer().getY() + core.getPlayer().getH()/2 > y + h * HALLWAY_DECM && core.getPlayer().getY() + core.getPlayer().getH()/2 < y + h * (1.0f - HALLWAY_DECM) || 
		 !vertical && core.getPlayer().getY() + core.getPlayer().getH()/2 > door.getY() - DOOR_OPEN_OFF && core.getPlayer().getY() + core.getPlayer().getH()/2 < door.getY() + door.getH() + DOOR_OPEN_OFF
		 && core.getPlayer().getX() + core.getPlayer().getW()/2 > x + w * HALLWAY_DECM && core.getPlayer().getX() + core.getPlayer().getW()/2 < x + w * (1.0f - HALLWAY_DECM)))
		{
			if (closeSoundPlaying)
			{
				DoorCloseSound.stopAndReset();
				closeSoundPlaying = false;
			}
			if (door.getH() > 0 && vertical|| door.getW() > 0 && !vertical)
			{
				if (vertical)
				{
					door.changeH((float)core.rate * -6.0f);
				}
				else
				{
					door.changeW((float)core.rate * -6.0f);
				}
				if (!openSoundPlaying)
				{
					DoorOpenSound.stopAndReset();
					DoorOpenSound.play();
					openSoundPlaying = true;
				}
				
			}
		}
		else if (door.getH() < h * HALLWAY_DECM * 2 && vertical || door.getW() < w * HALLWAY_DECM * 2 && !vertical)
		{
			if (vertical)
			{
				door.changeH((float)core.rate * 6.0f);
			}
			else
			{
				door.changeW((float)core.rate * 6.0f);
			}
			if (openSoundPlaying)
			{
				DoorOpenSound.stopAndReset();
				openSoundPlaying = false;
			}
			if (!closeSoundPlaying)
			{
				DoorCloseSound.stopAndReset();
				DoorCloseSound.play();
				closeSoundPlaying = true;
			}
		}
		else
		{
			if (openSoundPlaying)
			{
				DoorOpenSound.stopAndReset();
				openSoundPlaying = false;
			}
			if (closeSoundPlaying)
			{
				DoorCloseSound.stopAndReset();
				closeSoundPlaying = false;
			}
		}
	}

	public boolean isLeaveable() {
		if (vertical)
		{
			return (door.getH() < core.getPlayer().getH());
		}
		else
		{
			return (door.getW() < core.getPlayer().getW());
		}
	}
	
	public void shoot() {
		ShotSound.stopAndReset();
		ShotSound.play();
		BlowSound.stopAndReset();
		BlowSound.play();
		isShot = true;
	}
	
	public boolean checkShoot() {
		return GameCore.IsFacing(core.getPlayer(), x, y, x + w, y + h);
	}
}
