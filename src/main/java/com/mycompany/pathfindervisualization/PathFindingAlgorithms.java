package com.mycompany.pathfindervisualization;

import java.util.List;
import java.util.Queue;

import javax.swing.Timer;

import java.util.LinkedList;

public class PathFindingAlgorithms {

    private List<Node> pathSteps = new LinkedList<>();
    private List<Node> foundPathSteps = new LinkedList<>();

    private GridGraph grid;
    private int gridSize;

    public PathFindingAlgorithms(GridGraph grid, int gridSize) {
        this.grid = grid;
        this.gridSize = gridSize;

    }
    
    public List<Node> getPathSteps() {
        return pathSteps;
    }

    public List<Node> getFoundPathSteps() {
        return foundPathSteps;
    }

    private void setFoundPathSteps(Node[][] previous) {
        Node startNode = grid.getStartNode();
        Node endNode = grid.getEndNode();
        Node step = endNode;

        while (step != null && !step.equals(startNode)) {
            this.foundPathSteps.add(step);
            step = previous[step.col][step.row];
        }
    }

    /**
     * Runs the chosen pathfinding algorithm.
     * 
     * @param algorithm The algorithm to run:
     *                  0 - Breadth-First Search (BFS)
     *                  1 - Depth-First Search (DFS)
     *                  2 - Dijkstra's algorithm (currently not implemented)
     *                  3 - A* Search (currently not implemented)
     * 
     */
    public void RunChosenAlgorithm(int algorithm) {
        pathSteps.clear();
        foundPathSteps.clear();

        switch (algorithm) {
            case 0:
                startBFS();
                break;
            case 1:
                startDFSRecursive();
                break;
            case 2:
                startDijkstrasAlgorithm();
                break;
            default:
                break;
        }

        return;
    }

    private void startDijkstrasAlgorithm(){




    }

    private void startBFS() {
        Queue<Node> queue = new LinkedList<>();
        boolean[][] visited = new boolean[gridSize][gridSize];
        Node[][] previous = new Node[gridSize][gridSize];

        Node startNode = grid.getStartNode();
        Node endNode = grid.getEndNode();
        queue.add(startNode);
        visited[startNode.col][startNode.row] = true;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            if (current.equals(endNode)) {
                //remove all the steps after the last step from pathSteps
                for(int i=0; i < queue.size(); ++i){
                    pathSteps.removeLast();
                }
                break;
            }
            
            for(Node neighbor : current.neighbors) {
                if (!visited[neighbor.col][neighbor.row] && neighbor.isPassable) {
                    visited[neighbor.col][neighbor.row] = true;
                    previous[neighbor.col][neighbor.row] = current;
                    queue.add(neighbor);

                    pathSteps.add(neighbor);
                }
            }
        }

        this.setFoundPathSteps(previous);

    }

    private void startDFSRecursive() {
        boolean[][] visited = new boolean[gridSize][gridSize];
        Node[][] previous = new Node[gridSize][gridSize];

        Node startNode = grid.getStartNode();
        Node endNode = grid.getEndNode();

        //if path is found
        if (dfsRecursive(startNode, endNode, visited, previous)) {
            this.setFoundPathSteps(previous);
        }
    }

    private boolean dfsRecursive(Node current, Node endNode, boolean[][] visited, Node[][] previous) {

        // Mark the current node as visited and color it
        visited[current.col][current.row] = true;
        pathSteps.add(current);

        if (current.equals(endNode)) {
            return true;
        }

        // Recursively visit each neighbor
        for(Node neighbor : current.neighbors) {
            if (!visited[neighbor.col][neighbor.row] && neighbor.isPassable) {
                previous[neighbor.col][neighbor.row] = current;

                if (dfsRecursive(neighbor, endNode, visited, previous)) {
                    return true;
                }
            }
        }

        return false;
    }
}
