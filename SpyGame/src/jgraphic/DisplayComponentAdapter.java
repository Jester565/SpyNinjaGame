package jgraphic;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DisplayComponentAdapter extends ComponentAdapter{
	DisplayComponentAdapter(DisplayManager dm)
	{
		this.dm = dm;
	}
	@Override
	public void componentResized(ComponentEvent e)
	{
		dm.updateScreenSize();
	}
	
	private DisplayManager dm;
}
