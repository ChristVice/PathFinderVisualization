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


public class PathFinderVisualization {

    private GridGraph graph;
    private int screenWidth = 1300;
    private int screenHeight = 800;
    private int gridSize = 50;

    PathFinderVisualization() {

        Color settingsPanelBkg =  Color.BLUE;
        Color gridPanelBkg = Color.GREEN;
        int settingsPanelWidth = 200;
        int gridPanelWidth = screenWidth-settingsPanelWidth-1;

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

                StartNodeMethod(x,y);
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

                EndNodeMethod(x,y);
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
        });

        // add buttons to panel
        actionButtons.setBackground(Color.CYAN);
        actionButtons.add(runButton);
        actionButtons.add(pauseButton);
        actionButtons.add(resetButton);



        // INFORMATION LABEL 
        JPanel informationTextPanel = new JPanel();
        informationTextPanel.setLayout(new BorderLayout());
        JLabel statusLabel = new JLabel();
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
        JPanel gridPanel = new JPanel();
        gridPanel.setBackground(gridPanelBkg);
        gridPanel.setPreferredSize(new Dimension(gridPanelWidth, screenHeight));
        gridPanel.setLayout(new GridLayout(gridSize, gridSize,0,0)); // 10x10 grid

        // Add squares to the grid panel
        for (int i = 0; i < gridSize*gridSize; i++) {
            JPanel cell = new JPanel();

            //add coordinates to cell
            cell.setLayout(new BorderLayout());
            int[] coordinates = getRelativeCoordinates(i, gridSize); 
            //cell.add(new JLabel(coordinates[0]+","+coordinates[1]), BorderLayout.NORTH);

            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Add border to distinguish cells
            cell.setBackground(Color.white); // Set cell background color
            cell.setPreferredSize(new Dimension(10, 10));

            cell.addMouseListener(createMouseAdapter(i, gridPanel, gridSize));

            gridPanel.add(cell); // Add cell to the grid

        }
        
        
        
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

    // Extracted method to handle mouse click on a cell
    private MouseAdapter createMouseAdapter(int cellIndex, JPanel gridPanel, int gridSize) {
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
    private Component getComponentAt(int x, int y, JPanel gridPanel, int gridSize) {
        int index = (x * gridSize) + y;

        return gridPanel.getComponent(index);
    }


    // Static method to run the selected algorithm
    private void runAlgorithm(String algorithm) {
        // Run the selected algorithm
        System.out.println("Running " + algorithm);
    }

    private void StartNodeMethod(int x, int y){
        System.out.println("Start Node coordinate :: "+x+", "+y);
    }

    private void EndNodeMethod(int x, int y){
        System.out.println("End Node coordinate :: "+x+", "+y);
    }

    private int[] getRelativeCoordinates(int cellIndex, int gridSize){
        int row = cellIndex / gridSize;
        int col = cellIndex % gridSize;
        return new int[]{row, col}; 
    }

}