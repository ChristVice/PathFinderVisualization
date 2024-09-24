package com.mycompany.pathfindervisualization;

public class GridGraph {
    private Node[][] grid;
    private int numRows, numCols;

    public GridGraph(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        grid = new Node[numRows][numCols];
        initializeGrid();
        connectNodes();
    }

    // Initialize the grid with nodes
    private void initializeGrid() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                grid[row][col] = new Node(row, col, true, false, false); // Assume all nodes are passable initially
            }
        }
    }

    // Connect adjacent nodes (up, down, left, right)
    private void connectNodes() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Node current = grid[row][col];

                // Check the four possible directions (up, down, left, right)
                if (row > 0) current.addNeighbor(grid[row - 1][col]); // Up
                if (row < numRows - 1) current.addNeighbor(grid[row + 1][col]); // Down
                if (col > 0) current.addNeighbor(grid[row][col - 1]); // Left
                if (col < numCols - 1) current.addNeighbor(grid[row][col + 1]); // Right
            }
        }
    }

    // Get the node at a specific position
    public Node getNode(int row, int col) {
        return grid[row][col];
    }

    public Node getStartNode() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Node node = grid[row][col];
                if (node.isStartNode) {
                    return node;
                }
            }
        }
        return null;
    }

    public Node getEndNode() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Node node = grid[row][col];
                if (node.isEndNode) {
                    return node;
                }
            }
        }
        return null;
    }

    public void setStartNode(int row, int col) {
        Node startNode = grid[row][col];
        startNode.isStartNode = true;
    }

    public void setEndNode(int row, int col) {
        Node endNode = grid[row][col];
        endNode.isEndNode = true;
    }

    // Print the grid and neighbors (for debugging)
    public void printGrid() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                Node node = grid[row][col];
                System.out.print("Node " + node + " has neighbors: ");
                for (Node neighbor : node.neighbors) {
                    System.out.print(neighbor + " ");
                }
                System.out.println();
            }
        }
    }

}