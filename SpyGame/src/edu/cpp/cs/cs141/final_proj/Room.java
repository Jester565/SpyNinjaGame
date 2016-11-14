package edu.cpp.cs.cs141.final_proj;

public class Room extends GameObject{

	private boolean briefCase = false;
	
	/**
	 * Every room has type "R"
	 */
	public Room() {
		setObjectType("R");
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
}
