package jgraphic;

import java.awt.event.KeyEvent;

import jgraphic.Core;

public class TextField {
	private int w;
	private int h;
	private int cursorOff = 0;
	private int cursorX = 0;
	private double cursorFlashTimer = 0;
	private int cursorPos = -1;
	private boolean fieldSelected = false;
	private String message = "";
	private Core core;
	
	public TextField(Core core, int w, int h)
	{
		this.core = core;
		this.w = w;
		this.h = h;
	}
	public boolean isSelected(){
		return fieldSelected;
	}
	public void reset() {
		fieldSelected = false;
		message = "";
		cursorX = 30;
		cursorPos = -1;
		cursorOff = 0;
	}
	public String getMessage(){
		return message;
	}
	public void getInput()
	{
		for (KeyEvent typedEvent : core.getInputManager().getTypedEvents())
		{
			Character typedChar = null;
			if(fieldSelected)
			{
				if (typedEvent.getKeyCode() == KeyEvent.VK_BACK_SPACE)
				{
					if(cursorPos > -1)
					{
						message = message.substring(0,cursorPos)+message.substring(cursorPos+1,message.length());
						cursorPos--;
					}
				}
				else if (typedEvent.getKeyCode() == KeyEvent.VK_LEFT)
				{
					if(cursorPos>-1)
					{
						cursorPos--;
					}
				}
				else if (typedEvent.getKeyCode() == KeyEvent.VK_RIGHT)
				{
					if(cursorPos < message.length()-1)
					{
						cursorPos++;
					}
				}
				else
				{
					typedChar = typedEvent.getKeyChar();
					if (typedChar != KeyEvent.CHAR_UNDEFINED)
					{
						cursorPos++;
					}
					else
					{
						typedChar = null;
					}
				}
				if(typedChar != null)
				{
					message = smartSubstr(message, cursorPos)+typedChar+smartSubstr(message,cursorPos,message.length());
				}
			}
			if (cursorPos > message.length() - 1)
			{
				System.out.println("CURSOR POS EXCEEDED STRING");
			}
		}
	}
	private String smartSubstr(String str, int end)
	{
		if (end >= 0 && end <= str.length())
		{
			return str.substring(0,end);
		}
		return "";
	}
	private String smartSubstr(String str, int begin, int end)
	{
		if (end >= 0 && end <= str.length() && begin >= 0 && begin <= str.length())
		{
			return str.substring(begin,end);
		}
		return "";
	}
	private void drawMessage(float x, float y){
		if(fieldSelected){
			if(cursorX > w - 30){
				cursorOff++;
			}else if(cursorX < 30 && cursorOff > 0){
				cursorOff--;
			}
		}
		String s = refinedExtendable();
		int endPoint = s.length();
		for(int i = 1; i < s.length();i++){
			if(core.getTextRenderer().getTextWidth(s.substring(0, i), (3f * h)/4f) > w-30){
				endPoint = i;
				break;
			}
		}
		core.getTextRenderer().drawText(s.substring(0, endPoint), x, y + (3f * h)/4f, (3f * h)/4f, 0, 0, 0, 1);
	}
	private String refinedExtendable(){
		return message.substring(cursorOff);
	}
	public void draw(float x, float y)
	{
		getInput();
		core.getShapeRenderer().drawRect(x, y, w, h,.6f,.6f,.6f,1);
		drawMessage(x, y);
		if(fieldSelected)
			cursorManager(x, y);
		mouseOnTextManager(x, y);
	}
	public void draw(String string, float x, float y)
	{
		draw(x, y);
		core.getTextRenderer().drawText(string, x+5, y+(int)(h/3.7d), (3f * h)/4f, 0, 0, 0, 1);
	}
	
	public void cursorManager(float x, float y)
	{
		cursorFlashTimer-=core.rate;
		if(cursorFlashTimer < 0)
		{
			cursorFlashTimer = 40;
		}
		cursorX = (int) (core.getTextRenderer().getTextWidth(refinedExtendable().substring(0, cursorPos - cursorOff + 1), (3f * h)/4f));
		if(cursorFlashTimer < 25)
		{
			core.getShapeRenderer().drawRect(cursorX/core.getDisplayManager().screenWScale + x + 1, y + (int)(h/6d), (int)(h/15d), h-(int)(h/4d),0,0,0,1);
		}
	}
	private void mouseOnTextManager(float x, float y)
	{
		if(core.getButtonManager().buttonClicked(x-5, y, w, h))
		{
			fieldSelected = true;
			core.getInputManager().activeTextField = this;
			int smallestDistance = Integer.MAX_VALUE;
			int smallestCursorPos = Integer.MAX_VALUE;
			for(int i = 0; i < message.length();i++)
			{
				int distance = (int) Math.abs(core.getInputManager().getScaleMouseClickX()-(x+core.getTextRenderer().getTextWidth(message.substring(0,  i),  h-(int)(h/4d))+core.getTextRenderer().getTextWidth(message.substring(i, i + 1),  h-(int)(h/4d))+5));
				if(distance < smallestDistance)
				{
					smallestDistance = distance;
					smallestCursorPos = i;
				}
			}
			if(core.getInputManager().getScaleMouseClickX()>x && message.length()>1)
			{
				cursorFlashTimer = 25 ;
				cursorPos = smallestCursorPos + cursorOff;
			}
			else
			{
				cursorFlashTimer = 25;
				cursorPos = -1 + cursorOff;
			}
		}
		else if(core.getInputManager().isMouseClicked())
		{
			if (core.getInputManager().activeTextField == this)
			{
				core.getInputManager().activeTextField = null;
			}
			fieldSelected = false;
		}
	}
}