package impl;

import java.awt.Color;

import edu.cpp.cs.cs141.final_proj.GameEngine;
import jgraphic.Sound;

public class ItemRoom extends Room {
	private static GameImage SpinnerImg;
	private static GameImage SpinnerBaseImg;
	private static GameImage SpinnerCoverUpImg;
	private static GameImage SpinnerCoverDownImg;
	private static Sound CoverOpenSound;
	private static boolean PowerUpNotificationDisplayed = false;
	
	private static float ITEM_VELOCITY = .035f;
	private static float CONTAIN_ITEM_W = 50;
	private static float CONTAIN_ITEM_H = 30;
	private static float FULL_COVER_OUT = 20;
	private static float COVER_VELOCITY = 2;
	private static float DEFAULT_DELTA_RADS = .1f;
	private static float SPINNER_DIAM = 200;
	
	private Color circleColor;
	private float rads = 0;
	private float deltaRads = DEFAULT_DELTA_RADS;
	private float coverOut = 0;
	private boolean used = false;
	private float itemDis = 0;
	private Item containItem;
	

	public ItemRoom(GameCore core, Item item, float x, float y, float r, float g, float b) {
		super(core, x, y);
		containItem = item;
		if (SpinnerImg == null)
		{
			SpinnerImg = new GameImage(core);
			SpinnerImg.init("./resources/Imgs/Structure/spinner.png");
			SpinnerImg.setRotateOriginScale(.5, .5);
			SpinnerBaseImg = new GameImage(core);
			SpinnerBaseImg.init("./resources/Imgs/Structure/spinnerCoverBase.png");
			SpinnerCoverUpImg = new GameImage(core);
			SpinnerCoverUpImg.init("./resources/Imgs/Structure/spinnerCover1.png");
			SpinnerCoverDownImg = new GameImage(core);
			SpinnerCoverDownImg.init("./resources/Imgs/Structure/spinnerCover2.png");
			CoverOpenSound = new Sound("./resources/Sounds/itemCover.wav");
		}
		circleColor = new Color(r, g, b, 1);
	}
	
	public void setUsed(boolean mode)
	{
		used = mode;
	}
	
	@Override
	public void draw()
	{
		if (visible || GameEngine.DebugMode)
		{
			if (!PowerUpNotificationDisplayed)
			{
				core.getNotificationManager().addNotification("Hover mouse over power up (the spinning circle) to see description");
				PowerUpNotificationDisplayed = true;
			}
			FloorImg.draw(x, y, ROOM_W, ROOM_W);
			for (int i = 0; i < walls.length; i++)
			{
				if (walls[i] != null)
				{
					walls[i].draw();
				}
			}
			rads += deltaRads * core.rate;
			if (rads > Math.PI * 2)
			{
				rads %= Math.PI * 2;
			}
			core.getShapeRenderer().drawCircleOutline((x + Room.ROOM_W/2 - core.gameX) * core.gameScaleX, (y + Room.ROOM_W/2 - core.gameY) * core.gameScaleY, (SPINNER_DIAM/2) * ((core.gameScaleX + core.gameScaleY)/2), 15 * core.gameScaleX, .5f, .5f, .5f, 1.0f);
			core.getShapeRenderer().drawCircle((x + Room.ROOM_W/2 - core.gameX) * core.gameScaleX, (y + Room.ROOM_W/2 - core.gameY) * core.gameScaleY, (SPINNER_DIAM/2) * ((core.gameScaleX + core.gameScaleY)/2), circleColor);
			SpinnerImg.setRads(rads);
			SpinnerImg.draw(Room.ROOM_W/2 + x - SPINNER_DIAM/2, Room.ROOM_W/2 + y - SPINNER_DIAM/2, SPINNER_DIAM, SPINNER_DIAM);
			SpinnerBaseImg.draw(x + Room.ROOM_W/2 - SPINNER_DIAM/2, y + Room.ROOM_W/2 - SPINNER_DIAM/2, SPINNER_DIAM, SPINNER_DIAM);
			SpinnerCoverUpImg.draw(x + Room.ROOM_W/2 - SPINNER_DIAM/2, y + Room.ROOM_W/2 - SPINNER_DIAM/2 + coverOut, SPINNER_DIAM, SPINNER_DIAM);
			SpinnerCoverDownImg.draw(x + Room.ROOM_W/2 - SPINNER_DIAM/2, y + Room.ROOM_W/2 - SPINNER_DIAM/2 - coverOut, SPINNER_DIAM, SPINNER_DIAM);
			drawOccupants();
			if (containItem != null)
			{
				if (core.getButtonManager().overRect((x + Room.ROOM_W/2 - SPINNER_DIAM/2 - core.gameX) * core.gameScaleX, 
						(y + Room.ROOM_W/2 - SPINNER_DIAM/2 - core.gameY) * core.gameScaleY, SPINNER_DIAM * core.gameScaleX, SPINNER_DIAM * core.gameScaleY))
				{
					core.getHUD().setPowerUpDescription(containItem.getDescription());
				}
			}
		}
	}
	
	@Override
	public void drawPlayerRoom(boolean doorLocked)
	{
		super.drawPlayerRoom(doorLocked);
		if (used && containItem != null)
		{
			core.getShapeRenderer().drawLine((core.getPlayer().getX() + core.getPlayer().getW()/2 - core.gameX) * core.gameScaleX, 
					(core.getPlayer().getY() + core.getPlayer().getH()/2 - core.gameY) * core.gameScaleY, 
					(x + Room.ROOM_W/2 - core.gameX) * core.gameScaleX, (y + Room.ROOM_W/2 - core.gameY) * core.gameScaleY, 
					20 * core.gameScaleX, (circleColor.getRed() / 255.0f) / 2.0f, (circleColor.getGreen() / 255.0f) / 2.0f, 
					(circleColor.getBlue() / 255.0f) / 2.0f, .5f);
			float itemX = x + ROOM_W/2;
			float itemY = y + ROOM_W/2;
			float playerX = core.getPlayer().getX() + core.getPlayer().getW()/2;
			float playerY = core.getPlayer().getY() + core.getPlayer().getH()/2;
			if (coverOut < FULL_COVER_OUT)
			{
				if (coverOut == 0)
				{
					CoverOpenSound.stopAndReset();
					CoverOpenSound.play();
				}
				coverOut += COVER_VELOCITY * core.rate;
			}
			else
			{
				itemDis += ITEM_VELOCITY * core.rate;
				float playerDis = (float) Math.sqrt(Math.pow(itemX - playerX, 2) + Math.pow(itemY - playerY, 2));
				if (itemDis > 1)
				{
					core.getHUD().addItem(containItem);
					containItem = null;
					return; 
				}
				float itemPlayerAngle = (float) Math.atan2(playerY - itemY, playerX - itemX);
				itemX += Math.cos(itemPlayerAngle) * itemDis * playerDis;
				itemY += Math.sin(itemPlayerAngle) * itemDis * playerDis;
			}
			containItem.draw(itemX - CONTAIN_ITEM_W/2, itemY - CONTAIN_ITEM_H/2, CONTAIN_ITEM_W, CONTAIN_ITEM_H);
		}
	}
}
