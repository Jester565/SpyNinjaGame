package edu.cpp.cs.cs141.final_proj;

/**
 * An abstract class for Spy and Ninja class which has the attributes and behaviors for 
 * Spy and Ninja
 */
public abstract class Character extends GameObject{
	/**
	 * Value to keep track of how much more damage can be taken before dead.
	 */
	private int health;
	
	/**
	 * Call super constructor passing gridRepresentation & set {@link #health} to initialHealth
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
	 * @return {@code true} if {@link #health} <= 0, {@code false} otherwise
	 */
	public boolean isAlive() {
		
		if (health <= 0)
			return false;
		else
			return true;
	}
}
