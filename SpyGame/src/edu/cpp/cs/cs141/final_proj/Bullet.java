package edu.cpp.cs.cs141.final_proj;

public class Bullet implements Item{
	
	int bullets;
	
	public void use(Spy player){
		player.getGun().addBullet(bullets);
	}

}
