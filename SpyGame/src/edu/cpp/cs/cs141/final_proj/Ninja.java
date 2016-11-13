package edu.cpp.cs.cs141.final_proj;

public class Ninja extends ActiveAgent{

	private static enum DIRECTION{};
	
	/**
	 * Every ninja have 1 live
	 */
	public Ninja() {
		setObjectType("n");
		setLife(1);
	}

	/**
	 * Stab spy and spy will lose one live
	 */
	public void stab(Spy spy) {
		spy.getAttacted();
	}
	
	/**
	 * move to the direction
	 */
	public void move(DIRECTION direction) {
		
	}
}
