package edu.cpp.cs.cs141.final_proj;

import java.util.ArrayList;
import java.util.Random;

import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

public class Grid {
	public enum DIRECTION {
		UP, DOWN, LEFT, RIGHT
	}

	public static final int GRID_SIZE = 9;
	public static final int ROOMS_SIZE = 9;
	public static final int NINJAS_SIZE = 6;
	private Random rng = new Random();
	private GameObject[][] gameObjects = new GameObject[GRID_SIZE][GRID_SIZE];
	private Spy spy= new Spy();
	private ArrayList<Ninja> ninjas;
	private Room[] rooms = new Room[ROOMS_SIZE];

	public Grid() {
		
	}
	
	/**
	 * reset the building 
	 */
	public void reset() {
		//set the player
		setGameObject(spy, 0, GRID_SIZE - 1);
		//set rooms
		int roomIndex = 0;
		for (int rowIndex = 1; rowIndex < GRID_SIZE; rowIndex += 3) {
			for (int colIndex = 1; colIndex < GRID_SIZE; colIndex += 3) {
				rooms[roomIndex] = new Room();
				setGameObject(rooms[roomIndex], colIndex, rowIndex);
				roomIndex++;
			}
		}
		rooms[rng.nextInt(rooms.length)].setBriefCase();
		
		int diceX, diceY;
		//set invincibilityItem
		do {
			diceX = rng.nextInt(9);
			diceY = rng.nextInt(9);
		} while (!emptyGrid(diceX, diceY));
		setGameObject(new Invincibility(), diceX, diceY);
		
		//set radar item
		do {
			diceX = rng.nextInt(9);
			diceY = rng.nextInt(9);
		} while (!emptyGrid(diceX, diceY));
		setGameObject(new Radar(), diceX, diceY);
	
		//set additionalBullet item
		do {
			diceX = rng.nextInt(9);
			diceY = rng.nextInt(9);
		} while (!emptyGrid(diceX, diceY));
		setGameObject(new Bullet(), diceX, diceY);
		
		//set ninjas 
		ninjas = new ArrayList<Ninja>();
		for (int i = 0; i < NINJAS_SIZE; i ++) {
			Ninja ninja = new Ninja();
			do {
				diceX = rng.nextInt(9);
				diceY = rng.nextInt(9);
			} while(!canSetNinja(diceX, diceY));
			setGameObject(ninja, diceX, diceY);
			ninjas.add(ninja);
		}
	}
	
	/**
	 * Set the game objects in the grid
	 */
	public void setGameObject(GameObject gameObject, int x, int y) {
		gameObject.setLocation(x, y);
		gameObjects[y][x] = gameObject;
	}
	
	/**
	 * This method can get spy object
	 */
	public Spy getSpy() {
		return spy;
	}
	
	/**
	 * Accessor for the ninjas arraylist
	 */
	public ArrayList <Ninja> getNinjas() {
		return ninjas;
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
		if (moveX >= 0 && moveX < GRID_SIZE && moveY >= 0 && moveY < GRID_SIZE)
		{
			GameObject gameObj = getGameObject(moveX, moveY);
			if (gameObj == null)
			{
				return new MoveStatus(MOVE_RESULT.LEGAL, "Moved!");
			}
			else
			{
				
			}
		}
		else
		{
			return new MoveStatus(MOVE_RESULT.ILLEGAL, "Out of bounds");
		}
		return null;
	}
	
	/**
	 * Check if the grid can set ninja
	 */
	public boolean canSetNinja(int x, int y) {
		if (Math.abs(spy.getX() - x) + Math.abs(spy.getY() - y) <= 2 || gameObjects[y][x] != null) {
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
