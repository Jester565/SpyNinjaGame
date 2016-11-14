package edu.cpp.cs.cs141.final_proj;

public class Ninja extends Character{
	private static final int NINJA_MAX_HEALTH = 1;
	private Sword sword;
	
	/**
	 * Every ninja have 1 life point
	 */
	public Ninja() {
		super("N", NINJA_MAX_HEALTH);
		sword = new Sword();
	}

	/**
	 * Stab spy and spy will lose one live
	 */
	public void stab(Character enemy) {
		sword.attack(enemy);
	}
}
