package edu.cpp.cs.cs141.final_proj;

public class AdditionalBullet extends Item{

	public AdditionalBullet() {
		setObjectType("a");
	}

	/**
	 * Give the spy one more bullet
	 */
	public void toSpy(Spy spy) {
		spy.getBullet();
	}
}
