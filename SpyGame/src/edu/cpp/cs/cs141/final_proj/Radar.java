package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * This class represents the Radar Power-Up
 * Upon usage, the player is given the location of the briefcase.
 * @author Jason
 *
 */
public class Radar extends GameObject implements Useable, Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8261583359429564007L;

	/**
	 * Creates an instance of {@link Radar} and sets the gridRepresentation.
	 * Lower-case due to "Room" using "R"
	 */
	public Radar() {
		super("r");
	}

	/**
	 * This method gives the spy radar
	 * 
	 * @param spy The player character
	 * @return true for usage of Radar powerup
	 */
	public boolean useOn(Spy spy)
	{
		spy.setRadar(true);
		return true;
	}
	
	/**
	 * Overrides so that when an {@link Spy} steps on {@code this}, it knows it is a powerup.
	 */
	@Override
	public MoveStatus stepOn(DIRECTION approachDirection)
	{
		return new MoveStatus(MOVE_RESULT.LEGAL, "Radar acquired!");
	}
}
