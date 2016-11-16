package edu.cpp.cs.cs141.SpyGame;

public class Radar extends Item{

	@Override
	public void toSpy(Spy spy) {
		spy.getRadar();
		
	}
	
	public String toString(boolean isDebug) {
		if(isDebug||isVisiable()) {
			if (isUsed()) return " ";
			return "r";
		} else
			return "*";
	}
}
