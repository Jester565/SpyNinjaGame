package edu.cpp.cs.cs141.SpyGame;

import java.util.Random;

public class GameEngine {

	private Spy player;
	private Grid grid;
	private int numOfNinja;
	private int numOfRoom = 9;
	private Room[] roomList;
	private Ninja[] ninjaList;
	private Item[] items;
	
	private int randGen(int min,int max)
	{
		Random rng = new Random();
		return rng.nextInt( max- min + 1) + min;
	}
	
	public void newGame(int numOfNinja)
	{
		grid = new Grid();
		this.numOfNinja = numOfNinja;
		player = new Spy();
		grid.clear();
		//grid.getObject(grid.GRID_SIZE-2,0).setVisible(true);
		//grid.getObject(grid.GRID_SIZE-1,1).setVisible(true);
		resetSpy();
		setRoom();
		setItem();
		setNinja();
	}
	
	public void resetSpy() {
		player.setLocation(grid.GRID_SIZE - 1, 0);
		player.changeDirection('w');
		grid.setObject(player);
	}
	
	public void setRoom() {
		roomList = new Room[numOfRoom];
		for( int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 3; j++)
			{
				grid.setObject( roomList[3 * i + j] =new Room(1 + i*3,1 + j*3));		
			}	
		}
		roomList[randGen(0,2)].setBriefCase();
	}
	
	public void setItem() {
		items=new Item[3];
		items[0] = new Bullet();
		items[1] = new Radar();
		items[2] = new Invincibility();
		for(int i = 0; i < 3; i++)
		{
			int x,y;
			do{
				x= randGen(0, grid.GRID_SIZE - 1);
				y= randGen(0, grid.GRID_SIZE - 1);
			   
			} while (!(grid.getObject(x, y) instanceof EmptyPlace)); 
			items[i].setLocation(x,y); 
			grid.setObject(items[i]);
		}
	}
	
	public void setNinja() {
		ninjaList = new Ninja[numOfNinja];
		for(int i = 0;i < numOfNinja; i++)
		{
			int x,y;
			do{
			  do{
				  x=randGen(0, grid.GRID_SIZE -1);
				  y=randGen(0, grid.GRID_SIZE -1);
			  } while( grid.canSetNinja(x, y) );//Math.abs(player.getX() - x) + Math.abs(player.getY() - y) <= 2);
			  y = grid.GRID_SIZE - 1 - y;
			} while (grid.getObject(x, y) instanceof Room);
			
			grid.setObject(ninjaList[i] = new Ninja(x,y));	
		}
	}
	
	private GameObject getObjAhead(GameObject thisObj,char direction)
	{
		if (thisObj == null) {
			return null;
		}
		
		int thisX = thisObj.getX()
		   ,thisY = thisObj.getY();
		int aheadX = -1,
			aheadY = -1;
		switch(direction)
			{
				case 'w':
					aheadX = thisX - 1;
					aheadY = thisY;
					break;
				case 's':
					aheadX = thisX + 1;
					aheadY = thisY;
					break;
				case 'a':
					aheadX = thisX;
					aheadY = thisY - 1;
					break;
				case 'd':
					aheadX = thisX;
					aheadY = thisY + 1;
					break;
			}
		if(aheadX >= 0 && aheadX < grid.GRID_SIZE && aheadY >= 0 && aheadY < grid.GRID_SIZE) {
			return grid.getObject(aheadX, aheadY);
		} else {
			return null;
		}
	}
	
	private void visionControl(boolean control)
	{
		GameObject obj1 = getObjAhead(player, player.getDirection()),
					 obj2 = obj1==null?null:getObjAhead(obj1,player.getDirection());
		if(control==true)
		{
			if(obj1!=null)
				obj1.setVisible(true);
			if(obj2!=null)
				obj2.setVisible(true);
		}
		else
		{
			if(obj1!=null)
				obj1.setVisible(false);
			if(obj2!=null)
				obj2.setVisible(false);
		}

	}
	
	/**
	 * Player move 
	 * @param direction
	 * @return
	 */
	public String playerTurn(char direction)
	{
		GameObject obj = null;
		grid.setAllUnvisible();
		if (getObjAhead(player,direction) != null & getObjAhead(getObjAhead(player,direction), direction) != null)
			getObjAhead(getObjAhead(player,direction), direction).setVisible(true);
			obj = getObjAhead(getObjAhead(player,direction), direction);

		if (getObjAhead(obj, direction) != null) {
			getObjAhead(obj, direction).setVisible(true);
		}
		if (getObjAhead(player,direction) instanceof Room) {
			grid.setAllUnvisible();
		}
		boolean isMove=false;
		String reaction = "";
		if (getObjAhead(player,direction) instanceof Room) {
			reaction = "Illegal move";
		} else {
			reaction = "no thing special happens";
		}
		GameObject objAhead=getObjAhead(player,direction);
		if(player.getInvinc() > 0) 
			player.weakenInvinc();
		
		if(objAhead!=null)
		{
			if(objAhead instanceof EmptyPlace)
			{
			isMove=true;
			}
			else if( objAhead instanceof Bullet)
			{
			isMove=true;
			if(player.hasBullet()) {
				reaction = "you alrealy have a bullet";
			} else {
				reaction="you get a bullet";
				player.getAdditionalBullet();
			}
			((Bullet) objAhead).beenUsed();
			}
			else if(objAhead instanceof Radar)
			{
				isMove=true;
				reaction="you get a radar";
				((Radar) objAhead).beenUsed();
				for(int i=0;i<9;i++)
				{
					roomList[i].setDebug();
				}
			}
			else if(objAhead instanceof Invincibility)
			{
				isMove=true;
				reaction="you get invincibility";
				player.setInvinc();
				((Invincibility) objAhead).beenUsed();
			}
			else if(objAhead instanceof Room && direction =='s')
			{
				if( ((Room)objAhead).hasBriefCase())
				{
					player.getBriefCase();
					reaction="getCase";
				}
				else
				{
					reaction="noCase";
				}
			}
		}
		
		if(isMove)
		{
			grid.moveObject(player, objAhead.getX(), objAhead.getY());
			player.changeDirection(direction);
			if(player.isInvinc())
				player.weakenInvinc();
			
		}
		return reaction;
	}
	
	///////////////////////////
	public boolean ninjaTurn()
	{
		boolean isStab=false;
		for(int i = 0;i < numOfNinja; i++)
		{
			Ninja thisN = ninjaList[i];
			//not sure ninja check spy and stab him
			if (player.getX() == ninjaList[i].getX()){
				if (Math.abs(player.getY() - ninjaList[i].getY()) <= 1)
					if(!player.isInvinc())
					{
						player.getStabbed();
						isStab=true;
					}
			}
			if (player.getY() == ninjaList[i].getY()){
				if (Math.abs(player.getX() - ninjaList[i].getX()) <= 1)
					if(!player.isInvinc())
					{
						player.getStabbed();
						isStab=true;
					}
			}
					
			boolean moved = false;
			if( thisN.isAlive())
			{
				if(thisN.getX()==player.getX()||thisN.getY()==player.getY())
				{
					if(Math.abs(thisN.getX()-player.getX())==1||Math.abs(thisN.getY()-player.getY())==1)
					{
						moved=true;
						if(!player.isInvinc())
						{
							player.getStabbed();
							isStab=true;
						}	
					}
				//I
				}
				
				char direction='w';
				while(moved==false)
				{
					int rand=randGen(1,4);
					
					switch(rand)
					{
					case 1:
						direction='w';
						break;
					case 2:
						direction='a';
						break;
					case 3:
						direction='s';
						break;
					case 4:
						direction='d';
						break;
					}
					GameObject objAhead=getObjAhead(thisN,direction);
					if(objAhead instanceof Room || objAhead==null || objAhead instanceof Ninja)
					{
						moved=false;
						/// not sure
							if (player.getX() == ninjaList[i].getX()){
								if (Math.abs(player.getY() - ninjaList[i].getY()) <= 1)
									isStab = true;
							}
							if (player.getY() == ninjaList[i].getY()){
								if (Math.abs(player.getX() - ninjaList[i].getX()) <= 1)
								isStab = true;
							}
						
					} 
					else {
						moved=true;
						//not sure
						if (player.getX() == ninjaList[i].getX()){
							if (Math.abs(player.getY() - ninjaList[i].getY()) <= 1)
								if(!player.isInvinc())
								{
									player.getStabbed();
									isStab=true;
								}
						}
						if (player.getY() == ninjaList[i].getY()){
							if (Math.abs(player.getX() - ninjaList[i].getX()) <= 1)
								if(!player.isInvinc())
								{
									player.getStabbed();
									isStab=true;
								}
						}
						grid.moveObject(thisN, objAhead.getX(), objAhead.getY());
					}
				}
			}
			putBackItem();
		}
		return isStab;
	}
	
	private void putBackItem()
	{
		for(int i=0;i<3;i++)
		{
			if(!items[i].isUsed())
			{
				if(grid.getObject(items[i].getX(), items[i].getY()) instanceof EmptyPlace)
					grid.setObject(items[i]);
			}
		}
	}
	
	public boolean look(char direction)
	{
		boolean hasNinja=false;
		GameObject nextObj = getObjAhead(player, direction);
		while(nextObj!=null)
		{
			if(nextObj instanceof Ninja)
				hasNinja=true;
			nextObj=getObjAhead(nextObj,direction);
		}
		return hasNinja;
	}
	
	public String shoot(char direction)
	{
		String reaction;
		if(!player.hasBullet())
			reaction="no more bullet";
		else
		{
			player.loseBullet();
			GameObject nextObj = getObjAhead(player,direction);
			while(nextObj!=null &&!(nextObj instanceof Ninja))
			{
				nextObj=getObjAhead(nextObj,direction);
			}
			if(nextObj instanceof Ninja)
			{
				reaction="kill";
				((Ninja) nextObj).getShoot();
				grid.setObject(new EmptyPlace(nextObj.getX(),nextObj.getY()));
				putBackItem();
			}
			else
				reaction="kill nobody and waste bullet";
		}
		return reaction;
	}
	
	public int invinc()
	{
		return player.getInvinc();
	}
	
	public boolean gameFinished() {
		boolean gameFinished = false;
		if (player.hasBriefCase() || !player.hasLives()) gameFinished = true;
		return gameFinished;
	}
	
	public boolean gameWin() {
		boolean gameWin = false;
		if (player.hasBriefCase()) 
			gameWin = true;
		return gameWin;
	}
	
	public Spy getPlayer() {
		return player;
	}
	
	public void gameLoad(String fileName) {
		
	}
	
	public void saveGame(String fileName) {
		
	}
	
	public String toString(boolean isDebug)
	{
		visionControl(true);
		String printGrid = grid.toString(isDebug);
		for(int i=0;i<9;i++)
		{
			roomList[i].setDebug();
		}
		visionControl(false);
		return printGrid;
	}
}

