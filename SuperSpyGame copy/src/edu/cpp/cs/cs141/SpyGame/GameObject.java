package edu.cpp.cs.cs141.SpyGame;

public abstract class GameObject {

	private int x;
	private int y;
	private boolean visible;

	public GameObject() {
		
	}
	
	
	public GameObject(int x, int y) {
		setLocation(x, y);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * set visible to true or false
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * get x of gameObject
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * get y of gameObject
	 */
	public int getY() {
		return y;
	}
	
	public abstract String toString(boolean isDebug);
}
