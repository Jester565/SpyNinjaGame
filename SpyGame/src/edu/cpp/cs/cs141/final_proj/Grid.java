package edu.cpp.cs.cs141.final_proj;

import java.util.ArrayList;

import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

public class Grid {
	private class VisiblePair
	{
		public VisiblePair(boolean isMark, GameObject gameObj)
		{
			this.isMark = isMark;
			this.gameObject = gameObj;
		}
		public boolean isMark;
		public GameObject gameObject;
	}
	
	public enum DIRECTION {
		UP, DOWN, LEFT, RIGHT
	}

	public static final int GRID_SIZE = 9;
	private GameObject[][] gameObjects = new GameObject[GRID_SIZE][GRID_SIZE];
	private ArrayList<VisiblePair> visiblePairs;

	public Grid() {
		visiblePairs = new ArrayList<VisiblePair>();
	}
	
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
	 * Sets the {@link GameObject} at the position to null.
	 */
	public void removeGameObject(int x, int y) {
		gameObjects[y][x] = null;
	}
	
	/**
	 * Set the game objects in the grid
	 */
	public void setGameObject(GameObject gameObject, int x, int y) {
		if (gameObject != null)
		{
			gameObject.setLocation(x, y);
		}
		gameObjects[y][x] = gameObject;
	}
	
	/**
	 * Move to the direction
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
	 * @param x coordinate to select object to move
	 * @param y coordinate to select object to move 
	 * @param moveX new x coordinate for object 
	 * @param moveY new y coordinate for object
	 */
	private void move(int x, int y, int moveX, int moveY)
	{
		GameObject movingObject = getGameObject(x, y);
		GameObject moveToObject = getGameObject(moveX, moveY);
		if (movingObject != null)
		{
			setGameObject(movingObject.getOnTopObject(), x, y);
		}
		setGameObject(movingObject, moveX, moveY);
		movingObject.setOnTopObject(moveToObject);
	}
	
	/**
	 * Check if the grid can set ninja
	 */
	public boolean canSetNinja(int x, int y) {
		if (Math.abs(Spy.INITIAL_X - x) + Math.abs(Spy.INITIAL_Y - y) <= 2 || gameObjects[y][x] != null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Get the object from a position on grid
	 * @param x
	 * @param y
	 * @return {@link GameObject} located with parameters x and y on grid
	 */
	public GameObject getGameObject(int x, int y) {
		return gameObjects[y][x];
	}
	
	/**
	 * Check if there are any object in the grid
	 */
	public boolean emptyGrid(int x, int y) {
		return getGameObject(x, y) == null ? true : false;
	}
	
	/**
	 * Checks if x and y values are in range of the grid or out of bounds
	 * @param x
	 * @param y 
	 * @return {@code true} if x is in the horizontal & y is in the vertical
	 * range on the grid 
	 */
	private boolean inRange(int x, int y)
	{
		return (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE);
	}
	
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
	 * Display the grid to String
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
