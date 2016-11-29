package impl;

import jgraphic.Image;

public class Bullet extends Item {
	private static GameImage BulletImg;
	private static Image BulletBlueprintImg;
	
	public Bullet(GameCore core)
	{
		super(core);
		if (BulletImg == null)
		{
			BulletImg = new GameImage(core);
			BulletBlueprintImg = new Image(core.getDisplayManager());
			BulletImg.init("./resources/Imgs/Structure/bullet.png");
			BulletBlueprintImg.init("./resources/Imgs/Structure/bulletBlueprint.png");
		}
		itemImg = BulletImg;
		blueprintImg = BulletBlueprintImg;
	}

	@Override
	public boolean isExpired() {
		return (core.getGameEngine().getSpy().getGun().getNumRounds() <= 0);
	}

	@Override
	public void appyEffect() {
		
	}
	
}
