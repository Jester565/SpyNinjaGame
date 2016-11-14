package edu.cpp.cs.cs141.final_proj;

public class Ninja extends Character{

	private static enum DIRECTION{};
	
	/**
	 * Every ninja have 1 life point
	 */
	public Ninja() {
		setObjectType("N");
		setLife(1);
	}

	/**
	 * Stab spy and spy will lose one live
	 */
	public void stab(Spy spy) {
		spy.attacked();
	}
	
	/**
	 * move to the direction
	 */
	public void move(DIRECTION direction) {
		
	}
}
