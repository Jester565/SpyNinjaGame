package jgraphic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class DisplayManager {
	public static final double DISPLAY_DEFAULT_W = 1920d;
	public static final double DISPLAY_DEFAULT_H = 1080d;
	protected static final int SCREEN_Y_OFF_DEFAULT = 0;
	protected static final int SCREEN_X_OFF_DEFAULT = 0;
	
	private boolean antiAliasing = true;
	private boolean isFullScreen = false;
	private Color backGroundColor = new Color(0, 0, 0, 1);
	
	DisplayManager()
	{
		
	}
	
	public boolean isFullScreen()
	{
		return isFullScreen;
	}
	
	public void setWindowed()
	{
		frame.dispose();
		frame.setLocation(0, 0);
		frame.setUndecorated(false);
		frame.setIgnoreRepaint(true);
		frame.setResizable(true);
		frame.setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int)screenSize.getWidth(), (int)((29.0 * screenSize.getHeight())/30.0));
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.createBufferStrategy(2);
		buffStrat = null;
		isFullScreen = false;
	}
	
	public void setFullScreen()
	{
		frame.dispose();
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
		frame.setVisible(true);
		frame.createBufferStrategy(2);
		buffStrat = null;
		isFullScreen = true;
	}
	
	public void setAntiAliasing()
	{
		antiAliasing = true;
	}
	
	public boolean getAntiAliasing()
	{
		return antiAliasing;
	}
	
	boolean init(InputManager im, String windowTitle)
	{
		frame = new JFrame(windowTitle);
		dca = new DisplayComponentAdapter(this);
		frame.addComponentListener(dca);
		frame.addMouseListener(im);
		frame.addMouseMotionListener(im);
		frame.addKeyListener(im);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocation(0, 0);
		frame.setUndecorated(false);
		frame.setIgnoreRepaint(true);
		frame.setResizable(true);
		frame.setVisible(true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setSize((int)screenSize.getWidth(), (int)((29.0 * screenSize.getHeight())/30.0));
		frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
		frame.createBufferStrategy(2);
		updateScreenSize();
		return true;
	}
	
	public void setBackgroundColor(float r, float g, float b, float a)
	{
		frame.getContentPane().setBackground(new Color(r, g, b, a));
		backGroundColor = new Color(r, g, b, a);
		frame.setBackground(backGroundColor);
	}
	
	void update()
	{
		if (buffStrat != null)
		{
			if (graphics != null)
			{
				graphics.dispose();
			}
			buffStrat.show();
		}
		BufferStrategy tempBuffStrat = frame.getBufferStrategy();
		if (tempBuffStrat != null)
		{
			buffStrat = tempBuffStrat;
			graphics = (Graphics2D) buffStrat.getDrawGraphics();
			if (antiAliasing)
			{
				graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			}
			graphics.clearRect(screenXOff, screenYOff, (int)(DISPLAY_DEFAULT_W * screenWScale), (int)(DISPLAY_DEFAULT_H * screenHScale));
			graphics.setColor(backGroundColor);
			graphics.drawRect(0, 0, (int)(DISPLAY_DEFAULT_W * screenWScale), (int)(DISPLAY_DEFAULT_H * screenHScale));
		}
	}
	
	void updateScreenSize()
	{
		Dimension actualSize = frame.getContentPane().getSize();
		screenW = actualSize.getWidth();
		screenH = actualSize.getHeight();
		screenWScale = screenW/DISPLAY_DEFAULT_W;
		screenHScale = screenH/DISPLAY_DEFAULT_H;
		screenYOff = frame.getInsets().top;
		screenXOff = frame.getInsets().left;
	}
	
	void close()
	{
		frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	public double screenW = DISPLAY_DEFAULT_W;
	public double screenH = DISPLAY_DEFAULT_H;
	public double screenWScale = 1d;
	public double screenHScale = 1d;
	public int screenYOff = SCREEN_Y_OFF_DEFAULT;
	public int screenXOff = SCREEN_X_OFF_DEFAULT;
	public Graphics2D graphics;
	public JFrame frame;
	private DisplayComponentAdapter dca;
	private BufferStrategy buffStrat;
}