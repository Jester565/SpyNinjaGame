package edu.cpp.cs.cs141.final_proj;

import java.util.ArrayList;
import java.util.Random;

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
	private Random rng = new Random();
	private GameObject[][] gameObjects = new GameObject[GRID_SIZE][GRID_SIZE];
	private ArrayList<VisiblePair> visiblePairs;

	public Grid() {
		visiblePairs = new ArrayList<VisiblePair>();
	}
	
	/**
	 * reset the building 
	 */
	public void reset() {
		
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
		gameObject.setLocation(x, y);
		gameObjects[y][x] = gameObject;
	}
	
	/**
	 * Move to the direction
	 */
	public MoveStatus move(DIRECTION direction, int x, int y) {
		int moveX = x;
		int moveY = y;
		if (direction == DIRECTION.UP)
		{
			moveY--;
		}
		else if (direction == DIRECTION.RIGHT)
		{
			moveX++;
		}
		else if (direction == DIRECTION.DOWN)
		{
			moveY++;
		}
		else if (direction == DIRECTION.LEFT)
		{
			moveX--;
		}
		if (inRange(moveX, moveY))
		{
			GameObject gameObj = getGameObject(moveX, moveY);
			if (gameObj == null)
			{
				return new MoveStatus(MOVE_RESULT.LEGAL, "Moved!");
			}
			else
			{
				return gameObj.stepOn(direction);
			}
		}
		else
		{
			return new MoveStatus(MOVE_RESULT.ILLEGAL, "Out of bounds");
		}
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
	 * Get the object in the grid
	 * @param x
	 * @param y
	 * @return
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
	
	private boolean inRange(int x, int y)
	{
		return (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE);
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
