package edu.cpp.cs.cs141.final_proj;

import java.util.Scanner;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;

public class UserInterface {
	private GameEngine game = null;
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
		System.out.println("Welcome to Spy Game(by Random 6) v1.0!\n");
	}
	
	/**
	 * This method prompt user to choose start game or quit game,
	 * and take player's input.
	 * @return input {@code 1} indicates start new game, {@code 2} indicates quit game
	 */
	private int mainMenu() {
		int option = 2;
		
		System.out.println("Select an option:\n"
				+ "1. Start New Game.\n"
				+ "2. Quit.");
		
		option = keyboard.nextInt();
		keyboard.nextLine();
		
		return option;
	}
	
	/**
	 * Game loop to handle player and enemy turns.
	 */
	private void gameLoop() {
		game.reset();
		System.out.println("New game started! ");
		while (true)
		{
			System.out.println(game.displayBoard());
			playerLookLoop();
			System.out.println(game.displayBoard());
			playerActionLoop();
			System.out.println(game.displayBoard());
			System.out.println("To allow enemys to take turn press enter...");
			keyboard.nextLine();
			game.enemyTurn();
		}
	}
	
	/**
	 * Ask the user for a direction to look in then change the {@link Game#spy}
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
	 * Asks the user for the direction to move in then change the position of {@link GameEngine#spy} 
	 * by calling {@link GameEngine#playerMove(DIRECTION)}.
	 */
	private void playerActionLoop()
	{
		System.out.println("W  Move Up\nD  Move Right\nS  Move Down\nA  Move Left");
		while (true)
		{
			String selection = keyboard.nextLine();
			selection = selection.toLowerCase();
			DIRECTION moveDirection = null;
			switch (selection)
			{
			case "w":
				moveDirection = DIRECTION.UP;
				break;
			case "d":
				moveDirection = DIRECTION.RIGHT;
				break;	
			case "s":
				moveDirection = DIRECTION.DOWN;
				break;
			case "a":
				moveDirection = DIRECTION.LEFT;
				break;
			default:
				System.out.println("Invalid option... try again");
			}
			if (moveDirection != null)
			{
				MoveStatus moveStatus = game.playerMove(moveDirection);
				System.out.println("Move Status: " + moveStatus.msg);
				break;
			}
		}
	}
}
