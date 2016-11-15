package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

/**
 * The short ranged and high damage weapons used by {@link Ninja}s.
 */
public class Sword extends Weapon implements Serializable {
	
	/**
	 * This integer keeps track of the damage done by the sword. It's kept at 1 for now 
	 * since the player is 1 hit kill.
	 */
	static final int SWORD_DAMAGE = 1;
	
	/**
	 * Creates an instance of {@link Sword} and sets the damage to {@link #SWORD_DAMAGE}.
	 */
	public Sword(){
		super(SWORD_DAMAGE);
	}
}
