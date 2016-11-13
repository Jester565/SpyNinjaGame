package edu.cpp.cs.cs141.final_proj;

import java.util.ArrayList;
import java.util.Random;

public class Grid {

	public static final int GRID_SIZE = 9;
	private GameObject[][] gameObjects = new GameObject[GRID_SIZE][GRID_SIZE];
	private Random rng = new Random();
	private Spy spy= new Spy();
	private ArrayList<Ninja> ninjas;
	private Ninja[] ninja = new Ninja[6];
	private Room[] rooms = new Room[9];
	private InvincibilityItem invincibilityItem = new InvincibilityItem();
	private Radar radar = new Radar();
	private AdditionalBullet additionalBullet = new AdditionalBullet();
	private int roomsNumber = 0;	//Check how many rooms have been set
	private int ninjaNumber = 0;
	//private GameObject[] ninjas = new Ninja[ninjaNumber];
	public static enum OBJECT_KIND {
		R, N, I, S
	};

	/**
	 * reset the building 
	 */
	public void reset() {
		//set the player
		setGameObject(spy, 0, 8);
		
		//set rooms 
		for(int i = 0; i < rooms.length; i ++) {
			rooms[i] = new Room();
		}
		setGameObject(rooms[0], 1, 1);
		setGameObject(rooms[1], 1, 4);
		setGameObject(rooms[2], 1, 7);
		setGameObject(rooms[3], 4, 1);
		setGameObject(rooms[4], 4, 4);
		setGameObject(rooms[5], 4, 7);
		setGameObject(rooms[6], 7, 1);
		setGameObject(rooms[7], 7, 4);
		setGameObject(rooms[8], 7, 7);
		int dice = rng.nextInt(9);
		rooms[dice].setBriefCase();
		
		//set invincibilityItem
		int diceX = rng.nextInt(9);
		int diceY = rng.nextInt(9);
		while(!emptyGrid(diceX, diceY)) {
			diceX = rng.nextInt(9);
			diceY = rng.nextInt(9);
		}
		setGameObject(invincibilityItem, diceX, diceY);
		
		//set radar item
		diceX = rng.nextInt(9);
		diceY = rng.nextInt(9);
		while(!emptyGrid(diceX, diceY)) {
			diceX = rng.nextInt(9);
			diceY = rng.nextInt(9);
		}
		setGameObject(radar, diceX, diceY);
	
		//set additionalBullet item
		diceX = rng.nextInt(9);
		diceY = rng.nextInt(9);
		while(!emptyGrid(diceX, diceY)) {
			diceX = rng.nextInt(9);
			diceY = rng.nextInt(9);
		}
		setGameObject(additionalBullet, diceX, diceY);
		
		//set ninjas 
		for (int i = 0; i < 6; i ++) {
			ninjas.add(new Ninja());
		}	
		for (int i = 0; i < ninjas.size(); i ++) {
			diceX = rng.nextInt(9);
			diceY = rng.nextInt(9);
			while(!canSetNinja(diceX, diceY)) {
				diceX = rng.nextInt(9);
				diceY = rng.nextInt(9);
			}
			ninjas.get(i).setLocation(diceX, diceY);
		}
	}
	
	/**
	 * Set the game objects in the grid
	 */
	public void setGameObject(GameObject gameObject, int x, int y) {
		gameObject.setLocation(x, y);
		gameObjects[x][y] = gameObject;
	}
	
	/**
	 * Make the object in the grid
	 */
	public void setGameObject(String type, int x, int y) {
		GameObject newObject = null;
		if (gameObjects[x][y] == null) {
			switch(type) {
			case "R":
				newObject = new Room();
				rooms[roomsNumber] = (Room)newObject;
				roomsNumber += 1;
				break;
			case "n":
				newObject = new Ninja();
				break;
			case "i":
				newObject = new InvincibilityItem();
				break;
			case "r":
				newObject = new Radar();
				break;
			case "a":
				newObject = new AdditionalBullet();
				break;
			case "s":
				newObject = new Spy();
				break;
			}

			gameObjects[x][y] = newObject;
		}
	}
	
	/**
	 * Set the briefcase in the room
	 */
	public void putBriefcaseInRoom(int roomNumber)	{
		rooms[roomNumber].setBriefCase();
	}
	
	/**
	 * This method can get spy object
	 */
	public Spy getSpy() {
		return spy;
	}
	
	/**
	 * This method can get ninja object
	 */
	public Ninja getNinja(int i) {
		return ninja[i];
	}
	
	/**
	 * This method can get invincibilityItem object.
	 */
	public InvincibilityItem getInvincibilityItem() {
		return invincibilityItem;
	}
	
	/**
	 * This method can get radar object.
	 */
	public Radar getRadar() {
		return radar;
	}
	
	/**
	 * This method can get additionalBullet item.
	 */
	public AdditionalBullet getAdditionlBullet() {
		return additionalBullet;
	}
	
	/**
	 * move gameObject up
	 */
	public void moveUpObject(GameObject object) {
		object.moveUp();
	}
	
	/**
	 * move gameObject down
	 */
	public void moveDownObject(GameObject object) {
		object.moveDown();
	}
	/**
	 * move gameObject left
	 */
	public void moveLeftObject(GameObject object) {
		object.moveLeft();
	}
	/**
	 * move gameObject right
	 */
	public void moveRightObject(GameObject object) {
		object.moveRight();
	}
	
	
	/**
	 * Just access {@link #ninjas} instead of whatever this method does
	 * get ninja in the grid
	 */
//	public Ninja getNinja() {
//		Ninja ninja = ninjas[]
//		return ninjas
//	}
	
	/**
	 * set the ninjas in the grid
	 */
	public void setNinja() {
		for (Ninja ninja: ninjas) {
			gameObjects[ninja.getY()][ninja.getX()] = ninja;
		}
	}
	
	/**
	 * Move to the direction
	 */
	public MOVE_STATUS move(DIRECTION direction, int x, int y) {
		return MOVE_STATUS.illegalMove;
	}
	
	/**
	 * Check if the grid can set ninja
	 */
	public boolean canSetNinja(int x, int y) {
		boolean result = true;
		if(x == spy.getX() & spy.getY() - y < 3)
			result = false;
		if(y == spy.getY() & y - spy.getX() < 3)
			result = false;
		if(gameObjects[x][y] != null) 
			result = false;
		return result;
	}
	
	/**
	 * Get the object in the grid
	 * @param i
	 * @param j
	 * @return
	 */
	public GameObject getGameObject(int i, int j) {
		return gameObjects[i][j];
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
				result += m == null ? "[ ]" : "[" + m.getGridRepresentation() + "]";
			}

			result += "\n";
		}

		return result;
	}
}
