package edu.cpp.cs.cs141.final_proj;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import edu.cpp.cs.cs141.final_proj.GameEngine.GAME_OBJECT_TYPE;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Holds the 2D array containing all of the {@link #gameObjects} and provides
 * behaviors to manipulate the {@link #gameObjects}
 */
public class Grid implements Serializable {
	private static final long serialVersionUID = -8171612926598162473L;
	
	/**
	 * Stores the different directions that can be easily interpreted by the {@link #gameObjects}.
	 */
	public enum DIRECTION {
		UP("w", 0, -1), RIGHT("d", 1, 0), DOWN("s", 0, 1), LEFT("a", -1, 0);
		
		public final String keyCode;
		public final int deltaX;
		public final int deltaY;
		
		private DIRECTION(String code, int dX, int dY) {
			keyCode = code;
			deltaX = dX;
			deltaY = dY;
		}
		
		/**
		 * @return {@code {"UP", "RIGHT", "DOWN", "LEFT"}} in an ArrayList.
		 */
		public static ArrayList<String> names() {
			ArrayList<String> names = new ArrayList<String>();
			for (DIRECTION dir: DIRECTION.values()) {
				names.add(dir.name());
			}
			return names;
		}
		public static HashMap<String, DIRECTION> keyCodes() {
			HashMap<String, DIRECTION> keyCodes = new HashMap<String, DIRECTION>();
			for (DIRECTION command: DIRECTION.values()) {
				keyCodes.put(command.keyCode, command);
			}
			return keyCodes;
		}
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
	 * Stores the {@link GameObject}s that were effected by {@link #setAsVisible(int, int)} elements.
	 */
	private ArrayList<GameObject> visibleObjects;

	/**
	 * Creates instance of {@link Grid} initializing {@link #visibleObjects}.
	 */
	public Grid() {
		visibleObjects = new ArrayList<GameObject>();
	}
	
	/**
	 * If there is a {@link GameObject} at the coordinates, it is set to visible.  If there is no object, 
	 * a {@link VisibleMark} is put in the coordinates.
	 * Any element set to these coordinates is also appended to {@link #visibleObjects}.
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
				if (gameObj instanceof Room)
				{
					return false;
				}
				gameObj.setVisibility(true);
				visibleObjects.add(gameObj);
			}
			else
			{
				VisibleMark vMark = new VisibleMark();
				setGameObject(vMark, x, y);
				visibleObjects.add(vMark);
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Sets all elements in {@link #visibleObjects} as invisible. If the element is an instance of {@link VisibleMark}, it is removed from {@link #gameObjects}.
	 *  Clears {@link #visibleObjects}.
	 */
	public void setToInvisible()
	{
		for (GameObject vObj : visibleObjects)
		{
			if (vObj instanceof VisibleMark)
			{
				removeGameObject(vObj.getX(), vObj.getY());
			}
			else
			{
				vObj.setVisibility(false);
			}
		}
		visibleObjects.clear();
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
	 * Helper method - returns an array representing the coordinate of something located at (parameters) x and y after being moved
	 * in the direction of the (parameter) direction.
	 * @param direction Direction to move in.
	 * @param x current Horizontal position.
	 * @param y current Vertical position.
	 * @return array Integers representing the x and y coordinates after moving in the (parameter) direction.
	 */
	private int[] getCoordinate(DIRECTION direction, int x, int y) {
		int moveX = x + direction.deltaX;
		int moveY = y + direction.deltaY;
		int[] coord = {moveX, moveY};
		return coord;
	}
	
	/**
	 * Checks if the element at the specified coordinates in {@link #gameObjects} can move in the given direction.
	 * @param direction The {@link DIRECTION} to check if a move is possible.
	 * @param x The column in {@link #gameObjects} of the element to check the {@link MoveStatus} of.
	 * @param y THe row in {@link #gameObjects} of the elemnt to check the {@link MoveStatus} of.
	 * @return {@link MoveStatus} given when attempting to move a {@link GameObject}
	 * in a {@link DIRECTION}.
	 */
	public MoveStatus checkMoveStatus(DIRECTION direction, int x, int y) {
		int[] coord = getCoordinate(direction, x, y);
		int moveX = coord[0]; 
		int moveY = coord[1];
		if (inRange(moveX, moveY)) {
			GameObject gameObj = getGameObject(moveX, moveY);
			if (gameObj == null) {
				return new MoveStatus(MOVE_RESULT.LEGAL, "Moved!");
			}
			else {
				return gameObj.stepOn(direction);
			}
		}
		return new MoveStatus(MOVE_RESULT.ILLEGAL, "Out of bounds");
	}
	
	/**
	 * Moves the element at the coordinates in {@link #gameObjects} in the direction specified.
	 * @param direction The {@link DIRECTION} to move the {@link GameObject} in.
	 * @param x The column of the element to move.
	 * @param y The row of the element to move.
	 */
	public void move(DIRECTION direction, int x, int y) {
		int[] coord = getCoordinate(direction, x, y);
		int moveX = coord[0]; 
		int moveY = coord[1];
		move(x, y, moveX, moveY);
	}
	
	/**
	 * Moves the element at the coordinates x and y in {@link #gameObjects} to the coordinates moveX and moveY in {@link #gameObjects}.
	 * @param x The column of the element to move.
	 * @param y The row of the element to move.
	 * @param moveX The column to move the element to.
	 * @param moveY The row to move the element to.
	 */
	public void move(int x, int y, int moveX, int moveY)
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
	 * @param x The column to check if a {@link Ninja} can be placed.
	 * @param y The row to check if a {@link Ninja} can be placed.
	 * @return {@code true} if the {@link Ninja} can be put at the coordinates, {@code false} otherwise.
	 */
	public boolean canSetNinja(int x, int y) {
		if ((Math.abs(Spy.INITIAL_X - x) <= 2 && Math.abs(Spy.INITIAL_Y - y) <= 2) || getGameObject(x, y) != null) {
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
		if (inRange(x,y)) {
			return gameObjects[y][x];
		} else {
			return null;
		}
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
	 * @param x The column to check if in range.
	 * @param y The row to check if in range.
	 * @return {@code true} if inside the bounds, {@code false} otherwise.
	 */
	public boolean inRange(int x, int y)
	{
		return (x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE);
	}
	
	/**
	 * Appends the {@link #gameObjects} to a String.
	 * @return A visualization of the {@link #gameObjects}. Generated by calling {@link GameObject#getGridRepresentation()} on each element.
	 */
	public String toString() {
		String gridString = "";
		String gridFill = "*";
		if (GameEngine.DebugMode)
			gridFill = " ";
		
		for (GameObject[] row : gameObjects) {
			for (GameObject obj : row) {
				gridString += obj == null ? "[" + gridFill + "]" : "[" + obj.getGridRepresentation() + "]";
			}
			gridString += "\n";
		}
		return gridString;
	}
	
	/**
	 * Iterates over {@link #gameObjects} and generates each element to their corresponding {@link GAME_OBJECT_TYPE} based on their type. And stores them in a 2D array with the same dimensions
	 * as {@link #gameObjects}.
	 * @return A 2D array of {@link GAME_OBJECT_TYPE}s that represents the types of {@link #gameObjects}.
	 */
	public GAME_OBJECT_TYPE[][] getTypes()
	{
		GAME_OBJECT_TYPE[][] typeGrid = new GAME_OBJECT_TYPE[GRID_SIZE][GRID_SIZE];
		for (int i = 0; i < GRID_SIZE; i++)
		{
			for (int j = 0; j < GRID_SIZE; j++)
			{
				if (gameObjects[i][j] == null || gameObjects[i][j] instanceof VisibleMark)
				{
					typeGrid[j][i] = null;
				}
				else if (gameObjects[i][j] instanceof Ninja)
				{
					typeGrid[j][i] = GAME_OBJECT_TYPE.ENEMY;
				}
				else if (gameObjects[i][j] instanceof Spy)
				{
					typeGrid[j][i] = GAME_OBJECT_TYPE.PLAYER;
				}
				else if (gameObjects[i][j] instanceof Room)
				{
					if (((Room)gameObjects[i][j]).hasBriefcase())
					{
						typeGrid[j][i] = GAME_OBJECT_TYPE.BRIEFCASE;
					}
					else
					{
						typeGrid[j][i] = GAME_OBJECT_TYPE.ROOM;
					}
				}
				else if (gameObjects[i][j] instanceof Bullet)
				{
					typeGrid[j][i] = GAME_OBJECT_TYPE.BULLET;
				}
				else if (gameObjects[i][j] instanceof Radar)
				{
					typeGrid[j][i] = GAME_OBJECT_TYPE.RADAR;
				}
				else if (gameObjects[i][j] instanceof Invincibility)
				{
					typeGrid[j][i] = GAME_OBJECT_TYPE.INVINCIBILITY;
				}
			}
		}
		return typeGrid;
	}
	
	/**
	 * Iterates over {@link #gameObjects} and converts each element to a boolean representing their visiblity. And stores them in a 2D array with the same dimensions
	 * as {@link #gameObjects}.
	 * @return A 2D array of booleans that represents the visibility of elements in {@link #gameObjects}.
	 */
	public boolean[][] getVisibility()
	{
		boolean[][] visibleGrid = new boolean[GRID_SIZE][GRID_SIZE];
		for (int i = 0; i < GRID_SIZE; i++)
		{
			for (int j = 0; j < GRID_SIZE; j++)
			{
				if (gameObjects[i][j] != null)
				{
					visibleGrid[j][i] = gameObjects[i][j].isVisible();
				}
				else
				{
					visibleGrid[j][i] = false;
				}
			}
		}
		return visibleGrid;
	}
	
	/**
	 * Called when deserializing a Grid object.  Uses the default deserialization but calls setToInvisible() so the look turn is undone.
	 * @param ois The input stream to parse from.
	 * @throws ClassNotFoundException Thrown when a class is not found or cannot be parsed properly.
	 * @throws IOException Thrown when something is wrong with the ois.
	 */
	private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException 
	{
	   ois.defaultReadObject();
	   setToInvisible();
	}
}
