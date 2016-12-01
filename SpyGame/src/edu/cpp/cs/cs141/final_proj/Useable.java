package edu.cpp.cs.cs141.final_proj;

/**
 * Interface for power ups that can be used on a {@link Spy}.
 */
public interface Useable {
	/**
	 * Causes an effect on the {@link Spy} passed in as a parameter.
	 * @param player The {@link Spy} to effect.
	 * @return {@code true} when consumed, {@code false} otherwise.
	 */
	public abstract boolean useOn(Spy player);
}
