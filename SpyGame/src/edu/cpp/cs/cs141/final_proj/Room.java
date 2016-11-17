package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Represents a room in SpyGame
 */
public class Room extends GameObject implements Serializable {

	private static final long serialVersionUID = -2582178436693933013L;
	/**
	 * Indicates whether or not the {@link Room} has a briefCase in it.
	 */
	private boolean briefCase = false;
	
	/**
	 * The String shown on the Grid for a revealed room.
	 */
	private static final String BRIEF_CASE_REPRESENTATION = "b";
	
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
	 * Overrides the {@link #setVisible()} method so it does nothing
	 */
	public void setVisible()
	{
		
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
	
	/**
	 * Changes the {@link #gridRepresentation} to {@code "D"} if this Room has
	 * {@link #briefCase} 
	 */
	public void revealBriefCase()
	{
		if (briefCase)
			gridRepresentation = BRIEF_CASE_REPRESENTATION;
	}
	
	@Override
	public boolean isPenetrable()
	{
		return false;
	}
	
	@Override
	public String getGridRepresentation() {
		if (!GameEngine.DebugMode || !briefCase)
			return gridRepresentation;
		else
			return BRIEF_CASE_REPRESENTATION;
	}
}
