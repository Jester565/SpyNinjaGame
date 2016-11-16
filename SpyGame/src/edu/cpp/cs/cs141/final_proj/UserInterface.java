package edu.cpp.cs.cs141.final_proj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;

/**
 * Prints messages and gets input from the user to command the {@link GameEngine#spy}
 */
public class UserInterface {
	/**
	 * Commands the user can make the {@link GameEngine#spy} do during
	 * the spy's turn
	 */
	public enum USER_COMMAND {
		//WHY ARE MY PEPES SO DAMN DANK
		move, shoot, debug, dankmemes;
		
		/**
		 * @return {@code {"move", "shoot", "debug"}} in an ArrayList<String> 
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
		 * e.g. (sry Python syntax):
		 * {"m": USER_COMMAND.move}
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
	 * Constructor for creating UserInterface objects
	 * @param game set {@link #game} to (parameter) game used to call methods
	 */
	public UserInterface(GameEngine game) {
		this.game = game;
		keyboard = new Scanner(System.in);
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
			// print grid, do player 'look' action, print grid
			System.out.println(game.displayBoard());
			playerLook();
			System.out.println(game.displayBoard());
			
			// make all gameObjects invisible (except for spy & rooms)
			game.resetVisibility();
			
			// get command & direction from user then do that command in that direction
			playerTurn();
			
			// enemies follow their AI rules then check if spy is adjacent to them
			game.enemyTurn();
		}
	}
	
	/**
	 * Get a command and do that command
	 * check {@link #USER_COMMAND} for the options
	 */
	private void playerTurn() {
		USER_COMMAND command = getUserCommand();
		System.out.println("Command Entered = " + command.name());
		DIRECTION direction = null;
		
		switch(command)
		{
		case move:
			direction = getUserDirection(command.name());
			System.out.println("Direction Entered = " + direction.name());
			
			MoveStatus moveStatus = game.playerMove(direction);
			System.out.println("Move Status: " + moveStatus.msg);
			break;
		case shoot:
			direction = getUserDirection(command.name());
			System.out.println("Direction Entered = " + direction.name());
			
			boolean enemyHit = game.playerShoot(direction);
			System.out.println("you shot a bullet -- NOT IMPLEMENTED YET");
			break;
		case debug:
			GameEngine.SetDebugMode(GameEngine.DebugMode ? false: true);
			System.out.println(game.displayBoard());
			playerTurn();
			break;
		default:
			System.out.println("what happened in playerTurn() method");
		}
		game.updateSpy();
	}
	
	/**
	 * Ask the user for a direction to look in then change the {@link GameEngine#spy}
	 * change the tiles that are visible to the spy by calling {@link GameEngine#playerLook(DIRECTION)}
	 */
	private void playerLook() {
		game.displayBoard();
		DIRECTION lookDirection = getUserDirection("look");
		game.playerLook(lookDirection);
	}
	
	/**
	 * Continually ask user to give a command for the {@link GameEngine#spy}
	 * until a valid command is entered
	 * @return {@link #userCommand} of the command the user chose 
	 */
	private USER_COMMAND getUserCommand() {
		String question = "Enter one of the following commands:\n"
				+ "M  Move\n"
				+ "S  Shoot\n"
				+ "D  Debug\n";
		String userInput;
		game.enemyTurn();
		do 
		{
			System.out.print(question);
			userInput = keyboard.nextLine().toLowerCase().trim();
		} while(!USER_COMMAND.names().contains(userInput) && 
				!USER_COMMAND.abbreviatedNames().containsKey(userInput));
		
		if (userInput.length() > 1) 
			return USER_COMMAND.valueOf(userInput);
		else
			return USER_COMMAND.abbreviatedNames().get(userInput);
	}
	
	/**
	 * Continually ask user to enter a direction (abbreviated to the letter or full name)
	 * @param action a String that's a verb that describes why the direction is needed
	 * @return the {@link Grid#DIRECTION} entered by the user
	 */
	private DIRECTION getUserDirection(String action) {
		String question = "Enter direction to "
				+ action + "\n"
				+ "W  Up\n"
				+ "D  Right\n"
				+ "S  Down\n"
				+ "A  Left\n";
		String userInput;
		do
		{
			System.out.print(question);
			userInput = keyboard.nextLine().toLowerCase().trim();
		} while(!DIRECTION.names().contains(userInput) &&
				!DIRECTION.abbreviatedNames().containsKey(userInput));
		
		if (userInput.length() > 1) 
			return DIRECTION.valueOf(userInput);
		else
			return DIRECTION.abbreviatedNames().get(userInput);
	}
}
