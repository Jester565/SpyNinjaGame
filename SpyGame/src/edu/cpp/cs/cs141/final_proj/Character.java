package edu.cpp.cs.cs141.final_proj;

public abstract class Character extends GameObject{


	private int lifePts;
	
	/**
	 * Set the amount of life points an active agent has
	 * @param i
	 */
	public void setLife(int i) {
		lifePts = i;
	}
	
	/**
	 * Get stabbed or shot, lose 1 life point
	 */
	public void attacked() {
		lifePts -= 1;
	}
	
	/**
	 * Checks the life status of a character
	 * @return True if lifePts <= 0, false otherwise
	 */
	public boolean isAlive() {
		
		if (lifePts <= 0)
			return false;
		else
			return true;
	}
}
