package edu.cpp.cs.cs141.SpyGame;

public class Spy extends GameObject{

	private int bullet = 1;
	private boolean briefCase = false;
	private int invinc ;
	private boolean radar;
	private char direction;
	private int live = 3;
	private boolean alive = true;
	
	public Spy() {
		alive = true;
	}
	
	public int getLive() {
		return live;
	}
	
	public boolean hasLives() {
		if (live <= 0 ) {
			return false;
		}
		return true;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void getBriefCase() {
		briefCase = true;
	}
	
	public boolean hasBriefCase() {
		return briefCase;
	}
	public void getRadar() {
		radar = true;
	}
	
	public int getInvinc() {
		return invinc;
	}
	public boolean hasBullet() {
		boolean result = true;
		if (bullet <= 0) result = false;
		return result;
	}
	
	public void loseBullet() {
		bullet --;
	}
	
	/**
	 * get the direction of the spy
	 */
	public char getDirection() {
		return direction;
	}
	
	public void getStabbed() {
		alive = false;
		live --;
	}

	public String toString(boolean isDebug) {
		return "S";
	}

	public void changeDirection(char c) {
		// TODO Auto-generated method stub
		
	}

	public void getAdditionalBullet() {
		bullet ++;
	}
	public int getBullet() {
		return bullet;
	}

	public void setInvinc() {
		invinc += 5;
		
	}

	public boolean isInvinc() {
		boolean result = false;
		if (invinc > 0) {
			result = true;
		}
		return result;
	}

	public void weakenInvinc() {
		invinc --;
		
	}

}
