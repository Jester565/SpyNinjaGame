package impl;

import jgraphic.Image;

public class GameImage extends Image {
	private GameCore core;

	public GameImage(GameCore core) {
		super(core.getDisplayManager());
		this.core = core;
	}

	@Override
	public void draw(double x, double y, double w, double h)
	{
		super.draw((x - core.gameX) * (double)core.gameScaleX, (y - core.gameY) * (double)core.gameScaleY, w * (double)core.gameScaleX, h * (double)core.gameScaleY);
	}
	
	@Override
	public void draw(double x, double y)
	{
		super.draw((x - core.gameX) * (double)core.gameScaleX, (y - core.gameY) * (double)core.gameScaleY);
	}
}
