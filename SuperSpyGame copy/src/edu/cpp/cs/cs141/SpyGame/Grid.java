package edu.cpp.cs.cs141.SpyGame;

public class Grid {

		//size of the gird
		public static final int GRID_SIZE = 9;
		
		private GameObject[][] grid;

		/**
		 * Constructor of the grid
		 */
		public Grid() {
		   grid =new GameObject[GRID_SIZE][GRID_SIZE];

		  
		}
		
		/**
		 * return the size of grid
		 * @return
		 */
		public int size() {
			return GRID_SIZE;
		}
		
		/**
		 * Set the gameObject and set the gameObject in the grid
		 * @param so
		 */
		public void setObject(GameObject gameObject) {
			grid[gameObject.getX()][gameObject.getY()] = gameObject;
		}
		
		/**
		 * Get the gameObject in the grid
		 * @param x
		 * @param y
		 * @return
		 */
		public GameObject getObject(int x,int y) {
			return grid[x][y];
		}
		
		/**
		 * Clear the grid and set all grid to empty
		 */
		public void clear() {
			for(int i=0; i<grid.length; i++) {
				for(int j=0; j<grid.length; j++) {
					grid[i][j] = new EmptyPlace(i, j);
				}
			}
		}
		
		/**
		 * move the object and set the original grid to empty
		 * @param moveObj
		 * @param targetX
		 * @param targetY
		 */
		public void moveObject(GameObject gameObject,int moveX,int moveY) {
			grid[moveX][moveY] = gameObject;
			grid[gameObject.getX()][gameObject.getY()] = new EmptyPlace(gameObject.getX(), gameObject.getY());
			gameObject.setLocation(moveX, moveY);
		}
			
		/**
		 * Rest the grid and set all to empty
		 */
		public void resetGrid() {
			this.clear();
		}
		
		/**
		 * reset the grid all to unvisible
		 */
		public void setAllUnvisible() {
			for(int i=0; i<grid.length; i++) {
				for(int j=0; j<grid.length; j++) {
					grid[i][j].setVisible(false);
				}
			}
		}
			
		/**
		 * find the player in the grid
		 */
		public Spy findSpy() {
			Spy spy = null;
			for(int i=0; i<grid.length; i++) {
				for(int j=0; j<grid.length; j++) {
					if (grid[i][j] instanceof Spy) {
						spy = (Spy) grid[i][j];
					}
				}
			}
			return spy;
		}
		
		/**
		 * find the ninja in the grid
		 */
		public int findNinja() {
			int ninjaNum = 0;
			for(int i=0; i<grid.length; i++) {
				for(int j=0; j<grid.length; j++) {
					if (grid[i][j] instanceof Ninja) {
						ninjaNum ++;
					}
				}
			}
			return ninjaNum;
		}
		
		/**
		 * 
		 */
		public boolean canSetNinja(int x, int y) {
			if (Math.abs(0 - x) + Math.abs(8 - y) <= 2 || grid[x][y] != null)
				return false;
			return true;
		}
		
		/**
		 * String of the grid
		 * @param isDebug
		 * @return
		 */
		public String toString(boolean isDebug) {
			String result = "";
			for(int i=0; i < grid.length; i++) {
				for(int j=0; j < grid.length; j++) {
					
					result += "[" + grid[i][j].toString(isDebug) + "]";
					
					if(i == 3 & j == 8 & findSpy() != null) 
						result += "        *** NinjaNum:" + findNinja();
					if(i == 4 & j == 8 & findSpy() != null) 
						result += "        *** live:" + findSpy().getLive();
					if(i == 5 & j == 8 & findSpy() != null) 
						result += "        *** Bullet:" + findSpy().getBullet();
					if(i == 6 & j == 8 & findSpy() != null) 
						result += "        *** Invincibility round:" + findSpy().getInvinc();
				}
				result += "\n";
			}
			return result;
		}
}
