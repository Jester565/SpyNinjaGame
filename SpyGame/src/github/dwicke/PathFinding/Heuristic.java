package github.dwicke.PathFinding;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * This is an interface for all Heuristic algorithms.  It is neccessary for this
 * interface to be implemented in order for the AStar algorithm to work.
 * @author Drew Wicke
 * @param <AnyType>
 * This should be the class that the data about the position of the object is stored.
 */
public interface Heuristic<AnyType>
{
    /**
     * getHeuristic is a method that is required for the AStar algorithm to
     * work properly.
     * @param currentPos
     * This is where it is at.
     * @param goalPos
     * This is where it wants to be.
     * @return
     * This is an integer estimate of how many more nodes to be traveled until
     * the goal is reached.
     */
    public int getHeuristic(AnyType currentPos, AnyType goalPos);
    
}
