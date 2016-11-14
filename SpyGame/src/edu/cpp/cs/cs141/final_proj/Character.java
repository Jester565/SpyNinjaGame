package edu.cpp.cs.cs141.final_proj;

public abstract class Character extends GameObject{
	private int health;
	
	public Character(String gridRepresentation, int initialHealth)
	{
		super(gridRepresentation);
		this.health = initialHealth;
	}
	
	/**
	 * Get stabbed or shot, lose 1 life point
	 */
	public void takeDamage(int dmg) {
		health -= dmg;
	}
	
	/**
	 * Checks the life status of a character
	 * @return True if lifePts <= 0, false otherwise
	 */
	public boolean isAlive() {
		
		if (health <= 0)
			return false;
		else
			return true;
	}
}
