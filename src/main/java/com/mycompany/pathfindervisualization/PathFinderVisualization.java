package com.mycompany.pathfindervisualization;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
        //new PathFinderVisualization();
        CreateProgram();
        
        
        
    }
    
    public static void CreateProgram(){
        int screenWidth = 1300;
        int screenHeight = 800;

        Color settingsPanelBkg =  Color.BLUE;
        Color gridPanelBkg = Color.GREEN;
        int settingsPanelWidth = 200;
        int gridPanelWidth = screenWidth-settingsPanelWidth-1;

        JFrame frame = new JFrame("Path Finder Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight);
        //colors the frame, testing purposes
        frame.getContentPane().setBackground(Color.RED);
        
        /**************************************/
        /*          ADD SETTINGS PANEL        */
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setBackground(settingsPanelBkg);
        settingsPanel.setPreferredSize(new Dimension(settingsPanelWidth, screenHeight));

        
        //          ADDING COMPONENTS TO SETTINGS PANEL


        // ALGORITHMS
        JPanel algorithmsPanel = new JPanel();
        algorithmsPanel.setMaximumSize(new Dimension(settingsPanelWidth, 200));
        algorithmsPanel.setLayout(new FlowLayout());
        algorithmsPanel.add(new JLabel("Algorithm Options:"));
        String[] algorithms = {"A*", "Dijkstra", "BFS", "DFS"};

        JList<String> algorithmList = new JList<>(algorithms);
        algorithmList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // For single selection
        algorithmList.setSelectedIndex(0); // Default selection: "A*"

        JScrollPane scrollPane = new JScrollPane(algorithmList);
        scrollPane.setPreferredSize(new Dimension(180, 160));
        algorithmsPanel.add(scrollPane);

        algorithmsPanel.setBackground(Color.CYAN);

        // add to settings 
        settingsPanel.add(algorithmsPanel);




        // TIME TOOK FOR ALGORITHM
        JPanel timeTookPanel = new JPanel();
        timeTookPanel.add(new JLabel("Time Took (ms)"));
        timeTookPanel.setLayout(new FlowLayout());

        JPanel timeVariable = new JPanel();
        float timeTook = 0;
        timeVariable.add(new JLabel(timeTook+" ms"));
        timeTookPanel.add(timeVariable);
        timeTookPanel.setMaximumSize(new Dimension(settingsPanelWidth, 50));

        //add to settings
        settingsPanel.add(timeTookPanel);




        // START NODE
        JPanel startNodePanel = new JPanel();
        startNodePanel.setLayout(new FlowLayout());
        startNodePanel.add(new JLabel("Start Node Coordinates"));

        JPanel startNodeIput = new JPanel();
        startNodeIput.setSize(new Dimension(settingsPanelWidth-10, 20));
        JTextField xCoord = new JTextField(5); // 5 columns wide
        JTextField yCoord = new JTextField(5);

        // START NODE INPUT
        startNodeIput.add(new JLabel("["));
        startNodeIput.add(xCoord);
        startNodeIput.add(new JLabel(","));
        startNodeIput.add(yCoord);
        startNodeIput.add(new JLabel("]"));

        startNodePanel.setMaximumSize(new Dimension(settingsPanelWidth, 100));
        startNodePanel.setBackground(Color.CYAN);
        startNodePanel.add(startNodeIput);

        // set start node button
        JButton confirmStartNode = new JButton();
        confirmStartNode.add(new JLabel("Set Start Coordinates"));
        confirmStartNode.addActionListener(l -> {
            try{
                int x = Integer.parseInt(xCoord.getText().trim());
                int y = Integer.parseInt(yCoord.getText().trim());

                StartNodeMethod(x,y);
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Invalid number set for start node", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Not valid number inputted");
            }

        });
        startNodePanel.add(confirmStartNode);

        //add to settings
        settingsPanel.add(startNodePanel);


        /* 


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


        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(gridSize, gridSize)); // 10x10 grid
        // Add squares to the grid panel
        for (int i = 0; i < gridSize*gridSize; i++) {
            JPanel square = new JPanel();
            square.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // Set thin border
            gridPanel.add(square);
        }



        */




        
        
        
        /**********************************/
        /*          ADD GRID PANEL        */
        JPanel gridPanel = new JPanel();
        //gridPanel.setLayout(new BoxLayout(gridPanel, BoxLayout.Y_AXIS));
        gridPanel.setBackground(gridPanelBkg);
        gridPanel.setPreferredSize(new Dimension(gridPanelWidth, screenHeight));
        
        

        
        
        
        /*          ADD MAIN PANELS TO FRAME        */
        frame.add(settingsPanel, BorderLayout.WEST);
        frame.add(gridPanel, BorderLayout.EAST);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    // Static method to run the selected algorithm
    private void runAlgorithm(String algorithm) {
        // Run the selected algorithm
        System.out.println("Running " + algorithm);
    }

    private static void StartNodeMethod(int x, int y){
        System.out.println("Start Node coordinate :: "+x+", "+y);
    }
}