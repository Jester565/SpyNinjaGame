package edu.cpp.cs.cs141.final_proj;

/**
 * Represents the {@link Character} that will be controlled by the user.  Possesses a {@link Gun}.
 * @author Hao, ajcra
 *
 */
public class Spy extends Character{
	
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
	 * Shoot the target {@link Character} with the {@link #gun}
	 */
	public void shoot(Character target) {
		gun.attack(target);
	}
	
	/**
	 * Sets {@link #invincibleTurns} to the parameter turns.
	 */
	public void setInvincibility(int turns) {
		invincibleTurns = turns;
	}

	/**
	 * Modifier for the {@link #hashRadar} field.
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
	 * @return {@code true} if has radar power up, {@link false} otherwise.
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
}
