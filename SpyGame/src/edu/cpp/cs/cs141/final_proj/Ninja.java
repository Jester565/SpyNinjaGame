package edu.cpp.cs.cs141.final_proj;

/**
 * Represents the {@link Character} that will be controlled by the computer.  Possesses a {@link Sword}.
 * @author Hao, ajcra
 */
public class Ninja extends Character{
	/**
	 * The initial and maximum value of {@link Character#health}.
	 */
	private static final int NINJA_MAX_HEALTH = 1;
	
	/**
	 * The {@link Sword} to attack in the method {@link #stab(Character)}.
	 */
	private Sword sword;
	
	/**
	 * Every ninja have 1 life point
	 */
	public Ninja() {
		super("N", NINJA_MAX_HEALTH);
		sword = new Sword();
	}

	/**
	 * Stab spy and spy will lose health.
	 */
	public void stab(Character enemy) {
		sword.attack(enemy);
	}
}
