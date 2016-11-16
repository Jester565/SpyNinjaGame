package edu.cpp.cs.cs141.final_proj;

import java.util.ArrayList;
import java.util.Random;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;

/**
 * Handles high level game logic and user movement.
 */
public class GameEngine {
	/**
	 * Modifier for {@link #DebugMode}.  While not necessary to modify, the intent is more clear.
	 * @param mode The value to set {@link #DebugMode} to.
	 */
	public static void SetDebugMode(boolean mode)
	{
		DebugMode = mode;
	}
	
	/**
	 * {@code true} will make all elements in the {@link Grid} visible.
	 */
	public static boolean DebugMode = false;
	
	/**
	 * The amount of {@link Room}s in the {@link #grid}.
	 */
	public static final int ROOMS_SIZE = 9;
	
	/**
	 * The maximum number of {@link Ninja}s in the {@link #ninjas} array and inside of the {@link #grid}.
	 */
	public static final int NINJAS_SIZE = 6;
	
	/**
	 * Random number generator used to populate the {@link #grid}.
	 */
	private Random rng = new Random();
	
	/**
	 * Stores the environment the game is played in.
	 */
	private Grid grid = new Grid();
	
	/**
	 * Indicates whether the game has been won, {@code false} if lost.  Only set properly if {@link #gameFinished} is {@code true}.
	 */
	private boolean gameWin = false;
	
	/**
	 * Indicates whether the game has been finished.
	 */
	private boolean gameFinished = false;
	
	/**
	 * The {@link Character} controlled by the user.
	 */
	private Spy spy= new Spy();
	
	/**
	 * Stores all of the {@link Ninja}s in the {@link #grid}.
	 */
	private ArrayList<Ninja> ninjas = new ArrayList<Ninja>();
	
	/**
	 * Resets the visibility of the {@link #grid}.
	 */
	public void resetVisibility()
	{
		grid.setToInvisible();
	}
	
	/**
	 * Populates the {@link #grid} with {@link #spy}, {@link #ninjas}, and {@link Useable}s.
	 */
	public void reset() {
		//set the player
		grid.setGameObject(spy, Spy.INITIAL_X, Spy.INITIAL_Y);
		//set rooms
		int briefRoomIndex = rng.nextInt(ROOMS_SIZE);
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
			diceX = rng.nextInt(Grid.GRID_SIZE);
			diceY = rng.nextInt(Grid.GRID_SIZE);
		} while (!grid.emptyGrid(diceX, diceY));
		grid.setGameObject(new Invincibility(), diceX, diceY);
		
		//set radar item
		do {
			diceX = rng.nextInt(Grid.GRID_SIZE);
			diceY = rng.nextInt(Grid.GRID_SIZE);
		} while (!grid.emptyGrid(diceX, diceY));
		grid.setGameObject(new Radar(), diceX, diceY);
	
		//set additionalBullet item
		do {
			diceX = rng.nextInt(Grid.GRID_SIZE);
			diceY = rng.nextInt(Grid.GRID_SIZE);
		} while (!grid.emptyGrid(diceX, diceY));
		grid.setGameObject(new Bullet(), diceX, diceY);
		
		//set ninjas
		ninjas.clear();
		for (int i = 0; i < NINJAS_SIZE; i ++) {
			Ninja ninja = new Ninja();
			do {
				diceX = rng.nextInt(Grid.GRID_SIZE);
				diceY = rng.nextInt(Grid.GRID_SIZE);
			} while(!grid.canSetNinja(diceX, diceY));
			grid.setGameObject(ninja, diceX, diceY);
			ninjas.add(ninja);
		}
	}
	
	/**
	 * Check if the player has won.
	 * @return {@code true} if the player has won, {@code false} otherwise.
	 */
	public boolean checkWinCondition() {
		return gameWin;
	}
	
	/**
	 * Checks if the game is finished.
	 * @return {@code true} if the game is over, {@code false} otherwise.
	 */
	public boolean isGameFinished()
	{
		return gameFinished;
	}
	
	/**
	 * The {@link GameObject}s next to the {@link #spy} are set to visible.
	 * @param lookDirection The direction to look in.
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
	
	/**
	 * Sets the {@link GameObject}s in the vector specified to visible using {@link Grid#setAsVisible(int, int)}.
	 * @param dX The change in x from the {@link #spy} to set to visible.
	 * @param dY The change in y from the {@link #spy} to set to visible.
	 * @param range The amount of elements from the {@link #spy} to set to visible.
	 */
	private void playerLook(int dX, int dY, int range)
	{
		int sX = spy.getX();
		int sY = spy.getY();
		for (int i = 0; i < range; i++)
		{
			sX += dX;
			sY += dY;
			// if dX and dY can not be set as visible (i.e.: Room or out of bounds), return;
			if (!grid.setAsVisible(sX, sY))
			{
				return;
			}
		}
	}
	
	/**
	 * Moves the {@link #spy} in the direction specified.
	 * @param direction The direction to move the {@link #spy} in.
	 * @return The {@link MoveStatus} which indicates if the move successful and a message correlated to the action.
	 */
	public MoveStatus playerMove(DIRECTION direction)
	{
		return grid.move(direction, spy.getX(), spy.getY());
	}
	
	/**
	 * The {@link #spy} shoots in the direction specified.
	 * @param direction The direction to shoot in.
	 * @return {@code true} is enemy hit, {@code} false otherwise.
	 */
	public boolean playerShoot(DIRECTION direction)
	{
		return true;
	}
	
	/**
	 * Handles the enemie's AI and movement.  Called after the user has taken their turn.  Resets visibility of {@link #grid}.
	 */
	public void enemyTurn() {
		grid.setToInvisible();
	}
	
	/**
	 * Set six rooms
	 */
	public void setRooms() {
		
	}
	
	/**
	 * Saves the file to the fileName specified by the parameter.
	 * @param fileDir The directory to save in.
	 */
	public void save(String fileDir) {
		
		
		
	}
	
	/**
	 * Loads a previous session of the game from the directory specified.
	 * @param fileDir The file to load from.
	 */
	public void load(String fileDir) {
		
	}
	
	/**
	 * Shows the {@link Grid} in the String returned.
	 * @return The String holding the drawing of the {@link Grid}.
	 */
	public String displayBoard() {
		return grid.toString();
	}
}
