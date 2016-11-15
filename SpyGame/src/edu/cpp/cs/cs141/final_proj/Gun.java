package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

/**
 * This class represents the long range and one hit kill weapon used by the {@link Spy}.
 * @author Jessi
 *
 */
public class Gun extends Weapon implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4465513368274479943L;

	/**
	 * This integer keeps track of the damage done by the gun. It's kept at 1 for now 
	 * since the enemies are 1 hit kill.
	 */
	private static final int GUN_DAMAGE = 1;
	
	/**
	 * This keeps track of how many bullets the player has.
	 */
	int numRounds;
	
	/**
	 * This method is called to do damage to an enemy. It creates an instance of {@link Gun}
	 * and sets damage to {@link #GUN_DAMAGE}.
	 */
	public Gun(){
		super(GUN_DAMAGE);
	}
	
	/**
	 * This is called when the player picks up the bullet upgrade. It will add 1 to 
	 * {@link #numRounds}.
	 * @param numRoundsAdded Number of rounds to add to the gun.
	 */
	void addBullet(int numRoundsAdded){
		this.numRounds += numRoundsAdded;
	}
}
