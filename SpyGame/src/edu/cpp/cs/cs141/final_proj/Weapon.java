package edu.cpp.cs.cs141.final_proj;

public abstract class Weapon {
	
	/**
	 * This will store the amount of damage done by the sword or gun.
	 */
	int damage;
	
	/**
	 * This method is used in both the gun and sword class and it does damage 
	 * to a player or enemy.
	 * @param damage
	 */
	public Weapon(int damage){
		this.damage = damage;
	}
	
	/**
	 * This method is also used in both sword and gun class and it is called
	 * when something dies.
	 * @param character
	 */
	void kill(Character character){
		
	}

}
