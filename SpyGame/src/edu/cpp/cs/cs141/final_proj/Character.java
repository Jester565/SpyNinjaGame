package edu.cpp.cs.cs141.final_proj;

/**
 *an abstract class for Spy and Ninja class, have the attribes and behaviors for 
 *Spy and Ninja
 */
public abstract class Character extends GameObject{
	/**
	 * Value to keep track of how much more damage can be taken before dead.
	 */
	private int health;
	
	/**
	 * Sets {@
	 * @param gridRepresentation
	 * @param initialHealth
	 */
	public Character(String gridRepresentation, int initialHealth)
	{
		super(gridRepresentation);
		this.health = initialHealth;
	}
	
	/**
	 * Get stabbed or shot and health is reduced by dmg.
	 */
	public void takeDamage(int dmg) {
		health -= dmg;
	}
	
	/**
	 * Checks the life status of a character
	 * @return True if lifePts <= 0, false otherwise
	 */
	public boolean isAlive() {
		
		if (health <= 0)
			return false;
		else
			return true;
	}
}
