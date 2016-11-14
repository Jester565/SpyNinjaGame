package edu.cpp.cs.cs141.final_proj;

public abstract class GameObject {
	
	protected int x;
	protected int y;
	protected boolean visible = false;
	private String gridRepresentation;
	
	public GameObject(String gridRepresentation)
	{
		this.gridRepresentation = gridRepresentation;
	}
	/**
	 * Set the location
	 * @return
	 */
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Check if the object is visible
	 */
	public boolean isVisible() {
		return visible || GameEngine.DebugMode;
	}
	
	/**
	 * get representation letter in the grid
	 * @return
	 */
	public String getGridRepresentation() {
		String mark = "*";
		if (isVisible()) {
			mark = gridRepresentation;
		}
		return mark;
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
