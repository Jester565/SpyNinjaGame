package github.dwicke.PathFinding;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Drew Wicke
 */
import java.util.ArrayList;
import java.util.Collections;
public class AStar <AnyType extends Comparable<AnyType>, otherType extends NextMove<AnyType>, 
        HVal extends Heuristic<AnyType>>
{
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        // TODO code application logic here
       
    }
    
    public AStar(){ }
    
    private ArrayList<Node<AnyType>> openNodes;
    private ArrayList<Node<AnyType>> closedNodes;
    private ArrayList<AnyType> pathToGoal;
    private ArrayList<Node<AnyType>> nodesToGoal;
    private int depth;

    public ArrayList<Node<AnyType>> getClosedNodes() {
        return closedNodes;
    }

    public void setClosedNodes(ArrayList<Node<AnyType>> closedNodes) {
        this.closedNodes = closedNodes;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public ArrayList<Node<AnyType>> getOpenNodes() {
        return openNodes;
    }

    public void setOpenNodes(ArrayList<Node<AnyType>> openNodes) {
        this.openNodes = openNodes;
    }

    public ArrayList<AnyType> getPathToGoal() {
        return pathToGoal;
    }

    public void setPathToGoal(ArrayList<AnyType> pathToGoal) {
        this.pathToGoal = pathToGoal;
    }

    public ArrayList<Node<AnyType>> getNodesToGoal() {
        return nodesToGoal;
    }

    public void setNodesToGoal(ArrayList<Node<AnyType>> nodesToGoal) {
        this.nodesToGoal = nodesToGoal;
    }
    
    public ArrayList<AnyType> shortestPath(AnyType startPos,AnyType goalPos, otherType nextPos, HVal heuristicFunc)
    {
        openNodes = new ArrayList();
        closedNodes = new ArrayList();
        
        depth = 0;
        
        // Add the start position to the openNodes list
        openNodes.add(new Node<AnyType>(startPos,null,0,heuristicFunc.getHeuristic(startPos, goalPos),depth));
        
        boolean hasGoal = false;
        
        // Loop while the openNodes list is not empty
        while(openNodes.size() != 0)
        {
            // Get the last node off the openNode list and add it to the closed list.
            // This is Node n.
            closedNodes.add(openNodes.get(openNodes.size() - 1));
            Node currentNode = closedNodes.get(closedNodes.size() - 1);
            AnyType current = closedNodes.get(closedNodes.size() - 1).getStateData();
            // Remove that node off of the openNode list
            openNodes.remove(openNodes.size() - 1);
            // Check if the last node on the closedNodes list is the goal node
            if(closedNodes.get(closedNodes.size() - 1).getStateData().compareTo(goalPos) == 0)
            {
                hasGoal = true;
                // if it is, break from the while loop
                break;
            }
            
            // Now expand current Node n.
            ArrayList<AnyType> expanded = nextPos.genMoves(current);
            
            //Now I check to see if any of the expanded nodes are already on 
            // the open or closed lists
            
            NodeLoop:
            for(int i = 0; (i < openNodes.size() || i < closedNodes.size()); i++)
            {
                
               // int s = 0;
                //while (s < expanded.size()) 
                
                
                int s = expanded.size() - 1;
                while(s >= 0)
                {
                    if (i < openNodes.size()) 
                    {
                        if (openNodes.get(i).getStateData().compareTo(expanded.get(s)) == 0) 
                        {
                            // If the current cost to get to the node is less than 
                            // the cost it took the node currently on the openNodes
                            // list, then change the data of that node to suit the 
                            // new better path.
                            if ((currentNode.getG() + 1) < openNodes.get(i).getG())
                            {
                                openNodes.get(i).setG(currentNode.getG() + 1);
                                openNodes.get(i).setH(heuristicFunc.getHeuristic(expanded.get(s), goalPos));
                                openNodes.get(i).setF(openNodes.get(i).getG() + openNodes.get(i).getH());
                                openNodes.get(i).setParentNode(currentNode);
                            }
                            
                            expanded.remove(s);
                            if (expanded.isEmpty() == true) 
                            {
                                break NodeLoop;
                            }
                            continue;
                        }
                    }
                    
                    // Then if their are still closedNodes:
                    if (i < closedNodes.size())
                    {
                        // See if it is on the closedNodes list
                        if (closedNodes.get(i).getStateData().compareTo(expanded.get(s)) == 0) 
                        {
                            // If it is remove from the expanded list 
                            expanded.remove(s);
                            // If it was the last on the expanded list break from the loop
                            if (expanded.isEmpty() == true) 
                            {
                                break NodeLoop;
                            }
                            continue;
                        }
                    }

                }
                    s--;
          }
            
            // If not on either the open or closed list add new node to the
            // openNode list
            if (expanded.isEmpty() == false)
            {
                for (int i = 0; i < expanded.size(); i++)
                {
                    openNodes.add(new Node<AnyType>(expanded.get(i),currentNode,currentNode.getG() + 1,heuristicFunc.getHeuristic(expanded.get(i), goalPos), currentNode.getDepth() + 1));
                }
            }
        
            // Then reorder the open list.
            Collections.sort(openNodes);
        }
        
        
        
        
        
        if (hasGoal == true)
        {
        // Start from the last index in the closedNode list and traverse backwards
        // to create the path to the begining.  
        Node<AnyType> parent = closedNodes.get(closedNodes.size() - 1).getParentNode();
        pathToGoal = new ArrayList(closedNodes.get(closedNodes.size() - 1).getDepth());
        nodesToGoal = new ArrayList(closedNodes.get(closedNodes.size() - 1).getDepth());
        
        for(int s = (closedNodes.get(closedNodes.size() - 1).getDepth() - 1); s >= 0; s--)
        {
            nodesToGoal.set(s, parent);
            pathToGoal.set(s, parent.getStateData());
            parent = nodesToGoal.get(s).getParentNode();
        }
            return pathToGoal;
        }
        
        // Path to goal was not found so return null
        return null;
    }


}
