package edu.cpp.cs.cs141.final_proj;

/**
 * This class represents the Bullet Power-Up
 * Upon usage, the player is given an additional bullet to use.
 * @author Jason
 *
 */
public class Bullet extends GameObject implements Useable {
	/**
	 * This field holds the amount of bullets to be given to the player.
	 * This will never change and will stay at {@code 1}
	 */
	private static final int BULLETS_GIVEN = 1;

	/**
	 * Creates an instance of {@link Bullet} and sets the gridRepresentation.
	 */
	public Bullet() {
		super("B");
	}

	/**
	 * This method gives the spy a bullet
	 * @param spy - The player character
	 */
	public void useOn(Spy spy) {
		spy.getGun().addBullet(BULLETS_GIVEN);
	}
}
