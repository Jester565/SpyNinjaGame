package jgraphic;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

public class InputManager implements MouseListener, KeyListener, MouseMotionListener{
	
	private ArrayList<KeyEvent> typedEventsSave;
	private ArrayList<KeyEvent> typedEvents;
	private ArrayList<Character> pressedKeys;
	private boolean mouseDown = false;
	private boolean mouseClicked = false;
	private boolean mouseClickedSave = false;
	private double mouseClickX = 0;
	private double mouseClickY = 0;
	private double mouseX = 0;
	private double mouseY = 0;
	private DisplayManager dm;
	
	TextField activeTextField;
	
	InputManager(DisplayManager dm)
	{
		this.dm = dm;
		typedEvents = new ArrayList<KeyEvent>();
		typedEventsSave = new ArrayList<KeyEvent>();
		pressedKeys = new ArrayList<Character>();
	}
	
	public void update()
	{
		if (mouseClickedSave)
		{
			mouseClicked = true;
		}
		else
		{
			mouseClicked = false;
			mouseClickX = -1000;
			mouseClickY = -1000;
		}
		if (typedEventsSave.size() > 0)
		{
			typedEvents = new ArrayList <KeyEvent>(typedEventsSave);
		}
		else
		{
			typedEvents.clear();
		}
		mouseClickedSave = false;
		typedEventsSave.clear();
	}
	
	public boolean isKeyTyped(char c)
	{
		for (int i = 0; i < typedEvents.size(); i++)
		{
			if (typedEvents.get(i).getKeyChar() == Character.toLowerCase(c) || typedEvents.get(i).getKeyChar() == Character.toUpperCase(c))
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean isKeyPressed(char c)
	{
		if (isCharPressed(Character.toLowerCase(c)))
		{
			return true;
		}
		return isCharPressed(Character.toUpperCase(c));
	}
	
	public boolean isCharPressed(char c)
	{
		return activeTextField == null && (pressedKeys.contains(c) || isCharTyped(c));
	}
	
	public boolean isCharTyped(char c)
	{
		return activeTextField == null && typedEvents.contains(c);
	}
	
	public void keyPressed(KeyEvent event) {
		if (!pressedKeys.contains(event.getKeyChar()))
		{
			pressedKeys.add((Character)event.getKeyChar());
		}
		event.consume();
	}

	public void keyReleased(KeyEvent event) {
		pressedKeys.remove((Character)event.getKeyChar());
		typedEventsSave.add(event);
		event.consume();
	}

	public void keyTyped(KeyEvent event) {
	}

	public void mouseClicked(MouseEvent event) {
		event.consume();
	}

	public void mouseEntered(MouseEvent event) {
		event.consume();
	}

	public void mouseExited(MouseEvent event) {
		event.consume();
	}

	public void mousePressed(MouseEvent event) {
		mouseDown = true;
		event.consume();
	}
	
	public void mouseReleased(MouseEvent event) {
		mouseDown = false;
		mouseClickedSave = true;
		mouseClickX = event.getX();
		mouseClickY = event.getY();
		event.consume();
	}
	
	public void mouseDragged(MouseEvent event) {
		mouseX = event.getX();
		mouseY = event.getY();
		event.consume();
	}

	public void mouseMoved(MouseEvent event) {
		mouseX = event.getX();
		mouseY = event.getY();
		event.consume();
	}
	
	public double getScaleMouseX()
	{
		return ((double)mouseX - dm.screenXOff)/dm.screenWScale;
	}
	
	public double getScaleMouseY()
	{
		return ((double)mouseY - dm.screenYOff)/dm.screenHScale;
	}
	
	public double getScaleMouseClickX()
	{
		return ((double)mouseClickX - dm.screenXOff)/dm.screenWScale;
	}
	
	public double getScaleMouseClickY()
	{
		return ((double)mouseClickY - dm.screenYOff)/dm.screenHScale;
	}
	
	public boolean isMouseClicked()
	{
		return mouseClicked;
	}
	
	public boolean isMouseDown()
	{
		return mouseClicked || mouseDown;
	}
	
	ArrayList<KeyEvent> getTypedEvents()
	{
		return typedEvents;
	}
}
