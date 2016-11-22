package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

/**
 * This class represents the long range and one hit kill weapon used by the {@link Spy}.
 * @author Jessi
 *
 */
public class Gun extends Weapon implements Serializable {
	
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
	 * This integer stores the maximum number of rounds that the {@link Spy} can start with.
	 */
	public static final int MAX_ROUNDS = 1;
	
	/**
	 * this integer stores the amount of grid spaces a {@link Character}'s weapon can travel. Guns
	 * can travel the entire length of the grid if a {@link Room} or {@link Ninja} doesn't get in
	 * the way.
	 */
	private static final int RANGE = Grid.GRID_SIZE;
	
	/**
	 * This method is called to do damage to an enemy. It creates an instance of {@link Gun}
	 * and sets damage to {@link #GUN_DAMAGE}.
	 */
	public Gun(){
		super(GUN_DAMAGE, RANGE);
		this.numRounds = MAX_ROUNDS;
	}
	
	/**
	 * This is called when the player picks up the bullet upgrade. It will add 1 to 
	 * {@link #numRounds}.
	 * @param numRoundsAdded Number of rounds to add to the gun.
	 */
	void addBullet(int numRoundsAdded){
		if(this.numRounds < 1){
		this.numRounds += numRoundsAdded;
		}
	}
	
	/**
	 * This method is called when a {@link Character} is hit with a weapon. This instance uses
	 * the {@link #GUN_DAMAGE} of 1.
	 */
	public void hit(Character character){
		character.takeDamage(GUN_DAMAGE);
	}
	
	/**
	 * This method simply returns the number of rounds the {@link Spy} has left.
	 * @return
	 */
	public int getNumRounds() {	
		return numRounds;
	}
	
	/**
	 * This overrides the attack method in the parent class {@link Weapon} to check if the 
	 * {@link Spy} has any bullets left before shooting and also decreases the rounds if not.
	 */
	protected boolean attack(int dX, int dY, Character character, Grid grid){
	 	if (this.numRounds > 0){
	 		this.numRounds--;
	 		return super.attack(dX, dY, character, grid);
	 	}
	 	return false;
	}
		
}
