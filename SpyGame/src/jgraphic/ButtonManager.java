package jgraphic;

import jgraphic.Core;
import jgraphic.InputManager;

public class ButtonManager {
	public ButtonManager(Core core)
	{
		this.core = core;
	}
	
	public boolean overRect(double x, double y, double w, double h)
	{
		InputManager im = core.getInputManager();
		return (im.getScaleMouseX() > x && im.getScaleMouseX() < x + w && im.getScaleMouseY() > y && im.getScaleMouseY() < y + h);
	}
	
	public boolean buttonClicked(double x, double y, double w, double h)
	{
		if (overRect(x, y, w, h))
		{
			if (core.getInputManager().isMouseClicked())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean buttonPressed(double x, double y, double w, double h)
	{
		if (overRect(x, y, w, h))
		{
			if (core.getInputManager().isMouseDown())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean buttonClicked(double x, double y, double w, double h, float r, float g, float b, float a)
	{
		if (overRect(x, y, w, h))
		{
			if (core.getInputManager().isMouseClicked())
			{
				smartRectColorChange(x, y, w, h, r, g, b, a, .5f);
				return true;
			}
			else
			{
				smartRectColorChange(x, y, w, h, r, g, b, a, .5f);
			}
		}
		else
		{
			core.getShapeRenderer().drawRect(x, y, w, h, r, g, b, a);
		}
		return false;
	}
	
	public boolean buttonPressed(double x, double y, double w, double h, float r, float g, float b, float a)
	{
		if (overRect(x, y, w, h))
		{
			if (core.getInputManager().isMouseDown())
			{
				smartRectColorChange(x, y, w, h, r, g, b, a, .5f);
				return true;
			}
			else
			{
				smartRectColorChange(x, y, w, h, r, g, b, a, .5f);
			}
		}
		else
		{
			core.getShapeRenderer().drawRect(x, y, w, h, r, g, b, a);
		}
		return false;
	}
	
	private void smartRectColorChange(double x, double y, double w, double h, float r, float g, float b, float a, float off)
	{
		if (r + g + b > .8)
		{
			r -= off;
			g -= off;
			b -= off;
			if (r < 0)
				r = 0;
			if (g < 0)
				g = 0;
			if (b < 0)
				b = 0;
		}
		else
		{
			r += off;
			g += off;
			b += off;
			if (r > 1)
				r= 1;
			if (g > 1)
				g= 1;
			if (b > 1)
				b = 1;
		}
		core.getShapeRenderer().drawRect(x, y, w, h, r, g, b, a);
	}
	
	private Core core;
}
