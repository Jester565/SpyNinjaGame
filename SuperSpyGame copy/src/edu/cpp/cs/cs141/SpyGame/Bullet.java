package edu.cpp.cs.cs141.SpyGame;

public class Bullet extends Item{

	/**
	 * load a bullet to spy
	 * @param spy
	 */
	public void toSpy(Spy spy) {
		
	}
	
	public String toString(boolean isDebug) {
		if(isDebug||isVisiable()) {
			if (isUsed()) return " ";
			return "B";
		}else
			return "*";
	}
}
