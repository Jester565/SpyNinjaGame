package jgraphic;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class TextRenderer {
	private DisplayManager dm;
	private HashMap<Float, Font> fontSizeMap;
	private HashMap<String, HashMap<Float, Font>> fonts;
	
	public TextRenderer(DisplayManager dm)
	{
		this.dm = dm;
		fonts = new HashMap<String, HashMap<Float, Font>>();
	}
	
	public int getTextWidth(String msg)
	{
		FontMetrics fm = dm.graphics.getFontMetrics();
		return (int) (fm.stringWidth(msg));
	}
	
	public int getTextWidth(String msg, float fontSize)
	{
		fontSize *= (float)(dm.screenHScale * dm.screenWScale);
		if (setFontToSize(fontSize))
		{
			return getTextWidth(msg);
		}
		return 0;
	}
	
	public int getTextWidth(String msg, String fontName, float fontSize)
	{
		if (setFont(fontName))
		{
			return getTextWidth(msg, fontSize);
		}
		return 0;
	}
	
	private boolean setFontToSize(float size)
	{
		Font f = getFontFromSize(size);
		if (f == null)
		{
			f = addFontSize(size);
		}
		if (f != null)
		{
			if (f != dm.graphics.getFont())
			{
				dm.graphics.setFont(f);
			}
			return true;
		}
		return false;
	}
	
	private Font getFontFromSize(float size)
	{
		if (fontSizeMap != null)
		{
			return fontSizeMap.get(size);
		}
		return null;
	}
	
	private Font addFontSize(float size)
	{
		if (fontSizeMap != null && !fontSizeMap.containsKey(size))
		{
			Font f = fontSizeMap.get(0f).deriveFont(size);
			fontSizeMap.put(size, f);
			return f;
		}
		return null;
	}
	
	public boolean setFont(String fontName)
	{
		if (dm == null || dm.graphics == null || dm.graphics.getFont() == null || fontName != dm.graphics.getFont().getName())
		{
			fontSizeMap = fonts.get(fontName);
			if (fontSizeMap == null)
			{
				System.err.println("FAILED TO FIND FONT NAME");
				return false;
			}
		}
		return true;
	}
	
	public String loadFont(String fontPath)
	{
		InputStream is = getClass().getClassLoader().getResourceAsStream(fontPath);
		if (is == null)
		{
			System.err.println("Could not find font " + fontPath);
			return null;
		}
		BufferedInputStream bis = new BufferedInputStream(is);
		try {
			Font f = Font.createFont(Font.TRUETYPE_FONT, bis);
			int lastSlashI = fontPath.lastIndexOf('/');
			int lastPeriodI = fontPath.lastIndexOf('.');
			if (lastSlashI == -1)
			{
				lastSlashI = 0;
			}
			if (lastPeriodI == -1)
			{
				lastPeriodI = fontPath.length();
			}
			fonts.put(f.getName(), new HashMap<Float, Font>());
			fonts.get(f.getName()).put(0f, f);
			return f.getName();
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void drawText(String msg, double x, double y, float fontSize, float r, float g, float b, float a)
	{
		fontSize *= (float)(dm.screenHScale * dm.screenWScale);
		if (setFontToSize(fontSize))
		{
			dm.graphics.setColor(new Color(r, g, b, a));
			dm.graphics.drawString(msg, (int)(x * dm.screenWScale + dm.screenXOff), (int)(y * dm.screenHScale + dm.screenYOff));
		}
	}
	
	public void drawText(String msg, double x, double y, String fontName, float fontSize, float r, float g, float b, float a)
	{
		if (setFont(fontName))
		{
			fontSize *= (float)(dm.screenHScale * dm.screenWScale);
			if (setFontToSize(fontSize))
			{
				dm.graphics.setColor(new Color(r, g, b, a));
				dm.graphics.drawString(msg, (int)(x * dm.screenWScale) + dm.screenXOff, (int)(y * dm.screenHScale + dm.screenYOff));
			}
		}
	}
	

	public void drawCenteredText(String msg, double x, double y, float fontSize, float r, float g, float b, float a)
	{
		fontSize *= (float)(dm.screenHScale * dm.screenWScale);
		if (setFontToSize(fontSize))
		{
			dm.graphics.setColor(new Color(r, g, b, a));
			dm.graphics.drawString(msg, (int)((x - getTextWidth(msg)/2d) * dm.screenWScale + dm.screenXOff), (int)(y * dm.screenHScale + dm.screenYOff));
		}
	}
	
	public void drawCenteredText(String msg, double x, double y, String fontName, float fontSize, float r, float g, float b, float a)
	{
		if (setFont(fontName))
		{
			fontSize *= (float)(dm.screenHScale * dm.screenWScale);
			if (setFontToSize(fontSize))
			{
				dm.graphics.setColor(new Color(r, g, b, a));
				dm.graphics.drawString(msg, (int)((x - getTextWidth(msg)/2d) * dm.screenWScale + dm.screenXOff), (int)(y * dm.screenHScale + dm.screenYOff));
			}
		}
	}
}
