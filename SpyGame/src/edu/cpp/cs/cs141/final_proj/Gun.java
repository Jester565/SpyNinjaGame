package edu.cpp.cs.cs141.final_proj;

public class Gun extends Weapon{
	
	/**
	 * This integer keeps track of the damage done by the gun. It's kept at 1 for now 
	 * since the enemies are 1 hit kill.
	 */
	static final int damage = 1;
	
	/**
	 * This keeps track of how many bullets the player has.
	 */
	int numRounds;
	
	/**
	 * This method is called to do damage to an enemy.
	 * @param damage
	 */
	public Gun(int damage){
		super(damage);
	}
	
	/**
	 * This is called when the player picks up the bullet upgrade.
	 * @param numRounds
	 */
	void addBullet(int numRounds){
		
	}
	
	/**
	 * This is called when a player hits an enemy, the method will kill them.
	 * @param character
	 */
	public void kill(Character character){
		
	}

}
