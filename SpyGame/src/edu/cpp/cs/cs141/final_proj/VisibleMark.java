package edu.cpp.cs.cs141.final_proj;

/**
 * Put into {@link Grid} to represent and empty but visible position.
 * @author ajcra
 */
public class VisibleMark extends GameObject {
	/**
	 * Creates an instance of {@link VisibleMark} and sets {@link GameObject#visible} to {@code true}.
	 */
	public VisibleMark()
	{
		super(" ");
		visible = true;
	}
}
