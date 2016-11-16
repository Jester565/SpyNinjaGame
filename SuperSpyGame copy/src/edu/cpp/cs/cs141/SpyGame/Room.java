package edu.cpp.cs.cs141.SpyGame;

public class Room extends GameObject{

	private boolean briefCase = false;
	private boolean isDebug = false;
	
	public Room(int i, int j) {
		super(i , j);
	}
	
	public void setDebug() {
		isDebug = true;
	}
	
	/**
	 * Set brief case in the room
	 */
	public void setBriefCase() {
		briefCase = true;
	}
	
	/**
	 * check if the room has bried case
	 */
	public boolean hasBriefCase() {
		return briefCase;
	}

	public String toString(boolean isDebug) {
		if( briefCase & this.isDebug )
			return "C";
		else
			return "R";
	}
}
