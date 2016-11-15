package edu.cpp.cs.cs141.final_proj;

import java.util.ArrayList;
import java.util.Random;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;

public class GameEngine {
	public static boolean DebugMode = false;
	
	public static final int ROOMS_SIZE = 9;
	public static final int NINJAS_SIZE = 6;
	private Random rng = new Random();
	private Grid grid = new Grid();
	private boolean gameWin = false;
	private boolean gameFinished = false;
	
	private Spy spy= new Spy();
	private ArrayList<Ninja> ninjas = new ArrayList<Ninja>();
	
	/**
	 * reset the building 
	 */
	public void reset() {
		//set the player
		grid.setGameObject(spy, Spy.INITIAL_X, Spy.INITIAL_Y);
		//set rooms
		int briefRoomIndex = rng.nextInt();
		int roomIndex = 0;
		for (int rowIndex = 1; rowIndex < Grid.GRID_SIZE; rowIndex += 3) {
			for (int colIndex = 1; colIndex < Grid.GRID_SIZE; colIndex += 3) {
				Room room = new Room(roomIndex == briefRoomIndex);
				grid.setGameObject(room, colIndex, rowIndex);
				roomIndex++;
			}
		}
		
		int diceX, diceY;
		//set invincibilityItem
		do {
			diceX = rng.nextInt(9);
			diceY = rng.nextInt(9);
		} while (!grid.emptyGrid(diceX, diceY));
		grid.setGameObject(new Invincibility(), diceX, diceY);
		
		//set radar item
		do {
			diceX = rng.nextInt(9);
			diceY = rng.nextInt(9);
		} while (!grid.emptyGrid(diceX, diceY));
		grid.setGameObject(new Radar(), diceX, diceY);
	
		//set additionalBullet item
		do {
			diceX = rng.nextInt(9);
			diceY = rng.nextInt(9);
		} while (!grid.emptyGrid(diceX, diceY));
		grid.setGameObject(new Bullet(), diceX, diceY);
		
		//set ninjas 
		for (int i = 0; i < NINJAS_SIZE; i ++) {
			Ninja ninja = new Ninja();
			do {
				diceX = rng.nextInt(9);
				diceY = rng.nextInt(9);
			} while(!grid.canSetNinja(diceX, diceY));
			grid.setGameObject(ninja, diceX, diceY);
			ninjas.add(ninja);
		}
	}
	
	/**
	 * Check if the player win?
	 */
	public boolean checkWinCondition() {
		gameWin = false;
		//if (grid.getSpy().hasBriefCase()) gameWin = true;
		return gameWin;
	}
	
	/**
	 * Player Turn
	 */
	public void playerLook(DIRECTION lookDirection) {
		switch (lookDirection)
		{
		case UP:
			playerLook(0, -1, Spy.LOOK_RANGE);
			break;
		case RIGHT:
			playerLook(1, 0, Spy.LOOK_RANGE);
			break;
		case DOWN:
			playerLook(0, 1, Spy.LOOK_RANGE);
			break;
		case LEFT:
			playerLook(-1, 0, Spy.LOOK_RANGE);
			break;
		default:
			System.err.println("Invalid look option");	
		}
	}
	
	private void playerLook(int dX, int dY, int range)
	{
		int sX = spy.getX();
		int sY = spy.getY();
		for (int i = 0; i < range; i++)
		{
			sX += dX;
			sY += dY;
			if (!grid.setAsVisible(sX, sY))
			{
				return;
			}
		}
	}
	
	/**
	 * Enemy Turn
	 */
	public void enemyTurn() {
		
	}
	
	/**
	 * Enable the debug mode
	 */
	public void enableDebug() {
		
	}
	
	
	
	/**
	 * Set six rooms
	 */
	public void setRooms() {
		
	}
	
	/**
	 * Set new map
	 */
	public void setMap() {
		
	}
	
	/**
	 * Save the file
	 */
	public void save() {
		
	}
	
	/**
	 * Load the exited game
	 */
	public void load() {
		
	}
	
	public void quitGame() {
		
	}
	
	/**
	 * Display the game board
	 */
	public String displayBoard() {
		return grid.toString();
	}
}
