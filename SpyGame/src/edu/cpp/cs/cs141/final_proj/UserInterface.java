package edu.cpp.cs.cs141.final_proj;

import java.util.Scanner;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Prints messages and gets input from the user to command the {@link GameEngine#spy}
 */
public class UserInterface {
	private boolean toMainMenu = false;
	
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
			toMainMenu = false;
			int option = mainMenu();
			
			switch(option) {
			case 1:
				System.out.println("New game started");
				game.reset();
				gameLoop();
				break;
			case 2:
				if (handleLoad())
				{
					System.out.println("Game loaded");
					gameLoop();
				}
				break;
			case 3:
				System.out.println("Quitting...");
				return;
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
				+ "2. Load A Game.\n"
				+ "3. Quit");
		
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
			// print grid, do player 'look' action, print grid
			playerLook();
			if (toMainMenu)
			{
				return;
			}
			// get command & direction from user then do that command in that direction
			playerTurn();
			if (toMainMenu)
			{
				return;
			}
			
			if (checkGameStatus())
			{
				break;
			}
			game.useSpyPowerup();
			// enemies follow their AI rules then check if spy is adjacent to them
			game.enemyAttack();
			//Check if game was lost
			if (checkGameStatus())
			{
				game.enemyMove();
				break;
			}
			game.enemyMove();
			//Reduces spy invincibility
			game.updateSpyPowerups();
		}
	}
	
	private boolean checkGameStatus()
	{
		switch (game.getGameStatus())
		{
		case WON:
			System.out.println("You won!");
			return true;
		case LOST:
			System.out.println(game.displayBoard());
			game.resetVisibility();
			System.out.println("You lose...");
			return true;
		case KILLED:
			System.out.println(game.displayBoard());
			game.resetVisibility();
			System.out.println("You were killed... Press enter to continue");
			keyboard.nextLine();
			game.setSpyToInitialState();
			return false;
		default:
			return false;
		}
	}
	
	/**
	 * Get a command and do that command
	 * check {@link #USER_COMMAND} for the options
	 */
	private void playerTurn() {
		boolean moveMode = true;
		DIRECTION playerDirection = null;
		while (true)
		{
			System.out.println(game.displayBoard());
			if (moveMode)
			{
				System.out.println("F  shoot mode");
				printActionDirection("move");
			}
			else
			{
				System.out.println("F  move mode");
				printActionDirection("shoot");
			}
			String input = keyboard.nextLine();
			input = input.toLowerCase();
			playerDirection = interpretUserCommand(input);
			if (playerDirection == null)
			{
				if (input.equals("f")) 
				{
					moveMode = !moveMode;
					System.out.println("Switched to " + (moveMode ? "move" : "shoot") + " mode");
				}
				if (toMainMenu)
				{
					return;
				}
			}
			else
			{
				if (moveMode)
				{
					MoveStatus moveStatus = game.playerMove(playerDirection);
					if (moveStatus.moveResult == MOVE_RESULT.ILLEGAL)
					{
						System.out.println(moveStatus.msg + "  try again...");
					}
					else
					{
						System.out.println(moveStatus.msg);
						return;
					}
				}
				else
				{
					if (game.playerRunOutAmmo())
					{
						System.out.println("You don't have any ammo, can't shot :(");
					}
					else if (game.playerShoot(playerDirection))
					{
						System.out.println("A ninja was shot");
					}
					else
					{
						System.out.println("Shot did not hit an enemy");
					}
					return;
				}
			}
		}
	}
	
	/**
	 * Ask the user for a direction to look in then change the {@link GameEngine#spy}
	 * change the tiles that are visible to the spy by calling {@link GameEngine#playerLook(DIRECTION)}
	 */
	private void playerLook() {
		DIRECTION lookDirection = null;
		while (lookDirection == null && !GameEngine.DebugMode)
		{
			System.out.println(game.displayBoard());
			printActionDirection("look");
			String input = keyboard.nextLine();
			lookDirection = interpretUserCommand(input);
			if (toMainMenu)
			{
				return;
			}
		}
		if (!GameEngine.DebugMode)
		{
			game.playerLook(lookDirection);
		}
	}
	
	void printActionDirection(String action)
	{
		System.out.println("W  " + action + " up\nD  " + action + " right\nS  " + action + " down\nA  " + action + " left\nG  " + (GameEngine.DebugMode ? "disable " : "enable") + " debugging\nH  For more options");
	}
	
	/**
	 * Continually ask user to enter a direction (abbreviated to the letter or full name)
	 * @param action a String that's a verb that describes why the direction is needed
	 * @return the {@link Grid#DIRECTION} entered by the user
	 */
	private DIRECTION interpretUserCommand(String directionInput) {
		switch(directionInput)
		{
		case "w":
			return DIRECTION.UP;
		case "a":
			return DIRECTION.LEFT;
		case "s":
			return DIRECTION.DOWN;
		case "d":
			return DIRECTION.RIGHT;
		case "g":
			GameEngine.SetDebugMode(!GameEngine.DebugMode);
			System.out.println("DEBUGGING " + (GameEngine.DebugMode ? "ENABLED" : "DISABLED"));
			return null;
		case "h":
			handleOtherOptions();
			return null;
		default:
			return null;
		}
	}
	
	private void handleOtherOptions()
	{
		while (true)
		{
			System.out.println("S  save\nX  terminate program\nM  quit to main menu\nC  back to game");
			String input = keyboard.nextLine();
			input = input.toLowerCase();
			switch (input)
			{
			case "s":
			{
				System.out.print("Enter the name of the file you want to save to: ");
				String fileDir = keyboard.nextLine();
				if (game.save(fileDir))
				{
					System.out.println("Game saved to " + fileDir);
				}
				else
				{
					System.out.println("Unable to save game");
				}
				break;
			}
			case "x":
			{
				System.out.println("Exiting...");
				System.exit(0);
				break;
			}
			case "m":
			{
				System.out.println("Quitting to main menu...");
				toMainMenu = true;
				return;
			}
			case "c":
				return;
			default:
				System.out.println("Invalid option... try again");
			}
		}
	}
	
	private boolean handleLoad()
	{
		System.out.print("Enter the name of the file you want to load from: ");
		String fileDir = keyboard.nextLine();
		if (game.load(fileDir))
		{
			System.out.println("Load was succesful");
			return true;
		}
		else
		{
			System.out.println("Could not load from " + fileDir);
			return false;
		}
	}
}
