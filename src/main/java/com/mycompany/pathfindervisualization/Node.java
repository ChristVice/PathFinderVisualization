package com.mycompany.pathfindervisualization;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public int row, col;
    public boolean isPassable;
    public List<Node> neighbors;

    // Constructor
    public Node(int row, int col, boolean isPassable) {
        this.row = row;
        this.col = col;
        this.isPassable = isPassable;

        this.neighbors = new ArrayList<>();
    }

    // Add a neighboring node
    public void addNeighbor(Node neighbor) {
        neighbors.add(neighbor);
    }

    // Override toString for easy debugging
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
    
}
