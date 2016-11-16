package edu.cpp.cs.cs141.SpyGame;

public class Invincibility extends Item{

	public void toSpy(Spy spy) {
		
	}
	
	public String toString(boolean isDebug) {
		if (isDebug || isVisible()) {
			if (isUsed()) return " ";
			return "i";
		} else {
			return "*";
		}
	}
}
