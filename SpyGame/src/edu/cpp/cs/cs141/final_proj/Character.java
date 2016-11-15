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
	 * Call super constructor passing gridRepresentation and set {@link #health} to initialHealth
	 * @param gridRepresentation The letter to represent {@code this} when drawn in the {@link Grid}.
	 * @param initialHealth The initial value of {@link #health}.
	 */
	public Character(String gridRepresentation, int initialHealth)
	{
		super(gridRepresentation);
		this.health = initialHealth;
	}
	
	/**
	 * Get stabbed or shot and health is reduced by dmg.
	 * @param dmg The amount of damage to be dealt.
	 */
	public void takeDamage(int dmg) {
		health -= dmg;
	}
	
	/**
	 * Checks the life status of a character
	 * @return {@code true} if {@link #health} is less than 0, {@code false} otherwise
	 */
	public boolean isAlive() {
		
		if (health <= 0)
			return false;
		else
			return true;
	}
}
