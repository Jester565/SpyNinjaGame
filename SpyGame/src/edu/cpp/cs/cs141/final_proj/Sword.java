package edu.cpp.cs.cs141.final_proj;

public class Sword extends Weapon{
	
	/**
	 * This integer keeps track of the damage done by the sword. It's kept at 1 for now 
	 * since the player is 1 hit kill.
	 */
	static final int damage = 1;
	
	/**
	 * This method is called to do damage to player.
	 * @param damage
	 */
	public Sword(int damage){
		super(damage);
	}
	
	/**
	 * This is called when an enemy hits the player, the method will kill them.
	 */
	public void kill(Character character){
		
	}

}
