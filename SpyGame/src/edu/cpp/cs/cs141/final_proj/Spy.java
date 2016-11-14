package edu.cpp.cs.cs141.final_proj;

public class Spy extends Character{

	
	private int bullet = 1;
	private boolean radar = false;
	private boolean invincibility = false;
	private boolean briefCase = false;
	
	/**
	 * A spy starts with three lives and location at bottom.
	 */
	public Spy() {
		setObjectType("S");
		setLife(3);
	}
	
	/**
	 * check if the spy has a briefcase
	 */
	public boolean hasBriefCase() {
		return briefCase;
	}
	/**
	 * Look in the Grid
	 */
	public void look(int direction) {
		
	}
	
	/**
	 * Shoot enemy
	 */
	public void shoot(Ninja ninja) {
		ninja.attacked();
	}
	
	/**
	 * Sets invincibility
	 */
	public void hasInvincibility() {
		invincibility = true;
	}

	/**
	 * Spy gets a radar
	 */
	public void hasRadar() {
		radar = true;
	}

	/**
	 * Add a bullet to player
	 */
	public void getBullet() {
		bullet += 1;
	}
}
