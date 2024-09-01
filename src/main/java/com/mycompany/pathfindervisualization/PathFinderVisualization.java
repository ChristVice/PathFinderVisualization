package com.mycompany.pathfindervisualization;

import javax.swing.*;
import java.awt.*;

public class PathFinderVisualization {

    private GridGraph graph;
    private int screenWidth = 1300;
    private int screenHeight = 800;
    private int gridSize = 50;

    PathFinderVisualization() {
        // initialize the grid graph
        graph = new GridGraph(3, 3);

        // Create the GUI on the event dispatching thread
        JFrame frame = new JFrame("Path Finder Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight);


        /*
         * Create the settings panel on the left side of the frame  
         */
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBackground(Color.decode("#D9D9D9"));
        settingsPanel.setPreferredSize(new Dimension(200, screenHeight)); // Set width to 200, height to match frame

        // Add components to the settings panel
        settingsPanel.add(new JLabel("Algorithm Options:"));
        String[] algorithms = {"A*", "Dijkstra", "BFS", "DFS"};
        JComboBox<String> algorithmComboBox = new JComboBox<>(algorithms);
        settingsPanel.add(algorithmComboBox);


        // time took for the algorithm to run
        float timeTook = 0;
        settingsPanel.add(new JLabel("Time Took: "+timeTook+" ms"));


        //settingsPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add space between components

        // Start node button
        settingsPanel.add(new JLabel("Start Node:"));
        JCheckBox startNodeField = new JCheckBox();
        settingsPanel.add(startNodeField);


        // End node button
        settingsPanel.add(new JLabel("End Node:"));
        JCheckBox endNodeField = new JCheckBox();
        settingsPanel.add(endNodeField);

        // Run button
        JButton runButton = new JButton("Run");
        settingsPanel.add(runButton);

        // when run button is clicked run the selected algorithm 
        runButton.addActionListener(l -> {
            String selectedAlgorithm = algorithmComboBox.getSelectedItem().toString();
            runAlgorithm(selectedAlgorithm);
        });


        // pause visualizer
        JButton pauseButton = new JButton("Pause");
        settingsPanel.add(pauseButton);

        // when pause button is clicked
        pauseButton.addActionListener(l -> {
            System.out.println("Pause button clicked");
        });
        

        //reset the grid board
        JButton resetButton = new JButton("Reset");
        settingsPanel.add(resetButton);

        // when reset button is clicked
        resetButton.addActionListener(l -> {
            System.out.println("Reset button clicked");
        });



        // add panel to left side
        frame.add(settingsPanel, BorderLayout.WEST);



        /*
         * Create the grid panel on the right side of the frame
         */
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(gridSize, gridSize)); // 10x10 grid
        // Add squares to the grid panel
        for (int i = 0; i < gridSize*gridSize; i++) {
            JPanel square = new JPanel();
            square.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Set thin border
            gridPanel.add(square);
        }

        // Add grid panel to the right side of the frame
        frame.add(gridPanel, BorderLayout.CENTER);

        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new PathFinderVisualization();
    }

    // Static method to run the selected algorithm
    private void runAlgorithm(String algorithm) {
        // Run the selected algorithm
        System.out.println("Running " + algorithm);
    }
}