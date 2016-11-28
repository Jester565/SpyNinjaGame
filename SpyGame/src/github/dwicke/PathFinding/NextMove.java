package github.dwicke.PathFinding;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.ArrayList;

/**
 * This is the interface that you implement in order to 
 * @author Drew Wicke
 * @param <AnyType>
 * This should be the class that the data about the position of the object is stored.
 */
public interface NextMove<AnyType> 
{
    /**
     * This is a required method that generates a list of avaliable moves from 
     * the current position.
     * @param current
     * The current position.
     * @return
     * Returns a list of avaliable moves from current position.
     */
    //public ArrayList<AnyType> genMoves(AnyType current);
    public ArrayList<AnyType> genMoves(AnyType current);
}
