package jgraphic;

import java.awt.BasicStroke;
import java.awt.Color;

import jgraphic.DisplayManager;

public class ShapeRenderer {
	public ShapeRenderer(DisplayManager dm)
	{
		this.dm = dm;
	}
	
	public void drawRect(double x, double y, double w, double h, float r, float g, float b, float a)
	{
		dm.graphics.setColor(new Color(r,g,b,a));
		dm.graphics.fillRect((int)(x * dm.screenWScale + dm.screenXOff), (int)(y * dm.screenHScale + dm.screenYOff), (int)(w * dm.screenWScale), (int)(h * dm.screenHScale));
	}
	

	public void drawRect(double x, double y, double w, double h, Color color)
	{
		dm.graphics.setColor(color);
		dm.graphics.fillRect((int)(x * dm.screenWScale + dm.screenXOff), (int)(y * dm.screenHScale + dm.screenYOff), (int)(w * dm.screenWScale), (int)(h * dm.screenHScale));
	}
	
	public void drawRectOutline(double x, double y, double w, double h, float r, float g, float b, float a)
	{
		dm.graphics.setColor(new Color(r,g,b,a));
		dm.graphics.drawRect((int)(x * dm.screenWScale + dm.screenXOff), (int)(y * dm.screenHScale + dm.screenYOff), (int)(w * dm.screenWScale), (int)(h * dm.screenHScale));
	}
	
	public void drawCircle(float x, float y, float radius, float r, float g, float b, float a)
	{
		dm.graphics.setColor(new Color(r,g,b,a));
		dm.graphics.fillOval((int)((x-radius) * dm.screenWScale + dm.screenXOff), (int)((y-radius) * dm.screenHScale + dm.screenYOff), (int)((radius*2) * dm.screenWScale), (int)((radius*2) * dm.screenHScale));
	}
	
	public void drawCircle(float x, float y, float radius, Color color)
	{
		dm.graphics.setColor(color);
		dm.graphics.fillOval((int)((x-radius) * dm.screenWScale + dm.screenXOff), (int)((y-radius) * dm.screenHScale + dm.screenYOff), (int)((radius*2) * dm.screenWScale), (int)((radius*2) * dm.screenHScale));
	}
	
	public void drawCircleOutline(float x, float y, float radius, float strokeW, float r, float g, float b, float a)
	{
		dm.graphics.setColor(new Color(r,g,b,a));
		dm.graphics.setStroke(new BasicStroke((int)strokeW));
		dm.graphics.drawOval((int)((x-radius) * dm.screenWScale + dm.screenXOff), (int)((y-radius) * dm.screenHScale + dm.screenYOff), (int)((radius*2) * dm.screenWScale), (int)((radius*2) * dm.screenHScale));
	}
	
	public void drawCircleFillOutlined(int x, int y, int radius, float fillR, float fillG, float fillB, float fillA, float outR, float outG, float outB, float outA)
	{
		drawCircle(x, y, radius, fillR, fillG, fillB, fillA);
		drawCircleOutline(x, y, radius, 2, outR, outG,outB, outA);
	}
	
	public void drawLine(float x1, float y1, float x2, float y2, float strokeW, float r, float g, float b, float a)
	{
		dm.graphics.setColor(new Color(r,g,b,a));
		dm.graphics.setStroke(new BasicStroke((int)strokeW));
		dm.graphics.drawLine((int)(x1 * dm.screenWScale + dm.screenXOff), (int)(y1 * dm.screenHScale + dm.screenYOff), (int)(x2 * dm.screenWScale + dm.screenXOff), (int)(y2 * dm.screenHScale + dm.screenYOff));
	}
	
	private DisplayManager dm;
}
