package jphys;

public class Blocker {
	private float x;
	private float y;
	private float w;
	private float h;
	
	public Blocker()
	{
		this.x = 0;
		this.y = 0;
		this.w = 0;
		this.h = 0;
	}
	
	public Blocker(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}
	
	public void changeX(float dX)
	{
		x += dX;
	}
	
	public void changeY(float dY)
	{
		y += dY;
	}
	
	public void changeW(float dW)
	{
		w += dW;
	}
	
	public void changeH(float dH)
	{
		h += dH;
	}
	
	public void setShape(float x, float y, float w, float h)
	{
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
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
	
	boolean isBetween(float checkVal, float min, float max)
	{
		return checkVal >= min && checkVal <= max;
	}
	
	public Vector2D checkCollision(Body body, float vX, float vY)
	{
		Vector2D vec = new Vector2D(0, 0);
		boolean xOverlap = isBetween(body.getX(), x, x + w) ||
				isBetween(x, body.getX(), body.getX() + body.getW());
		boolean yOverlap = isBetween(body.getY(), y, y + h) ||
				isBetween(y, body.getY(), body.getY() + body.getH());
		if (xOverlap && yOverlap)
		{
			float upCollision = (body.getY() + body.getH()) - y;
			float leftCollision = (body.getX() + body.getW()) - x;
			float downCollision = (y + h) - body.getY();
			float rightCollision = (x + w) - body.getX();
			if (vY > 0 && upCollision < downCollision && upCollision <= leftCollision && upCollision <= rightCollision)
			{
				vec.vY = -upCollision;
			}
			else if (vY < 0 && downCollision < upCollision && downCollision <= leftCollision && downCollision <= rightCollision)
			{
				vec.vY = downCollision;
			}
			else if (vX > 0 && leftCollision < rightCollision && leftCollision <= upCollision && leftCollision <= downCollision)
			{
				vec.vX = -leftCollision;
			}
			else if (vX < 0 && rightCollision < leftCollision && rightCollision <= upCollision && rightCollision <= downCollision)
			{
				vec.vX = rightCollision;
			}
		}
		return vec;
	}
}
