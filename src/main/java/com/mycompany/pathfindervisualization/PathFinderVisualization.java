package com.mycompany.pathfindervisualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

import java.util.*;


public class PathFinderVisualization {


    private int screenWidth = 1300;
    private int screenHeight = 800;
    private int settingsPanelWidth = 200;
    private int gridPanelWidth = screenWidth-settingsPanelWidth-1;
    private int gridSize = 10;

    private int[] defaultStartNode = {0,1}; 
    private int[] defaultEndNode = {4,3}; 
    private JLabel statusLabel = new JLabel();

    private GridGraph gridGraph = new GridGraph(gridSize, gridSize);
    private JPanel gridPanel = new JPanel();

    //HOLDS ALL THE COLORS USED
    private Color settingsPanelBkg =  Color.BLUE;
    private Color gridPanelBkg = Color.GRAY;
    private Color startNodeColor = Color.green;// new Color(0x000000);
    private Color endNodeColor = Color.magenta;//new Color(0x000000);

    PathFinderVisualization() {

        JFrame frame = new JFrame("Path Finder Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(screenWidth, screenHeight);
        //colors the frame, testing purposes
        frame.getContentPane().setBackground(Color.RED);
        frame.setLayout(new BorderLayout());
        
        /**********************************************************************************************************************/
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




        // TIME TOOK FOR ALGORITHM
        JPanel timeTookPanel = new JPanel();
        timeTookPanel.add(new JLabel("Time Took (ms)"));
        timeTookPanel.setLayout(new FlowLayout());

        JPanel timeVariable = new JPanel();
        float timeTook = 0;
        timeVariable.add(new JLabel(timeTook+" ms"));
        timeTookPanel.add(timeVariable);
        timeTookPanel.setMaximumSize(new Dimension(settingsPanelWidth, 50));



        /*
        WILL NEED TO CONFIGURE THIS, NOW THAT GRID CELL WORKS WITH ONCLICK, 

        SETTINGS PANEL NEEDS HAVE OPTIONS ONLY FOR
        ALGORITHM PICKED
        SPEED OF VISUALIZATION
        CLEAR BOARD
        RUN, PAUSE ALGORITHM


        FOR START AND TARGET NODES
        WE WILL HAVE DEFAULT CELLS ALREADY SELECTED 

        START COORD/TARGET COORD:
        KEEP THE SAME INPUT AND BUTTON LAYOUT,
        EXCEPTS ITS PREFILLED WITH DEFAULT COORD
        USER CAN CHANGE ITS COORD BY CHANGING THE INPUT

        (MIGHT SEE IF USER CAN DRAG THE CELL, MIGHT BE TOO MUCH FOR NOW)

         */
        // START NODE
        JPanel startNodePanel = new JPanel();
        startNodePanel.setLayout(new FlowLayout());
        startNodePanel.add(new JLabel("Start Coordinates"));

        //LITTLE ICON TO SHOW ITS COLOR
        JPanel startNodeIcon = new JPanel();
        startNodeIcon.setBackground(startNodeColor);
        startNodeIcon.setSize(30, 30);
        startNodePanel.add(startNodeIcon);

        JPanel startNodeIput = new JPanel();
        startNodeIput.setSize(new Dimension(settingsPanelWidth-10, 20));
        JTextField StartXCoord = new JTextField(5); // 5 columns wide
        JTextField StartYCoord = new JTextField(5);

        // START NODE INPUT
        startNodeIput.add(new JLabel("["));
        startNodeIput.add(StartXCoord);
        startNodeIput.add(new JLabel(","));
        startNodeIput.add(StartYCoord);
        startNodeIput.add(new JLabel("]"));

        startNodePanel.setMaximumSize(new Dimension(settingsPanelWidth, 100));
        startNodePanel.setBackground(Color.CYAN);
        startNodePanel.add(startNodeIput);

        // set start node button
        JButton confirmStartNode = new JButton();
        confirmStartNode.add(new JLabel("Set Start Coordinates"));
        confirmStartNode.addActionListener(l -> {
            try{
                int x = Integer.parseInt(StartXCoord.getText().trim());
                int y = Integer.parseInt(StartYCoord.getText().trim());

                setStartNodeJPanel(x, y);
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Invalid number(s) set for start coordinates", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Not valid number inputted");
            }

        });
        startNodePanel.add(confirmStartNode);





        // END NODE
        JPanel endNodePanel = new JPanel();
        endNodePanel.setLayout(new FlowLayout());
        endNodePanel.add(new JLabel("Target Coordinates"));

        //LITTLE ICON TO SHOW ITS COLOR
        JPanel endNodeIcon = new JPanel();
        endNodeIcon.setBackground(endNodeColor);
        endNodeIcon.setSize(30, 30);
        endNodePanel.add(endNodeIcon);

        JPanel endNodeInput = new JPanel();
        endNodeInput.setSize(new Dimension(settingsPanelWidth-10, 20));
        JTextField EndXCoord = new JTextField(5); // 5 columns wide
        JTextField EndYCoord = new JTextField(5);

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
            try{
                int x = Integer.parseInt(EndXCoord.getText().trim());
                int y = Integer.parseInt(EndYCoord.getText().trim());

                setEndNodeJPanel(x, y);
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Invalid number(s) set for target coordinates", "Error", JOptionPane.ERROR_MESSAGE);
                System.out.println("Not valid number inputted");
            }

        });
        endNodePanel.add(confirmEndNode);




        // ACTION BUTTONS (RUN, PAUSE, RESET)
        JPanel actionButtons = new JPanel();
        actionButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        actionButtons.setMaximumSize(new Dimension(settingsPanelWidth, 100));


        // run button
        JButton runButton = new JButton("Run");
        // when run button is clicked run the selected algorithm 
        runButton.addActionListener(l -> {
            String selectedAlgorithm = algorithmList.getSelectedValue();
            statusLabel.setText("Running "+selectedAlgorithm+" Algorithm...");
            runAlgorithm(selectedAlgorithm);
        });

        // pause button
        JButton pauseButton = new JButton("Pause");
        // when pause button is clicked
        pauseButton.addActionListener(l -> {
            System.out.println("Pause button clicked");
        });
        
        //reset the grid
        JButton resetButton = new JButton("Reset");
        // when reset button is clicked
        resetButton.addActionListener(l -> {
            System.out.println("Reset button clicked");
            statusLabel.setText("Resetting board...");
            gridPanel.removeAll();
            gridPanel.repaint();
            setGridBoard();
            statusLabel.setText("Board has been reset");
        });

        // add buttons to panel
        actionButtons.setBackground(Color.CYAN);
        actionButtons.add(runButton);
        actionButtons.add(pauseButton);
        actionButtons.add(resetButton);



        // INFORMATION LABEL 
        JPanel informationTextPanel = new JPanel();
        informationTextPanel.setLayout(new BorderLayout());
        
        statusLabel.setText("Selected Algorithm... A*");
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
        settingsPanel.add(Box.createVerticalStrut(20));
        settingsPanel.add(actionButtons);
        settingsPanel.add(Box.createVerticalStrut(20));
        settingsPanel.add(timeTookPanel);
        settingsPanel.add(Box.createVerticalStrut(20));
        settingsPanel.add(informationTextPanel);

        
        
        
        /**********************************************************************************************************************/
        /*          ADD GRID PANEL        */
        gridGraph.printGrid();
        gridGraph.setStartNode(defaultStartNode[0], defaultStartNode[1]);
        gridGraph.setEndNode(defaultEndNode[0], defaultEndNode[1]);


        // Add squares to the grid panel
        setGridBoard(); 
        
        
        /*          ADD MAIN PANELS TO FRAME        */
        frame.add(settingsPanel, BorderLayout.WEST);
        frame.add(gridPanel, BorderLayout.CENTER);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);

    }

    public static void main(String[] args) {
        new PathFinderVisualization();
        
    }

    public void setGridBoard(){
        gridPanel.setBackground(gridPanelBkg);
        gridPanel.setPreferredSize(new Dimension(gridPanelWidth, screenHeight));
        gridPanel.setLayout(new GridLayout(gridSize, gridSize,0,0)); // 10x10 grid

        for (int i = 0; i < gridSize*gridSize; ++i) {
            JPanel cell = new JPanel();

            cell.setLayout(new BorderLayout());
            cell.setBackground(Color.white); // Set cell background color

            //add coordinates to cell
            int[] coordinates = getRelativeCoordinates(i, gridSize); 
            cell.add(new JLabel(coordinates[0]+","+coordinates[1]), BorderLayout.NORTH);



            Node currentNode = gridGraph.getNode(coordinates[0], coordinates[1]);
            if(currentNode.isStartNode()){
                System.out.println("Start node to jpanel "+gridGraph.getStartNode().toString());
                cell.setBackground(startNodeColor);
            }
            if(currentNode.isEndNode()){
                System.out.println("End node to panel "+gridGraph.getEndNode().toString());
                cell.setBackground(endNodeColor);
            }


            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to distinguish cells
            cell.setPreferredSize(new Dimension(10, 10));

            // when user clicks on cell
            cell.addMouseListener(createMouseAdapter(i, gridPanel, gridSize, gridGraph));

            gridPanel.add(cell); // Add cell to the grid
        }

    }


    // Extracted method to handle mouse click on a cell
    private MouseAdapter createMouseAdapter(int cellIndex, JPanel gridPanel, int gridSize, GridGraph gridGraph) {
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int[] coordinates = getRelativeCoordinates(cellIndex, gridSize);
                System.out.println("Cell "+coordinates[0]+","+coordinates[1]);


                gridPanel.getComponent(cellIndex).setBackground(Color.GREEN);

                /*
                //GETTING THE CELLS AROUND THE CELL
                //BUG: X+-1 OR Y+-1 COULD BE OUT OF RANGE
                Component cellLeft = getComponentAt(coordinates[0], coordinates[1]-1, gridPanel, gridSize);
                Component cellRight = getComponentAt(coordinates[0], coordinates[1]+1, gridPanel, gridSize);
                Component cellUp = getComponentAt(coordinates[0]+1, coordinates[1], gridPanel, gridSize);
                Component cellDown = getComponentAt(coordinates[0]-1, coordinates[1], gridPanel, gridSize);

                cellLeft.setBackground(Color.RED);
                cellRight.setBackground(Color.RED);
                cellUp.setBackground(Color.RED);
                cellDown.setBackground(Color.RED);
                 */

            }
        };
    }

    // Method to get the component at (x, y) in the grid
    private Component getComponentAt(int x, int y) {
        int index = (x * gridSize) + y;

        return gridPanel.getComponent(index);
    }


    // Static method to run the selected algorithm
    private void runAlgorithm(String algorithm) {
        // Run the selected algorithm
        if(algorithm.equals("BFS")){
            System.out.println("Running BFS");
            startBFS();
        }
        else if(algorithm.equals("DFS")){
            System.out.println("Running DFS");
        }

        
    }

    private void setStartNodeJPanel(int x, int y){
        Component currentStartNodeCell = getComponentAt(gridGraph.getStartNode().row, gridGraph.getStartNode().col);
        currentStartNodeCell.setBackground(Color.WHITE);

        gridGraph.setStartNode(x, y);
        currentStartNodeCell = getComponentAt(gridGraph.getStartNode().row, gridGraph.getStartNode().col);
        currentStartNodeCell.setBackground(startNodeColor);

        System.out.println("Start Node coordinate :: "+x+", "+y);
    }

    private void setEndNodeJPanel(int x, int y){
        Component currentEndNodeCell = getComponentAt(gridGraph.getEndNode().row, gridGraph.getEndNode().col);
        currentEndNodeCell.setBackground(Color.WHITE);

        gridGraph.setEndNode(x, y);
        currentEndNodeCell = getComponentAt(gridGraph.getEndNode().row, gridGraph.getEndNode().col);
        currentEndNodeCell.setBackground(endNodeColor);

        System.out.println("End Node coordinate :: "+x+", "+y);
    }

    private int[] getRelativeCoordinates(int cellIndex, int gridSize){
        int row = cellIndex / gridSize;
        int col = cellIndex % gridSize;
        return new int[]{row, col}; 
    }


    private void runAStarAlgorithm(){
        //RUN A* ALGORITHM
        Node startNode = gridGraph.getStartNode();
        Node endNode = gridGraph.getEndNode();

        startNode.neighbors.forEach(neighbor -> {
            System.out.println(neighbor.toString());

            try {
                Component cell = getComponentAt(neighbor.row, neighbor.col);
                Thread.sleep(500); // 500 milliseconds delay
                cell.setBackground(Color.RED);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        
        endNode.neighbors.forEach(neighbor -> {
            System.out.println(neighbor.toString());

            try {
                Component cell = getComponentAt(neighbor.row, neighbor.col);
                Thread.sleep(500); // 500 milliseconds delay
                cell.setBackground(Color.gray);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    private void startBFS() {
        Queue<Node> queue = new LinkedList<>();
        boolean[][] visited = new boolean[gridSize][gridSize];
        Node[][] previous = new Node[gridSize][gridSize];
        
        Node startNode = gridGraph.getStartNode();
        Node endNode = gridGraph.getEndNode();
        queue.add(startNode);
        visited[startNode.col][startNode.row] = true;

        while (!queue.isEmpty()) {
            Node current = queue.poll();
            
            if (current.equals(endNode)) {
                reconstructPath(previous);
                return;
            }
            
            for (Node neighbor : current.neighbors) {
                if (!visited[neighbor.col][neighbor.row]) {
                    visited[neighbor.col][neighbor.row] = true;
                    previous[neighbor.col][neighbor.row] = current;
                    queue.add(neighbor);

                    // Color the cell to visualize BFS in progress
                    Component cell = getComponentAt(neighbor.row, neighbor.col);

                    try { 
                        cell.setBackground(Color.BLUE);
                        Thread.sleep(100); 
                    } catch (InterruptedException e) { 
                        e.printStackTrace(); 
                    }

                }
            }
        }
    }

    private void reconstructPath(Node[][] previous) {
        Node startNode = gridGraph.getStartNode();
        Node endNode = gridGraph.getEndNode();

        Node step = endNode;
        while (step != null && !step.equals(startNode)) {
            Component cell = getComponentAt(step.row, step.col);
            cell.setBackground(Color.PINK);
            step = previous[step.col][step.row];
        }
    }


}