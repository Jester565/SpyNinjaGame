package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Represents an object that can be stored and drawn by the {@link Grid}.
 */
public abstract class GameObject implements Serializable {
	private static final long serialVersionUID = 6983278314945628251L;

	/**
	 * Stores the object {@link GameObject} that {@code this} covers up.
	 */
	private GameObject belowObject = null;
	
	/**
	 * The column of the {@link Grid} the {@link GameObject} is stored on.
	 */
	protected int x;
	
	/**
	 * The row of the {@link Grid} the {@link GameObject} is stored on.
	 */
	protected int y;
	
	/**
	 * Indicates whether or not the {@link GameObject} will be shown in {@link #getGridRepresentation()}.
	 */
	protected boolean visible = false;
	
	/**
	 * Stores the characters that will be printed when {@link Grid#toString()} is called.  Set by constructor.
	 */
	protected String gridRepresentation;
	
	/**
	 * Creates an instance of {@link GameObject}.  Sets the {@link #gridRepresentation}.
	 * @param gridRepresentation The string that is used to draw on the {@link Grid}.
	 */
	public GameObject(String gridRepresentation)
	{
		this.gridRepresentation = gridRepresentation;
	}
	
	/**
	 * When a {@link Character} attempts to step onto the {@link GameObject}, this will indicate what the outcome WILL be. This will not actually move the {@link GameObject}.
	 * It only gives a {@link MoveStatus} to attempting to step onto {@code this}.
	 * @param approachDirection The direction another {@link GameObject} is approaching {@code this} from.
	 * @return A {@link MoveStatus} that indicates the legality and result of the move.
	 */
	public MoveStatus stepOn(DIRECTION approachDirection)
	{
		return new MoveStatus(MOVE_RESULT.LEGAL, "Moved");
	}
	
	/**
	 * Modifier for the {@link #x} and {@link #y} fields.
	 * @param x The column {@code this} is on.
	 * @param y THe row {@code this} is on.
	 */
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Accessor for {@link #belowObject} which stores the object {@code this} is standing on top of.
	 * @return The {@link GameObject} {@code this} is standing on.
	 */
	public GameObject getBelowObject()
	{
		return belowObject;
	}
	
	/**
	 * Modifier for {@link #belowObject}.
	 * @param gameObj Value to set {@link #belowObject} to.
	 */
	public void setBelowObject(GameObject gameObj)
	{
		this.belowObject = gameObj;
	}
	
	/**
	 * Check if the object is visible.  Influences output of {@link #getGridRepresentation()}.
	 * @return {@code true} if visible or in {@link GameEngine#DebugMode}, {@code false} otherwise.
	 */
	public boolean isVisible() {
		return visible || GameEngine.DebugMode;
	}
	
	/**
	 * Modifier for {@link #visible} field.
	 * @param mode Value to set {@link #visible} to.
	 */
	public void setVisibility(boolean mode)
	{
		visible = mode;
	}
	
	/**
	 * Gets the characters that will be drawn in the {@link Grid#toString} function.
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
	 * @return The column {@code this} is on.
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Gets the row of the {@link Grid} {@code this} is at.
	 * @return The row {@code this} is on.
	 */
	public int getY() {
		return y;
	}
}
