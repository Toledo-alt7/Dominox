package br.maua.dominox;

import java.awt.*;
import javax.swing.JButton;
import javax.swing.JFrame;
 
public class WindowPage {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
 
    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }
 
    JButton button;
    pane.setLayout(new GridBagLayout());
    GridBagConstraints c = new GridBagConstraints();
    if (shouldFill) {
    //natural height, maximum width
    c.fill = GridBagConstraints.HORIZONTAL;
    }
 
    button = new JButton("Button 1");
    
    c.fill = GridBagConstraints.HORIZONTAL;
    c.gridx = 1;
    c.gridy = 0;
    c.ipady = 10;
    c.insets = new Insets(0,150,20,150);  //top padding

    pane.add(button, c);
 
    button = new JButton("Button 2");
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.5;
    c.gridx = 1;
    c.gridy = 1;
    c.ipady = 10;
    c.insets = new Insets(0,150,20,150);  //top padding

    pane.add(button, c);
 
    button = new JButton("Button 3");
    c.fill = GridBagConstraints.HORIZONTAL;
    c.weightx = 0.5;
    c.gridx = 1;
    c.insets = new Insets(0,150,20,150);  //top padding
    c.ipady = 10;      //make this component tall

    c.gridy = 2;
    c.anchor = GridBagConstraints.PAGE_END;
    pane.add(button, c);
 
    button = new JButton("4");
    c.fill = GridBagConstraints.HORIZONTAL;
    c.ipady = 10;      //make this component tall
    c.weightx = 0.0;
    c.gridwidth = 1;
    c.insets = new Insets(0,150,20,150);  //top padding

    c.gridx = 1;
    c.gridy = 3;
    pane.add(button, c);
 
    button = new JButton("Chablau");
    c.fill = GridBagConstraints.HORIZONTAL;
    c.ipady = 0;       //reset to default
    c.weighty = 0.0;   //request any extra vertical space
    c.anchor = GridBagConstraints.PAGE_END; //bottom of space
    c.insets = new Insets(0,150,20,150);  //top padding
    c.gridx = 1;       //aligned with button 2
    c.gridwidth = 0;   //2 columns wide
    c.gridy = 4;       //third row
    c.ipady = 10;
    pane.add(button, c);

    }
 
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Dominox");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        //Set up the content pane.
        addComponentsToPane(frame.getContentPane());
 
        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setSize(500, 500);
        frame.setMinimumSize(new Dimension(450, 400));
    }
 
    // public static void main(String[] args) {
    //     //Schedule a job for the event-dispatching thread:
    //     //creating and showing this application's GUI.
    //     javax.swing.SwingUtilities.invokeLater(new Runnable() {
    //         public void run() {
    //             createAndShowGUI();
    //         }
    //     });
    // }
}