package impl;

import jgraphic.Image;

public abstract class Item {
	protected GameCore core;
	
	protected GameImage itemImg;
	protected Image blueprintImg;
	
	public Item(GameCore core)
	{
		this.core = core;
	}
	
	public void draw(float x, float y, float w, float h)
	{
		itemImg.draw(x, y, w, h);
	}
	
	public void drawBlueprint(float x, float y, float w, float h)
	{
		blueprintImg.draw(x, y, w, h);
	}
	
	public abstract boolean isExpired();
	public abstract void appyEffect();
}
