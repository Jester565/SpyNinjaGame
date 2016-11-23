package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Represents the {@link Character} that will be controlled by the computer.  Possesses a {@link Sword}.
 * @author Hao, ajcra
 */
public class Ninja extends Character implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8819870751929612695L;

	/**
	 * The initial and maximum value of {@link Character#health}.
	 */
	private static final int NINJA_MAX_HEALTH = 1;
	
	/**
	 * The default value for the ninja look range
	 */
	public static final int DEFAULT_LOOK_RANGE = Spy.LOOK_RANGE;
	
	/**
	 * The range of the {@link Ninja} vision
	 */
	private int lookRange = DEFAULT_LOOK_RANGE;
	
	/**
	 * The {@link Sword} to attack in the method {@link #stab(Character)}.
	 */
	private Sword sword;
	
	/**
	 * The coordinate this ninja will move to
	 */
	private int[] destinationCoordinate = null;
	
	/**
	 * The {@link Grid#DIRECTION} this ninja is facing
	 */
	private DIRECTION directionFacing = DIRECTION.UP;
	
	/**
	 * Every ninja have 1 life point
	 */
	public Ninja() {
		super("N", NINJA_MAX_HEALTH);
		sword = new Sword();
	}

	/**
	 * Stab spy and spy will lose health.
	 * @param enemy {@link Character} to deal damage to.
	 */
	public void stab(Character enemy) {
		enemy.takeDamage(sword.damage);
	}
	
	/**
	 * @return {@link #sword}
	 */
	public Sword getSword(){
		return sword;
	}
	
	/**
	 * Overrides so that a {@link Spy} cannot step on a {@link Ninja}.
	 */
	@Override
	public MoveStatus stepOn(DIRECTION approachDirection)
	{
		return new MoveStatus(MOVE_RESULT.ILLEGAL, "You can't walk onto the ninja's tile");
	}
	
	/**
	 * Sets the {@link #destinationCoordinate} value to an array containing x and y
	 * @param x
	 * @param y
	 */
	public void setDestinationCoordinate(int x, int y) {
		int[] coord = {x, y};
		setDestinationCoordinate(coord);
	}
	
	/**
	 * Sets the {@link #destinationCoordinate} value to coord
	 * @param coord
	 */
	public void setDestinationCoordinate(int[] coord) {
		destinationCoordinate = coord;
	}
	
	/**
	 * @return {@link #destinationCoordinate}
	 */
	public int[] getDestinationCoordinate() {
		return destinationCoordinate;
	}
	
	/**
	 * @return {@code true} if this ninja's location is equal to
	 * its {@link #destinationCoordinate}, {@code false} otherwise
	 */
	public boolean arrivedAtDestination() {
		return destinationCoordinate == null ? 
				false: x == destinationCoordinate[0] && y == destinationCoordinate[1];		  
	}
	
	/**
	 * @return {@link #directionFacing}
	 */
	public DIRECTION getDirectionFacing() {
		return directionFacing;
	}
	
	/**
	 * @return {@link #directionFacing} in an array
	 */
	public DIRECTION[] getDirectionFacingAsArray() {
		DIRECTION[] dirFacing = {directionFacing};
		return dirFacing;
	}
	
	/**
	 * Set {@link #directionFacing}
	 * @param dirFacing {@link #directionFacing} becomes this value
	 */
	public void setDirectionFacing(DIRECTION dirFacing) {
		directionFacing = dirFacing;
	}
	
	/**
	 * @return {@link #lookRange}
	 */
	public int getLookRange() {
		return lookRange;
	}
	
	/**
	 * Set {@link #lookRange}
	 * @param sightRange {@link #lookRange} becomes this value
	 */
	public void setLookRange(int sightRange) {
		lookRange = sightRange;
	}
}
