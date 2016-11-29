package impl;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;

public class ForceWave {
	private GameCore core;
	private GameImage WaveImg;
	private static final float WAVE_VELOCITY = 150;
	
	private float x;
	private float y;
	private float dX = 0;
	private float dY = 0;
	private float rads = 0;
	private DIRECTION waveDirection;
	
	public ForceWave(GameCore core, float x, float y)
	{
		this.core = core;
		if (WaveImg == null)
		{
			WaveImg = new GameImage(core);
			WaveImg.init("./resources/Imgs/Structure/forceWave.png");
			WaveImg.setRotateOriginScale(.5d, .5d);
		}
		this.x = x;
		this.y = y;
	}
	
	public DIRECTION getWaveDirection()
	{
		return waveDirection;
	}
	
	public void setDirection(DIRECTION direction)
	{
		this.waveDirection = direction;
		switch (direction)
		{
		case LEFT:
			rads = (float) Math.PI;
			dX = - WAVE_VELOCITY;
			break;
		case RIGHT:
			rads = 0;
			dX = WAVE_VELOCITY;
			break;
		case UP:
			rads = (float) ((3 * Math.PI)/2);
			dY = -WAVE_VELOCITY;
			break;
		case DOWN:
			rads = (float) (Math.PI/2);
			dY = WAVE_VELOCITY;
			break;
		}
	}
	
	public void draw()
	{
		x += dX * core.rate;
		y += dY * core.rate;
		WaveImg.setRads(rads);
		WaveImg.draw(x, y, Room.ROOM_W, Room.ROOM_W);
	}
	
	public boolean checkCollision()
	{
		Room onRoom = core.getRoomManager().getRoom(x, y);
		if (onRoom == null || onRoom instanceof ServerRoom)
		{
			return true;
		}
		return false;
	}
}
