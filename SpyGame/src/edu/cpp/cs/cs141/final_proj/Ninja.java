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
	 * The {@link Sword} to attack in the method {@link #stab(Character)}.
	 */
	private Sword sword;
	
	/**
	 * Every ninja have 1 life point
	 */
	public Ninja() {
		super("N", NINJA_MAX_HEALTH);
		sword = new Sword();
	}
	
	public boolean hitSpy(Spy spy, Grid grid)
	{
		if (Math.abs(spy.getX() - x) + Math.abs(spy.getY() - y) <= 1)
		{
			if (spy.getX() < x)
			{
				return stab(DIRECTION.LEFT, grid);
			}
			else if (spy.getX() > x)
			{
				return stab(DIRECTION.RIGHT, grid);
			}
			else if (spy.getY() > y)
			{
				return stab(DIRECTION.DOWN, grid);
			}
			else if (spy.getY() < y)
			{
				return stab(DIRECTION.UP, grid);
			}
			else
			{
				System.err.println("Attempted stab for a spy who was inside of Ninja");
			}
		}
		return false;
	}

	/**
	 * Stab in the direction specified.  Other Ninjas could potentially be killed so make sure the AI knows where it is stabbing.
	 * @param enemy {@link Character} to deal damage to.
	 */
	private boolean stab(DIRECTION direction, Grid grid)
	{
		return sword.attack(direction, grid, x, y);
	}
	
	/**
	 * Overrides so that when an {@link Spy} steps on the {@code this}, it loses.
	 */
	@Override
	public MoveStatus stepOn(DIRECTION approachDirection)
	{
		return new MoveStatus(MOVE_RESULT.ILLEGAL, "Ninjas have personal space too...");
	}
	
	@Override
	public boolean isPenetrable()
	{
		return false;
	}
}
