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
		move, shoot, debug;
		
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
			playerLookLoop();
			System.out.println(game.displayBoard());
			
			// make all gameObjects invisible (except for spy & rooms)
			game.resetVisibility();
			
			// get command & direction from user then do that command in that direction
			playerTurn();
			
			// enemies follow their AI rules then check if spy is adjacent to them
			game.enemyTurn();
		}
	}
	
	private void playerTurn() {
		USER_COMMAND command = getUserCommand();
		System.out.println("Command Entered = " + command.name());
		
		// we actually don't to get a direction if command is debug
		DIRECTION direction = getUserDirection();
		System.out.println("Direction Entered = " + direction.name());
		switch(command) 
		{
		case move:
			MoveStatus moveStatus = game.playerMove(direction);
			System.out.println("Move Status: " + moveStatus.msg);
			break;
		case shoot:
//			Gun gun = new Gun();
			
			boolean enemyHit = game.playerShoot(direction);
			if(enemyHit){
				System.out.println("you shot the ninja!");
			}
			else{ //if((gun.collision)&&(!enemyHit)){
				System.out.println("you hit something but it wasnt a ninja...");
			}
//			else{
//				System.out.println("You shot a bullet but it missed...");
//				gun.collision = false;
//			}
			break;
		case debug:
			GameEngine.SetDebugMode(true);
			break;
		default:
			System.out.println("what happened in playerTurn() method");
		}
	}
	
	/**
	 * Ask the user for a direction to look in then change the {@link GameEngine#spy}
	 * change the tiles that are visible to the spy by calling {@link GameEngine#playerLook(DIRECTION)}
	 */
	private void playerLookLoop() {
		System.out.println("W  Look Up\nD  Look Right\nS  Look Down\nA  Look Left");
		while (true)
		{
			String selection = keyboard.nextLine();
			selection = selection.toLowerCase();
			DIRECTION lookDirection = null;
			switch (selection)
			{
			case "w":
				lookDirection = DIRECTION.UP;
				break;
			case "d":
				lookDirection = DIRECTION.RIGHT;
				break;	
			case "s":
				lookDirection = DIRECTION.DOWN;
				break;
			case "a":
				lookDirection = DIRECTION.LEFT;
				break;
			default:
				System.out.println("Invalid option... try again");
			}
			if (lookDirection != null)
			{
				game.playerLook(lookDirection);
				break;
			}
		}
	}
	
	/**
	 * Continually ask user to give a command for the {@link GameEngine#spy}
	 * until a valid command is entered
	 * @return {@link #userCommand} of the command the user chose 
	 */
	private USER_COMMAND getUserCommand() {
		String question = "Enter one of the following commands:\n"
				+ "Move\n"
				+ "Shoot\n"
				+ "Debug\n";
		String userInput;
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
	 * @return the {@link Grid#DIRECTION} entered by the user
	 */
	private DIRECTION getUserDirection() {
		String question = "W  Up\n"
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
