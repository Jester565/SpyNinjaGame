package edu.cpp.cs.cs141.final_proj;

public class Spy extends Character{
	private static final int SPY_MAX_HEALTH = 1;
	private boolean hasRadar = false;
	private Gun gun = new Gun();
	private int invincibleTurns = 0;
	
	/**
	 * A spy starts with three lives and location at bottom.
	 */
	public Spy() {
		super("S", SPY_MAX_HEALTH);
	}
	
	/**
	 * Look in the Grid
	 */
	public void look(int direction) {
		
	}
	
	/**
	 * Shoot enemy
	 */
	public void shoot(Character target) {
		gun.attack(target);
	}
	
	/**
	 * Sets invincibility
	 */
	public void setInvincibility(int turns) {
		invincibleTurns += turns;
	}

	/**
	 * Spy gets a radar
	 */
	public void setRadar(boolean mode) {
		hasRadar = mode;
	}

	/**
	 * Accessor for the {@link gun}.
	 * @return The Spy's gun
	 */
	public Gun getGun()
	{
		return gun;
	}
	
	//@Override
	public boolean isVisible()
	{
		return true;
	}
}
