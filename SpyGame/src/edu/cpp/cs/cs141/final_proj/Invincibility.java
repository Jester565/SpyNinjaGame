package edu.cpp.cs.cs141.final_proj;

public class Invincibility extends GameObject implements Useable{
	private static final int INVINCIBLE_TURNS = 5;
	
	public Invincibility() {
		super("I");
	}

	/**
	 * Set the spy invincibility
	 */
	public void useOn(Spy player) {
		player.setInvincibility(INVINCIBLE_TURNS);
	}
}
