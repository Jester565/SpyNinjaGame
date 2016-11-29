package impl;

public interface RoomOccupant {
	public void setXY(float x, float y);
	
	public void draw();
	public void drawBlocker();
	public Room getRoomOccupied();
}
