package edu.cpp.cs.cs141.final_proj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Prints messages and gets input from the user to command the {@link GameEngine#spy}
 */
public class UserInterface {
	
	private static final String STAND_STILL_COMMAND = "S";
	/**
	 * Commands the user can make the {@link GameEngine#spy} do during
	 * the spy's turn
	 */
	public enum USER_COMMAND {
		shoot("1"), debug("2"), options("3");
		
		public final String keyCode;
		private USER_COMMAND(String code) {
			keyCode = code;
		}
		
		/**
		 * @return {@code {"shoot", "debug", "options"}} in an ArrayList<String> 
		 */
		public static ArrayList<String> names() {
			ArrayList<String> names = new ArrayList<String>();
			for (USER_COMMAND command: USER_COMMAND.values()) {
				names.add(command.name());
			}
			return names;
		}
		
		/**
		 * @return {@code HashMap<String, USER_COMMAND>} where the key is the first letter 
		 * of a value of USER_COMMAND and the value is the value is the corresponding value for
		 * the USER_COMMAND
		 */
		public static HashMap<String, USER_COMMAND> abbreviatedNames() {
			HashMap<String, USER_COMMAND> abbreviatedNames = new HashMap<String, USER_COMMAND>();
			String abbrevName;
			for (USER_COMMAND command: USER_COMMAND.values()) {
				abbrevName = command.name().substring(0, 1);
				abbreviatedNames.put(abbrevName, command);
			}
			return abbreviatedNames;
		}
		
		public static HashMap<String, USER_COMMAND> abbreviatedKeyCodes() {
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
	 * Used to control the game
	 */
	private GameEngine game = null;
	
	/**
	 * Gets input from the user
	 */
	private Scanner keyboard = null;
	
	/**
	 * String of the options and hotkey for a direction
	 */
	private String directionOptions = "";
	
	/**
	 * String of grid 
	 */
	private String gridString = "";
	
	/**
	 * Constructor for creating UserInterface objects
	 * @param game set {@link #game} to (parameter) game used to call methods
	 */
	public UserInterface(GameEngine game) {
		this.game = game;
		keyboard = new Scanner(System.in);
		directionOptions = "W: Up | A: Left | S: Down | D: Right";
	}
	
	/**
	 * The method to start game.
	 */
	public void startGame() {
		printWelcomeMessage();
		boolean quit = false;
		while(!quit) {
			int option = mainMenu();
			
			switch(option) {
			case 1:
				gameLoop();
				break;
			case 2:
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
	 * @return input {@code 1} indicates start new game, {@code 2} indicates quit game
	 */
	private int mainMenu() {
		System.out.println("Select an option:\n"
				+ "1. Start New Game.\n"
				+ "2. Quit.");
		
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
		game.reset();
		System.out.println("New game started! ");
		while (true)
		{
			// print grid, do player 'look' action
			System.out.println(game.displayBoard());
			playerLookLoop();
			
			// 
			gridString = game.displayBoard();
			System.out.println(gridString);
			
			// make all gameObjects invisible (except for spy & rooms)
			game.resetVisibility();
			
			// get command & direction from user then do that command in that direction
			playerTurn();
			
			// enemies follow their AI rules then check if spy is adjacent to them
			game.enemyTurn();
		}
	}
	
	private void playerTurn() {
		String question;
		boolean moveable = game.playerMoveable();
		if (moveable)
		{
			question = "Enter direction to move or another command\n"
					+ directionOptions + "\n";
		}
		else
		{
			question = "You cannot move... enter " + STAND_STILL_COMMAND + " to stand still or another command\n";
		}
		question += "1: Shoot | 2: Debug | 3: More Options";
		String userInput;

		// loop until valid direction or command is entered
		while (true) {
			System.out.println(question);
			userInput = keyboard.nextLine().toLowerCase().trim();
			
			if (!moveable && userInput.equals(STAND_STILL_COMMAND)) {
				System.out.println("Stood still");
				return;
			}
				
			else if (DIRECTION.abbreviatedNames().containsKey(userInput)) {
				DIRECTION moveDir = DIRECTION.abbreviatedNames().get(userInput);
				MoveStatus moveStatus = game.playerMove(moveDir);
				System.out.println("Move Status: " + moveStatus.msg);
				
				// RECURSION: ILLEGAL move attempted, print grid & call this method
				if (moveStatus.moveResult == MOVE_RESULT.ILLEGAL) {
					System.out.println(gridString);
					playerTurn();
					return;
				}
				break;
			}
			
			switch(USER_COMMAND.abbreviatedKeyCodes().get(userInput)) {
			case shoot:
				DIRECTION shootDir = getUserDirection(USER_COMMAND.shoot.name());
				boolean enemyHit = game.playerShoot(shootDir);
				if(enemyHit) {
					System.out.println("you shot the ninja!");
				}
				else { //if((gun.collision)&&(!enemyHit)){
					System.out.println("you hit something but it wasnt a ninja...");
				}
//				else{
//					System.out.println("You shot a bullet but it missed...");
//					gun.collision = false;
//				}
				return;
				
			case debug: 
				GameEngine.SetDebugMode(GameEngine.DebugMode ? false: true);
				gridString = game.displayBoard();
				System.out.println(gridString);
				playerTurn();
				return;
				
			case options:
				pauseMenu();
				System.out.println(gridString);
				break;
				
			default:
				break;
			}
			game.updateSpy();
		}
	}
	
	/**
	 * Ask the user for a direction to look in then change the {@link GameEngine#spy}
	 * change the tiles that are visible to the spy by calling {@link GameEngine#playerLook(DIRECTION)}
	 */
	private void playerLookLoop() {
		String question = "Enter a direction to look in or another command\n"
				+ directionOptions + "\n"
				+ "2: Debug | 3: More Options";
		String action = "look";
		DIRECTION lookDirection = getUserDirection(action);
		game.playerLook(lookDirection);
	}
	
	/**
	 * Handle pause menu and execute the requested commands
	 */
	private void pauseMenu() {
		String userOptions = "Pause Menu\n"
				+ "1: Resume | 2: Save | 3: Main Menu | 4: Exit Game";
		
		String userInput;
		do {
			System.out.println(userOptions);
			userInput = keyboard.nextLine().toLowerCase().trim();
			System.out.println(userInput);
		} while (!PAUSE_COMMAND.keyCodes().containsKey(userInput));
		
		PAUSE_COMMAND command = PAUSE_COMMAND.keyCodes().get(userInput);
		switch (command) {
		case resume:
			break;
			
		case save:
			game.save("/home/j/savefile.dat");
			break;
			
		case menu:
			// go to main menu
			// Main.main(null);
			return;
			
		case exit:
			System.exit(0);
			break;
			
		default:
			break;
		}
	}
	
	/**
	 * Continually ask user to give a command for the {@link GameEngine#spy}
	 * until a valid command is entered
	 * @return {@link #userCommand} of the command the user chose 
	 */
	private USER_COMMAND getUserCommand() {
		String question = "Enter one of the following commands:\n"
				+ "Shoot\n"
				+ "Debug\n";
		String userInput;
		do 
		{
			System.out.print(question);
			userInput = keyboard.nextLine().toLowerCase().trim();
		} while(!USER_COMMAND.abbreviatedNames().containsKey(userInput));
		return USER_COMMAND.abbreviatedNames().get(userInput);
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
		} while(!DIRECTION.abbreviatedNames().containsKey(userInput));
		return DIRECTION.abbreviatedNames().get(userInput);
	}
}
