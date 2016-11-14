package edu.cpp.cs.cs141.final_proj;

public class Main {
	/**
	 * main method to start game
	 * @param args
	 */
	public static void main(String[] args) {
		UserInterface ui = new UserInterface(new GameEngine());
		ui.startGame();
	}

}
