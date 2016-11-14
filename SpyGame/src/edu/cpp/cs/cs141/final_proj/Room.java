package edu.cpp.cs.cs141.final_proj;

public class Room extends GameObject{

	private boolean briefCase = false;
	
	/**
	 * Every room has type "R"
	 */
	public Room() {
		super("R");
	}
	
	/**
	 * Check if the room has briefcase
	 */
	public boolean hasBriefcase() {
		return briefCase;
	}
	
	/**
	 * put a brief in the room
	 */
	public void setBriefCase() {
		briefCase = true;
	}
	
	@Override
	public boolean isVisible()
	{
		return true;
	}
}
