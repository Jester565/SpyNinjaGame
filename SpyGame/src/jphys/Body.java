package jphys;

public abstract class Body {
	private float x;
	private float y;
	private float w;
	private float h;
	private CollisionManager collisionManager;
	
	public Body(CollisionManager collisionManager, float x, float y, float w, float h)
	{
		this.collisionManager = collisionManager;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public void move(float vX, float vY)
	{
		if (vX != 0 || vY != 0)
		{
			x += vX;
			y += vY;
			Vector2D collisionVector = collisionManager.checkMove(this, vX, vY);
			x += collisionVector.vX;
			y += collisionVector.vY;
		}
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public float getW()
	{
		return w;
	}
	
	public float getH()
	{
		return h;
	}
}
