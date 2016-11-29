package edu.cpp.cs.cs141.final_proj;

import impl.GameCore;

/**
 * Stores the main method as an entry point to the program.
 */
public class Main {
	/**
	 * Initializes the {@link UserInterface} and starts the game.
	 * @param args The arguments passed in through command.
	 */
	public static void main(String[] args) {
		boolean graphicsMode = false;
		for (String arg : args)
		{
			if (arg.equals("-g"))
			{
				graphicsMode = true;
			}
		}
		if (graphicsMode)
		{
			GameCore core = new GameCore();
			core.run();
		}
		else
		{
			UserInterface ui = new UserInterface(new GameEngine());
			ui.startGame();
		}
	}
}