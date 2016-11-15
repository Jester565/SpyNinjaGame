package edu.cpp.cs.cs141.final_proj;
/**
 * Abstract class that all items will extend from.
 * @author Jason
 *
 */
public abstract class Item extends GameObject{

	/**
	 * Constructor - Creates an instance of the item and sets the gridRepresentation.
	 * @param gridRepresentation - What character the object should be represented by on the grid
	 */
	public Item(String gridRepresentation) {
		super(gridRepresentation);
	}

	/**
	 * Abstract method that will give the player some power-up depending on the power-up received.
	 */
	public abstract void toSpy(Spy spy);
}
