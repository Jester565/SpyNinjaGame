package github.dwicke.PathFinding;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author PA Cyber
 */

public final class Node<AnyType> implements Comparable<Node>
{
    private AnyType stateData;
    private Node parentNode;
    private int g,h,f, depth;
    

    
    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public Node getParentNode() {
        return parentNode;
    }

    public void setParentNode(Node parentNode) {
        this.parentNode = parentNode;
    }

    public AnyType getStateData() {
        return stateData;
    }

    public void setStateData(AnyType stateData) {
        this.stateData = stateData;
    }

   
    
    
    /**
     * A constructor that stores the data for the node.
     * @param stateData
     * The data for the state.
     * @param parentNode
     * The node that created this node.
     * @param g
     * The number of moves it took to get here from the initial start position.
     * @param h
     * The estimated number of moves it will take to travel to the goal position 
     * from this position given by the heuristic algorithm.
     * @param depth
     * The level in the tree the node is in (every time a node is expanded the 
     * depth of the tree is increased).
     */
    public Node(AnyType stateData, Node parentNode, int g, int h, int depth)
    {
        this.stateData = stateData;
        this.parentNode = parentNode;
        this.g = g;
        this.h = h;
        this.f = this.g + this.h;
        this.depth = depth;
    }
    
    @Override
    public int compareTo(Node other)
    {
        int NodeComp = (this.f - other.getF()) * -1;
        if (NodeComp == 0)
        {
            NodeComp = (this.depth - other.getDepth());
        }
        return NodeComp;
    }
}
