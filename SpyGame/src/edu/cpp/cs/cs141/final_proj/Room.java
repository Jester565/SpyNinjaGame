package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Represents a room in SpyGame
 */
public class Room extends GameObject implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2582178436693933013L;
	
	/**
	 * Indicates whether or not the {@link Room} has a briefCase in it.
	 */
	private boolean briefCase = false;
	
	/**
	 * This is the gridRepresentation for the Room when {@link #briefCase} is {@code true}
	 */
	private String briefCaseGridRepresentation = "b";
	
	/**
	 * Creates an instance of {@link Room} and sets the gridRepresentation.
	 * @param hasBriefCase Used to set whether or not the {@link Room} has a briefcase.
	 */
	public Room(boolean hasBriefCase) {
		super("R");
		briefCase = hasBriefCase;
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
				return new MoveStatus(MOVE_RESULT.UNMOVED_TURN_TAKEN, "The room did not have a briefcase");
			}
		}
		return new MoveStatus(MOVE_RESULT.ILLEGAL, "You could not enter the room");
	}
	
	/**
	 * Changes {@link GameObject#gridRepresentation} to {@link #briefCaseGridRepresentation}
	 * if {@link #briefCaseGridRepresentation}
	 */
	public void revealBriefCase() {
		if (briefCase)
			gridRepresentation = briefCaseGridRepresentation;
	}
	
	/**
	 * Overrides {@link GameObject#getGridRepresentation()} to return
	 * {@link #gridRepresentation} when not in debug mode or this Room does not have
	 * the {@link #briefCase}  
	 */
	@Override
	public String getGridRepresentation() 
	{
		if (!GameEngine.DebugMode || !briefCase)
			return gridRepresentation;
		else
			return briefCaseGridRepresentation;
	}
}
