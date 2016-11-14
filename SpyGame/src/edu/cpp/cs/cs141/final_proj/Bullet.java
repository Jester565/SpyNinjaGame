package edu.cpp.cs.cs141.final_proj;

public class Bullet extends GameObject implements Useable {
	private static final int BULLETS_GIVEN = 1;

	public Bullet() {
		super("B");
	}

	/**
	 * Give the spy one more bullet
	 */
	public void useOn(Spy spy) {
		spy.getGun().addBullet(BULLETS_GIVEN);
	}
}
