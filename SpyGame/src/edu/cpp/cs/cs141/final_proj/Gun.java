package edu.cpp.cs.cs141.final_proj;

import java.io.Serializable;
import java.util.ArrayList;

import edu.cpp.cs.cs141.final_proj.Grid.DIRECTION;

/**
 * This class represents the long range and one hit kill weapon used by the {@link Spy}.
 * @author Jessi
 *
 */
public class Gun extends Weapon implements Serializable {
	
	private static final long serialVersionUID = 4465513368274479943L;
	
	private Grid grid = new Grid();
	
	public boolean collision;
	
	public boolean hit;

	/**
	 * This integer keeps track of the damage done by the gun. It's kept at 1 for now 
	 * since the enemies are 1 hit kill.
	 */
	private static final int GUN_DAMAGE = 1;
	
	/**
	 * This keeps track of how many bullets the player has.
	 */
	int numRounds;
	
	/**
	 * This method is called to do damage to an enemy. It creates an instance of {@link Gun}
	 * and sets damage to {@link #GUN_DAMAGE}.
	 */
	public Gun(){
		super(GUN_DAMAGE);
	}
	
	/**
	 * This is called when the player picks up the bullet upgrade. It will add 1 to 
	 * {@link #numRounds}.
	 * @param numRoundsAdded Number of rounds to add to the gun.
	 */
	void addBullet(int numRoundsAdded){
		this.numRounds += numRoundsAdded;
	}
	
	public void shoot(DIRECTION shootDirection, Spy spy, ArrayList<Ninja> ninjas, ArrayList<Room> rooms) {
		switch (shootDirection)
		{
		case UP:
			shoot(0, -1, spy, ninjas, rooms);
			break;
		case RIGHT:
			shoot(1, 0, spy, ninjas, rooms);
			break;
		case DOWN:
			shoot(0, 1, spy, ninjas, rooms);
			break;
		case LEFT:
			shoot(-1, 0, spy, ninjas, rooms);
			break;
		default:
			System.err.println("Invalid shoot option");	
		}
	}
	
	public void setState(){
		hit = false;
		collision = false;
	}
	
	private void shoot(int dX, int dY, Spy spy, ArrayList<Ninja> ninjas, ArrayList<Room> rooms)
	{
		int bX = spy.getX();
		int bY = spy.getY();
		while(!collision) {
			bX += dX;
			bY += dY;
			System.out.println(bX + ", " + bY);
			if(!(grid.inRange(bX, bY))){
				collision = true;
				hit = false;
			}
			else{
				for(int i=0; i<ninjas.size(); i++){
					if((bX == ninjas.get(i).getX()) && (bY == ninjas.get(i).getY())){
						System.out.println("ninja spot" + bX + ", " + bY);
						collision = true;
						hit = true;
						ninjas.get(i).takeDamage(GUN_DAMAGE);
					}
				}
				if(!collision){
					for(int j = 0; j<rooms.size(); j++){
						if((bX == rooms.get(j).getX()) && (bY == rooms.get(j).getY())){
							System.out.println("room " + bX + ", " + bY);
							collision = true;
							hit = false;
						}
					}
				}
			}
		}
		}
	
}
