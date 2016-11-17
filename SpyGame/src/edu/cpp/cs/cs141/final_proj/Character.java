package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

/**
 * An abstract class for Spy and Ninja class which has the attributes and behaviors for 
 * Spy and Ninja
 */
public abstract class Character extends GameObject implements Serializable {
	private static final long serialVersionUID = 4446619415924403882L;
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
		if (dmg > 0)
		{
			health -= dmg;
		}
		else
		{
			System.err.println("takeDamage attempted to restore player health make sure the parameter is non negative.");
		}
	}
	
	public void setHealth(int health)
	{
		this.health = health;
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
