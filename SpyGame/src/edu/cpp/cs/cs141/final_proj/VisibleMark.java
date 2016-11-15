package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

/**
 * Put into {@link Grid} to represent an empty but visible position.
 * @author ajcra
 */
public class VisibleMark extends GameObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4887396807856126040L;

	/**
	 * Creates an instance of {@link VisibleMark} and sets {@link GameObject#visible} to {@code true}.
	 */
	public VisibleMark()
	{
		super(" ");
	}
	
	/**
	 * Override so that {@link VisibleMark} is always visible.
	 */
	@Override
	public boolean isVisible()
	{
		return true;
	}
}
