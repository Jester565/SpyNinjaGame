package impl;

import java.awt.Color;

public class Star {
	private GameCore core;
	private float x;
	private float y;
	private float radius;
	private float expandRadius;
	private float expandTimer = 0;
	private float expandTimerChange = 1;
	private Color starColor;
	private static final float EXPAND_TIME = 60;
	private static final float MAX_RADIUS = 100;
	private static final float MIN_RADIUS = 40;
	private static final float MAX_EXPAND_RADIUS = 80;
	private static final float MIN_EXPAND_RADIUS = 40;

	public Star(GameCore core, float x, float y)
	{
		this.core = core;
		this.x = x;
		this.y = y;
		starColor = new Color((float)(Math.random() * .5f + .4f), 0, 0, 1.0f);
		radius = (float) (MIN_RADIUS + (MAX_RADIUS - MIN_RADIUS) * Math.random());
		expandRadius = (float) (MIN_EXPAND_RADIUS + (MAX_EXPAND_RADIUS - MIN_EXPAND_RADIUS) * Math.random());
	}
	
	public void draw()
	{
		expandTimer += core.rate * expandTimerChange;
		if (expandTimer < 0)
		{
			expandTimer = 0;
			expandTimerChange *= -1;
		}
		if (expandTimer > EXPAND_TIME)
		{
			expandTimer = EXPAND_TIME;
			expandTimerChange *= -1;
		}
		core.getShapeRenderer().drawCircle((x - core.gameX) * core.gameScaleX, (y - core.gameY) * core.gameScaleY, (radius + expandRadius * (expandTimer/EXPAND_TIME)) * .02f, starColor);
	}
}
