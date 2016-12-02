package impl;

import jgraphic.Image;

public class RadarDish extends Item {
	private GameImage RadarImg;
	private Image RadarBlueprintImg;
	
	public RadarDish(GameCore core) {
		super(core);
		if (RadarImg == null)
		{
			RadarImg = new GameImage(core);
			RadarBlueprintImg = new Image(core.getDisplayManager());
			RadarImg.init("./resources/Imgs/Structure/radar.png");
			RadarBlueprintImg.init("./resources/Imgs/Structure/radarBlueprint.png");
		}
		itemImg = RadarImg;
		blueprintImg = RadarBlueprintImg;
	}

	@Override
	public boolean isExpired() {
		return false;
	}

	@Override
	public void appyEffect(boolean displayNotification) {
		if (displayNotification)
		{
			core.getNotificationManager().addNotification("You now have radar. Press R to activate/deactive it.");
		}
	}
	
	@Override
	public String getDescription()
	{
		return "Radar: Points to the functional server room";
	}
}
