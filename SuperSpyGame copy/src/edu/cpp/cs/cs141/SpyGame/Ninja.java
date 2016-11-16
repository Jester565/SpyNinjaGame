package edu.cpp.cs.cs141.SpyGame;

public class Ninja extends GameObject{

	private boolean alive = true;
	
	/**
	 * check if the ninja is dead
	 */
	public boolean isAlive() {
		return alive;
	}
	
	public void stab(Spy spy) {
		spy.getStabbed();
	}
	
	public Ninja(int x, int y) {
		super(x, y);
	}
	
	public void getShoot() {
		alive = false;
	}

	public String toString(boolean isDebug) {
		if (isDebug || isVisible()) {
			return "N";
		} else {
			return "*";
		}
	}

}
