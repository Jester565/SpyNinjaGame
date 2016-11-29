package jgraphic;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Image {
	public Image(DisplayManager dm)
	{
		this.dm = dm;
		trans = new AffineTransform();
	}
	
	public boolean init(BufferedImage img)
	{
		this.img = img;
		return true;
	}
	
	public boolean init(String filePath)
	{
		InputStream is = getClass().getClassLoader().getResourceAsStream(filePath);
		try {
			img = ImageIO.read(is);
			return true;
		} catch (IOException e) {
			System.err.println("Could not find the image " + filePath + " when creating an image");
		}
		return false;
	}
	
	public boolean init(File file)
	{
		try {
			img = ImageIO.read(file);
			return true;
		} catch (IOException e) {
			System.err.println("Could not find the image " + file.getAbsolutePath() + " when creating an image");
		}
		return false;
	}
	
	public void setDegs(double degs)
	{
		rads = (Math.PI/180d) * degs;
	}

	public void addDegs(double deltaDegs)
	{
		rads += (Math.PI/180d) * deltaDegs;
	}
	
	public double getDegs()
	{
		return rads * (180d/Math.PI);
	}
	
	public void setRads(double rads)
	{
		this.rads = rads;
	}
	
	public void addRads(double deltaRads)
	{
		rads += deltaRads;
	}
	
	public double getRads()
	{
		return rads;
	}
	
	public void setRotateOriginPixels(int imgX, int imgY)
	{
		origX = imgX;
		origY = imgY;
	}
	
	public void setRotateOriginScale(double imgScaleX, double imgScaleY)
	{
		origX = imgScaleX * img.getWidth();
		origY = imgScaleY * img.getHeight();
	}
	
	public void drawScale(double x, double y, double wS, double hS)
	{
		trans = new AffineTransform();
		trans.translate(x * dm.screenWScale + dm.screenXOff, y * dm.screenHScale + dm.screenYOff);
		trans.rotate(rads, origX * wS * dm.screenWScale, origY * hS * dm.screenHScale);
		trans.scale(wS * dm.screenWScale, hS * dm.screenHScale);
		dm.graphics.drawImage(img, trans, null);
	}
	
	public void draw(double x, double y, double w, double h)
	{
		trans = new AffineTransform();
		trans.translate(x * dm.screenWScale + dm.screenXOff, y * dm.screenHScale + dm.screenYOff);
		trans.rotate(rads, origX * (w/img.getWidth()) * dm.screenWScale, origY * (h/img.getHeight()) * dm.screenHScale);
		trans.scale((w / img.getWidth()) * dm.screenWScale, (h / img.getHeight()) * dm.screenHScale);
		dm.graphics.drawImage(img, trans, null);
	}
	
	public void draw(double x, double y)
	{
		trans = new AffineTransform();
		trans.translate(x * dm.screenWScale + dm.screenXOff, y * dm.screenHScale + dm.screenYOff);
		trans.rotate(rads, origX * dm.screenWScale, origY * dm.screenHScale);
		trans.scale(dm.screenWScale, dm.screenHScale);
		dm.graphics.drawImage(img, trans, null);
	}
	
	public BufferedImage img;
	protected double rads = 0;
	protected double origX = 0;
	protected double origY = 0;
	protected DisplayManager dm;
	protected AffineTransform trans;
}