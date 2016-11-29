package jgraphic;

public class FPSLogger {
	private static final int FRAME_UPDATE_RATE_MILLIS = 1000;
	private long lastUpdateMillis;
	private int frameCount = 0;
	private String frameStr = "NA";
	private Core core;
	
	public FPSLogger(Core core)
	{
		this.core = core;
		lastUpdateMillis = System.currentTimeMillis();
	}
	
	public void draw(float x, float y, float fontSize, float r, float g, float b, float a)
	{
		frameCount++;
		if (System.currentTimeMillis() > lastUpdateMillis + FRAME_UPDATE_RATE_MILLIS)
		{
			lastUpdateMillis = System.currentTimeMillis();
			frameStr = Integer.toString(frameCount);
			frameCount = 0;
		}
		core.getTextRenderer().drawText(frameStr, x, y, fontSize, r, g, b, a);
	}
}
