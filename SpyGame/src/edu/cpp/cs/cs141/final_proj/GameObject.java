package edu.cpp.cs.cs141.final_proj;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Represents an object that can be stored and drawn by the {@link Grid}.
 */
public abstract class GameObject {
	/**
	 * The column of the grid the {@link GameObject} is stored on.
	 */
	protected int x;
	
	/**
	 * The row of the grid the {@link GameObject} is stored on.
	 */
	protected int y;
	
	/**
	 * Indicates whether or not the {@link GameObject} will be shown in {@link #getGridRepresentation()}.
	 */
	protected boolean visible = false;
	
	/**
	 * Stores the characters that will be drawn to the {@link Grid}.  Set by constructor.
	 */
	private String gridRepresentation;
	
	/**
	 * Creates an intance of {@link GameObject}.  Sets the {@link #gridRepresentation}.
	 * @param gridRepresentation
	 */
	public GameObject(String gridRepresentation)
	{
		this.gridRepresentation = gridRepresentation;
	}
	
	/**
	 * When a {@link Character} attempts to step onto the {@link GridObject}, this will indicate the result of it.
	 * @param approachDirection The direction the {@link Character} is approaching from.
	 * @return A {@link MoveStatus} that indicates the status of the move.
	 */
	public MoveStatus stepOn(DIRECTION approachDirection)
	{
		return new MoveStatus(MOVE_RESULT.LEGAL, "Moved");
	}
	/**
	 * Modifier for the {@link #x} and {@link #y} fields.
	 */
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Check if the object is visible.  Influences output of {@link #getGridRepresentation()}.
	 */
	public boolean isVisible() {
		return visible || GameEngine.DebugMode;
	}
	
	public void setVisibility(boolean mode)
	{
		visible = mode;
	}
	
	/**
	 * Gets the characters that will be drawn in the {@link Grid}.
	 * @return The characters representing the {@link GameObject}.
	 */
	public String getGridRepresentation() {
		String mark = "*";
		if (isVisible()) {
			mark = gridRepresentation;
		}
		return mark;
	}
	
	/**
	 * Gets the column of the {@link Grid} {@code this} is at.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the row of the {@link Grid} {@code this} is at.
	 */
	public int getY() {
		return y;
	}
}
