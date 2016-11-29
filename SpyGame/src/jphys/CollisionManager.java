package jphys;

import java.util.HashSet;
import java.util.Iterator;

public class CollisionManager {
	public CollisionManager()
	{
		blockers = new HashSet<Blocker>();
	}
	
	Vector2D checkMove(Body body, float vX, float vY)
	{
		Vector2D compoundVector = new Vector2D(0, 0);
		for (Iterator<Blocker> blockIter = blockers.iterator(); blockIter.hasNext(); )
		{
			Blocker blocker = blockIter.next();
			Vector2D collisionVector = blocker.checkCollision(body, vX, vY);
			if (Math.abs(compoundVector.vX) < Math.abs(collisionVector.vX))
			{
				compoundVector.vX = collisionVector.vX;
			}
			if (Math.abs(compoundVector.vY) < Math.abs(collisionVector.vY))
			{
				compoundVector.vY = collisionVector.vY;
			}
		}
		return compoundVector;
	}
	
	public void addBlocker(Blocker blocker)
	{
		blockers.add(blocker);
	}
	
	public void removeBlocker(Blocker blocker)
	{
		blockers.remove(blocker);
	}
	
	private HashSet <Blocker> blockers;
}
