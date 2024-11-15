package com.mycompany.pathfindervisualization;

import java.util.Random;

public class GridGraph {
    private Node[][] grid;
    private int numRows, numCols;

    private Node startNode;
    private Node endNode;

    public GridGraph(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;

        grid = new Node[numRows][numCols];
        initializeGrid();

        //set start and end nodes
        Random random = new Random();
        int startRow = random.nextInt(numRows);
        int startCol = random.nextInt(numCols);
        startNode = grid[startRow][startCol];

        int endRow, endCol;
        do {
            endRow = random.nextInt(numRows);
            endCol = random.nextInt(numCols);

            //                                      (y1-y2) + (x1-x2) >= 
            boolean areNodesFarApart = Math.abs(startRow - endRow) + Math.abs(startCol - endCol) >= ((numRows + numCols) / 2)-10;
            if(endRow != startRow && endCol != startCol && areNodesFarApart) {
                break;
            }

        } while (true);
        endNode = grid[endRow][endCol];
        
        connectNodes();
    }

    // Initialize the grid with nodes
    private void initializeGrid() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                grid[row][col] = new Node(row, col, true); // Assume all nodes are passable initially
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
                if (col < numCols - 1) current.addNeighbor(grid[row][col + 1]); // Right
                if (row < numRows - 1) current.addNeighbor(grid[row + 1][col]); // Down
                if (col > 0) current.addNeighbor(grid[row][col - 1]); // Left
            }
        }
    }

    // Get the node at a specific position
    public Node getNode(int row, int col) {
        return grid[row][col];
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public boolean getIsPassable(int row, int col) {
        return grid[row][col].isPassable;
    }

    public void setStartNode(int row, int col) {
        startNode = grid[row][col];
        startNode.isPassable = true;
    }

    public void setEndNode(int row, int col) {
        endNode = grid[row][col];
        endNode.isPassable = true;
    }

    public void setIsPassable(int row, int col, boolean isPassable) {
        grid[row][col].isPassable = isPassable;
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
