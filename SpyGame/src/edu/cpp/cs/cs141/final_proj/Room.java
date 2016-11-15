package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Represents a room in SpyGame
 */
public class Room extends GameObject implements Serializable {

	/**
	 * Indicates whether or not the {@link Room} has a briefCase in it.
	 */
	private boolean briefCase = false;
	
	/**
	 * Creates an instance of {@link Room} and sets the gridRepresentation.
	 * @param hasBriefCase Used to set whether or not the {@link Room} has a briefcase.
	 */
	public Room(boolean hasBriefCase) {
		super("R");
		this.briefCase = hasBriefCase;
	}
	
	/**
	 * Accessor for the {@link #briefCase} field.
	 * @return {@code true} if has a briefCase in the room, {@code false} otherwise.
	 */
	public boolean hasBriefcase() {
		return briefCase;
	}
	
	/**
	 * Overrides the {@link #isVisible()} method to always be {@code true}.
	 */
	@Override
	public boolean isVisible()
	{
		return true;
	}
	
	/**
	 * Overrides so that the room can only be entered when approaching from the south.
	 */
	@Override
	public MoveStatus stepOn(DIRECTION approachDirection)
	{
		if (approachDirection == DIRECTION.DOWN)
		{
			if (hasBriefcase())
			{
				return new MoveStatus(MOVE_RESULT.WIN, "You found the briefcase!");
			}
			else
			{
				return new MoveStatus(MOVE_RESULT.ILLEGAL, "The room did not have a briefcase");
			}
		}
		else
		{
			return new MoveStatus(MOVE_RESULT.ILLEGAL, "You could not enter the room");
		}
	}
}
