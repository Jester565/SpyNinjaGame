package impl;


public class TerminalSolidWall extends SolidWall {
	private static GameImage TerminalWallImg;
	
	private static final int TERMINAL_OFF = 140;
	
	private GameCore core;

	public TerminalSolidWall(GameCore core, float x, float y, float w, float h, boolean vertical) {
		super(core, x, y, w, h, vertical);
		this.core = core;
		if (TerminalWallImg == null)
		{
			TerminalWallImg = new GameImage(core);
			TerminalWallImg.init("./resources/Imgs/Structure/roomWallHorz.png");
		}
		wallImg = TerminalWallImg;
	}
	
	public boolean checkTerminalUsed()
	{
		if (core.getInputManager().isKeyTyped('e'))
		{
			float playerX = core.getPlayer().getX()  + core.getPlayer().getW()/2;
			float playerY = core.getPlayer().getY()  + core.getPlayer().getH()/2;
			float wallX = getX();
			float wallW = getW();
			float wallY = getY();
			float wallH = getH();
			wallY -= TERMINAL_OFF;
			wallH += TERMINAL_OFF * 2;
			if (playerX > wallX && playerX < wallX + wallW && playerY > wallY && playerY < wallY + wallH)
			{
				if (GameCore.IsFacing(core.getPlayer(), getX(), getY(), getX() + getW(), getY() + getH()))
				{
					return true;
				}
			}
		}
		return false;
	}
}
