package edu.cpp.cs.cs141.final_proj;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
	public void resetGrid()	{
		grid = new Grid();
		spy.aliveAgain();
		setPlayer();
		setRooms();
		setItems();
		setNinjas();
		grid.setToInvisible();
	}
	
	/**
	 * Set the player
	 */
	public void setPlayer()	{
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
		String message1 = "You can see nothing in this direction...";
		String message2 = "Ninja Ahead!";
		String message3 = "Clear";
		String message4 = "A room is blocking your vision ahead";
		switch (lookDirection)
		{
		case UP:
			playerLook(0, -1, Spy.LOOK_RANGE);
			if (grid.getGameObject(spy.getX(), spy.getY() - 1) instanceof Ninja ||
				grid.getGameObject(spy.getX(), spy.getY() - 2) instanceof Ninja)
				return message2;
			if (!grid.inRange(spy.getX(), spy.getY() - 1))
				return message1;
			if (grid.getGameObject(spy.getX(), spy.getY() - 1) instanceof Room)
				return message4;
			break;
		case RIGHT:
			playerLook(1, 0, Spy.LOOK_RANGE);
			if (grid.getGameObject(spy.getX() + 1, spy.getY()) instanceof Ninja ||
				grid.getGameObject(spy.getX() + 2, spy.getY()) instanceof Ninja)
					return message2;
			if (!grid.inRange(spy.getX() + 1, spy.getY()))
					return message1;
			if (grid.getGameObject(spy.getX() + 1, spy.getY()) instanceof Room)
				return message4;
			break;
		case DOWN:
			playerLook(0, 1, Spy.LOOK_RANGE);
			if (grid.getGameObject(spy.getX(), spy.getY() + 1) instanceof Ninja ||
				grid.getGameObject(spy.getX(), spy.getY() + 2) instanceof Ninja)
					return message2;
			if (!grid.inRange(spy.getX(), spy.getY() + 1))
					return message1;
			if (grid.getGameObject(spy.getX(), spy.getY() + 1) instanceof Room)
				return message4;
			break;
		case LEFT:
			playerLook(-1, 0, Spy.LOOK_RANGE);
			if (grid.getGameObject(spy.getX() - 1, spy.getY()) instanceof Ninja ||
				grid.getGameObject(spy.getX() - 2, spy.getY()) instanceof Ninja)
					return message2;
			if (!grid.inRange(spy.getX() - 1, spy.getY()))
					return message1;
			if (grid.getGameObject(spy.getX() + 1, spy.getY()) instanceof Room)
				return message4;
			break;
		}
		return message3;
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
			grid.setToInvisible();  //You messed up the order
			grid.move(direction, spyX, spyY);
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
			
			// Ninja attacks if Spy is in range
			if(Math.abs(spy.getX() - ninX) + Math.abs(spy.getY() - ninY) <= 1){
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
				if (ninjas.get(i).getSword().attack(stabDirection, ninjas.get(i), grid))
				{
					if (!spy.isAlive())
					{
						if (!spy.hasLives())
						{
							gameStatus = GAME_STATE.LOST;
							return;
						}
						break;
					}
				}
			}
			
			// Ninja moves in a random direction
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
		File file = new File(fileDir);
		FileOutputStream fileOut;
		try {
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
		File file = new File(fileDir);
		FileInputStream fileIn;
		try {
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
