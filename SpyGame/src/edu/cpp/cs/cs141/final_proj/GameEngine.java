package edu.cpp.cs.cs141.final_proj;

import java.util.ArrayList;
import java.util.Random;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

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
	 *Describes the status of the game: unfinished, won, or lost.
	 */
	public enum GAME_STATE {
		UNFINISHED, WON, LOST;
	}
	/**
	 * Describes the current status of the game.
	 */
	private GAME_STATE gameStatus;
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
	private Grid grid;
	
	/**
	 * The {@link Character} controlled by the user.
	 */
	private Spy spy;
	
	/**
	 * Stores all of the {@link Ninja}s in the {@link #grid}.
	 */
	private ArrayList<Ninja> ninjas;
	
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
		//initialize objects
		grid = new Grid();
		spy = new Spy();
		ninjas = new ArrayList<Ninja>();
		
		//set gameStatus as unfinished
		gameStatus = GAME_STATE.UNFINISHED;
		//set debugMode as false
		DebugMode = false;
		
		//set the player
		grid.setGameObject(spy, Spy.INITIAL_X, Spy.INITIAL_Y);
		
		//set rooms
		setRooms();
		
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
	 * Set six rooms
	 */
	public void setRooms() {
		int briefRoomIndex = rng.nextInt(ROOMS_SIZE);
		int roomIndex = 0;
		for (int rowIndex = 1; rowIndex < Grid.GRID_SIZE; rowIndex += 3) {
			for (int colIndex = 1; colIndex < Grid.GRID_SIZE; colIndex += 3) {
				Room room = new Room(roomIndex == briefRoomIndex);
				grid.setGameObject(room, colIndex, rowIndex);
				roomIndex++;
			}
		}
	}
	
	/**
	 * Checks status of the game
	 * @return {@link #gameStatus} to check current status
	 */
	public GAME_STATE getGameStatus(){
		return gameStatus;
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
	 * Checks if the player is allowed to move in any direction. Note that this includes MOVE_RESULT.NOMOVE because they can still use their turn.
	 * @return {@code true} if player can move, {@code false} otherwise.
	 */
	public boolean playerMoveable()
	{
		//Gets all types of the DIRECTION enumerator as an array {UP, LEFT, DOWN, RIGHT}
		DIRECTION[] directions = DIRECTION.values();
		for (DIRECTION direction : directions)
		{	
			//If MOVE_RESULT is not illegal then the player can use the turn
			if (grid.checkMoveStatus(direction, spy.getX(), spy.getY()).moveResult != MOVE_RESULT.ILLEGAL)	
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Moves the {@link #spy} in the direction specified. If the player found the briefcase, 
	 * then the game is finished and the game was won.
	 * @param direction The direction to move the {@link #spy} in.
	 * @return The {@link MoveStatus} which indicates if the move successful and a message 
	 * correlated to the action.
	 */
	public MoveStatus playerMove(DIRECTION direction)
	{
		int spyX = spy.getX(); 
		int spyY = spy.getY();
		MoveStatus moveStatus = grid.checkMoveStatus(direction, spyX, spyY);
		
		if (moveStatus.moveResult == MOVE_RESULT.LEGAL) { 
			grid.move(direction, spyX, spyY);
			grid.setToInvisible();
		}
			
		if (moveStatus.moveResult == MOVE_RESULT.WIN)
			gameStatus = GAME_STATE.WON;
		
		return moveStatus;
	}
	
	/**
	 * The {@link #spy} shoots in the direction specified.
	 * @param direction The direction to shoot in.
	 * @return {@code true} is enemy hit, {@code} false otherwise.
	 */
	public boolean playerShoot(DIRECTION direction)
	{
		return spy.getGun().attack(direction, spy, grid);
	}

	
	/**
	 * Updates the spy's attributes when upon using powerups.
	 */
	public void updateSpy(){
		spy.usePowerups();
	}
	
	/**
	 * Handles the enemie's AI and movement.  Called after the user has taken their turn. A ninja will
	 * check all 4 possible directions (randomly) until a valid move can be made.
	 */
	public void enemyTurn() {
		for (int i = 0; i < ninjas.size(); i++)
		{
			ArrayList<DIRECTION> directionArray = new ArrayList<DIRECTION>();
			directionArray.add(DIRECTION.DOWN);
			directionArray.add(DIRECTION.UP);
			directionArray.add(DIRECTION.LEFT);
			directionArray.add(DIRECTION.RIGHT);
			int randomNum, ninX, ninY;
			DIRECTION currentDir;
			MoveStatus moveStatus;
			ninX = ninjas.get(i).getX();
			ninY = ninjas.get(i).getY();
			if(Math.abs(spy.getX() - ninX) + Math.abs(spy.getY() - ninY) <= 1){
				gameStatus = GAME_STATE.LOST;
				return;
			}
			while (directionArray.size() > 0)
			{
				randomNum = rng.nextInt(directionArray.size());
				currentDir = directionArray.get(randomNum);
				moveStatus = grid.checkMoveStatus(currentDir, ninX, ninY);
				if (moveStatus.moveResult == MOVE_RESULT.LEGAL)
				{
					grid.move(currentDir, ninX, ninY);
					break;
				}
				else
				{
					directionArray.remove(randomNum);
				}
			}
			directionArray.clear();
			DIRECTION direction = null;
			enemyStab(i, direction);
		}
	}
	
	public boolean enemyStab(int i, DIRECTION direction){

		return ninjas.get(i).getSword().attack( direction, ninjas.get(i), grid);
	}
	
	/**
	 * @return {@link #spy}
	 */
	public Spy getSpy() {
		return spy;
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
		String whiteSpacePadding = String.format("%3s", "");
		ArrayList<String> spyInfoMessages = new ArrayList<String>();
		spyInfoMessages.add("Lives: " + spy.getLives() + "\n");
		spyInfoMessages.add("Ammo: " + spy.getGun().getNumRounds() + "\n");
		spyInfoMessages.add("Invincible Turns: " + spy.getInvincibleTurns() + "\n");
		spyInfoMessages.add("Radar: " + (spy.hasRadar() ? "Enabled": "Disabled") + "\n");
		
		String board = "";
		String gridString = grid.toString();
		int msgIndex = 0;
		
		for (String line: gridString.split("\n")) {
			board += line 
					+ (msgIndex < spyInfoMessages.size() ? 
							whiteSpacePadding + spyInfoMessages.get(msgIndex): "\n");
			msgIndex++;
		}
		return board;
	}
}
