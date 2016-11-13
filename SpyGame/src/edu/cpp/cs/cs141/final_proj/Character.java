package edu.cpp.cs.cs141.final_proj;

public abstract class Character extends GameObject{


	private int live;
	
	/**
	 * Set live of active agent
	 * @param i
	 */
	public void setLife(int i) {
		live = i;
	}
	
	/**
	 * Get stabbed or shoot lose one live
	 */
	public void getAttacted() {
		live -= 1;
	}
	
	/**
	 * Check if the actvie agent is alive
	 */
	public boolean isAlive() {
		boolean alive = true;
		if (live <= 0) {
			alive = false;
		}
		return alive;
	}
}
