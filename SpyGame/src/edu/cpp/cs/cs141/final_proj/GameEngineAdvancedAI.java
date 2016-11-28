package edu.cpp.cs.cs141.final_proj;

import java.util.ArrayList;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;

public class GameEngineAdvancedAI extends GameEngine {
	private int[][] patrolSpots = new int[6][2];
	
	public GameEngineAdvancedAI() {
		super();
		// define patrolSpots
		int pSpotIndex = 0;
		for (int rowIndex = 2; rowIndex < Grid.GRID_SIZE; rowIndex += 2) {
			for (int colIndex = 2; colIndex < Grid.GRID_SIZE; colIndex += 4) {
				int[] coordinate = {colIndex, rowIndex};
				patrolSpots[pSpotIndex] = coordinate;
				pSpotIndex++;
			}
		}
	}
	
	public Ninja getNearestNinja(int tileX, int tileY) {
		Ninja nearestNinja = null;
		int distance = (int)Math.pow(Grid.GRID_SIZE, 2);
		for (Ninja ninja: ninjas) {
			if (Math.abs(ninja.getX() - tileX) + Math.abs(ninja.getY() - tileY) < distance)
				nearestNinja = ninja;
		}
		return nearestNinja;
	}
	
	public void aStar(int[] start, int[] goal) {
		ArrayList<int[]> closedSet = new ArrayList<int[]>();
		ArrayList<int[]> openSet = new ArrayList<int[]>();
		ArrayList<DIRECTION> cameFrom = new ArrayList<DIRECTION>();
	}
}
