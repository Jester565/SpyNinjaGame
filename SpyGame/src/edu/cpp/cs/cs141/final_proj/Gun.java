package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;
import java.util.ArrayList;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;

/**
 * This class represents the long range and one hit kill weapon used by the {@link Spy}.
 * @author Jessi
 *
 */
public class Gun extends Weapon implements Serializable {
	
	private static final long serialVersionUID = 4465513368274479943L;

	/**
	 * Maximum and initial amount of ammo.
	 */
	public static final int MAX_AMMO = 1;
	/**
	 * This integer keeps track of the damage done by the gun. It's kept at 1 for now 
	 * since the enemies are 1 hit kill.
	 */
	private static final int GUN_DAMAGE = 1;
	
	/**
	 * Value to set {@link #range} to.
	 */
	private static final int GUN_RANGE = Grid.GRID_SIZE;
	
	/**
	 * This keeps track of how many bullets the player has.
	 */
	private int numRounds;
	
	/**
	 * This method is called to do damage to an enemy. It creates an instance of {@link Gun}
	 * and sets damage to {@link #GUN_DAMAGE}.
	 */
	public Gun(){
		super(GUN_DAMAGE, GUN_RANGE);
		this.numRounds = MAX_AMMO;
	}
	
	/**
	 * Accessor for number of rounds in the {@link Gun}.
	 * @return
	 */
	public int getNumRounds()
	{
		return numRounds;
	}
	
	/**
	 * This is called when the player picks up the bullet upgrade. It will add 1 to 
	 * {@link #numRounds}.
	 * @param numRoundsAdded Number of rounds to add to the gun.
	 */
	public void addBullet(int numRoundsAdded){
		if (numRounds + numRoundsAdded > MAX_AMMO)
		{
			this.numRounds += numRoundsAdded;
		}
		else
		{
			numRounds = MAX_AMMO;
		}
	}
	
	protected boolean attack(int x, int y, int dX, int dY, Grid grid)
	{
		if (this.numRounds > 0)
		{
			this.numRounds--;
			return super.attack(x, y, dX, dY, grid);
		}
		return false;
	}
}
