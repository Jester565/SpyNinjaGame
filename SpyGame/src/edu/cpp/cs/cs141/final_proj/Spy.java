package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;

/**
 * Represents the {@link Character} that will be controlled by the user.  Possesses a {@link Gun}.
 * @author Hao
 *
 */
public class Spy extends Character implements Serializable {
	
	/**
	 * Stores how far the {@link Spy} can look.
	 */
	public static final int LOOK_RANGE = 2;
	
	/**
	 * Initial column the {@link Spy} spawns on.
	 */
	public static final int INITIAL_X = 0;
	
	/**
	 * Initial row the {@link Spy} spawns on.
	 */
	public static final int INITIAL_Y = Grid.GRID_SIZE - 1;
	
	/**
	 * The maximum and initial value for {@link Character#health}.
	 */
	private static final int SPY_MAX_HEALTH = 1;
	
	/**
	 * Keeps track of if the {@link Spy} has aquired the {@link Radar} powerup.
	 */
	private boolean hasRadar = false;
	
	/**
	 * The Gun possessed by the {@link Spy} used to shoot other {@link Character}s.
	 */
	private Gun gun;
	
	/**
	 * The amount of turns left for the {@link Spy} to be invincible for.
	 */
	private int invincibleTurns = 0;
	
	/**
	 * Creates an instance of {@link Spy} with a {@link Gun}.
	 */
	public Spy() {
		super("S", SPY_MAX_HEALTH);
		gun = new Gun();
	}
	
	/**
	 * Called before every player's turn to decrement {@link #invincibleTurns}.
	 */
	public void reduceInvisibility()
	{
		if (invincibleTurns > 0)
		{
			invincibleTurns--;
		}
	}
	
	/**
	 * Shoot the target {@link Character} with the {@link #gun}.
	 * @param target {@link Character} to deal damage to.
	 */
	public void shoot(Character target) {
		gun.attack(target);
	}
	
	/**
	 * Sets {@link #invincibleTurns} to the parameter turns.
	 * @param turns Amount of turns to grant invincibility for.
	 */
	public void setInvincibility(int turns) {
		invincibleTurns = turns;
	}

	/**
	 * Modifier for the {@link #hasRadar} field.
	 * @param mode Value to set {@link #hasRadar} to.
	 */
	public void setRadar(boolean mode) {
		hasRadar = mode;
	}

	/**
	 * Accessor for the {@link #gun}.
	 * @return The Spy's {@link Gun}.
	 */
	public Gun getGun()
	{
		return gun;
	}
	
	/**
	 * Accessor for the {@link #hasRadar} field.
	 * @return {@code true} if has radar power up, {@code false} otherwise.
	 */
	public boolean hasRadar()
	{
		return hasRadar;
	}
	
	/**
	 * Accessor to show if the {@link Spy} is invincible or not.
	 * @return {@code true} if invincible, {@code false} otherwise.
	 */
	public boolean isInvincible()
	{
		return invincibleTurns > 0;
	}
	
	/**
	 * Overrides {@link GameObject#isVisible()} so that the {@link Spy} is always visible.
	 */
	//@Override
	public boolean isVisible()
	{
		return true;
	}
	
	/**
	 * Overrides so that when an enemy steps on the {@code this}, it loses.
	 */
	@Override
	public MoveStatus stepOn(DIRECTION approachDirection)
	{
		return new MoveStatus(MOVE_RESULT.LOSE, "A ninja snuck up on you and cut you in half");
	}
}
