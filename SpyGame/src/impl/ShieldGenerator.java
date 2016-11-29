package impl;

import jgraphic.Image;

public class ShieldGenerator extends Item{
	private static GameImage ShieldGeneratorImg;
	private static Image ShieldBlueprintImg;

	public ShieldGenerator(GameCore core)
	{
		super(core);
		if (ShieldGeneratorImg == null)
		{
			ShieldGeneratorImg = new GameImage(core);
			ShieldBlueprintImg = new Image(core.getDisplayManager());
			ShieldGeneratorImg.init("./resources/Imgs/Structure/shieldGenerator.png");
			ShieldBlueprintImg.init("./resources/Imgs/Structure/shieldGeneratorBlueprint.png");
		}
		itemImg = ShieldGeneratorImg;
		blueprintImg = ShieldBlueprintImg;
	}

	@Override
	public boolean isExpired() {
		return (!core.getGameEngine().getSpy().isInvincible());
	}

	@Override
	public void appyEffect() {
		core.getPlayer().setDrawShield(true);
	}
	
	@Override
	public void draw(float x, float y, float w, float h)
	{
		itemImg.draw(x, y, w, h);
	}
	
	@Override
	public void drawBlueprint(float x, float y, float w, float h)
	{
		blueprintImg.draw(x, y, w, h);
		float batteryH = ((float)core.getGameEngine().getSpy().getInvincibleTurns() / ((float)edu.cpp.cs.cs141.final_proj.Invincibility.INVINCIBLE_TURNS - 1)) * 50.0f;
		core.getShapeRenderer().drawRect(x + w - 32, y + 8 + (50 - batteryH), 26, batteryH, .8f, .8f, 1, 1);
	}
}
