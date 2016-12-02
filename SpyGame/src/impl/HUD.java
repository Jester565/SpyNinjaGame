package impl;

import java.util.ArrayList;
import java.util.Iterator;

import edu.cpp.cs.cs141.final_proj.GameEngine;
import edu.cpp.cs.cs141.final_proj.GameEngine.GAME_DIFFICULTY;
import edu.cpp.cs.cs141.final_proj.GameEngine.GAME_STATE;
import impl.DoorWall.BUTTON_RESULT;
import jgraphic.DisplayManager;
import jgraphic.Image;
import jgraphic.Sound;
import jgraphic.TextField;

public class HUD {
	private static Image InstructionImg;
	private static Image MoveInstructionImg;
	private static Image LightInstructionImg;
	private static Image RoomInstructionImg;
	private static Image ShootInstructionImg;
	private static Image ShootInstructionImg2;
	private static Image TerminalInstructionImg;
	
	private static final float INSTRUCTION_BOX_W = 405;
	private static final float INSTRUCTION_BOX_H = 500;
	private static final float INSTRUCTION_BOX_X = 0;
	private static final float INSTRUCTION_BOX_Y = (float) (DisplayManager.DISPLAY_DEFAULT_H/2 - INSTRUCTION_BOX_H/2);
	
	private static int MAX_ITEMS = 3;
	private static final float ITEM_BOX_W = 360;
	private static final float ITEM_BOX_H = 400;
	private static final float ITEM_OFF_W = 10;
	private static final float ITEM_OFF_H = 30;
	private static final float ITEM_BOX_X = (float) (DisplayManager.DISPLAY_DEFAULT_W - 405 + (405 - ITEM_BOX_W)/2);
	private static final float ITEM_BOX_Y = (float) (DisplayManager.DISPLAY_DEFAULT_H - ITEM_BOX_H - ITEM_OFF_H);
	private static final float ITEM_BOX_OUTLINE_W = 10;
	private static final float ITEM_W = ITEM_BOX_W - ITEM_OFF_W * 2;
	private static final float ITEM_H = ((float)(ITEM_BOX_H - ((MAX_ITEMS + 1) * ITEM_OFF_H)))/(float)MAX_ITEMS;
	
	private static final float LIVE_BOX_W = ITEM_BOX_W;
	private static final float LIVE_BOX_H = 100;
	private static final float LIVE_BOX_X = ITEM_BOX_X;
	private static final float LIVE_BOX_Y = 200;
	
	private static Image TerminalImg1;
	private static Image TerminalImg2;
	private static Image TerminalErrorImg;
	private static Image TerminalConnectedImg;
	private static Image TerminalDeathImg;
	
	private static Sound TerminalDeathSound;
	
	private static final float MENU_W = 800;
	private static final float MENU_H = 1000;
	private static final float MENU_X = (float) (DisplayManager.DISPLAY_DEFAULT_W/2 - MENU_W/2);
	private static final float MENU_Y = (float) (DisplayManager.DISPLAY_DEFAULT_H/2 - MENU_H/2);
	private static final float MENU_BUTTON_W = 600;
	private static final float MENU_BUTTON_H = 100;
	private static final float MENU_BUTTON_Y_OFF = 30;
	private static final float MENU_BUTTON_X = (float) (DisplayManager.DISPLAY_DEFAULT_W/2 - MENU_BUTTON_W/2);
	private static final float MENU_BUTTON_START_Y = MENU_Y + MENU_BUTTON_Y_OFF;
	private static final float TERMINAL_W = 800;
	private static final float TERMINAL_H = 600;
	private static final float TERMINAL_X = 560;
	private static final float TERMINAL_Y = 240;
	private static final float SHOW_STATUS_TIME = 160;
	private static final float MAX_TERMINAL_LOAD_BAR_W = 200;
	
	private static final float RADAR_X = (float) (DisplayManager.DISPLAY_DEFAULT_W/2);
	private static final float RADAR_Y = (float) (DisplayManager.DISPLAY_DEFAULT_H/2);
	private static final float RADAR_RADIUS = 400;
	
	private static final float STATUS_TIME = 300;
	
	private static int InstructionStage = 0;
	
	private boolean inTerminal = false;
	private float terminalLoadBarW = 0;
	private float terminalStatusTimer = 0;
	
	private boolean radarEnabled = false;
	private float scanY = 0;
	private float scanVelocityY = 1f;
	private GameCore core;
	private boolean saveButtonPressed = false;
	private boolean difficultyButtonPressed = false;
	private ArrayList<Item> items;
	private TextField saveFileField;
	private boolean drawMenu = false;
	private boolean saveSuccess = false;
	private boolean drawSaveErrorMsg = false;
	private float statusAlpha = 0;
	private boolean drawStatus = false;
	private float statusTimer = 0;
	private String powerUpDescription = null;
	
	public HUD(GameCore core)
	{
		this.core = core;
		if (TerminalImg1 == null)
		{
			InstructionImg = new Image(core.getDisplayManager());
			InstructionImg.init("./resources/Imgs/Menu/insturctions.png");
			MoveInstructionImg = new Image(core.getDisplayManager());
			MoveInstructionImg.init("./resources/Imgs/Menu/moveInsturctions.png");
			LightInstructionImg = new Image(core.getDisplayManager());
			LightInstructionImg.init("./resources/Imgs/Menu/lightInsturctions.png");
			RoomInstructionImg = new Image(core.getDisplayManager());
			RoomInstructionImg.init("./resources/Imgs/Menu/roomInstructions.png");
			ShootInstructionImg = new Image(core.getDisplayManager());
			ShootInstructionImg.init("./resources/Imgs/Menu/shootInstructions.png");
			ShootInstructionImg2 = new Image(core.getDisplayManager());
			ShootInstructionImg2.init("./resources/Imgs/Menu/shootInstructions2.png");
			TerminalInstructionImg = new Image(core.getDisplayManager());
			TerminalInstructionImg.init("./resources/Imgs/Menu/terminalInstructions.png");
			TerminalImg1 = new Image(core.getDisplayManager());
			TerminalImg2 = new Image(core.getDisplayManager());
			TerminalErrorImg = new Image(core.getDisplayManager());
			TerminalConnectedImg = new Image(core.getDisplayManager());
			TerminalDeathImg = new Image(core.getDisplayManager());
			TerminalImg1.init("./resources/Imgs/Structure/terminalScreen.png");
			TerminalImg2.init("./resources/Imgs/Structure/terminalScreen2.png");
			TerminalErrorImg.init("./resources/Imgs/Structure/terminalScreenError.png");
			TerminalConnectedImg.init("./resources/Imgs/Structure/terminalScreenWin.png");
			TerminalDeathImg.init("./resources/Imgs/Structure/terminalDeath.png");
			TerminalDeathSound = new Sound("./resources/Sounds/terminalDeath.wav");
		}
		saveFileField = new TextField(core, (int)MENU_BUTTON_W, (int)(MENU_BUTTON_H/2));
		reset();
	}
	
	public void setPowerUpDescription(String description)
	{
		powerUpDescription = description;
	}
	
	public void enableDrawStatus()
	{
		drawStatus = true;
	}
	
	public boolean inMenu()
	{
		return drawMenu;
	}
	
	public boolean inTextField()
	{
		return saveFileField.isSelected();
	}
	
	public void reset()
	{
		drawSaveErrorMsg = false;
		drawMenu = false;
		items = new ArrayList<Item>();
		if (core.getGameEngine().getSpy().getGun().getNumRounds() > 0)
		{
			Item item = new Bullet(core);
			item.appyEffect(false);
			items.add(item);
		}
		if (core.getGameEngine().getSpy().getInvincibleTurns() > 0)
		{
			Item item = new ShieldGenerator(core);
			item.appyEffect(false);
			items.add(item);
		}
		if (core.getGameEngine().getSpy().hasRadar())
		{
			Item item = new RadarDish(core);
			item.appyEffect(false);
			items.add(item);
		}
	}
	
	public void addItem(Item item)
	{
		item.appyEffect(true);
		items.add(item);
	}
	
	private boolean inAnimation()
	{
		return !(core.getGameEngine().getGameStatus() == GAME_STATE.UNFINISHED && core.getGameEngine().getSpy().isAlive());
	}
	
	public void draw()
	{
		if (core.getInputManager().isKeyTyped('m') && !saveFileField.isSelected())
		{
			drawMenu = !drawMenu;
			if (!drawMenu && !inAnimation())
			{
				core.setPlayerMovement(true);
			}
		}
		if (!drawMenu)
		{
			drawSaveErrorMsg = false;
			saveFileField.reset();
		}
		scanY += scanVelocityY;
		if (scanY < 0)
		{
			scanVelocityY *= -1;
			scanY = 0;
		}
		if (scanY > ITEM_BOX_H)
		{
			scanVelocityY *= -1;
			scanY = ITEM_BOX_H;
		}
		
		core.getShapeRenderer().drawRect(0, 0, 420, DisplayManager.DISPLAY_DEFAULT_H, .2f, .3f, .3f, 1);
		core.getShapeRenderer().drawRect(405, 0, 15, DisplayManager.DISPLAY_DEFAULT_H, 0f, .5f, .5f, 1);
		core.getShapeRenderer().drawRect(DisplayManager.DISPLAY_DEFAULT_W - 420, 0, 420, DisplayManager.DISPLAY_DEFAULT_H, .2f, .3f, .3f, 1);
		core.getShapeRenderer().drawRect(DisplayManager.DISPLAY_DEFAULT_W - 420, 0, 15, DisplayManager.DISPLAY_DEFAULT_H, 0f, .5f, .5f, 1);
		core.getShapeRenderer().drawRect(ITEM_BOX_X - ITEM_BOX_OUTLINE_W, ITEM_BOX_Y - ITEM_BOX_OUTLINE_W, ITEM_BOX_W + ITEM_BOX_OUTLINE_W * 2, ITEM_BOX_H + ITEM_BOX_OUTLINE_W * 2, .7f, .7f, .7f, 1);
		core.getShapeRenderer().drawRect(ITEM_BOX_X, ITEM_BOX_Y, ITEM_BOX_W, ITEM_BOX_H, .3f, .6f, .3f, 1);
		
		core.getShapeRenderer().drawRect(LIVE_BOX_X - ITEM_BOX_OUTLINE_W, LIVE_BOX_Y - ITEM_BOX_OUTLINE_W, LIVE_BOX_W + ITEM_BOX_OUTLINE_W * 2, LIVE_BOX_H + ITEM_BOX_OUTLINE_W * 2, .7f, .7f, .7f, 1);
		core.getShapeRenderer().drawRect(LIVE_BOX_X, LIVE_BOX_Y, LIVE_BOX_W, LIVE_BOX_H, .3f, .6f, .3f, 1);
		core.getTextRenderer().drawCenteredText("LIVES: " + core.getGameEngine().getSpy().getLives(), LIVE_BOX_X + LIVE_BOX_W/2, LIVE_BOX_Y + LIVE_BOX_H * .7f, 60, 0, 0, 1, 1);
		
		float y = ITEM_BOX_Y + ITEM_OFF_H; 
		for (int i = 0; i < items.size(); i++)
		{
			core.getShapeRenderer().drawRect(ITEM_BOX_X + ITEM_OFF_W, y, ITEM_W, ITEM_H, .3f, .4f, .3f, .2f);
			items.get(i).drawBlueprint(ITEM_BOX_X + ITEM_OFF_W, y, ITEM_W, ITEM_H);
			if (items.get(i).isExpired())
			{
				items.remove(i);
				i--;
			}
			y += ITEM_H + ITEM_OFF_H;
		}
		core.getShapeRenderer().drawRect(ITEM_BOX_X, ITEM_BOX_Y + scanY, ITEM_BOX_W, 5, 0, 0, 1, .02f);
		core.getShapeRenderer().drawRect(ITEM_BOX_X, ITEM_BOX_Y + ITEM_BOX_H - scanY, ITEM_BOX_W, 5, 0, 0, 1, .02f);
		
		DrawInstructions();
		
		if (inTerminal)
		{
			drawTerminal();
		}
		else
		{
			if (core.getGameEngine().getSpy().hasRadar() || GameEngine.DebugMode)
			{
				drawRadar();
			}
			else
			{
				radarEnabled = false;
			}
		}
		drawPowerUpDescription();
		if (drawStatus)
		{
			drawStatus();
		}
		if (drawMenu)
		{
			drawMenu();
		}
	}
	
	private void drawPowerUpDescription()
	{
		if (powerUpDescription != null)
		{
			float strWidth = core.getTextRenderer().getTextWidth(powerUpDescription, 30);
			core.getShapeRenderer().drawRect(DisplayManager.DISPLAY_DEFAULT_W/2 - (strWidth/2)/core.getDisplayManager().screenWScale, 10, strWidth/core.getDisplayManager().screenWScale, 40, 0, 0, 0, 1);
			core.getTextRenderer().drawCenteredText(powerUpDescription, DisplayManager.DISPLAY_DEFAULT_W/2, 40, 30, 1, 1, 1, 1);
			powerUpDescription = null;
		}
	}
	
	private void DrawInstructions()
	{
		if (InstructionStage == 0)
		{
			MoveInstructionImg.draw(INSTRUCTION_BOX_X, INSTRUCTION_BOX_Y, INSTRUCTION_BOX_W, INSTRUCTION_BOX_H);
			if (core.getInputManager().isKeyPressed('w'))
			{
				core.getShapeRenderer().drawRect(INSTRUCTION_BOX_X + 154, INSTRUCTION_BOX_Y + 92, 94, 90, 0, 1, 0, .6f);
			}
			if (core.getInputManager().isKeyPressed('a'))
			{
				core.getShapeRenderer().drawRect(INSTRUCTION_BOX_X + 49, INSTRUCTION_BOX_Y + 199, 97, 99, 0, 1, 0, .6f);
			}
			if (core.getInputManager().isKeyPressed('s'))
			{
				core.getShapeRenderer().drawRect(INSTRUCTION_BOX_X + 154, INSTRUCTION_BOX_Y + 199, 97, 97, 0, 1, 0, .6f);
			}
			if (core.getInputManager().isKeyPressed('d'))
			{
				core.getShapeRenderer().drawRect(INSTRUCTION_BOX_X + 255, INSTRUCTION_BOX_Y + 199, 97, 99, 0, 1, 0, .6f);
			}
			if (core.getRoomManager().getPlayerRoom().getButtonResult() != BUTTON_RESULT.TOO_FAR)
			{
				if (core.getRoomManager().getPlayerRoom().getButtonResult() == BUTTON_RESULT.PRESSED && core.getInputManager().isKeyTyped('e'))
				{
					InstructionStage++;
				}
				InstructionStage++;
			}
		}
		else if (InstructionStage == 1)
		{
			LightInstructionImg.draw(INSTRUCTION_BOX_X, INSTRUCTION_BOX_Y, INSTRUCTION_BOX_W, INSTRUCTION_BOX_H);
			BUTTON_RESULT buttonResult = core.getRoomManager().getPlayerRoom().getButtonResult();
			if (buttonResult != BUTTON_RESULT.TOO_FAR)
			{
				core.getShapeRenderer().drawRect(INSTRUCTION_BOX_X + 46, INSTRUCTION_BOX_Y + 120, 30, 30, 0, 1, 0, 1);
			}
			if (buttonResult == BUTTON_RESULT.PRESSED)
			{
				core.getShapeRenderer().drawRect(INSTRUCTION_BOX_X + 46, INSTRUCTION_BOX_Y + 193, 30, 30, 0, 1, 0, 1);
				if (core.getInputManager().isKeyTyped('e'))
				{
					InstructionStage++;
				}
			}
			if (core.getInputManager().isKeyTyped('e'))
			{
				core.getShapeRenderer().drawRect(INSTRUCTION_BOX_X + 46, INSTRUCTION_BOX_Y + 266, 30, 30, 0, 1, 0, 1);
			}
		}
		else if (InstructionStage == 2)
		{
			RoomInstructionImg.draw(INSTRUCTION_BOX_X, INSTRUCTION_BOX_Y, INSTRUCTION_BOX_W, INSTRUCTION_BOX_H);
			if (core.getInputManager().isKeyTyped('i'))
			{
				InstructionStage++;
			}
		}
		else if (InstructionStage == 3)
		{
			ShootInstructionImg.draw(INSTRUCTION_BOX_X, INSTRUCTION_BOX_Y, INSTRUCTION_BOX_W, INSTRUCTION_BOX_H);
			if (core.getInputManager().isKeyTyped('i'))
			{
				InstructionStage++;
			}
		}
		else if (InstructionStage == 4)
		{
			ShootInstructionImg2.draw(INSTRUCTION_BOX_X, INSTRUCTION_BOX_Y, INSTRUCTION_BOX_W, INSTRUCTION_BOX_H);
			if (core.getInputManager().isKeyTyped('i'))
			{
				InstructionStage++;
			}
		}
		else if (InstructionStage == 5)
		{
			TerminalInstructionImg.draw(INSTRUCTION_BOX_X, INSTRUCTION_BOX_Y, INSTRUCTION_BOX_W, INSTRUCTION_BOX_H);
			if (core.getInputManager().isKeyTyped('i'))
			{
				InstructionStage++;
			}
		}
		else
		{
			InstructionImg.draw(INSTRUCTION_BOX_X, INSTRUCTION_BOX_Y, INSTRUCTION_BOX_W, INSTRUCTION_BOX_H);
		}
	}
	
	public void checkItems()
	{
		for (int i = 0; i < items.size(); i++)
		{
			if (items.get(i).isExpired())
			{
				items.remove(i);
				i--;
			}
		}
	}
	
	public void initiateTerminal()
	{
		inTerminal = true;
		core.setPlayerMovement(false);
	}
	
	private void drawStatus()
	{
		if (statusAlpha < 1)
		{
			statusAlpha += core.rate * .05f;
			if (statusAlpha > 1)
			{
				statusAlpha = 1;
			}
		}
		statusTimer += core.rate;
		core.getShapeRenderer().drawRect(420, 0, 1080, 1080, 0, 0, 0, 1);
		String msg = "";
		if (core.getGameEngine().getGameStatus() == GAME_STATE.WON)
		{
			msg = "YOU WON!";
			if (statusTimer > STATUS_TIME)
			{
				statusTimer = 0;
				statusAlpha = 0;
				drawStatus = false;
				core.exitToMenu();
			}
		}
		else if (core.getGameEngine().getGameStatus() == GAME_STATE.LOST)
		{
			msg = "YOU LOST";
			if (statusTimer > STATUS_TIME)
			{
				statusTimer = 0;
				statusAlpha = 0;
				drawStatus = false;
				core.exitToMenu();
			}
		}
		else
		{
			msg = "YOU DIED";
			if (statusTimer > STATUS_TIME)
			{
				statusTimer = 0;
				statusAlpha = 0;
				drawStatus = false;
				terminalLoadBarW = 0;
				terminalStatusTimer = 0;
				inTerminal = false;
				core.setSpyToInitialState();
			}
		}
		core.getTextRenderer().drawCenteredText(msg, DisplayManager.DISPLAY_DEFAULT_W/2, DisplayManager.DISPLAY_DEFAULT_H/2, 60, 0, 1, 0, statusAlpha);
	}
	
	private void drawTerminal()
	{
		core.getShapeRenderer().drawRect(420, 0, 1080, 1080, 0, 0, 0, .97f);
		if (terminalLoadBarW < MAX_TERMINAL_LOAD_BAR_W)
		{
			terminalLoadBarW += (float)(Math.random() + 1) * core.rate;
			if (Math.random() < .5)
			{
				TerminalImg1.draw(TERMINAL_X, TERMINAL_Y, TERMINAL_W, TERMINAL_H);
			}
			else
			{
				TerminalImg2.draw(TERMINAL_X, TERMINAL_Y, TERMINAL_W, TERMINAL_H);
			}
			core.getShapeRenderer().drawRect(TERMINAL_X + TERMINAL_W * .1, TERMINAL_Y + TERMINAL_H * .62, TERMINAL_W * .78 * (terminalLoadBarW / MAX_TERMINAL_LOAD_BAR_W), TERMINAL_H * .05, 0, 1, 0, 1);
		}
		else
		{
			if (terminalStatusTimer < SHOW_STATUS_TIME)
			{
				if (core.getGameEngine().getGameStatus() == GAME_STATE.WON)
				{
					TerminalConnectedImg.draw(TERMINAL_X, TERMINAL_Y, TERMINAL_W, TERMINAL_H);
					enableDrawStatus();
				}
				else
				{
					TerminalErrorImg.draw(TERMINAL_X, TERMINAL_Y, TERMINAL_W, TERMINAL_H);
					if (terminalStatusTimer == 0 && !core.getGameEngine().getSpy().isAlive())
					{
						TerminalDeathSound.stopAndReset();
						TerminalDeathSound.play();
					}
				}
				terminalStatusTimer += core.rate;
			}
			else if (!core.getGameEngine().getSpy().isAlive() && core.getGameEngine().getGameStatus() != GAME_STATE.WON)
			{
				TerminalDeathImg.draw(TERMINAL_X, TERMINAL_Y, TERMINAL_W, TERMINAL_H);
				if (terminalStatusTimer > SHOW_STATUS_TIME + 60)
				{
					enableDrawStatus();
				}
				terminalStatusTimer += core.rate;
			}
			else
			{
				terminalLoadBarW = 0;
				terminalStatusTimer = 0;
				core.setPlayerMovement(true);
				inTerminal = false;
			}
		}
	} 
	
	private void drawRadar()
	{
		if (core.getInputManager().isKeyTyped('r'))
		{
			radarEnabled = !radarEnabled;
		}
		if (radarEnabled)
		{
			float roomAngle = (float) Math.atan2(core.getRoomManager().getWinRoomY() - core.getPlayer().getY(), core.getRoomManager().getWinRoomX() - core.getPlayer().getX());
			float roomCircleX = (float) (Math.cos(roomAngle) * RADAR_RADIUS) + RADAR_X;
			float roomCircleY = (float) (Math.sin(roomAngle) * RADAR_RADIUS) + RADAR_Y;
			core.getShapeRenderer().drawCircleOutline(RADAR_X, RADAR_Y, RADAR_RADIUS, 20, 0, 1, 0, 1);
			core.getShapeRenderer().drawCircle(RADAR_X, RADAR_Y, 20, 0, 1, 0, 1);
			core.getShapeRenderer().drawCircle(roomCircleX, roomCircleY, 40, 0, 1, 0, 1);
			core.getShapeRenderer().drawLine(RADAR_X, RADAR_Y, roomCircleX, roomCircleY, 5, 0, 1, 0, 1);
		}
	}
	
	private void drawMenu()
	{
		core.setPlayerMovement(false);
		core.getShapeRenderer().drawRect(MENU_X, MENU_Y, MENU_W, MENU_H, 0, .2f, 0, 1);
		float buttonY = MENU_BUTTON_START_Y;
		if (!inAnimation())
		{
			if (drawButton("Main Menu", MENU_BUTTON_X, buttonY, MENU_BUTTON_W, MENU_BUTTON_H))
			{
				saveButtonPressed = false;
				saveFileField.reset();
				core.exitToMenu();
			}
		}
		buttonY += MENU_BUTTON_H + MENU_BUTTON_Y_OFF;
		if (!inAnimation())
		{
			if (!saveButtonPressed)
			{
				if (drawButton("Save", MENU_BUTTON_X, buttonY, MENU_BUTTON_W, MENU_BUTTON_H))
				{
					saveButtonPressed = true;
				}
			}
			else
			{
				if (drawButton("Save", MENU_BUTTON_X, buttonY, MENU_BUTTON_W/2, MENU_BUTTON_H))
				{
					if (core.save(saveFileField.getMessage()))
					{
						saveSuccess = true;
					}
					else
					{
						saveSuccess = false;
					}
					drawSaveErrorMsg = true;
				}
				if (drawButton("Cancel", MENU_BUTTON_X + MENU_BUTTON_W/2, buttonY, MENU_BUTTON_W/2, MENU_BUTTON_H))
				{
					saveButtonPressed = false;
					drawSaveErrorMsg = false;
					saveFileField.reset();
				}
				saveFileField.draw(MENU_BUTTON_X, buttonY + MENU_BUTTON_H);
				if (drawSaveErrorMsg)
				{
					if (saveSuccess)
					{
						core.getTextRenderer().drawCenteredText("Save Success!", DisplayManager.DISPLAY_DEFAULT_W/2, buttonY + MENU_BUTTON_H * 1.7f, 20, 0, 1, 0, 1);
					}
					else
					{
						core.getTextRenderer().drawCenteredText("Save Failed...", DisplayManager.DISPLAY_DEFAULT_W/2, buttonY + MENU_BUTTON_H * 1.7f, 20, 1, 0, 0, 1);
					}
				}
				buttonY += MENU_BUTTON_H/2;
			}
			buttonY += MENU_BUTTON_H + MENU_BUTTON_Y_OFF;
		}
		else
		{
			saveButtonPressed = false;
			drawSaveErrorMsg = false;
			saveFileField.reset();
		}
		if (drawButton("Difficulty: " + core.getGameEngine().getDifficulty().name(), MENU_BUTTON_X, buttonY, MENU_BUTTON_W, MENU_BUTTON_H))
		{
			difficultyButtonPressed = !difficultyButtonPressed;
		}
		buttonY += MENU_BUTTON_H;
		if (difficultyButtonPressed)
		{
			Iterator<GAME_DIFFICULTY> iter = GAME_DIFFICULTY.keyCodes().values().iterator();
			while (iter.hasNext())
			{
				GAME_DIFFICULTY gameDifficulty = iter.next();
				if (gameDifficulty != core.getGameEngine().getDifficulty())
				{
					if (drawButton(gameDifficulty.name(), MENU_BUTTON_X + 10, buttonY, MENU_BUTTON_W - 20, MENU_BUTTON_H))
					{
						core.getGameEngine().changeDifficulty(gameDifficulty);
					}
					buttonY += MENU_BUTTON_H;
				}
			}
		}
		buttonY += MENU_BUTTON_Y_OFF;
		if (drawButton("Exit", MENU_BUTTON_X, buttonY, MENU_BUTTON_W, MENU_BUTTON_H))
		{
			System.exit(0);
		}
	}
	
	private boolean drawButton(String msg, float x, float y, float w, float h)
	{
		if (core.getButtonManager().buttonClicked(x, y, w, h, .4f, .4f, .4f, 1))
		{
			core.getTextRenderer().drawCenteredText(msg, x + w/2, y + h/2, h/2, 0, 1, 0, 1);
			return true;
		}
		core.getTextRenderer().drawCenteredText(msg, x + w/2, y + h/2, h/2, 0, 1, 0, 1);
		return false;
	}
}
