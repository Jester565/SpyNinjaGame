package edu.cpp.cs.cs141.final_proj;

public class Radar extends Item{

	public Radar() {
		setObjectType("R");
	}

	/**
	 * Display the briefcase in the room.
	 */
	public void toSpy(Spy player) {
		player.hasRadar();
	}
}
