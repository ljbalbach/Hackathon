import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Icon;
import java.net.*;
import java.io.*;

import javax.swing.ImageIcon;

 public class Puzzles implements ActionListener {
    // <<<MineSweeper Puzzle>>>>>

    private static boolean started = false;
    private static JPanel board = new JPanel(new GridLayout(10, 10));
    private static JButton[][] grid = new JButton[10][10];
    private static JFrame myFrame = new JFrame();
    private static Random rand = new Random();
    private static Boolean[][] mines = new Boolean[10][10];
    private static boolean mineHIT3 = false;
    private static int mineHITCounter = 0;
    URL gif = getClass().getResource("gameOver.gif");
    //if true therefore mine


    public void createGrid() {
        // Create a window

        myFrame.setVisible(true);

        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 10; column++) {
                grid[row][column] = new JButton();
                grid[row][column].setBorder(BorderFactory.createLineBorder(Color.black));
                grid[row][column].setPreferredSize(new Dimension(90,90));
                grid[row][column].addActionListener(this);
                board.add(grid[row][column]);
            }
        }

        myFrame.add(board);
        myFrame.pack();
        

    }

    public void runner() {
        Puzzles board = new Puzzles();
        //have to initialise board
        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 10; column++) {
                mines[row][column] = false;
            }
        }
        board.createGrid();
    }

    
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        int adjacentCounter = 0;
        //source is now the button that's been clicked
        if (source instanceof JButton) {
            source = (JButton)source;
            int rowSelect = 1;
            int colSelect = 1;
            if (started == false){
                started = true;
                //distributes bombs around. 35 as 100 squares
                for (int i =0; i <10; i++){
                    int random1 = rand.nextInt(10);
                    int random2 = rand.nextInt(10);
                    mines[random1][random2] = true;
                }
                for (int row = 0; row < 10; row++) {
                    for (int column = 0; column < 10; column++) {
                        if (source.equals(grid[row][column])){
                        mines[row][column] = false;
                        }
                    }
                }
            }
            //gathers row and column as selected button to 
            //do operations on
            for (int row = 0; row < 10; row++) {
                for (int column = 0; column < 10; column++) {
                    if (source.equals(grid[row][column])){
                        rowSelect = row;
                        colSelect = column;
                        grid[rowSelect][colSelect].setBackground(Color.PINK);
                    }
                }
            }
            //now mines are distributed and first selected isnt one
            //after first selected looking if it has mines
          
            
            for (int row = 0; row < 10; row++) {
                for (int column = 0; column < 10; column++) {
                    //looking for every adjacent square and if they have a mine
                    if (((row == rowSelect - 1 && column == colSelect - 1)|| (row == rowSelect - 1 && column == colSelect)|| 
                    (row == rowSelect - 1 && column == colSelect + 1)|| (row == rowSelect && column == colSelect - 1)||
                    (row == rowSelect && column == colSelect + 1)|| (row == rowSelect + 1 && column == colSelect - 1)||
                    (row == rowSelect + 1 && column == colSelect)|| (row == rowSelect + 1 && column == colSelect + 1))) {
                        if (mines[row][column] == true){                   
                        adjacentCounter = adjacentCounter + 1;
                        }
                    }
                }
            }
            if (mines[rowSelect][colSelect] == true){
                grid[rowSelect][colSelect].setText("MINE!");
                mineHITCounter = mineHITCounter + 1;
                if (mineHITCounter == 3){
                    mineHIT3 = true;
                }
            }
            else{
            String adjacentString = Integer.toString(adjacentCounter);
            grid[rowSelect][colSelect].setText(adjacentString);
            }
        } 
        detectWin();
    }
  
    public void detectWin(){
        boolean win = true;
        for (int row = 0; row < 10; row++) {
            for (int column = 0; column < 10; column++) {
                if (mines[row][column] == false && grid[row][column].getBackground().equals(Color.PINK)){               
                    win = false;
                    break;
                }
            }
        }
        if (mineHIT3 == true){
            gameOver();
        }
        if (win){
            myFrame.dispose();
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }    

    public static void reset(){
        started = false;
        board = new JPanel(new GridLayout(10, 10));
        grid = new JButton[10][10];
        myFrame = new JFrame();
        rand = new Random();
        mines = new Boolean[10][10];
        mineHIT3 = false;
    }
    
    public void gameOver(){
        myFrame.remove(board);
        myFrame.setPreferredSize(new Dimension(900,900));
        JPanel gameOver = new JPanel();
        JLabel imagePlace = new JLabel();
        
        
        Icon playingGif = new ImageIcon(gif);
        imagePlace.setIcon(playingGif);

        gameOver.add(imagePlace);
        myFrame.add(gameOver);



    }

}