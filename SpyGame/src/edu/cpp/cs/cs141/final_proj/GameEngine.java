package edu.cpp.cs.cs141.final_proj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Handles high level game logic and user movement.
 */
public class GameEngine {
	
	private static String[] LOOK_MESSAGES = new String[] {"Something is blocking your vision", "Clear", "Ninja Ahead!"};
	
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
	 * The {@link Room} that contains the briefcase.
	 */
	private Room briefcaseRoom;
	
	/**
	 * The {@link Character} controlled by the user.
	 */
	private Spy spy;
	
	/**
	 * Stores all of the {@link Ninja}s in the {@link #grid}.
	 */
	private ArrayList<Ninja> ninjas;
	
	/**
	 * Hard mode
	 */
	public static boolean HardMode = false;
	
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
		
		//set gameStatus as unfinished
		gameStatus = GAME_STATE.UNFINISHED;
		//set debugMode as false
		DebugMode = false;
		HardMode = false;
		//set the player
		setPlayer();
		//set rooms
		setRooms();
		//three items
		setItems();
		//set ninjas
		setNinjas();
	}
	
	/**
	 * Reset the grid when player is stabbed, back to original point when player still have lives 
	 */
	public void setSpyBackToInitialState()	{
		spy.aliveAgain();
		grid.removeGameObject(spy.getX(), spy.getY());
		grid.setGameObject(spy.getBelowObject(), spy.getX(), spy.getY());
		spy.setBelowObject(null);
		for (int i = 0; i < ninjas.size(); i++)
		{
			if (Math.abs(ninjas.get(i).getX() - Spy.INITIAL_X) <= 2 && Math.abs(ninjas.get(i).getY() - Spy.INITIAL_Y) <= 2)
			{
				int diceX;
				int diceY;
				do {
					diceX = rng.nextInt(Grid.GRID_SIZE);
					diceY = rng.nextInt(Grid.GRID_SIZE);
				} while(!grid.canSetNinja(diceX, diceY));
				grid.move(ninjas.get(i).getX(), ninjas.get(i).getY(), diceX, diceY);
			}
		}
		setPlayer();
	}
	
	/**
	 * Set the player
	 */
	private void setPlayer()	{
		grid.setGameObject(spy, Spy.INITIAL_X, Spy.INITIAL_Y);
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
				if (roomIndex == briefRoomIndex)
				{
					briefcaseRoom = room;
				}
				grid.setGameObject(room, colIndex, rowIndex);
				roomIndex++;
			}
		}
	}
	
	/**
	 * Set three items.
	 */
	public void setItems() {
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
	}
	
	/**
	 * Set the enemies
	 */
	public void setNinjas() {
		ninjas = new ArrayList<Ninja>();
		int diceX, diceY;
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
	public String playerLook(DIRECTION lookDirection) {
		return playerLook(lookDirection.deltaX, lookDirection.deltaY, Spy.LOOK_RANGE);
	}
	
	/**
	 * Sets the {@link GameObject}s in the vector specified to visible using {@link Grid#setAsVisible(int, int)}.
	 * @param dX The change in x from the {@link #spy} to set to visible.
	 * @param dY The change in y from the {@link #spy} to set to visible.
	 * @param range The amount of elements from the {@link #spy} to set to visible.
	 */
	private String playerLook(int dX, int dY, int range)
	{
		int msgI = 0;
		int sX = spy.getX();
		int sY = spy.getY();
		for (int i = 0; i < range; i++)
		{
			sX += dX;
			sY += dY;
			if (!grid.setAsVisible(sX, sY))
			{
				break;
			}
			GameObject gObj = grid.getGameObject(sX, sY);
			if (gObj != null)
			{
				if (gObj instanceof Ninja && msgI < 2)
				{
					msgI = 2;
				}
				else if (msgI < 1)
				{
					msgI = 1;
				}
			}
			else if (msgI < 1)
			{
				msgI = 1;
			}
		}
		return LOOK_MESSAGES[msgI];
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
			grid.setToInvisible();  //You messed up the order
			grid.move(direction, spyX, spyY);
			if (spy.getGun().getNumRounds() == 1 & moveStatus.msg == "You picked up a bullet!") {
				moveStatus.msg = "Your ammo is full, can't pick this bullet!";
			}
		}
		
		// The player tried to move into the room from the north side but it didn't have the briefcase
		if (moveStatus.moveResult == MOVE_RESULT.UNMOVED_TURN_TAKEN)
			grid.setToInvisible();
			
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
		grid.setToInvisible();
		if (spy.getGun().attack(direction, spy, grid))
		{
			for (int i = 0; i < ninjas.size(); i++)
			{
				if (!ninjas.get(i).isAlive())
				{
					grid.removeGameObject(ninjas.get(i).getX(), ninjas.get(i).getY());
					ninjas.remove(i);
					i--;
				}
			}
			return true;
		}
		return false;
	}

	
	/**
	 * Updates the spy's attributes when upon using powerups.
	 * Reduces the invincibilty counter one and reveals the briefcase
	 * to the player.
	 */
	public void updateSpyPowerups(){
		spy.reduceInvincibility();
		if (spy.hasRadar()){
			briefcaseRoom.revealBriefCase();
		}
	}
	/**
	 * Method to use powerups on the spy.
	 */
	public void useSpyPowerup(){
		spy.usePowerups();
	}
	
	/**
	 * For each ninja in {@link #ninjas} attack spy with {@link Ninja#weapon}
	 * if spy is in range
	 */
	public void enemyAttack() {		
		for (Ninja ninja: ninjas) {
			int ninX = ninja.getX();
			int ninY = ninja.getY();
		
			// Ninja attacks if Spy is in range
			if(Math.abs(spy.getX() - ninX) + Math.abs(spy.getY() - ninY) <= 1)
			{
				DIRECTION stabDirection = null;
				if (spy.getX() < ninX)
				{
					stabDirection = DIRECTION.LEFT;
				}
				else if (spy.getX() > ninX)
				{
					stabDirection = DIRECTION.RIGHT;
				}
				else if (spy.getY() > ninY)
				{
					stabDirection = DIRECTION.DOWN;
				}
				else if (spy.getY() < ninY)
				{
					stabDirection = DIRECTION.UP;
				}
				// if successfully stabbed
				if (ninja.getSword().attack(stabDirection, ninja, grid))
				{
					if (!spy.isAlive())
					{
						grid.setAsVisible(ninja.getX(), ninja.getY());
						if (!spy.hasLives())
						{
							gameStatus = GAME_STATE.LOST;
						}
						return;
					}
				}
			}
		}
	}
	
	/**
	 * The ninja looks in all four directions as far as {@link Ninja#SIGHT_RANGE}
	 * with any {@link Room} blocking vision, and if {@link GameEngine#spy} is 
	 * spotted then set {@link Ninja#destinationCoordinate} to the spy's coordinate
	 * @param ninjaIndex
	 */
	public void enemyLook(int ninjaIndex)
	{
		Ninja ninja = ninjas.get(ninjaIndex);
		int ninjaX = ninja.getX();
		int ninjaY = ninja.getY();
		
		DIRECTION[] directions = GameEngine.HardMode ? DIRECTION.values(): ninja.getDirectionFacingAsArray();
		// iterate over all four directions to check for spy
		for (DIRECTION direction: directions) 
		{
			int x = ninjaX;
			int y = ninjaY;
			// iterate over each tile in a direction
			for (int tileIndex = 0; tileIndex < ninja.getLookRange();)
			{
				x += direction.deltaX;
				y += direction.deltaY;
				if (grid.inRange(x, y)) {
					GameObject object = grid.getGameObject(x, y);
					if (object instanceof Spy) {
						ninja.setDestinationCoordinate(x, y);
						ninja.setDirectionFacing(direction);
						return;
					}
					if (object instanceof Room) {
						break;
					}
				}
				else if (!grid.inRange(x, y))
					break;
			}
		}
	}
	
	/**
	 * For each ninja in {@link #ninjas} this makes the ninja look which checks in all four
	 * directions to see if spy is there. If spotted then stores the coordinate of the spy 
	 * and changes the {@link Ninja#directionFacing} towards the spy. The ninja will move 
	 * in the direction facing until it reaches {@link Ninja#destinationCoordinate} at which
	 * point it'll use the {@link enemyMoveInRandomDirection(int)} method.
	 */
	public void enemyMove()
	{
		for (int ninjaIndex = 0; ninjaIndex < ninjas.size(); ninjaIndex++) {
			Ninja ninja = ninjas.get(ninjaIndex);
			
			if (ninja.arrivedAtDestination())
				ninja.setDestinationCoordinate(null);
			
			if (ninja.getDestinationCoordinate() != null)
				enemyMoveFoward(ninjaIndex);
			else if (ninja.getDestinationCoordinate() == null)
				enemyMoveInRandomDirection(ninjaIndex);
			
			enemyLook(ninjaIndex);
		}
	}
	
	/**
	 * Moves the ninja in the direction its facing {@link Ninja#directionFacing}
	 * @param ninjaIndex the index of {@link #ninjas} used to select a ninja
	 */
	public void enemyMoveFoward(int ninjaIndex)
	{
		Ninja ninja = ninjas.get(ninjaIndex);
		int ninjaX = ninja.getX();
		int ninjaY = ninja.getY();
		DIRECTION ninjaDirection = ninja.getDirectionFacing();
		
		// if ninja can move in the ninjaDirection on the grid
		if (grid.checkMoveStatus(ninjaDirection, ninjaX, ninjaY).moveResult == 
				MOVE_RESULT.LEGAL) {
			grid.move(ninjaDirection, ninjaX, ninjaY);
		}
		else {
			ninja.setDestinationCoordinate(null);
		}
	}
	
	/**
	 * Move a ninja from {@link #ninjas} in a random direction & set the ninja {@link Ninja#directionFacing}
	 * to the direction moved in
	 * @param ninjaIndex the index of {@link #ninjas} used to select a ninja
	 */
	public void enemyMoveInRandomDirection(int ninjaIndex)
	{
		Ninja ninja = ninjas.get(ninjaIndex);
		int ninjaX = ninja.getX();
		int ninjaY = ninja.getY();
		ArrayList<DIRECTION> availableDirections = new ArrayList<DIRECTION>(Arrays.asList(DIRECTION.values()));
		
		// check each direction randomly until list is exhausted without repeating a direction
		while (availableDirections.size() > 0) {
			DIRECTION randomDir = availableDirections.get(rng.nextInt(availableDirections.size()));
			MoveStatus moveStatus = grid.checkMoveStatus(randomDir, ninjaX, ninjaY);
			if (moveStatus.moveResult == MOVE_RESULT.LEGAL) {
				grid.move(randomDir, ninjaX, ninjaY);
				ninja.setDirectionFacing(randomDir);
				break;
			}
			availableDirections.remove(randomDir);
		}
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
	public boolean save(String fileDir) {
		try {
			File file = new File(fileDir);
			FileOutputStream fileOut;
			fileOut = new FileOutputStream(file);
			ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
			objectOut.writeBoolean(DebugMode);
			objectOut.writeObject(gameStatus);
			objectOut.writeObject(spy);
			objectOut.writeObject(ninjas);
			objectOut.writeObject(briefcaseRoom);
			objectOut.writeObject(grid);
			objectOut.close();
			fileOut.close();
			return true;
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Loads a previous session of the game from the directory specified.
	 * @param fileDir The file to load from.
	 */
	public boolean load(String fileDir) {
		try {
			File file = new File(fileDir);
			FileInputStream fileIn;
			fileIn = new FileInputStream(file);
			ObjectInputStream objectOut = new ObjectInputStream(fileIn);
			DebugMode = objectOut.readBoolean();
			gameStatus = (GAME_STATE)objectOut.readObject();
			spy = (Spy)objectOut.readObject();
			ninjas = (ArrayList <Ninja>)objectOut.readObject();
			briefcaseRoom = (Room)objectOut.readObject();
			grid = (Grid)objectOut.readObject();
			objectOut.close();
			fileIn.close();
			return true;
		} catch (FileNotFoundException e) {
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public void changeAllNinjasLookRangeTo(int lookRange) {
		for (Ninja ninja: ninjas) {
			ninja.setLookRange(lookRange);
		}
	}
	
	/**
	 * Modifier for {@link #DebugMode}.  While not necessary to modify, the intent is more clear.
	 * @param mode The value to set {@link #DebugMode} to.
	 */
	public static void SetDebugMode(boolean mode)
	{
		DebugMode = mode;
	}
	
	/**
	 * Set hard mode
	 */
	public static void setHardMode(boolean mode) {
		HardMode = mode;
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
		spyInfoMessages.add("---\n");
		spyInfoMessages.add("Hard Mode: " + (GameEngine.HardMode ? "Enabled": "Disabled") + "\n");
		spyInfoMessages.add("Debug Mode: " + (GameEngine.DebugMode ? "Enabled": "Disabled") + "\n");
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
