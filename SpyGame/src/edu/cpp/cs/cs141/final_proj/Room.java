package edu.cpp.cs.cs141.finalproject;

public class Room extends GameObject{

	private boolean briefCase = false;
	
	/**
	 * Every room has type "s"
	 */
	public Room() {
		setObjectType("r");
	}
	
	/**
	 * Check if the room have briefcase();
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
