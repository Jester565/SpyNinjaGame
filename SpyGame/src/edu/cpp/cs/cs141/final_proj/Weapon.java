package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;

/**
 * Represents a Weapon that can attack {@link Character}s.
 */
public abstract class Weapon implements Serializable {	
	private static final long serialVersionUID = 6423199165881024149L;

	/**
	 * This will store the amount of damage done by the sword or gun.
	 */
	int damage;
	
	/**
	 * This will store the amount of grid spaces the weapon can travel to hit an enemy.
	 */
	int range;
	
	/**
	 * This method is used in both the gun and sword class and it does damage 
	 * to a player or enemy.
	 * @param damage The amount of damage that will dealt in {@link #attack(Character)}.
	 */
	public Weapon(int damage, int range){
		this.damage = damage;
		this.range = range;
	}
	
	public boolean attack(DIRECTION shootDirection, Grid grid, int x, int y) {
		switch (shootDirection)
		{
		case UP:
			return attack(x, y, 0, -1, grid);
		case RIGHT:
			return attack(x, y, 1, 0, grid);
		case DOWN:
			return attack(x, y, 0, 1, grid);
		case LEFT:
			return attack(x, y, -1, 0, grid);
		default:
			System.err.println("Invalid shoot option");	
			return false;
		}
	}
	
	protected boolean attack(int x, int y, int dX, int dY, Grid grid)
	{
		boolean characterShot = false;
		for (int i = 0; i < range; i++) {
			x += dX;
			y += dY;
			if (!grid.inRange(x, y))
			{
				break;
			}
			GameObject gObj = grid.getGameObject(x, y);
			if (gObj != null)
			{
				if (gObj instanceof Character)
				{
					((Character)gObj).takeDamage(damage);
					characterShot = true;
				}
				if (!gObj.isPenetrable())
				{
					break;
				}
			}
		}
		return characterShot;
	}

}
