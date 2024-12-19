package com.mycompany.pathfindervisualization;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;


public class PathFinderVisualization {

    private final int screenWidth = 1300;
    private final int screenHeight = 800;
    private final int settingsPanelWidth = 200;
    private final int gridPanelWidth = screenWidth-settingsPanelWidth-1;

    private final int searchingTimerDelay = 50; // in ms
    private final int foundPathTimerDelay = 60; //in ms

    private final int gridSize = 10;

    private final JFrame frame;
    private final GridGraph gridGraph;
    private final JPanel gridPanel;

    //HOLDS ALL THE COLORS USED
    private final Color settingsPanelColor = new Color(0xEEEEEE);
    private final Color gridPanelBkg = Color.WHITE;
    private final Color startNodeColor = Color.GREEN;// new Color(0x000000);
    private final Color endNodeColor = Color.RED;//new Color(0x000000);
    private final Color pathColor = Color.PINK;
    private final Color visitedColor = Color.BLUE;
    //private final Color obstacleColor = new Color(0xC9C9C9);
    private final Color obstacleColor = Color.BLACK;
    
    private JLabel statusLabel = new JLabel();

    private final String[] algorithms = {"BFS", "DFS", "Dijkstra's", "A*"};
    private List<Node> pathSteps = new LinkedList<>();
    private List<Node> foundPathSteps = new LinkedList<>();
    private ArrayList<Integer> obstacleCells = new ArrayList<Integer>();


    PathFinderVisualization() {

        gridGraph = new GridGraph(gridSize, gridSize);
        gridPanel = new JPanel();
        

        frame = new JFrame("Path Finder Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight);
        //colors the frame, testing purposes
        frame.getContentPane().setBackground(Color.RED);
        frame.setLayout(new BorderLayout());
        
        /**********************************************************************************************************************/
        /*          ADD SETTINGS PANEL        */
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setPreferredSize(new Dimension(settingsPanelWidth, screenHeight));
        settingsPanel.setBackground(settingsPanelColor);

        
        //          ADDING COMPONENTS TO SETTINGS PANEL
        /*  ALGORITHMS ****************************************************** */
        JPanel algorithmsPanel = new JPanel();
        algorithmsPanel.setMaximumSize(new Dimension(settingsPanelWidth, 200));
        algorithmsPanel.setBackground(settingsPanelColor);
        algorithmsPanel.setLayout(new FlowLayout());
        algorithmsPanel.add(new JLabel("Algorithm Options:"));

        JList<String> algorithmList = new JList<>(algorithms);
        algorithmList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // For single selection
        algorithmList.setSelectedIndex(0); // Default selection to first element

        JScrollPane scrollPane = new JScrollPane(algorithmList);
        scrollPane.setPreferredSize(new Dimension(180, 160));
        algorithmsPanel.add(scrollPane);


        /*  START NODE ****************************************************** */
        JPanel startNodePanel = new JPanel();
        startNodePanel.setLayout(new FlowLayout());
        startNodePanel.add(new JLabel("Start Coordinates"));

        //LITTLE ICON TO SHOW ITS COLOR
        JPanel startNodeIcon = new JPanel();
        startNodeIcon.setBackground(startNodeColor);
        startNodeIcon.setSize(30, 30);
        startNodePanel.add(startNodeIcon);

        JPanel startNodeInput = new JPanel();
        startNodeInput.setSize(new Dimension(settingsPanelWidth-10, 20));
        startNodeInput.setBackground(settingsPanelColor);
        JTextField StartXCoord = new JTextField(5); // 5 columns wide
        StartXCoord.setText(gridGraph.getStartNode().row+"");
        JTextField StartYCoord = new JTextField(5);
        StartYCoord.setText(gridGraph.getStartNode().col+"");

        // START NODE INPUT
        startNodeInput.add(new JLabel("["));
        startNodeInput.add(StartXCoord);
        startNodeInput.add(new JLabel(","));
        startNodeInput.add(StartYCoord);
        startNodeInput.add(new JLabel("]"));

        startNodePanel.setMaximumSize(new Dimension(settingsPanelWidth, 100));
        startNodePanel.setBackground(settingsPanelColor);
        startNodePanel.add(startNodeInput);

        // set start node button
        JButton confirmStartNode = new JButton();
        confirmStartNode.add(new JLabel("Set Start Coordinates"));
        confirmStartNode.addActionListener(l -> {
            ResetColoredCells();
            statusLabel.setText("Starting coordinates set");

            if(isValidCoordinates(StartXCoord, StartYCoord)){
                int x = Integer.parseInt(StartXCoord.getText().trim());
                int y = Integer.parseInt(StartYCoord.getText().trim());

                setStartNodeJPanel(x, y);
            }

        });
        startNodePanel.add(confirmStartNode);


        /*  END NODE ****************************************************** */
        JPanel endNodePanel = new JPanel();
        endNodePanel.setLayout(new FlowLayout());
        endNodePanel.setBackground(settingsPanelColor);
        endNodePanel.add(new JLabel("Target Coordinates"));

        //LITTLE ICON TO SHOW ITS COLOR
        JPanel endNodeIcon = new JPanel();
        endNodeIcon.setBackground(endNodeColor);
        endNodeIcon.setSize(30, 30);
        endNodePanel.add(endNodeIcon);

        JPanel endNodeInput = new JPanel();
        endNodeInput.setSize(new Dimension(settingsPanelWidth-10, 20));
        endNodeInput.setBackground(settingsPanelColor);

        JTextField EndXCoord = new JTextField(5); // 5 columns wide
        EndXCoord.setText(gridGraph.getEndNode().row+"");
        JTextField EndYCoord = new JTextField(5);
        EndYCoord.setText(gridGraph.getEndNode().col+"");

        // END NODE INPUT 
        endNodeInput.add(new JLabel("["));
        endNodeInput.add(EndXCoord);
        endNodeInput.add(new JLabel(","));
        endNodeInput.add(EndYCoord);
        endNodeInput.add(new JLabel("]"));

        endNodePanel.setMaximumSize(new Dimension(settingsPanelWidth, 100));
        endNodePanel.add(endNodeInput);

        // set start node button
        JButton confirmEndNode = new JButton();
        confirmEndNode.add(new JLabel("Set Target Coordinates"));
        confirmEndNode.addActionListener(l -> {
            ResetColoredCells();
            statusLabel.setText("Target coordinates set");

            if(isValidCoordinates(EndXCoord, EndYCoord)){
                int x = Integer.parseInt(EndXCoord.getText().trim());
                int y = Integer.parseInt(EndYCoord.getText().trim());

                setEndNodeJPanel(x, y);
            }

        });
        endNodePanel.add(confirmEndNode);


        /*  ACTION BUTTONS (RUN, PAUSE, RESET) ****************************************************** */
        JPanel actionButtons = new JPanel();
        actionButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionButtons.setBackground(settingsPanelColor);
        actionButtons.setMaximumSize(new Dimension(settingsPanelWidth, 150));


        //          RUN BUTTON 
        JButton runAlgorithmButton = new JButton("Run Algorithm");
        // when run button is clicked run the selected algorithm 
        runAlgorithmButton.addActionListener(l -> {
            ResetColoredCells();
            String selectedAlgorithm = algorithmList.getSelectedValue();
            statusLabel.setText("Running "+selectedAlgorithm+" Algorithm...");
            runAlgorithm(selectedAlgorithm);
        });

        
        //          CLEAR PATH BUTTON 
        JButton clearPathButton = new JButton("Clear Path");
        // when reset button is clicked
        clearPathButton.addActionListener(l -> {
            ResetColoredCells();
        });


        //          CLEAR WALLS BUTTON 
        JButton clearWallsButton = new JButton("Clear Walls");
        // when reset button is clicked
        clearWallsButton.addActionListener(l -> {
            ClearWalls();
        });

        // add buttons to panel
        actionButtons.add(runAlgorithmButton);
        actionButtons.add(clearPathButton);
        actionButtons.add(clearWallsButton);


        //          INFORMATION LABEL 
        JPanel informationTextPanel = new JPanel();
        informationTextPanel.setLayout(new BorderLayout(10, 100));
        informationTextPanel.setBackground(settingsPanelColor);
        
        statusLabel.setText("Selected Algorithm... BFS");
        informationTextPanel.setMaximumSize(new Dimension(settingsPanelWidth, 100));

        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        informationTextPanel.add(statusLabel, BorderLayout.SOUTH);

        // Add ListSelectionListener to update the statusLabel when an item is selected
        algorithmList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) { // Ensure it only updates on final selection
                String selectedAlgorithm = algorithmList.getSelectedValue();
                if (selectedAlgorithm != null) {
                    statusLabel.setText("Selected Algorithm... " + selectedAlgorithm);
                }
            }
        });


        /*      ADD COMPONENTS TO SETTINGS PANEL     */
        settingsPanel.add(algorithmsPanel);
        settingsPanel.add(Box.createVerticalStrut(20));
        settingsPanel.add(startNodePanel);
        settingsPanel.add(Box.createVerticalStrut(20));
        settingsPanel.add(endNodePanel);
        settingsPanel.add(Box.createVerticalStrut(10));
        settingsPanel.add(actionButtons);
        settingsPanel.add(Box.createVerticalStrut(60));
        settingsPanel.add(informationTextPanel);

        
        /**********************************************************************************************************************/
        /*          ADD GRID PANEL        */
        // Add squares to the grid panel
        setGridBoard(); 
        
        /*          ADD MAIN PANELS TO FRAME        */
        frame.add(settingsPanel, BorderLayout.WEST);
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

    private boolean isValidCoordinates(JTextField xCoordField, JTextField yCoordField) {
        try {
            int x = Integer.parseInt(xCoordField.getText().trim());
            int y = Integer.parseInt(yCoordField.getText().trim());

            if (x >= gridSize || y >= gridSize) {
                throw new IllegalArgumentException("Coordinates must be within the grid size.");
            }

            return true;

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid number(s) entered for coordinates", "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Not valid number inputted");
            return false;
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println(e.getMessage());
            return false;
        }
    }

    public void setGridBoard(){
        gridPanel.setBackground(gridPanelBkg);
        gridPanel.setPreferredSize(new Dimension(gridPanelWidth, screenHeight));
        gridPanel.setLayout(new GridLayout(gridSize, gridSize,0,0)); // 10x10 grid

        Node startNode = gridGraph.getStartNode();
        Node endNode = gridGraph.getEndNode();

        //start filling the grid with panels, each with coordinates, color start and end nodes, then add to gridPanel
        for(int i = 0; i < gridSize*gridSize; ++i) {
            JPanel cell = new JPanel();

            cell.setLayout(new BorderLayout());
            cell.setBackground(Color.white); // Set cell background color

            //add coordinates to cell
            int[] coordinates = getRelativeCoordinates(i, gridSize); 
            JLabel coordLabel = new JLabel(coordinates[0]+","+coordinates[1]);
            coordLabel.setBackground(Color.RED);
            //cell.add(new JLabel(coordinates[0]+","+coordinates[1]), BorderLayout.NORTH);
            cell.add(coordLabel, BorderLayout.NORTH);

            Node currentNode = gridGraph.getNode(coordinates[0], coordinates[1]);

            //color in the obstacles
            if(currentNode.isPassable){
                cell.setBackground(Color.WHITE);
            }
            else{
                cell.setBackground(obstacleColor);
            }   

            if(currentNode.equals(startNode)){
                cell.setBackground(startNodeColor);
            }
            else if(currentNode.equals(endNode)){
                cell.setBackground(endNodeColor);
            }

            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to distinguish cells
            cell.setPreferredSize(new Dimension(10, 10));


            // what to do when cell is clicked
            int cellIndex = i;
            cell.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent l) {
                    int[] coords = new int[]{ currentNode.row, currentNode.col};
                    statusLabel.setText("Cell "+coords[0]+","+coords[1]+" clicked");


                    //if the cell clicked is not the start nor end node, free to color cell
                    if(!(currentNode.equals(startNode) || currentNode.equals(endNode))){
                        
                        if(!currentNode.isPassable){
                            gridPanel.getComponent(cellIndex).setBackground(Color.WHITE);
                        }
                        else{
                            //if now an obstacle ... 
                            obstacleCells.add(cellIndex);
                            gridPanel.getComponent(cellIndex).setBackground(obstacleColor);
                        }

                        //set the node to oppisite of what isPassable is
                        gridGraph.setIsPassable(coords[0], coords[1], !gridGraph.getIsPassable(coords[0], coords[1]));
                        cell.repaint();
                    }

                }
            }); // Add mouse listener to each cell

            gridPanel.add(cell); // Add cell to the grid
        }

    }

    private void ResetColoredCells(){
        System.out.println("Reset button clicked");
        statusLabel.setText("Resetting colored cells...");

        for(Node node : pathSteps){
            if(!node.equals(gridGraph.getStartNode()) && !node.equals(gridGraph.getEndNode())){
                Component cell = getComponentAt(node.row, node.col);
                cell.setBackground(Color.WHITE);
            }
        }
        for(Node node : foundPathSteps){
            if(!node.equals(gridGraph.getStartNode()) && !node.equals(gridGraph.getEndNode())){
                Component cell = getComponentAt(node.row, node.col);
                cell.setBackground(Color.WHITE);
            }
        }
        pathSteps.clear();
        foundPathSteps.clear(); 

        statusLabel.setText("Board has been reset");

    }

    private void ClearWalls(){
        System.out.println("Clear walls button clicked");
        statusLabel.setText("Clearing walls...");

        for(int i : obstacleCells){
            int[] coords = getRelativeCoordinates(i, gridSize);
            gridGraph.setIsPassable(coords[0], coords[1], true);
            gridPanel.getComponent(i).setBackground(Color.WHITE);
        }

        obstacleCells.clear();
        statusLabel.setText("Walls have been cleared");
    }


    // Static method to run the selected algorithm
    private void runAlgorithm(String algorithm) {
        PathFindingAlgorithms pathfindingAlgorithms = new PathFindingAlgorithms(gridGraph, gridSize);

        switch (algorithm) {
            case "BFS":
                System.out.println("Running BFS");
                pathfindingAlgorithms.RunChosenAlgorithm(0);
                break;

            case "DFS":
                System.out.println("Running DFS");
                pathfindingAlgorithms.RunChosenAlgorithm(1);
                break;  
        
            default:
                break;
        }

        this.pathSteps = pathfindingAlgorithms.getPathSteps();
        this.foundPathSteps = pathfindingAlgorithms.getFoundPathSteps();

        AnimateSearchPath();
        
    }

    /**
     * Sets the start node on the JPanel at the specified coordinates (x, y).
     * This method updates the background color of the previous start node cell to white,
     * sets the new start node in the grid graph, and updates the background color of the
     * new start node cell to the designated start node color.
     *
     * @param x the x-coordinate of the new start node
     * @param y the y-coordinate of the new start node
     */
    private void setStartNodeJPanel(int x, int y){
        Node startNode = gridGraph.getStartNode();

        Component currentStartNodeCell = getComponentAt(startNode.row, startNode.col);
        currentStartNodeCell.setBackground(Color.WHITE);
    
        gridGraph.setStartNode(x, y);
        currentStartNodeCell = getComponentAt(x, y);
        currentStartNodeCell.setBackground(startNodeColor);
    }
    
    /**
     * Sets the end node in the grid graph to the specified coordinates (x, y).
     * Updates the background color of the previous end node cell to white and
     * sets the background color of the new end node cell to the designated end node color.
     *
     * @param x the x-coordinate (row) of the new end node
     * @param y the y-coordinate (column) of the new end node
     */
    private void setEndNodeJPanel(int x, int y){
        Node endNode = gridGraph.getEndNode();
    
        Component currentEndNodeCell = getComponentAt(endNode.row, endNode.col);
        currentEndNodeCell.setBackground(Color.WHITE);
    
        gridGraph.setEndNode(x, y);
        currentEndNodeCell = getComponentAt(x, y);
        currentEndNodeCell.setBackground(endNodeColor);
    }


    /**
     * Animates the pathfinding steps on the visualization grid.
     */
    private void AnimateSearchPath(){
        Node startNode = gridGraph.getStartNode();

        Timer timer = new Timer(searchingTimerDelay, new ActionListener() {
            int stepIndex = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (stepIndex < pathSteps.size()-1) { 
                    Node stepNode = pathSteps.get(stepIndex);

                    //in case path adds start node to its list
                    if(!stepNode.equals(startNode)){
                        Component cell = getComponentAt(stepNode.row, stepNode.col);
                        cell.setBackground(visitedColor);
                    }

                    stepIndex++;
                } else {
                    ((Timer) e.getSource()).stop(); // Stop the timer after all steps are displayed
                    AnimateFoundPath();
                }
            }
        });
        timer.start();

    }

    /**
     * Animates the coloring of the found path on the grid.
     * 
     * This method uses a Timer to sequentially color each cell in the found path,
     * starting from the end node and moving backwards to the start node. The end
     * node is colored red at the end of the animation.
     * 
     */
    private void AnimateFoundPath(){

        // Start a Timer to animate the coloring of each cell in the foundPathSteps list
        Timer timer = new Timer(foundPathTimerDelay, new ActionListener() {
            int pathIndex = foundPathSteps.size()-1;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (pathIndex >= 1) {
                    Node pathNode = foundPathSteps.get(pathIndex);
                    getComponentAt(pathNode.row, pathNode.col).setBackground(pathColor);
                    pathIndex--;
                } else {
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();

    }

    /**
     * Retrieves the component located at the specified grid coordinates.
     *
     * @param x the x-coordinate of the grid position
     * @param y the y-coordinate of the grid position
     * @return the component at the specified (x, y) position in the grid
     */
    private Component getComponentAt(int x, int y) {
        int index = (x * gridSize) + y;

        return gridPanel.getComponent(index);
    }

    /**
     * Calculates the relative coordinates (row and column) of a cell in a grid.
     *
     * @param cellIndex the index of the cell in the grid
     * @param gridSize the size of the grid (number of cells per row/column)
     * @return an array containing the row and column of the cell, respectively
     */
    private int[] getRelativeCoordinates(int cellIndex, int gridSize){
        int row = cellIndex / gridSize;
        int col = cellIndex % gridSize;
        return new int[]{row, col};
    }


    public static void main(String[] args) {
        new PathFinderVisualization();
    }

}