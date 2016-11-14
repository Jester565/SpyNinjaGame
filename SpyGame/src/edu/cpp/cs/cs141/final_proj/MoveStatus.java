package edu.cpp.cs.cs141.final_proj;

public class MoveStatus {

	public enum MOVE_RESULT {
		ILLEGAL, LEGAL, WIN, LOSE, POWERUP
	}
	
	public MoveStatus(MOVE_RESULT moveResult, String msg)
	{
		this.moveResult = moveResult;
		this.msg = msg;
	}
	
	public MOVE_RESULT moveResult;
	public String msg;
}
