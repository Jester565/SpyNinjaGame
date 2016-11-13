package edu.cpp.cs.cs141.finalproject;

public class InvincibilityItem extends Item{
	
	public InvincibilityItem() {
		setObjectType("i");
	}

	/**
	 * Set the spy invincibility
	 */
	public void toSpy(Spy player) {
		player.getInvincibility();
	}
}
