package edu.cpp.cs.cs141.final_proj;

/**
 * Represents a Weapon that can attack {@link Character}s.
 */
public abstract class Weapon {	
	/**
	 * This will store the amount of damage done by the sword or gun.
	 */
	int damage;
	
	/**
	 * This method is used in both the gun and sword class and it does damage 
	 * to a player or enemy.
	 * @param damage The amount of damage that will dealt in {@link #attack(Character)}.
	 */
	public Weapon(int damage){
		this.damage = damage;
	}
	
	/**
	 * This method is also used in both sword and gun class and it is called
	 * when something dies.
	 * @param character The {@link Character} to deal damage to.
	 */
	void attack(Character character){
		character.takeDamage(damage);
	}

}
