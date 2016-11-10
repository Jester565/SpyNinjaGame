package edu.cpp.cs.cs141.final_proj;

public abstract class GameObject {
	
	int x;
	int y;
	String boardRep;
	String msg;
	
	public String getBoardRepresentation(){
		
		return boardRep;
	}
	
	public void MoveStatus(){
		
	}
	
	enum MOVE_RESULT {
		WIN, LOSE, LEGAL, ILLEGAL, ON_ITEM
	}
	
	MOVE_RESULT stepOn(DIRECTION dir){
		
		return null;
	}
	
	GameObject onTopOfObject;

}
