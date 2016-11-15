package edu.cpp.cs.cs141.final_proj;

/**
 * Represents a room in SpyGame
 */
public class Room extends GameObject{

	/**
	 * Indicates whether or not the {@link Room} has a briefCase in it.
	 */
	private boolean briefCase = false;
	
	/**
	 * Creates an instance of {@link Room} and sets the gridRepresentation.
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
}
