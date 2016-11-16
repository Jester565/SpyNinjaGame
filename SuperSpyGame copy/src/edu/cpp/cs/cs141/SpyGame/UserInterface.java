package edu.cpp.cs.cs141.SpyGame;

import java.util.Random;
import java.util.Scanner;


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
				System.out.println("How many ninja you want to set in the game");
				gameLoop();
				break;
			case 2:
				System.out.println("Please enter the file name");
				game.gameLoad(keyboard.nextLine());
			case 3:
				quit = true;
				break;
			default:
				System.out.println("Invalid option. Try again...");
				break;
			}
		}
	}
	
	public void gameLoop() {
		game = new GameEngine();
		game.newGame(keyboard.nextInt());
		
		Scanner scan=new Scanner(System.in);
		
		boolean deBugMode = false;
		System.out.println(game.toString(deBugMode));
		System.out.println();
		
		
		while(!game.gameFinished()) {
			if (!game.getPlayer().isAlive()) {
				System.out.println("player is stabbed and return to original point");
				game = new GameEngine();
				game.newGame(6);
				System.out.println(game.toString(deBugMode));
				System.out.println();
				
			}
			int choose;
			System.out.println("1.move      2.look    3.shoot    4.set dubug mode  5.save game  6.quit game");
			choose = keyboard.nextInt();
			switch(choose) {
			case 1:
					System.out.println("move direction: w(up) a(left) s(down) d(right)");
					String option;
					option = keyboard.next();
					System.out.println(game.playerTurn(option.charAt(0)));
					if (game.ninjaTurn()) {
						System.out.println("player is stabbed");
						game.getPlayer().getStabbed();
					}
					System.out.println("ninja moved");
					System.out.println(game.toString(deBugMode));
					
					break;
			case 2:
					System.out.println("look direction:w(up) a(left) s(down) d(right)");
					boolean look=game.look(scan.next().charAt(0));
					System.out.println(look?"yes have ninja":"no ninja");
					System.out.println(game.toString(deBugMode));
					break;
				
			case 3:	
					System.out.println("shoot direction:w(up) a(left) s(down) d(right)");
					
					System.out.println(game.shoot(scan.next().charAt(0)));
					boolean die=game.ninjaTurn();
					if(die)
					{
						System.out.println("ninja die");
						System.out.println(game.toString(deBugMode))
						;
					}
					else
						System.out.println(game.toString(deBugMode));
					break;
			case 4:
					System.out.println("1.open debug mode  2.close debug mode");
					int chose = keyboard.nextInt();
					switch(chose) {
					case 1:
						deBugMode = true;
						System.out.println(game.toString(deBugMode));
						break;
					case 2:
						deBugMode = false;
						System.out.println(game.toString(deBugMode));
						break;
					}
					break;
			case 5:
					System.out.println("Enter the file name you want save");
					game.saveGame(keyboard.next());
					break;
					
			case 6:
					System.exit(0);
			
			}
				
		}
		
		if(game.gameWin()) {
			System.out.println(game.toString(true));
			System.out.println("you win the game");
		}
		
		
		
	}
	
	/**
	 * This method print out the welcome message to the player.
	 */
	private void printWelcomeMessage() {
		System.out.println("Welcome to Escape-The-Dungeon(by Hao Zheng) v1.0!\n");
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
				+ "2. Load a exit game.\n"
				+ "3. Quit.");
		
		option = keyboard.nextInt();
		keyboard.nextLine();
		
		return option;
	}
}