package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;
import java.util.ArrayList;

import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

public class Grid implements Serializable {
	/**
	 * Nested class to associate a {@link GameObject} with a boolean indicating if it is a {@link VisibleMark}.
	 */
	private class VisiblePair implements Serializable
	{
		/**
		 * Creates instance of visible pair setting the parameter's corresponding attributes.
		 * @param isMark {@link #isMark} is set to this value.
		 * @param gameObj {@link #gameObject} is set to this value.
		 */
		public VisiblePair(boolean isMark, GameObject gameObj)
		{
			this.isMark = isMark;
			this.gameObject = gameObj;
		}
		
		/**
		 * {@code true} if {@link GameObject} is a {@link VisibleMark}, {@code false} otherwise.
		 */
		public boolean isMark;
		
		/**
		 * Stores a {@link GameObject}.  Cannot be {@code null}.
		 */
		public GameObject gameObject;
	}
	
	/**
	 * Stores the different directions that can be easily interpreted by the {@link #gameObjects}.
	 */
	public enum DIRECTION {
		UP, DOWN, LEFT, RIGHT
	}
	
	/**
	 * The amount of rows and columns in {@link #gameObjects}.
	 */
	public static final int GRID_SIZE = 9;
	
	/**
	 * Stores the {@link #gameObjects} to represent an environment.  Elements can be {@code null}.
	 */
	private GameObject[][] gameObjects = new GameObject[GRID_SIZE][GRID_SIZE];
	
	/**
	 * Stores the {@link GameObject}s set to visible inside of the {@link VisiblePair} elements.
	 */
	private ArrayList<VisiblePair> visiblePairs;

	/**
	 * Creates instance of {@link Grid} initializing {@link #visiblePairs}.
	 */
	public Grid() {
		visiblePairs = new ArrayList<VisiblePair>();
	}
	
	/**
	 * If there is a {@link GameObject} at the coordinates, it is set to visible.  If there is no object, a {@link VisibleMark} is put in the coordinates.
	 * Any element set to these coordinates is also appended to {@link #visiblePairs}.
	 * @param x The column of the element to set to visible.
	 * @param y The row of the element to set to visible.
	 * @return {@code true} if the position was set to visible, {@code false} otherwise.
	 */
	public boolean setAsVisible(int x, int y)
	{
		if (inRange(x, y))
		{
			GameObject gameObj = getGameObject(x, y);
			if (gameObj != null)
			{
				gameObj.setVisibility(true);
				visiblePairs.add(new VisiblePair(false, gameObj));
			}
			else
			{
				VisibleMark vMark = new VisibleMark();
				setGameObject(vMark, x, y);
				visiblePairs.add(new VisiblePair(true, vMark));
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Sets all elements that were set to visible as invisible.  Clears {@link #visiblePairs}.
	 */
	public void setToInvisible()
	{
		for (VisiblePair vPair : visiblePairs)
		{
			if (vPair.isMark)
			{
				removeGameObject(vPair.gameObject.getX(), vPair.gameObject.getY());
			}
			else
			{
				vPair.gameObject.setVisibility(false);
			}
		}
		visiblePairs.clear();
	}
	
	/**
	 * Sets the element in {@link #gameObjects} at the coordinates to {@code null}.
	 * @param x The column of the element to set to null.
	 * @param y The row of the element to set to null.
	 */
	public void removeGameObject(int x, int y) {
		gameObjects[y][x] = null;
	}
	
	/**
	 * Sets the element in {@link #gameObjects} at the coordinates to the gameObject parameter.
	 * @param gameObject Value to set the element to.
	 * @param x The column of the element to set to gameObject.
	 * @param y The row of the element to set to gameObject.
	 */
	public void setGameObject(GameObject gameObject, int x, int y) {
		if (gameObject != null)
		{
			gameObject.setLocation(x, y);
		}
		gameObjects[y][x] = gameObject;
	}
	
	/**
	 * Moves the element at the coordinates in the direction specified.
	 * @param direction The direction to move the element in.
	 * @param x The column of the element to move.
	 * @param y The column of the element to move.
	 * @return The status of the move represented as a {@link MoveStatus}.
	 */
	public MoveStatus move(DIRECTION direction, int x, int y) {
		int moveX = x;
		int moveY = y;
		switch (direction)
		{
		case UP:
			moveY--;
			break;
		case RIGHT:
			moveX++;
			break;
		case DOWN:
			moveY++;
			break;
		case LEFT:
			moveX--;
			break;
		default:
			System.err.println("INVALID DIRECTION IN MOVE");
		}
		if (inRange(moveX, moveY))
		{
			GameObject gameObj = getGameObject(moveX, moveY);
			if (gameObj == null)
			{
				move(x, y, moveX, moveY);
				return new MoveStatus(MOVE_RESULT.LEGAL, "Moved!");
			}
			else
			{
				MoveStatus status = gameObj.stepOn(direction);
				if (status.moveResult == MOVE_RESULT.LEGAL || status.moveResult == MOVE_RESULT.POWERUP)
				{
					removeVisibleMark(moveX, moveY);
					move(x, y, moveX, moveY);
				}
				return status;
			}
		}
		else
		{
			return new MoveStatus(MOVE_RESULT.ILLEGAL, "Out of bounds");
		}
	}
	
	/**
	 * Moves the element at the coordinates x and y to the coordinates moveX and moveY.
	 * @param x The column of the element to move.
	 * @param y The row of the element to move.
	 * @param moveX The column to move the element to.
	 * @param moveY The row to move the element to.
	 */
	private void move(int x, int y, int moveX, int moveY)
	{
		GameObject movingObject = getGameObject(x, y);
		GameObject moveToObject = getGameObject(moveX, moveY);
		if (movingObject != null)
		{
			setGameObject(movingObject.getBelowObject(), x, y);
		}
		setGameObject(movingObject, moveX, moveY);
		movingObject.setBelowObject(moveToObject);
	}
	
	/**
	 * Checks if a {@link Ninja} can be placed at the coordinates specified.
	 * @param x The column to put the {@link Ninja}.
	 * @param y The row to put the {@link Ninja}.
	 * @return {@code true} if the {@link Ninja} can be put at the coordinates, {@code false} otherwise.
	 */
	public boolean canSetNinja(int x, int y) {
		if (Math.abs(Spy.INITIAL_X - x) + Math.abs(Spy.INITIAL_Y - y) <= 2 || gameObjects[y][x] != null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Get the object from {@link #gameObjects} at the specified coordinates.
	 * @param x The column to get the object from.
	 * @param y The row to get the object from.
	 * @return The {@link GameObject} at the position.  Can be {@code null}.
	 */
	public GameObject getGameObject(int x, int y) {
		return gameObjects[y][x];
	}
	
	/**
	 * Check if there are any {@link GameObject}s in {@link #gameObjects} at the coordinates.
	 * @param x The column to check.
	 * @param y The row to check.
	 * @return {@code true} if the element is null, {@code false} otherwise.
	 */
	public boolean emptyGrid(int x, int y) {
		return getGameObject(x, y) == null ? true : false;
	}
	
	/**
	 * Checks if the coordinates are inside the bounds of {@link #gameObjects}.
	 * @param x The column.
	 * @param y The row.
	 * @return {@code true} if inside the bounds, {@code false} otherwise.
	 */
	private boolean inRange(int x, int y)
	{
		return (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE);
	}
	
	/**
	 * Sets the {@link #gameObjects} element at the position to {@code null} if it is a {@link VisibleMark}.
	 * @param x The column of the element to check.
	 * @param y The row of the element to check.
	 * @return {@code true} if the element was a {@link VisibleMark} and was removed successfully.  {@code false} otherwise.
	 */
	private boolean removeVisibleMark(int x, int y)
	{
		for (int i = 0; i < visiblePairs.size(); i++)
		{
			if (visiblePairs.get(i).isMark && visiblePairs.get(i).gameObject.getX() == x && visiblePairs.get(i).gameObject.getY() == y)
			{
				visiblePairs.remove(i);
				removeGameObject(x, y);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Draws the {@link #gameObjects} to String.
	 */
	public String toString() {
		String result = "";
		for (GameObject[] row : gameObjects) {
			for (GameObject m : row) {
				String gridFill = "*";
				if (GameEngine.DebugMode)
				{
					gridFill = " ";
				}
				else
				{
					gridFill = "*";
				}
				result += m == null ? "[" + gridFill + "]" : "[" + m.getGridRepresentation() + "]";
			}
			result += "\n";
		}
		return result;
	}
}
