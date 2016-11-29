package impl;

import edu.cpp.cs.cs141.final_proj.GameEngine;

public class ServerRoom extends Room {
	private static GameImage ServerRoomFloorImg;
	private static GameImage ServerRoomSpinnerImg;
	private static final float SERVER_SPIN_W = 177;
	private static final float SERVER_SPIN_H = 77;

	public ServerRoom(GameCore core, float x, float y) {
		super(core, x, y);
		if (ServerRoomFloorImg == null)
		{
			ServerRoomFloorImg = new GameImage(core);
			ServerRoomFloorImg.init("./resources/Imgs/Structure/serverRoom.png");
			ServerRoomSpinnerImg = new GameImage(core);
			ServerRoomSpinnerImg.init("./resources/Imgs/Structure/serverRoomSpinner.png");
			ServerRoomSpinnerImg.setRotateOriginScale(.5d, .5d);
		}
	}

	public void draw()
	{
		if (visible || GameEngine.DebugMode)
		{
			ServerRoomFloorImg.draw(x, y, ROOM_W, ROOM_W);
			ServerRoomSpinnerImg.draw(x + ROOM_W/2 - SERVER_SPIN_W/2, y + ROOM_W/2 - SERVER_SPIN_H/2 - 25, SERVER_SPIN_W, SERVER_SPIN_H);
			for (int i = 0; i < walls.length; i++)
			{
				if (walls[i] != null)
				{
					walls[i].draw();
				}
			}
		}
	}
}
