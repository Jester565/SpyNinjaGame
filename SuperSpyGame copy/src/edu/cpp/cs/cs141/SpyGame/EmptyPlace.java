package edu.cpp.cs.cs141.SpyGame;

public class EmptyPlace extends GameObject{

	public EmptyPlace(int x, int y) {
		super(x, y);
	}

	public String toString(boolean isDebug) {
		if (isDebug || isVisible()) {
			return " ";
		} else {
			return "*";
		}
	}
}
	
