package com.mycompany.pathfindervisualization;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
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
            case 3:
                startAStarAlgorithm();
                break;
            case 4:
                startGreedyBestFirstAlgorithm();
                break;
            case 5:
                break;
            default:
                break;
        }


        // Resetting the distances of all nodes in the grid
        grid.getStartNode().distance = Integer.MAX_VALUE; //done individually cause its not part of either list 
        pathSteps.forEach(node -> {
            node.distance = Integer.MAX_VALUE; // Resetting the distance of all nodes
        });
        foundPathSteps.forEach(node -> {
            node.distance = Integer.MAX_VALUE; // Resetting the distance of all nodes
        });


        System.out.println("pathSteps: " + pathSteps);
        System.out.println("foundSteps: " + foundPathSteps);

        return;
    }


    /**
     * Executes the Greedy Best-First Search algorithm to find a path from the start node
     * to the end node in the grid. This algorithm uses a heuristic to prioritize nodes
     * closer to the goal, aiming to find a path efficiently.
     *
     */
    private void startGreedyBestFirstAlgorithm() {
        Node endNode = grid.getEndNode();
        Node startNode = grid.getStartNode();

        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> heuristic(node, endNode)));
        HashSet<Node> visited = new HashSet<>();
        Node[][] previous = new Node[gridSize][gridSize];
        visited.add(startNode);
        pq.add(startNode);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.equals(endNode)) {
                break; // Goal reached
            }

            for (Node.Edge adjacent : current.neighbors) {
                Node neighbor = adjacent.node;

                if (!visited.contains(neighbor) && neighbor.isPassable && !visited.contains(neighbor)) {
                    visited.add(current); // Mark as visited when polled
                    previous[neighbor.col][neighbor.row] = current;
                    pq.add(neighbor);

                    pathSteps.add(neighbor);
                }
            }
        }

        this.setFoundPathSteps(previous);

        return;
    }

    /**
     * Executes the A* pathfinding algorithm to find the shortest path 
     * from the start node to the end node on a grid.
     * 
     * The algorithm uses a priority queue to explore nodes based on their 
     * distance and heuristic value (estimated cost to the end node). It 
     * updates the shortest path dynamically and stops when the end node 
     * is reached.
     * 
     */
    private void startAStarAlgorithm() {
        Node endNode = grid.getEndNode();
        Node startNode = grid.getStartNode();
     
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance + heuristic(node, endNode)));
        HashSet<Node> visited = new HashSet<>();
        Node[][] previous = new Node[gridSize][gridSize];
        startNode.distance = 0;
        pq.add(startNode);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (visited.contains(current)) { // Skip if already visited
                continue;
            }
            visited.add(current); // Mark as visited when polled

            if (current.equals(endNode)) {
                break; // Goal reached
            }

            for (Node.Edge adjacent : current.neighbors) {
                Node neighbor = adjacent.node;
                int newDistance = current.distance + adjacent.weight;

                if (neighbor.isPassable && newDistance < neighbor.distance) {
                    neighbor.distance = newDistance;
                    previous[neighbor.col][neighbor.row] = current;
                    pq.add(neighbor);

                    pathSteps.add(neighbor);
                }
            }
        }


        this.setFoundPathSteps(previous);

        return;
    }

    /**
     * Heuristic function using the Euclidean distance
     * 
     * @param node The current node.
     * @param endNode The target node.
     * @return The heuristic value is the estimated cost from the current node to the target node.
     */
    private int heuristic(Node node, Node endNode) {
        double insideCalculation = Math.pow((double) Math.abs(node.col - endNode.col), 2) + Math.pow((double) Math.abs(node.row - endNode.row), 2);

        // using the Euclidean distance
        return (int) Math.sqrt(insideCalculation); 
    }

    /**
     * Implements Dijkstra's algorithm to find the shortest path in a grid.
     * The algorithm uses a priority queue to explore nodes with the smallest
     * distance first, updating distances and tracking the path as it progresses.
     * 
     * @param gridSize The size of the grid (assumes a square grid).
     * @param grid The grid containing nodes, start node, and end node.
     * @param pathSteps A list to track the steps of the path being explored.
     * 
     * @return void This method modifies the state of the grid and pathSteps
     *         to reflect the shortest path found, if any.
     */
    private void startDijkstrasAlgorithm() {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.distance));

        boolean[][] visited = new boolean[gridSize][gridSize];
        Node[][] previous = new Node[gridSize][gridSize];

        Node startNode = grid.getStartNode();
        Node endNode = grid.getEndNode();

        startNode.distance = 0;
        pq.add(startNode);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (current.equals(endNode)) {
                //remove all the steps after the last step from pathSteps
                for(int i=0; i < pq.size(); ++i){
                    pathSteps.removeLast();
                }
                break;
            }

            for(Node.Edge adjacent : current.neighbors) {
                Node neighbor = adjacent.node;
                int newDistance = current.distance + adjacent.weight;

                if (!visited[neighbor.col][neighbor.row] && neighbor.isPassable && newDistance < neighbor.distance) {
                    visited[current.col][current.row] = true;

                    neighbor.distance = newDistance;
                    previous[neighbor.col][neighbor.row] = current;
                    pq.add(neighbor);

                    pathSteps.add(neighbor);
                }
            }
        }


        this.setFoundPathSteps(previous);
    }

    /**
     * Implements the Breadth-First Search (BFS) algorithm to find a path 
     * from the start node to the end node in a grid. The method explores 
     * all possible paths level by level until the shortest path to the 
     * goal is found or all possibilities are exhausted.
     *
     */
    private void startBFS() {
        Queue<Node> queue = new LinkedList<>();
        HashSet<Node> visited = new HashSet<>();
        Node[][] previous = new Node[gridSize][gridSize];

        Node startNode = grid.getStartNode();
        Node endNode = grid.getEndNode();

        if(startNode == endNode) {
            return; // Start and end are the same
        }

        visited.add(startNode);
        queue.add(startNode);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            for (Node.Edge adjacent : current.neighbors) {
                Node neighbor = adjacent.node;

                if (neighbor.equals(endNode)) {
                    previous[neighbor.col][neighbor.row] = current;
                    this.setFoundPathSteps(previous); // Reconstruct path immediately
                    return; // Goal found, exit
                }

                if (neighbor.isPassable && !visited.contains(neighbor)) {
                    visited.add(neighbor);
                    previous[neighbor.col][neighbor.row] = current;
                    queue.add(neighbor);

                    pathSteps.add(neighbor); // Add to path steps
                }
            }
        }

    }

    /**
     * Initiates the Depth-First Search (DFS) algorithm in a recursive manner to find a path
     * between the start node and the end node on the grid. If a path is found, it sets the 
     * steps of the found path.
     *
     * This method initializes the visited nodes matrix and the previous nodes matrix, 
     * then calls the recursive DFS function to explore the grid.
     */
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

    /**
     * Performs a Depth-First Search (DFS) recursively to find a path from the current node to the end node.
     *
     * @param current The current node being visited.
     * @param endNode The target node to reach.
     * @param visited A 2D boolean array indicating whether each node has been visited.
     * @param previous A 2D array of nodes used to track the path taken to reach each node.
     * @return true if a path to the end node is found, false otherwise.
     */
    private boolean dfsRecursive(Node current, Node endNode, boolean[][] visited, Node[][] previous) {

        // Mark the current node as visited and color it
        visited[current.col][current.row] = true;
        pathSteps.add(current);

        if (current.equals(endNode)) {
            return true;
        }

        // Recursively visit each neighbor

        for(Node.Edge adjacent : current.neighbors) {
            Node neighbor = adjacent.node;
            
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
