package edu.cpp.cs.cs141.final_proj;

public class Sword extends Weapon{
	
	/**
	 * This integer keeps track of the damage done by the sword. It's kept at 1 for now 
	 * since the player is 1 hit kill.
	 */
	static final int SWORD_DAMAGE = 1;
	
	/**
	 * This method is called to do damage to player.
	 * @param damage
	 */
	public Sword(){
		super(SWORD_DAMAGE);
	}
}
