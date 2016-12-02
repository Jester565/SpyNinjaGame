package impl;

import java.util.Iterator;
import java.util.LinkedList;

public class NotificationManager {

	class Notification {
		private static final int FADE_TIME = 200;
		private static final float Y_OFF = 20;
		private static final float MAX_ALPHA = .9f;
		private float fadeTimer = 0;
		private float alpha = 0;
		private GameCore core;
		private String msg;
		
		Notification(GameCore core, String msg)
		{
			this.core = core;
			this.msg = msg;
		}
		
		void draw(float x, float y, float fontSize)
		{
			if (alpha >= MAX_ALPHA)
			{
				fadeTimer += core.rate;
			}
			else if (fadeTimer < FADE_TIME)
			{
				alpha += core.rate * .05f;
				if (alpha > MAX_ALPHA)
				{
					alpha = MAX_ALPHA;
				}
			}
			if (fadeTimer > FADE_TIME)
			{
				alpha -= core.rate * .01f;
				if (alpha < 0)
				{
					alpha = 0;
				}
			}
			float textWidth = core.getTextRenderer().getTextWidth(msg, fontSize);
			core.getShapeRenderer().drawRect(x - (textWidth / 2)/core.getDisplayManager().screenWScale - 5, y - fontSize * .9f, (textWidth)/core.getDisplayManager().screenWScale + 10, fontSize * 1.3f, .2f, .2f, .2f, alpha);
			core.getTextRenderer().drawCenteredText(msg, x, y, fontSize, 1, 1, 1, alpha);
		}
		
		float getHeight(float fontSize)
		{
			if (alpha < MAX_ALPHA && fadeTimer > FADE_TIME)
			{
				return (fontSize + Y_OFF) * (alpha/MAX_ALPHA);
			}
			return fontSize + Y_OFF;
		}
		
		boolean isExpired()
		{
			return alpha <= 0 && fadeTimer > FADE_TIME;
		}
	}
	
	private int maxShownNotifications;
	private GameCore core;
	private LinkedList <Notification> notifications;
	private int MAX_NUM_NOTIFICATIONS = 20;
	private float stopNotificationTimer = 0;
	
	public NotificationManager(GameCore core, int maxShownNotifications)
	{
		this.core = core;
		this.maxShownNotifications = maxShownNotifications;
		notifications = new LinkedList<Notification>();
	}
	
	public void addNotification(String msg)
	{
		if (stopNotificationTimer <= 0)
		{
			notifications.add(new Notification(core, msg));
			if (notifications.size() > MAX_NUM_NOTIFICATIONS)
			{
				notifications.clear();
				stopNotificationTimer = 100;
				notifications.add(new Notification(core, "You can try to break this game... but don't expect to be successful"));
			}
		}
	}
	
	public void draw(float x, float y, float fontSize)
	{
		int iterCount = 0;
		Iterator<Notification> iter = notifications.iterator();
		while (iter.hasNext())
		{
			Notification notification = iter.next();
			notification.draw(x, y + fontSize, fontSize);
			y -= notification.getHeight(fontSize);
			if (notification.isExpired())
			{
				iter.remove();
			}
			iterCount++;
			if (iterCount >= maxShownNotifications)
			{
				break;
			}
		}
		if (stopNotificationTimer > 0)
		{
			stopNotificationTimer -= core.rate;
		}
	}
}
