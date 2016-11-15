package edu.cpp.cs.cs141.final_proj;

import java.util.Scanner;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;

public class UserInterface {
	private GameEngine game = null;
	private Scanner keyboard = null;
	
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
	 * This method print out the welcome message to the player.
	 */
	private void printWelcomeMessage() {
		System.out.println("Welcome to Spy Game(by Random 6) v1.0!\n");
	}
	
	/**
	 * This method prompt player to choose start game or quit game.
	 * And take player's input.
	 * @return input
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
	 * Game loop
	 */
	private void gameLoop() {
		game.reset();
		System.out.println("New game started! ");
		System.out.println(game.displayBoard());
		playerTurnLoop();
		System.out.println(game.displayBoard());
	}
	
	private void playerTurnLoop() {
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
}
