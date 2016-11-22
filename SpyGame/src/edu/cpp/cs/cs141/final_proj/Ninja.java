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

	/**
	 * Stab spy and spy will lose health.
	 * @param enemy {@link Character} to deal damage to.
	 */
	public void stab(Character enemy) {
		enemy.takeDamage(sword.damage);
	}
	
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
}
