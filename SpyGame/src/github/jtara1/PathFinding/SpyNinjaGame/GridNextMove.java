package github.jtara1.PathFinding.SpyNinjaGame;

import edu.cpp.cs.cs141.final_proj.GameObject;
import edu.cpp.cs.cs141.final_proj.Grid;
import edu.cpp.cs.cs141.final_proj.MoveStatus.MOVE_RESULT;
import github.dwicke.PathFinding.NextMove;

import java.util.ArrayList;

public class GridNextMove extends Grid implements NextMove<GameObject> {
	
	public ArrayList<GameObject> genMoves(GameObject gameObject) {
		ArrayList<GameObject> validMoveSpots = new ArrayList<GameObject>();
		for (DIRECTION direction: DIRECTION.values()) {
			int x = gameObject.getX();
			int y = gameObject.getY();
			
			MOVE_RESULT moveResult = checkMoveStatus(direction, x, y).moveResult;
			if (moveResult == MOVE_RESULT.LEGAL) {
				validMoveSpots.add(getGameObject(x + direction.deltaX, y + direction.deltaY));
			}
		}
		return validMoveSpots;
	}
}
