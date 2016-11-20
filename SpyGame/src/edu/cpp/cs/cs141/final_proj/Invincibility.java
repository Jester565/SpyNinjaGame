package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * This class represents the Invincibility Power-Up
 * Upon usage, the player is given invincibility.
 * @author Jason
 *
 */
public class Invincibility extends GameObject implements Useable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5977137733391818570L;
	/**
	 * This field represents the number of turns that the player
	 * is given invincibility.
	 * This field will never change and will stay at {@code 5}
	 */
	private static final int INVINCIBLE_TURNS = 5;
	
	/**
	 * Creates an instance of {@link Invincibility} and sets the gridRepresentation.
	 */
	public Invincibility() {
		super("I");
	}

	/**
	 * This method gives the spy invincibility for {@link #INVINCIBLE_TURNS}.
	 * 
	 * @param spy - The player character
	 */
	public void useOn(Spy spy) {
		spy.setInvincibility(INVINCIBLE_TURNS);
	}
	
	/**
	 * Overrides so that when an {@link Spy} steps on {@code this}, it knows it is a powerup.
	 */
	@Override
	public MoveStatus stepOn(DIRECTION approachDirection)
	{
		return new MoveStatus(MOVE_RESULT.LEGAL, "You aquired invisiblility!");
	}
}
