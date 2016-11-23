package edu.cpp.cs.cs141.final_proj;

import java.util.HashMap;
import java.util.Scanner;

import edu.cpp.cs.cs141.final_proj.GameEngine.GAME_STATE;
import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Prints messages and gets input from the user to command the {@link GameEngine#spy}
 */
public class UserInterface {
	
	/**
	 * Hotkey command for when {@link GameEngine#spy} is stuck and needs to do nothing
	 * this turn
	 */
	private static final String STAND_STILL_COMMAND = "s";
	
	/**
	 * Commands the user can do during the spy's turn
	 */
	public enum USER_COMMAND {
		shoot("1"), debug("2"), options("3"), hardMode("4");
		
		public final String keyCode;
		private USER_COMMAND(String code) {
			keyCode = code;
		}
		
		public static HashMap<String, USER_COMMAND> keyCodes() {
			HashMap<String, USER_COMMAND> abbreviatedKeyCodes = new HashMap<String, USER_COMMAND>();
			String abbrevName;
			for (USER_COMMAND command: USER_COMMAND.values()) {
				abbrevName = command.keyCode;
				abbreviatedKeyCodes.put(abbrevName, command);
			}
			return abbreviatedKeyCodes;
		}
	}
	
	/**
	 * Commands available when user pauses game
	 */
	public enum PAUSE_COMMAND {
		resume("1"), save("2"), menu("3"), exit("4");
		
		public String keyCode;
		private PAUSE_COMMAND(String code) {
			keyCode = code;
		}
		
		public static HashMap<String, PAUSE_COMMAND> keyCodes() {
			HashMap<String, PAUSE_COMMAND> keyCodes = new HashMap<String, PAUSE_COMMAND>();
			for (PAUSE_COMMAND command: PAUSE_COMMAND.values()) {
				keyCodes.put(command.keyCode, command);
			}
			return keyCodes;
		}
	}
	
	/**
	 * Used to exit main game loop to enter menu
	 */
	private boolean exitToMenu = false;
	
	/**
	 * Used to skip enemy turn when switching from !debug to
	 * debug mode in player turn
	 */
	private boolean skipEnemyTurn = false;
	
	/**
	 * Used to control the game
	 */
	private GameEngine game = null;
	
	/**
	 * Gets input from the user
	 */
	private Scanner keyboard = null;
	
	/**
	 * options and hotkeys for the directions
	 */
	private String directionOptions = "";
	
	/**
	 * options and hotkeys for {@link #PAUSE_COMMAND}
	 */
	private String pauseMenuOptions = "";
	
	/**
	 * Constructor for creating UserInterface objects
	 * @param game set {@link #game} to (parameter) game used to call methods
	 */
	public UserInterface(GameEngine game) {
		this.game = game;
		keyboard = new Scanner(System.in);
		directionOptions = "W: Up | A: Left | S: Down | D: Right";
		pauseMenuOptions = "1: Resume | 2: Save | 3: Main Menu | 4: Exit Game";
	}
	
	/**
	 * The method to start game.
	 */
	public void startGame() {
		printWelcomeMessage();
		boolean quit = false;
		while(!quit) {
			exitToMenu = false;
			int option = mainMenu();
			switch(option) {
			case 1:
				game.reset();
				System.out.println("New game started! ");
				gameLoop();
				break;
			case 2:
				System.out.print("Enter in the file to load from: ");
				String loadFile = keyboard.nextLine();
				if (game.load(loadFile))
				{
					System.out.println("Load was successful!");
					gameLoop();
				}
				else
				{
					System.out.println("Load failed");
				}
				break;
			case 3:
				quit = true;
				break;
			default:
				System.out.println("Invalid option. Try again...");
				break;
			}
		}
	}
	
	/**
	 * Prints out the welcome message to the user.
	 */
	private void printWelcomeMessage() {
		System.out.println("Welcome to Spy Game (by Random 6)!\n");
	}
	
	/**
	 * This method prompt user to choose start game or quit game,
	 * and take player's input.
	 * @return input integers indicate the following: {@code 1} to start new game, 
	 * {@code 2} to load a game, and {@code 3} to quit game
	 */
	private int mainMenu() {
		System.out.println("Select an option:\n"
				+ "1: Start New Game\n"
				+ "2: Load Game\n"
				+ "3: Exit");
		
		while(!keyboard.hasNextInt()) {
			keyboard.nextLine();
		}
		int selection = keyboard.nextInt();
		keyboard.nextLine();
		return selection;
	}
	
	/**
	 * Game loop to handle player and enemy turns.
	 */
	private void gameLoop() {
		while (true)
		{	
			if (!GameEngine.DebugMode) {
				playerLookLoop();
				if (exitToMenu)
				{
					return;
				}
			}
			
			// get command or direction to move in from user then do corresponding action
			playerTurn();
			if (exitToMenu)
			{
				return;
			}
			
			if (game.getGameStatus().equals(GAME_STATE.WON))
			{
				break;
			}
			
			if (!skipEnemyTurn) {
				// each ninja kills player if in range
				game.enemyAttack();
				
				// if spy is not alive reset the grid
				if (!game.getSpy().isAlive()) {
					if (game.getGameStatus().equals(GAME_STATE.LOST))
					{
						break;
					}
					System.out.println(game.displayBoard());
					System.out.println("You were stabbed by the ninja, press enter to continue");
					keyboard.nextLine();
					game.resetVisibility();
					game.enemyMove();
					game.setSpyBackToInitialState();
				}
				else
				{
					// each ninja moves
					game.enemyMove();
				}
				
				//Prevents false advertising of Powerups
				game.updateSpyPowerups();
			}
		}
		
		// Game is finished
		GameEngine.SetDebugMode(true);
		if (game.getGameStatus().equals(GAME_STATE.LOST)) {
			System.out.println("Game Over");
		}
		else if (game.getGameStatus().equals(GAME_STATE.WON)) {
			System.out.println("You Win!");
		}
		System.out.println(game.displayBoard());
	}
	
	/**
	 * Print question asking user to enter a direction to move
	 * or command 
	 */
	private void playerTurn() {
		String question;
		boolean gunHasAmmo = game.getSpy().getGun().getNumRounds() > 0;
		boolean moveable = game.playerMoveable();
		skipEnemyTurn = false;
		if (moveable)
		{
			question = "Enter a direction to move or another command\n"
					+ directionOptions + "\n";
		}
		else
		{
			question = "You cannot move... enter " + STAND_STILL_COMMAND + " to stand still or another command\n";
		}
		question += (gunHasAmmo ? "1: Shoot | ": "") +  "2: Debug | 3: More Options | 4: HardMode";
		String userInput;
		USER_COMMAND command = null;

		// loop until valid direction or command is entered
		while (true) {
			System.out.println(game.displayBoard());
			System.out.println(question);
			userInput = keyboard.nextLine().toLowerCase().trim();
			
			// Spy is stuck
			if (!moveable && userInput.equals(STAND_STILL_COMMAND)) {
				System.out.println("Stood still");
				break;
			}
				
			// DIRECTION was given
			else if (DIRECTION.keyCodes().containsKey(userInput)) {
				DIRECTION moveDir = DIRECTION.keyCodes().get(userInput);
				MoveStatus moveStatus = game.playerMove(moveDir);
				System.out.println("Move Status: " + moveStatus.msg);
				
				// ILLEGAL move attempted, print grid & call this method
				if (moveStatus.moveResult == MOVE_RESULT.ILLEGAL) {
					continue;  //Go back to top of the while loop
				}
				game.useSpyPowerup();
				break;
			}
			
			// USER_COMMAND was given
			else if (USER_COMMAND.keyCodes().containsKey(userInput)) {
				command = USER_COMMAND.keyCodes().get(userInput);
			
				switch(command) {
				case shoot:
					if (!gunHasAmmo)
						break;
					DIRECTION shootDir = getUserDirection(command.name());
					boolean enemyHit = game.playerShoot(shootDir);
					if(enemyHit) {
						System.out.println("You shot the ninja!");
					}
					else {
						System.out.println("You hit something, but it wasn't a ninja...");
					}
					return;
					
				case debug:
					toggleDebugMode();
					if (!GameEngine.DebugMode) {
						game.resetVisibility();
						skipEnemyTurn = true;
						return;
					}
					else {
						break;
					}
				case options:
					pauseMenu();
					if (exitToMenu)
					{
						return;
					}
					break;
				case hardMode:
					toggleHardMode();
					break;
				default:
					break;
				}
			}
		}
	}
	
	/**
	 * Ask the user for a direction to look in then
	 * change the tiles that are visible to the spy by 
	 * calling {@link GameEngine#playerLook(DIRECTION)}
	 * {@link #USER_COMMAND} can also be entered here 
	 * though {@link #USER_COMMAND#shoot} is ignored
	 */
	private void playerLookLoop() {
		String question = "Enter a direction to look in or another command\n"
				+ directionOptions + "\n"
				+ "2: Debug | 3: More Options";
		String userInput;
		
		DIRECTION lookDirection = null;
		USER_COMMAND command = null;
		while (true) {
			System.out.println(game.displayBoard());  //Show board here.  Less code needed.
			System.out.println(question);
			userInput = keyboard.nextLine().toLowerCase().trim();
			
			// DIRECTION to look in given
			if (DIRECTION.keyCodes().containsKey(userInput)) {
				lookDirection = DIRECTION.keyCodes().get(userInput);
				System.out.println(game.playerLook(lookDirection));
				break;
			}
			
			// USER_COMMAND given
			else if (USER_COMMAND.keyCodes().containsKey(userInput)) {
				command = USER_COMMAND.keyCodes().get(userInput);
			
				switch(command) {		
				case debug: 
					toggleDebugMode();
					if (GameEngine.DebugMode)
						return;
					break;
				case options:
					pauseMenu();
					if (exitToMenu)
					{
						return;
					}
					break;
				default:
					break;
				}
			}
			else
			{
				System.out.println("Invalid input... try again");
			}
		}
	}
	
	/**
	 * Handle pause menu and executes the requested command
	 */
	private void pauseMenu() {
		String userOptions = "Pause Menu\n" + pauseMenuOptions;
		String userInput;
		
		do {
			System.out.println(userOptions);
			userInput = keyboard.nextLine().toLowerCase().trim();
		} while (!PAUSE_COMMAND.keyCodes().containsKey(userInput));
		
		PAUSE_COMMAND command = PAUSE_COMMAND.keyCodes().get(userInput);
		switch (command) {
		case resume:
			break;
			
		case save:
			System.out.print("Enter in the file to save to: ");
			String saveFile = keyboard.nextLine();
			if (game.save(saveFile))
			{
				System.out.println("Save was successful!");
			}
			else
			{
				System.out.println("Save failed");
			}
			break;
			
		case menu:
			exitToMenu = true;
			return;
			
		case exit:
			System.exit(0);
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * Change {@link GameEngine#HardMode} to opposite boolean & print message indicating mode entered
	 * Changes the sight or look range of all the ninjas to a either {@link Grid#GRID_SIZE} or 
	 * {@link Ninja#DEFAULT_LOOK_RANGE} depending on whether debug mode was disabled or enabled
	 */
	private void toggleHardMode() {
		GameEngine.setHardMode(GameEngine.HardMode ? false: true);
		System.out.println("Hard Mode is " + (GameEngine.HardMode ? "activated": "deactivated"));
		game.changeAllNinjasLookRangeTo(GameEngine.HardMode ? Grid.GRID_SIZE: Ninja.DEFAULT_LOOK_RANGE);
	}
	
	/**
	 * Change {@link GameEngine#DebugMode} to opposite boolean & print message indicating mode entered
	 */
	private void toggleDebugMode() {
		GameEngine.SetDebugMode(GameEngine.DebugMode ? false: true);
		System.out.println("Debug Mode is " + (GameEngine.DebugMode ? "activated": "deactivated"));
	}
	
	/**
	 * Continually ask user to enter a direction, and then use the String
	 * the user entered as a key to get the value from the map returned by
	 * {@link Grid#DIRECTION#abbreviatedNames}
	 * @return the {@link Grid#DIRECTION} corresponding to String entered by the user
	 */
	private DIRECTION getUserDirection(String action) {
		String question = "Enter direction to " + action + "\n"
				+ directionOptions;
		String userInput;
		do
		{
			System.out.println(question);
			userInput = keyboard.nextLine().toLowerCase().trim();
		} while(!DIRECTION.keyCodes().containsKey(userInput));
		return DIRECTION.keyCodes().get(userInput);
	}
}
