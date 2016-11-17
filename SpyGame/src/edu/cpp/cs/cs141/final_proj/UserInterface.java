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
			playerLook();
			
			// get command & direction from user then do that command in that direction
			playerTurn();
			if (checkGameStatus())
			{
				break;
			}
			game.useSpyPowerup();
			// enemies follow their AI rules then check if spy is adjacent to them
			game.enemyTurn();
			//Reduces spy invincibility
			game.updateSpyPowerups();
			if (checkGameStatus())
			{
				break;
			}
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
			System.out.println("You lose...");
			return true;
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
		while (playerDirection == null)
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
				else
				{
					System.out.println("Invalid input... try again");	
				}
			}
		}
		game.resetVisibility();
		if (moveMode)
		{
			MoveStatus moveStatus = game.playerMove(playerDirection);
			System.out.println(moveStatus.msg);
		}
		else
		{
			if (game.playerShoot(playerDirection))
			{
				System.out.println("A ninja was shot");
			}
			else
			{
				System.out.println("Shot did not hit an enemy");
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
		}
		if (!GameEngine.DebugMode)
		{
			game.playerLook(lookDirection);
		}
	}
	
	void printActionDirection(String action)
	{
		System.out.println("W  " + action + " up\nD  " + action + " right\nS  " + action + " down\nA  " + action + " left\nG  " + (GameEngine.DebugMode ? "disable " : "enable") + " debugging");
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
		default:
			return null;
		}
	}
}
