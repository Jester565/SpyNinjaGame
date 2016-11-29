package impl;

import java.util.ArrayList;
import java.util.Arrays;

import jgraphic.Sound;

public class Alien implements RoomOccupant{
	public static final float ALIEN_W = 500;
	private static final float ROOM_BOUND_OFF = 0;
	private static final float ALIEN_MAX_VELOCITY = 2;
	private static final float MAX_STEP_TIME = 120;
	private static final float MAX_VELOCITY_CHANGE_TIME = 400;
	private static final float MIN_VELOCITY_CHANGE_TIME = 200;
	
	private static GameImage AlienLeftImg1;
	private static GameImage AlienLeftImg2;
	private static GameImage AlienLeftImg3;
	private static GameImage AlienRightImg1;
	private static GameImage AlienRightImg2;
	private static GameImage AlienRightImg3;
	public static GameImage AlienMouthImg;
	private static Sound AlienDeathSound;
	
	public static ArrayList <GameImage> AlienWalkImgs;
	
	public static void InitAlienImages(GameCore core)
	{
		if (AlienLeftImg1 == null)
		{
			AlienLeftImg1 = new GameImage(core);
			AlienLeftImg1.init("./resources/Imgs/Alien/alienLeft1.png");
			AlienLeftImg2 = new GameImage(core);
			AlienLeftImg2.init("./resources/Imgs/Alien/alienLeft2.png");
			AlienLeftImg3 = new GameImage(core);
			AlienLeftImg3.init("./resources/Imgs/Alien/alienLeft3.png");
			AlienRightImg1 = new GameImage(core);
			AlienRightImg1.init("./resources/Imgs/Alien/alienRight1.png");
			AlienRightImg2 = new GameImage(core);
			AlienRightImg2.init("./resources/Imgs/Alien/alienRight2.png");
			AlienRightImg3 = new GameImage(core);
			AlienRightImg3.init("./resources/Imgs/Alien/alienRight3.png");
			AlienMouthImg = new GameImage(core);
			AlienMouthImg.init("./resources/Imgs/Alien/alienMouth.png");
			AlienDeathSound = new Sound("./resources/Sounds/alienDeath.wav");
			AlienWalkImgs = new ArrayList <GameImage>(Arrays.asList(AlienLeftImg1, AlienLeftImg2, AlienLeftImg3, AlienLeftImg2, AlienLeftImg1,
					AlienRightImg1, AlienRightImg2, AlienRightImg3, AlienRightImg2, AlienRightImg1));
		}
	}
	
	private GameImage alienImg;
	
	private GameCore core;
	private float stepTimer = 0;
	
	private float x;
	private float y;
	private float dX;
	private float dY;
	private float imgAngle = 0;
	private float velocityChangeTimer = 0;
	private float velocityChangeTimeLimit = 0;
	private Room room;
	
	public static void PlayDeathSound()
	{
		if (AlienDeathSound != null)
		{
			AlienDeathSound.stopAndReset();
			AlienDeathSound.play();
		}
	}
	
	public Alien(GameCore core, Room room)
	{
		this.core = core;
		this.room = room;
		resetVelocity();
		InitAlienImages(core);
		alienImg = AlienLeftImg1;
	}

	@Override
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
		resetVelocity();
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	private void resetVelocity()
	{
		velocityChangeTimer = 0;
		velocityChangeTimeLimit = (float) (Math.random() * (MAX_VELOCITY_CHANGE_TIME - MIN_VELOCITY_CHANGE_TIME) + MIN_VELOCITY_CHANGE_TIME);
		dX = (float) (Math.random() * ALIEN_MAX_VELOCITY * 2 - ALIEN_MAX_VELOCITY);
		dY = (float) (Math.random() * ALIEN_MAX_VELOCITY * 2 - ALIEN_MAX_VELOCITY);
		if (x - ALIEN_W/2 < room.x + ROOM_BOUND_OFF)
		{
			dX = (float) (Math.random() * ALIEN_MAX_VELOCITY);
		}
		if (y - ALIEN_W/2 < room.y + ROOM_BOUND_OFF)
		{
			dY = (float) (Math.random() * ALIEN_MAX_VELOCITY);
		}
		if (x + ALIEN_W/2 > room.x + Room.ROOM_W - ROOM_BOUND_OFF)
		{
			dX = (float) (Math.random() * -ALIEN_MAX_VELOCITY);
		}
		if (y + ALIEN_W/2 > room.y + Room.ROOM_W - ROOM_BOUND_OFF)
		{
			dY = (float) (Math.random() * -ALIEN_MAX_VELOCITY);
		}
		imgAngle = (float) (Math.atan2(dY, dX) + Math.PI/2);
	}

	@Override
	public void draw() {
		velocityChangeTimer += core.rate;
		if (velocityChangeTimer > velocityChangeTimeLimit)
		{
			resetVelocity();
		}
		if (dX != 0 || dY != 0)
		{
			if (x - ALIEN_W/2 < room.x + ROOM_BOUND_OFF && dX < 0)
			{
				dX = 0;
				dY = 0;
			}
			if (y - ALIEN_W/2 < room.y + ROOM_BOUND_OFF && dY < 0)
			{
				dX = 0;
				dY = 0;
			}
			if (x + ALIEN_W/2 > room.x + Room.ROOM_W - ROOM_BOUND_OFF && dX > 0)
			{
				dX = 0;
				dY = 0;
			}
			if (y + ALIEN_W/2 > room.y + Room.ROOM_W - ROOM_BOUND_OFF && dY > 0)
			{
				dX = 0;
				dY = 0;
			}
			updateStepImg();
		}
		x += core.rate * dX;
		y += core.rate * dY;
		alienImg.setRotateOriginScale(.5, .5);
		alienImg.setRads(imgAngle);
		alienImg.draw(x - ALIEN_W/2, y - ALIEN_W/2, ALIEN_W, ALIEN_W);
	}
	
	private void updateStepImg()
	{
		stepTimer += core.rate;
		if (stepTimer >= MAX_STEP_TIME)
		{
			stepTimer = 0;
		}
		int imgI = (int) ((stepTimer/MAX_STEP_TIME) * (float)AlienWalkImgs.size());
		alienImg = AlienWalkImgs.get(imgI);
	}

	@Override
	public Room getRoomOccupied() {
		return room;
	}

	@Override
	public void drawBlocker() {
		
	}
}
