package edu.cpp.cs.cs141.final_proj;

/**
 * Describes the result of a move in the {@link Grid}.
 */
public class MoveStatus {

	/**
	 * Describes the result of the attempted movement.
	 */
	public enum MOVE_RESULT {
		ILLEGAL, LEGAL, UNMOVED, UNMOVED_TURN_TAKEN, WIN;
	}
	
	/**
	 * Describes the result of this attempted move.
	 */
	public MOVE_RESULT moveResult;
	
	/**
	 * The message to be printed with a detailed description of the movement's result.
	 */
	public String msg;
	
	/**
	 * Creates instance of {@link MoveStatus} setting the parameters to the corresponding fields.
	 * @param moveResult {@link #moveResult} is set to this value.
	 * @param msg {@link #msg} is set to this value.
	 */
	public MoveStatus(MOVE_RESULT moveResult, String msg)
	{
		this.moveResult = moveResult;
		this.msg = msg;
	}
	
}
