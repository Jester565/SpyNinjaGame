package edu.cpp.cs.cs141.final_proj;

public class Bullet extends Item{

	public Bullet() {
		setObjectType("A");
	}

	/**
	 * Give the spy one more bullet
	 */
	public void toSpy(Spy spy) {
		spy.getBullet();
	}
}
