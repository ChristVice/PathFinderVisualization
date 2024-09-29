package com.mycompany.pathfindervisualization;

import java.util.ArrayList;
import java.util.List;

public class Node {
    int row, col;
    boolean isPassable;
    boolean isStartNode;
    boolean isEndNode;
    List<Node> neighbors;

    // Constructor
    public Node(int row, int col, boolean isPassable, boolean isStartNode, boolean isEndNode) {
        this.row = row;
        this.col = col;
        this.isPassable = isPassable;
        this.isStartNode = isStartNode;
        this.isEndNode = isEndNode;

        this.neighbors = new ArrayList<>();
    }

    public void setStartNode(boolean isStartNode) {
        this.isStartNode = isStartNode;
    }

    public void setEndNode(boolean isEndNode) {
        this.isEndNode = isEndNode;
    }

    public boolean getStartNode() {
        return this.isStartNode;
    }

    public boolean getEndNode() {
        return this.isEndNode;
    }

    // Add a neighboring node
    public void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }

    public int[] getCoordinates(){
        return new int[]{row, col};
    }

    // Override toString for easy debugging
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
    
}
