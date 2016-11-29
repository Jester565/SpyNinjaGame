package impl;

import jgraphic.Sound;

public class PlayerDeadBody implements RoomOccupant {
	private GameCore core;
	private static GameImage BloodPoolImg;
	
	private static Sound PlayerDeathSound;
	private Room room;
	private float animationTimer = 0;
	private static GameImage PlayerDeadBodyImg;
	private static float ALIEN_SPEED = .15f;
	private float MOVE_SPEED = 3.0f;
	private float alienX;
	private float alienY;
	private float alienMouthX;
	private float bloodPoolW = 0;
	private float deadBodyX = 0;
	private float deadBodyY = 0;
	private boolean statusSet = false;
	
	public PlayerDeadBody(GameCore core, Room room)
	{
		this.core = core;
		this.room = room;
		if (PlayerDeathSound == null)
		{
			BloodPoolImg = new GameImage(core);
			BloodPoolImg.init("./resources/Imgs/Soldier/bloodPool.png");
			PlayerDeadBodyImg = new GameImage(core);
			PlayerDeadBodyImg.init("./resources/Imgs/Soldier/soldierDead.png");
			PlayerDeathSound = new Sound("./resources/Sounds/playerDeath.wav");
		}
		Alien.InitAlienImages(core);
		alienX = room.getX() - 5;
		alienY = room.getY() - Alien.ALIEN_W/2 + Room.ROOM_W/2;
	}

	@Override
	public void setXY(float x, float y) {
		
	}
	
	@Override
	public void drawBlocker()
	{
		if (animationTimer < 180 || animationTimer >= 600 && animationTimer < 700)
		{
			core.getShapeRenderer().drawRect((room.getX() - core.gameX) * core.gameScaleX, (room.getY() - core.gameY) * core.gameScaleY, Room.ROOM_W * core.gameScaleX, Room.ROOM_W * core.gameScaleY, 0, 0, 0, 1);
		}
	}

	@Override
	public void draw() {
		if (animationTimer == 0)
		{
			PlayerDeathSound.stopAndReset();
			PlayerDeathSound.play();
		}
		if (animationTimer < 900)
		{
			animationTimer += core.rate;
		}
		if (animationTimer < 700)
		{
			core.setPlayerMovement(false);
		}
		if (animationTimer < 180)
		{
			float vX = 0;
			float vY = 0;
			if (core.getPlayer().getX() + core.getPlayer().getW() > room.getX() + Room.ROOM_W - 95)
			{
				vX = -MOVE_SPEED;
			}
			if (core.getPlayer().getX() + core.getPlayer().getW() < room.getX() + Room.ROOM_W - 105)
			{
				vX = MOVE_SPEED;
			}
			if (core.getPlayer().getY() + core.getPlayer().getH()/2 > room.getY() + Room.ROOM_W/2 - 5)
			{
				vY = -MOVE_SPEED;
			}
			if (core.getPlayer().getY() + core.getPlayer().getH()/2 < room.getY() + Room.ROOM_W/2 + 5 )
			{
				vY = MOVE_SPEED;
			}
			core.getPlayer().setRads((float) (-Math.PI));
			core.getPlayer().move((float)(vX), (float)(vY));
			core.zoomIn(.004f);
		}
		else if (animationTimer < 240)
		{
			int imgI = (int) ((animationTimer/240.0f) * 9.0f);
			GameImage alienImg = Alien.AlienWalkImgs.get(imgI);
			alienX += core.rate * ALIEN_SPEED;
			alienImg.setRotateOriginScale(.5, .5);
			alienImg.setRads(Math.PI/2);
			alienImg.draw(alienX, alienY, Alien.ALIEN_W, Alien.ALIEN_W);
		}
		else if (animationTimer < 600)
		{
			if (animationTimer > 420)
			{
				if (alienMouthX < alienX + Alien.ALIEN_W * .8f)
				{
					alienMouthX += core.rate * 36.0f;
				}
				else
				{
					alienMouthX = alienX + Alien.ALIEN_W * .8f;
				}
				bloodPoolW += core.rate * .5f;
				BloodPoolImg.draw(core.getPlayer().getX() + core.getPlayer().getW()* .75f - bloodPoolW/2, core.getPlayer().getY() + core.getPlayer().getH()/2 - bloodPoolW/2, bloodPoolW, bloodPoolW);
				Alien.AlienMouthImg.draw(alienMouthX, alienY + Alien.ALIEN_W/2, Alien.ALIEN_W * .5, 30);
			}
			else
			{
				bloodPoolW = 30;
				alienMouthX = alienX + Alien.ALIEN_W * .5f;
			}
			GameImage alienImg = Alien.AlienWalkImgs.get(9);
			alienImg.setRotateOriginScale(.5, .5);
			alienImg.setRads(Math.PI/2);
			alienImg.draw(alienX, alienY, Alien.ALIEN_W, Alien.ALIEN_W);
			deadBodyX = core.getPlayer().getX();
			deadBodyY = core.getPlayer().getY();
		}
		else
		{
			if (!statusSet)
			{
				core.getHUD().enableDrawStatus();
				statusSet = true;
			}
			PlayerDeadBodyImg.draw(deadBodyX, deadBodyY, 300, 300);
		}
	}

	@Override
	public Room getRoomOccupied() {
		animationTimer += core.rate;
		return null;
	}
}
