package edu.cpp.cs.cs141.finalproject;

public class Spy extends ActiveAgent{

	private int bullet = 1;
	private boolean radar = false;
	private boolean invincibility = false;
	private boolean briefCase = false;
	
	/**
	 * Every spy have three lives first and location at bottom.
	 */
	public Spy() {
		setObjectType("s");
		setLife(3);
	}
	
	/**
	 * check if the spy have briefcase
	 */
	public boolean getBriefCase() {
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
		ninja.getAttacted();
	}
	
	/**
	 * Set invincibility
	 */
	public void getInvincibility() {
		invincibility = true;
	}

	/**
	 * spy get a radar
	 */
	public void getRadar() {
		radar = true;
	}

	/**
	 * Add a bullet to player
	 */
	public void getBullet() {
		bullet += 1;
	}
}
