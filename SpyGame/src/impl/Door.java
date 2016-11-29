package impl;

import jphys.Blocker;

public class Door extends Blocker {
	
	private static GameImage DoorImg;
	private static GameImage DoorHorzImg;
	
	protected GameImage drawImg;
	
	public Door(GameCore core)
	{
		super();
		if (DoorImg == null)
		{
			DoorImg = new GameImage(core);
			DoorHorzImg = new GameImage(core);
			GameCore.SetImage(DoorImg, DoorHorzImg, "./resources/Imgs/Structure/door.png");
		}
		core.getCollisionManager().addBlocker(this);
		drawImg = DoorImg;
	}
	
	public Door(GameCore core, float x, float y, float w, float h, boolean vertical)
	{
		super(x, y, w, h);
		if (DoorImg == null)
		{
			DoorImg = new GameImage(core);
			DoorHorzImg = new GameImage(core);
			GameCore.SetImage(DoorImg, DoorHorzImg, "./resources/Imgs/Structure/door.png");
		}
		if (vertical)
		{
			drawImg = DoorImg;
		}
		else
		{
			drawImg = DoorHorzImg;
		}
		core.getCollisionManager().addBlocker(this);
	}
	
	public void setVerticallity(boolean vertical)
	{
		if (vertical)
		{
			drawImg = DoorImg;
		}
		else
		{
			drawImg = DoorHorzImg;
		}
	}
	
	public void draw()
	{
		drawImg.draw(getX(), getY(), getW(), getH());
	}
}
