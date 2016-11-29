package impl;

import jphys.Blocker;

public class SolidWall extends Blocker implements Wall {

	private static GameImage SolidWallImg;
	private static GameImage SolidWallHorzImg;
	
	protected GameImage wallImg;
	
	public SolidWall(GameCore core, float x, float y, float w, float h, boolean vertical) {
		super(x, y, w, h);
		if (SolidWallImg == null)
		{
			SolidWallImg = new GameImage(core);
			SolidWallHorzImg = new GameImage(core);
			GameCore.SetImage(SolidWallImg, SolidWallHorzImg, "./resources/Imgs/Structure/solidWall.png");
		}
		if (vertical)
		{
			wallImg = SolidWallImg;
		}
		else
		{
			wallImg = SolidWallHorzImg;
		}
		core.getCollisionManager().addBlocker(this);
	}
	
	@Override
	public void draw()
	{
		wallImg.draw(getX(), getY(), getW(), getH());
	}
}
