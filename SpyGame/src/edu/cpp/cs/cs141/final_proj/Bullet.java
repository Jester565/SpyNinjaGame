package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * This class represents the Bullet Power-Up
 * Upon usage, the player is given an additional bullet to use.
 * @author Jason
 *
 */
public class Bullet extends GameObject implements Useable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1032007805159733083L;
	/**
	 * This field holds the amount of bullets to be given to the player.
	 * This will never change and will stay at {@code 1}
	 */
	private static final int BULLETS_GIVEN = 1;

	/**
	 * Creates an instance of {@link Bullet} and sets the gridRepresentation.
	 */
	public Bullet() {
		super("B");
	}

	/**
	 * This method gives the spy a bullet
	 * @param spy The player character
	 * @return {@code true} if bullet is used, {@code false} otherwise
	 */
	public boolean useOn(Spy spy) {
		if (spy.getGun().getNumRounds() < Gun.MAX_ROUNDS) {
			spy.getGun().addBullet(BULLETS_GIVEN);
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Overrides so that when an {@link Spy} steps on {@code this}, it knows it is a powerup.
	 */
	@Override
	public MoveStatus stepOn(DIRECTION approachDirection)
	{
		return new MoveStatus(MOVE_RESULT.LEGAL, "You picked up a bullet!");
	}
}
