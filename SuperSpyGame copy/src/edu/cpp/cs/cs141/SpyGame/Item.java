package edu.cpp.cs.cs141.SpyGame;

public abstract class Item extends GameObject{

	private boolean visible;
	private boolean used;
	
	/**
	 * check if the item is used
	 */
	public boolean isUsed() {
		return used;
	}
	
	public void beenUsed() {
		used = true;
	}
	public Item() {
		visible =false;
		used =false;
	}
	
	public boolean isVisiable() {
		return visible;
	}
	
	
	/**
	 * Give item to spy
	 */
	public abstract void toSpy(Spy spy);
}
