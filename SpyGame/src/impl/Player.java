package impl;

import jgraphic.Image;
import jgraphic.Sound;
import jphys.Body;
import jphys.CollisionManager;

public class Player extends Body {
	public static final float SPEED = 5f;
	public static final float STEP_SWITCH_TIME = 90;
	private static final float SHIELD_DELTA_RADS = .02f;
	
	private static boolean ImgsLoaded = false;
	private static GameImage MuzzleFlareImg;
	private static GameImage HalfRightStepImg;
	private static GameImage RightStepImg;
	private static GameImage LeftStepImg;
	private static GameImage HalfLeftStepImg;
	private static GameImage StillImg;
	private static GameImage ShieldImg;
	private static GameImage ShieldImg2;
	
	private static Sound StepSound1;
	private static Sound StepSound2;
	private static Sound StepSound3;
	
	private static final float SHOOT_TIME = 2;
	
	private boolean playSound = false;
	private boolean moved = false;
	private float stepTimer = 0;
	private float angle = 0;
	private int stepI = 0;
	private boolean drawShield = false;
	private float shieldRads = 0;
	private float shieldRadius = 1;
	private float shieldRadiusVelocity = 2;
	private float shootTimer = 0;
	private boolean isShooting = false;

	private GameCore core;
	
	public Player(GameCore core, CollisionManager collisionManager, float x, float y, float w, float h) {
		super(collisionManager, x, y, w, h);
		this.core = core;
		if (!ImgsLoaded)
		{
			HalfRightStepImg = new GameImage(core);
			HalfRightStepImg.init("./resources/Imgs/Soldier/soldierStep1Half.png");
			RightStepImg = new GameImage(core);
			RightStepImg.init("./resources/Imgs/Soldier/soldierStep1.png");
			HalfLeftStepImg = new GameImage(core);
			HalfLeftStepImg.init("./resources/Imgs/Soldier/soldierStep2Half.png");
			LeftStepImg = new GameImage(core);
			LeftStepImg.init("./resources/Imgs/Soldier/soldierStep2.png");
			StillImg = new GameImage(core);
			StillImg.init("./resources/Imgs/Soldier/soldierStill.png");
			ShieldImg = new GameImage(core);
			ShieldImg.init("./resources/Imgs/Soldier/shield1.png");
			MuzzleFlareImg = new GameImage(core);
			MuzzleFlareImg.init("./resources/Imgs/Soldier/muzzleFlare.png");
			ShieldImg.setRotateOriginScale(.5d, .5d);
			ShieldImg2 = new GameImage(core);
			ShieldImg2.init("./resources/Imgs/Soldier/shield2.png");
			StepSound1 = new Sound("./resources/Sounds/metalWalk1.wav");
			StepSound2 = new Sound("./resources/Sounds/metalWalk2.wav");
			StepSound3 = new Sound("./resources/Sounds/metalWalk3.wav");
			ImgsLoaded = true;
		}
	}
	
	public void setDrawShield(boolean mode)
	{
		drawShield = mode;
	}
	
	public float getRads()
	{
		return (float)angle;
	}
	
	public void setRads(float rads)
	{
		angle = rads;
	}
	
	@Override
	public void move(float vX, float vY)
	{
		super.move(vX * (float)core.rate, vY * (float)core.rate);
		if (vX != 0 || vY != 0)
		{
			moved = true;
		}
	}
	
	private void playStepSound()
	{
		if (stepI == 0)
		{
			StepSound1.stopAndReset();
			StepSound1.play();
		}
		else if (stepI == 1)
		{
			StepSound2.stopAndReset();
			StepSound2.play();
		}
		else
		{
			StepSound3.stopAndReset();
			StepSound3.play();
		}
		stepI++;
		if (stepI > 2)
		{
			stepI = 0;
		}
	}
	
	public void setAngleToPointTo(float x, float y)
	{
		angle = (float) Math.atan2(y - (getY() + getH()/2f - core.gameY)*core.gameScaleY, x - (getX() + getW()/2f - core.gameX)*core.gameScaleX);
		angle = GameCore.NormalizeRadian(angle);
	}
	
	public void setShoot(boolean mode)
	{
		isShooting = mode;
	}
	
	private void drawMuzzleFlare()
	{
		MuzzleFlareImg.setRotateOriginScale(.5f, .5f);
		MuzzleFlareImg.setRads(angle);
		MuzzleFlareImg.draw(getX(), getY(), getW(), getH());
	}
	
	public void draw(float xOff, float yOff)
	{
		Image activeImg = null;
		if (moved)
		{
			if (stepTimer < 15)
			{
				//HalfRightStepImg.draw(getX(), getY(), getW(), getH());
				activeImg = HalfRightStepImg;
				playSound = false;
			}
			else if (stepTimer < 30)
			{
				if (!playSound)
				{
					playStepSound();
					playSound = true;
				}
				activeImg = RightStepImg;
			}
			else if (stepTimer < 45)
			{
				activeImg = HalfRightStepImg;
				playSound = false;
			}
			else if (stepTimer < 60)
			{
				activeImg = HalfLeftStepImg;
			}
			else if (stepTimer < 75)
			{
				if (!playSound)
				{
					playStepSound();
					playSound = true;
				}
				activeImg = LeftStepImg;
			}
			else
			{
				activeImg = HalfLeftStepImg;
			}
			stepTimer += core.rate * 1.6f;
			if (stepTimer > STEP_SWITCH_TIME)
			{
				stepTimer = 0;
			}
			moved = false;
		}
		else
		{
			activeImg = StillImg;
			stepTimer = 0;
		}
		activeImg.setRotateOriginScale(.5f, .5f);
		activeImg.setRads(angle);
		activeImg.draw(getX(), getY(), getW(), getH());
		
		if (isShooting)
		{
			shootTimer += core.rate;
			drawMuzzleFlare();
			if (shootTimer > SHOOT_TIME)
			{
				shootTimer = 0;
				isShooting = false;
			}
		}
		
		if (core.getGameEngine().getSpy().isInvincible() && drawShield)
		{
			shieldRadius += shieldRadiusVelocity;
			if (shieldRadius > getW())
			{
				shieldRadius = getW();
				shieldRadiusVelocity = -shieldRadiusVelocity;
			}
			if (shieldRadius < 1)
			{
				shieldRadius = 1;
				shieldRadiusVelocity = -shieldRadiusVelocity;
			}
			shieldRads += SHIELD_DELTA_RADS;
			if (shieldRads > 2 * Math.PI)
			{
				shieldRads %= 2 * Math.PI;
			}
			ShieldImg.setRads(shieldRads);
			ShieldImg.draw(getX(), getY(), getW(), getH());
			ShieldImg2.draw(getX() + getW()/2 - shieldRadius/2, getY() + getH()/2 - shieldRadius/2, shieldRadius, shieldRadius);
		}
	}
}
