package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

/**
 * The short ranged and high damage weapons used by {@link Ninja}s.
 */
public class Sword extends Weapon implements Serializable {

	private static final long serialVersionUID = -5577106023978504789L;
	/**
	 * This integer keeps track of the damage done by the sword. It's kept at 1 for now 
	 * since the player is 1 hit kill.
	 */
	static final int SWORD_DAMAGE = 1;
	
	/**
	 * this integer stores the amount of grid spaces a {@link Character}'s weapon can travel. Swords
	 * can only travel one grid space.
	 */
	private static final int RANGE = 1;
	
	/**
	 * Creates an instance of {@link Sword} and sets the damage to {@link #SWORD_DAMAGE}.
	 */
	public Sword(){
		super(SWORD_DAMAGE, RANGE);
	}
	
	/**
	 * This method is called when a {@link Character} is hit with a weapon. This instance uses
	 * the {@link #SWORD_DAMAGE} of 1.
	 */
	public void hit(Character character){
		character.takeDamage(SWORD_DAMAGE);
	}
}
