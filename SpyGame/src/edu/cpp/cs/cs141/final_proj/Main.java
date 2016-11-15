package edu.cpp.cs.cs141.final_proj;

/**
 * Stores the main method as an entry point to the program.
 */
public class Main {
	/**
	 * Initializes the {@link UserInterface} and starts the game.
	 * @param args The arguments passed in through command.
	 */
	public static void main(String[] args) {
//		GameEngine.SetDebugMode(true);
		UserInterface ui = new UserInterface(new GameEngine());
		ui.startGame();
	}

}
