package edu.cpp.cs.cs141.prog_assgmnt_Final;

public class Bullet implements Item{
	
	int bullets;
	
	public void use(Spy player){
		player.getGun().addBullet(bullets);
	}

}
