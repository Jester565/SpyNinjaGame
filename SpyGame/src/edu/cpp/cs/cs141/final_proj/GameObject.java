package edu.cpp.cs.cs141.final_proj;

public abstract class GameObject {
	
	private int x;
	private int y;
	private boolean visible = false;
	private boolean debugType = false;
	private String gameObjectType;
	
	/**
	 * Set the location
	 * @return
	 */
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Set the game object typeÍ
	 */
	public void setObjectType(String type) {
		switch(type) {
		case "R":
			visible = true;
			gameObjectType = "R";
			break;
		case "n":
			gameObjectType = "n";
			break;
		case "r":
			gameObjectType = "r";
			break;
		case "i":
			gameObjectType = "i";
			break;
		case "a":
			gameObjectType = "a";
			break;
		case "s":
			visible = true;
			gameObjectType = "s";
			break;
		}
	}
	
	/**
	 * Check if the object is visible
	 */
	public boolean isVisible() {
		return visible;
	}
	
	/**
	 * get representation letter in the grid
	 * @return
	 */
	public String getGridRepresentation() {
		String mark = "*";
		if (visible) mark = gameObjectType;
		return mark;
	}
	
	public String getGameObjectType() {
		return gameObjectType;
	}
	
	/**
	 * move up
	 */
	public void moveUp() {
		y -= 1;
	}
	
	/**
	 * move down
	 */
	public void moveDown() {
		y += 1;
	}
	
	/**
	 * move left
	 */
	public void moveLeft() {
		x -= 1;
	}
	
	/**
	 * move right
	 */
	public void moveRight() {
		x += 1;
	}
	
	/**
	 * Get location x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Get location y
	 */
	public int getY() {
		return y;
	}
}
