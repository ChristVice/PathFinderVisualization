/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.pathfindervisualization;

/**
 *
 * @author chvicencio
 */
public class PathFinderVisualization {

    public static void main(String[] args) {
        GridGraph graph = new GridGraph(3, 3);
        graph.printGrid();

        // Example: Accessing a specific node and its neighbors
        Node node = graph.getNode(1, 1); // Get the node at (1, 1)
        System.out.println("Neighbors of node " + node + ": " + node.neighbors);
    }
}
