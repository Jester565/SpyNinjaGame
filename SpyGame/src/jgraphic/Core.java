package jgraphic;

import jgraphic.ButtonManager;
import jgraphic.DisplayManager;
import jgraphic.InputManager;
import jgraphic.ShapeRenderer;
import jgraphic.TextRenderer;

public abstract class Core {
	public final double DEFAULT_FRAME_RATE = 60;
	
	public Core()
	{
		
	}
	
	public void setFrameRateCap(int frameRate)
	{
		this.frameRateCap = frameRate;
	}
	
	public void run()
	{
		if (init("NO NAME"))
		{
			while (true)
			{
				long startTime = System.currentTimeMillis();
	        	dm.update();
	        	im.update();
	        	if (dm.graphics != null)
	        	{
	        		draw();
	        	}
	        	long timeLeft = (long)(1000.0/frameRateCap - (System.currentTimeMillis() - startTime));
	        	if (timeLeft > 0)
	        	{
	        		try {
						Thread.sleep(timeLeft);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	        	}
	        	rate = (System.currentTimeMillis() - startTime) / (1000.0/logicalFrameRate);
	        }
		}
		else
		{
			System.err.println("Init of the Core failed");
		}
		dm.close();
	}
	
	public ShapeRenderer getShapeRenderer()
	{
		return sr;
	}
	
	public DisplayManager getDisplayManager()
	{
		return dm;
	}
	
	public InputManager getInputManager()
	{
		return im;
	}
	
	public ButtonManager getButtonManager()
	{
		return bm;
	}
	
	public TextRenderer getTextRenderer()
	{
		return tr;
	}
	
	protected boolean init(String windowName)
	{
		System.setProperty("sun.java2d.opengl", "true");
		dm = new DisplayManager();
		im = new InputManager(dm);
        if (!dm.init(im, windowName))
        {
        	return false;
        }
        dm.setBackgroundColor(0, 0, 0, 1);
        sr = new ShapeRenderer(dm);
        tr = new TextRenderer(dm);
        bm = new ButtonManager(this);
		return true;
	}
	
	abstract protected void draw();
	
	protected TextRenderer tr;
	protected ShapeRenderer sr;
	protected DisplayManager dm;
	protected InputManager im;
	protected ButtonManager bm;
	public double logicalFrameRate = DEFAULT_FRAME_RATE;
	public double frameRateCap = DEFAULT_FRAME_RATE;
	public double rate = 1;
}