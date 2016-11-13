package edu.cpp.cs.cs141.finalproject;

public class Main {

	// TODO Auto-generated method stub
			/**
			 * main method to start game
			 * @param args
			 */
	public static void main(String[] args) {
		UserInterface ui = new UserInterface(new GameEngine());
			ui.startGame();
	}

}
