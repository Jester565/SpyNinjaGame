package edu.cpp.cs.cs141.final_proj;

import java.util.ArrayList;
import java.util.Random;

public class GameEngine {


		private Random rng = new Random();
		private Grid grid = new Grid();
		private boolean gameWin = false;
		private boolean gameFinished = false;
		
		/**
		 * reset the building 
		 */
		public void reset() {
			grid.reset();
		}
		
		/**
		 * Check if the player win?
		 */
		public boolean checkWinCondition() {
			gameWin = false;
			if (grid.getSpy().hasBriefCase()) gameWin = true;
			return gameWin;
		}
		
		
		/**
		 * Check if there are any object in the grid
		 */
		public boolean emptyGrid(int x, int y) {
				return grid.getGameObject(x, y) == null ? true : false;
		}
		
		/**
		 * Player Turn
		 */
		public void playerTure() {
			
		}
		
		/**
		 * Enemy Turn
		 */
		public void enemyTurn() {
			
		}
		
		/**
		 * Enable the debug mode
		 */
		public void enableDebug() {
			
		}
		
		
		
		/**
		 * Set six rooms
		 */
		public void setRooms() {
			
		}
		
		/**
		 * Set new map
		 */
		public void setMap() {
			
		}
		
		/**
		 * Save the file
		 */
		public void save() {
			
		}
		
		/**
		 * Load the exited game
		 */
		public void load() {
			
		}
		
		public void quitGame() {
			
		}
		
		/**
		 * Display the game board
		 */
		public String displayBoard() {
			return grid.toString();
		}
}
