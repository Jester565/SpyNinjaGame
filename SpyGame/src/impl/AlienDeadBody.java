package impl;

public class AlienDeadBody implements RoomOccupant {
	private static GameImage AlienDeadImg;
	
	private static final float ALIEN_IMG_W = 500;
	
	private float x;
	private float y;
	private Room room;
	
	public AlienDeadBody(GameCore core, Room room)
	{
		this.room = room;
		if (AlienDeadImg == null)
		{
			AlienDeadImg = new GameImage(core);
			AlienDeadImg.init("./resources/Imgs/Alien/alienDead.png");
		}
	}

	@Override
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public void draw() {
		AlienDeadImg.draw(x - ALIEN_IMG_W/2, y - ALIEN_IMG_W/2, ALIEN_IMG_W, ALIEN_IMG_W);
	}

	@Override
	public Room getRoomOccupied() {
		return room;
	}

	@Override
	public void drawBlocker() {
		
	}

}
