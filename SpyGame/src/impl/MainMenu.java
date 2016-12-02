package impl;

import jgraphic.DisplayManager;
import jgraphic.Image;
import jgraphic.TextField;

public class MainMenu {
	private static final float BUTTON_X = (float) (DisplayManager.DISPLAY_DEFAULT_W - 420);
	private static final float BUTTON_W = 400;
	private static final float BUTTON_H = 70;
	private static final float BUTTON_Y_OFF = 20;
	private static final float BUTTON_Y_START = (float) (DisplayManager.DISPLAY_DEFAULT_H - (BUTTON_H + BUTTON_Y_OFF) * 4);
	private GameCore core;
	private TextField loadFileField;
	private Image titleImg;
	private Image newGameButtonImg;
	private Image newGameButtonOverImg;
	private Image loadButtonImg;
	private Image loadButtonOverImg;
	private Image exitButtonImg;
	private Image exitButtonOverImg;
	private Image cancelButtonImg;
	private Image cancelButtonOverImg;
	private float titleX = 0;
	private float loadFieldY = 10;
	private boolean loadButtonPressed = false;
	private boolean showError = false;
	
	public MainMenu(GameCore core)
	{
		this.core = core;
		loadFileField = new TextField(core, (int)BUTTON_W - 20, (int)(BUTTON_H/2));
		titleImg = new Image(core.getDisplayManager());
		titleImg.init("./resources/Imgs/Menu/title.png");
		newGameButtonImg = new Image(core.getDisplayManager());
		newGameButtonImg.init("./resources/Imgs/Menu/newGameButton.png");
		newGameButtonOverImg = new Image(core.getDisplayManager());
		newGameButtonOverImg.init("./resources/Imgs/Menu/newGameButtonOver.png");
		loadButtonImg = new Image(core.getDisplayManager());
		loadButtonImg.init("./resources/Imgs/Menu/loadButton.png");
		loadButtonOverImg = new Image(core.getDisplayManager());
		loadButtonOverImg.init("./resources/Imgs/Menu/loadButtonOver.png");
		exitButtonImg = new Image(core.getDisplayManager());
		exitButtonImg.init("./resources/Imgs/Menu/exitButton.png");
		exitButtonOverImg = new Image(core.getDisplayManager());
		exitButtonOverImg.init("./resources/Imgs/Menu/exitButtonOver.png");
		cancelButtonImg = new Image(core.getDisplayManager());
		cancelButtonImg.init("./resources/Imgs/Menu/cancelButton.png");
		cancelButtonOverImg = new Image(core.getDisplayManager());
		cancelButtonOverImg.init("./resources/Imgs/Menu/cancelButtonOver.png");
	}
	
	public boolean inTextField()
	{
		return loadFileField.isSelected();
	}
	
	public boolean checkButtons()
	{
		 titleImg.draw(titleX, DisplayManager.DISPLAY_DEFAULT_H - 200, 800, 150);
		 float buttonY = BUTTON_Y_START;
		 boolean buttonPressed = false;
		 if (checkButton(newGameButtonImg, newGameButtonOverImg, BUTTON_X, buttonY, BUTTON_W, BUTTON_H))
		 {
			 buttonPressed = true;
		 }
		 buttonY += BUTTON_H + BUTTON_Y_OFF;
		 loadFileField.draw(BUTTON_X + 10, buttonY + loadFieldY);
		 if (loadButtonPressed)
		 {
			 if (loadFieldY < BUTTON_H)
			 {
				 loadFieldY += core.rate;
				 loadFileField.reset();
			 }
			 if (checkButton(loadButtonImg, loadButtonOverImg, BUTTON_X, buttonY, BUTTON_W/2, BUTTON_H, BUTTON_X, BUTTON_W))
			 {
				if (core.load(loadFileField.getMessage()))
				{
					buttonPressed = true;
				}
				else
				{
					showError = true;
				}
			 }
			 if (checkButton(cancelButtonImg, cancelButtonOverImg, (float)(BUTTON_X + BUTTON_W * 1.5f - BUTTON_W * (loadFieldY/BUTTON_H)), buttonY, BUTTON_W/2, BUTTON_H, BUTTON_X + BUTTON_W - BUTTON_W * (loadFieldY/BUTTON_H), BUTTON_W))
			 {
				 showError = false;
				 loadButtonPressed = !loadButtonPressed;
			 }
			 if (showError)
			 {
				 core.getTextRenderer().drawCenteredText("UNABLE TO LOAD", BUTTON_X + BUTTON_W/2, buttonY + BUTTON_H + loadFieldY, 20, 1, 0, 0, 1);
			 }
		 }
		 else
		 {
			 if (loadFieldY > 10)
			 {
				 loadFieldY -= core.rate;
			 }
			 if (checkButton(loadButtonImg, loadButtonOverImg, BUTTON_X, buttonY, BUTTON_W, BUTTON_H))
			 {
				 loadButtonPressed = !loadButtonPressed;
			 }
			 loadFileField.reset();
		 }
		 buttonY += loadFieldY;
		 buttonY += BUTTON_H + BUTTON_Y_OFF;
		 if (checkButton(exitButtonImg, exitButtonOverImg, BUTTON_X, buttonY, BUTTON_W, BUTTON_H))
		 {
			 System.exit(0);
		 }
		 if (buttonPressed)
		 {
			 loadFileField.reset();
			 loadFieldY = 10;
			 loadButtonPressed = false;
			 showError = false;
			 buttonY = 10;
		 }
		 return buttonPressed;
	}
	
	private boolean checkButton(Image buttonImg, Image buttonOverImg, float x, float y, float w, float h)
	{
		if (core.getButtonManager().overRect(x, y, w, h))
		{
			buttonOverImg.draw(x, y, w, h);
			if (core.getInputManager().isMouseClicked())
			{
				return true;
			}
		}
		else
		{
			buttonImg.draw(x, y, w, h);
		}
		return false;
	}
	
	private boolean checkButton(Image buttonImg, Image buttonOverImg, float x, float y, float w, float h, float imgX, float imgW)
	{
		if (core.getButtonManager().overRect(x, y, w, h))
		{
			buttonOverImg.draw(imgX, y, imgW, h);
			if (core.getInputManager().isMouseClicked())
			{
				return true;
			}
		}
		else
		{
			buttonImg.draw(imgX, y, imgW, h);
		}
		return false;
	}
}
