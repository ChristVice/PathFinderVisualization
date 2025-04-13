package com.mycompany.pathfindervisualization;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public int row, col;
    public boolean isPassable;
    public List<Edge> neighbors;
    public int distance = Integer.MAX_VALUE; 

    private int edgeWeight = 1;

    // Constructor
    public Node(int row, int col, boolean isPassable) {
        this.row = row;
        this.col = col;
        this.isPassable = isPassable;

        this.neighbors = new ArrayList<>();
    }

    // Add a neighboring node
    public void addNeighbor(Node neighbor) {
        neighbors.add(new Edge(neighbor, edgeWeight));
    }

    static class Edge{
        Node node;
        int weight;

        public Edge(Node node, int weight) {
            this.node = node;
            this.weight = weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getWeight() {
            return weight;
        }
    }

    // Override toString for easy debugging
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }

    
}
