package edu.cpp.cs.cs141.final_proj;

public class Radar extends GameObject implements Useable{

	public Radar() {
		super("r");
	}

	/**
	 * Display the briefcase in the room.
	 */
	public void useOn(Spy spy)
	{
		spy.setRadar(true);
	}
}
