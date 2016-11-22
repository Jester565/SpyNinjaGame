package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;

/**
 * Represents a Weapon that can attack {@link Character}s.
 */
public abstract class Weapon implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5302248009740573761L;

	/**
	 * This will store the amount of damage done by the sword or gun.
	 */
	int damage;
	
	/**
	 * this integer stores the amount of grid spaces a {@link Character}'s weapon can travel.
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
	
	
	/**
	 * This is the attack method that will be called by {@link GameEngine}. It calls on the 
	 * other attack method, passing in different int's depending on the direction the player
	 * shot in.
	 * @param shootDirection
	 * @param character
	 * @param grid
	 * @return
	 */
	public boolean attack(DIRECTION shootDirection, Character character, Grid grid) {
		return attack(shootDirection.deltaX, shootDirection.deltaY, character, grid);
	}
	
	/**
	 * This method is called by the previous attack method. It checks if the weapon hit another
	 * {@link Character} or if it ran into a {@link Room}.
	 * @param dX
	 * @param dY
	 * @param character
	 * @param grid
	 * @return
	 */
	protected boolean attack(int dX, int dY, Character character, Grid grid){
		int bX = character.getX();
		int bY = character.getY();
		
		boolean targetHit = false;
		for(int i=0; i<range; i++){
			bX += dX;
			bY += dY;
			if(!(grid.inRange(bX, bY))){
				break;
			}
			GameObject object = grid.getGameObject(bX, bY);
		 	if (object != null)
		 	{
		 		if (object instanceof Character)
		 		{
		 			((Character)object).takeDamage(damage);
		 			targetHit = true;
		 		}
		 		else if (object instanceof Room)
		 		{
		 			break;
		 		}
		 	}
		}
		return targetHit;
	}
		
}
