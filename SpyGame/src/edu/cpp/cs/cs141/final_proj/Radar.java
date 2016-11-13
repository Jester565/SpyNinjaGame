package edu.cpp.cs.cs141.final_proj;

public class Radar extends Item{

	public Radar() {
		setObjectType("r");
	}

	/**
	 * Display the biefcase in the room.
	 */
	public void toSpy(Spy player) {
		player.getRadar();
	}
}
