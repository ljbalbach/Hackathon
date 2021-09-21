import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Fight extends JPanel implements ActionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Frame for the game
     */
    private JFrame myFrame;

    // Game buttons

    boolean gameOver = false;

    /**
     * Play button
     */
    final private JButton FIGHT_BUTTON = new JButton("FIGHT");

    /**
     * Pause button
     */
    final private JButton RUN_BUTTON = new JButton("RUN");

    /**
     * Revive mode button
     */
    final private JButton BAG_BUTTON = new JButton("BAG");

    /**
     * Age limit mode button
     */
    final private JButton POK_BUTTON = new JButton("POKEMON");

    /**
     * This is the player health
     */
    private int player_1_health = 100;

    /**
     * This is the player health
     */
    private int player_2_health = 100;

    /**
     * health bar for player 1
     */
    private JLabel player_1_healthBar = new JLabel();

    /**
     * health bar for player 1
     */
    private JLabel player_2_healthBar = new JLabel();

    /**
     * answers
     */
    private JButton[][] answers = new JButton[5][4];

    /**
     * Correct answers
     */
    private String[] correctAnswers = new String[5];

    /**
     * question
     */
    private JLabel[] question = new JLabel[5];

    /**
     * font
     */
    private Font customFont;

    /**
     * 
     */
    GraphicsEnvironment ge;

    /**
     * this is the current question index of array
     */
    private int currentQuestionIndex = 0;

    /**
     * 
     */
    private int currentAnswerIndex = 0;

    private JLabel picLabel;

    /**
     * This starts the game, by adding the listeners and creating the frame
     */
    public void startGame() throws IOException {
        try {
            fileReader();
        } catch (FontFormatException e) {
            System.out.println("invalid font file");
            e.printStackTrace();
        }
        gameFrame();
        addListeners();
    }
    
    public static void main(String[] args){
        Fight fight = new Fight();
        try {
            fight.fileReader();
            fight.gameFrame();
            fight.addListeners();
        } catch (FontFormatException | IOException e) {
            System.out.println("invalid font file");
            e.printStackTrace();
        }
        
    }

    /**
     * This adds the listeners to the buttons and list choosers
     */
    public void addListeners() {
        FIGHT_BUTTON.addActionListener(this);
        RUN_BUTTON.addActionListener(this);
        RUN_BUTTON.addActionListener(this);
        POK_BUTTON.addActionListener(this);

    }

    public void setFont() {
        // create the font

        try {
            // create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf")).deriveFont(12f);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }

    public void fileReader() throws FontFormatException, IOException {
        BufferedReader reader = null;

        int j = 0;

        Font font = Font.createFont(Font.TRUETYPE_FONT, new File("font.ttf"));
        font = font.deriveFont(Font.PLAIN, 20);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(font);

        try {
            reader = new BufferedReader(new FileReader("Riddles.txt"));
        } catch (FileNotFoundException e1) {
            System.out.println("Invalid file");
            e1.printStackTrace();
        }

        String nextLine = null;

        BufferedImage wood = ImageIO.read(new File("wood.jpeg"));
        Image scaledImage = null;

        try {
            while ((nextLine = reader.readLine()) != null) {
                if (!nextLine.isEmpty()) {
                    // scaledImage = wood.getScaledInstance(520, 80, Image.SCALE_DEFAULT);
                    question[j] = new JLabel("<html>" + nextLine + "</html>");
                    question[j].setFont(font);
                    question[j].setHorizontalAlignment(JLabel.CENTER);
                    question[j].setVerticalAlignment(JLabel.CENTER);
                    // question[j].setIcon(new ImageIcon("wood.jpeg"));
                    question[j].setPreferredSize(new Dimension(wood.getWidth(), wood.getHeight()));
                    question[j].setHorizontalTextPosition(JButton.CENTER);
                    question[j].setVerticalTextPosition(JButton.CENTER);
                    question[j].setForeground(Color.BLACK);
                    question[j].setOpaque(true);
                    j++;
                }

            }
        } catch (IOException e) {
            try {
                reader.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        j = 0;

        try {
            reader = new BufferedReader(new FileReader("Answers.txt"));
        } catch (FileNotFoundException e1) {
            System.out.println("Invalid file");
            e1.printStackTrace();
        }

        nextLine = null;

        int i = 0;

        try {
            while ((nextLine = reader.readLine()) != null) {
                if (!nextLine.isEmpty()) {
                    if (j == 4) {
                        j = 0;
                        i++;
                    }
                    answers[i][j] = new JButton("<html>" + nextLine + "</html>");
                    answers[i][j].setIcon(new ImageIcon("wood.jpeg"));
                    answers[i][j].setForeground(Color.WHITE);
                    answers[i][j].setPreferredSize(new Dimension(wood.getWidth(), wood.getHeight()));
                    answers[i][j].setHorizontalTextPosition(JButton.CENTER);
                    answers[i][j].setVerticalTextPosition(JButton.CENTER);
                    // answers[i][j].setPreferredSize(new Dimension(100,100));
                    answers[i][j].addActionListener(this);
                    answers[i][j].setFont(font);
                    j++;
                }

            }
        } catch (IOException e) {
            try {
                reader.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        correctAnswers[0] = "C: Adriana";
        correctAnswers[1] = "B: He was playing a board game";
        correctAnswers[2] = "A: 99999999";
        correctAnswers[3] = "D: They aren't human";
        correctAnswers[4] = "A: Stop imagining you are in that room";

    }

    /**
     * This sets up the frame and panels for the game
     */
    public void gameFrame() throws IOException {

        // creates a new frame
        myFrame = new JFrame();

        myFrame.setLayout(new BorderLayout());

        // closes the frame upon pressing x
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // main panels

        JPanel main = new JPanel(new BorderLayout());
        JPanel life = new JPanel(new GridLayout(1, 2));
        JPanel game = new JPanel(new GridLayout(1, 1));
        JPanel interaction = new JPanel(new GridLayout(2, 1));

        // life bars
        player_1_healthBar.setPreferredSize(new Dimension(200, 20));
        setHealthBar(1);
        life.add(player_1_healthBar);
        player_2_healthBar.setPreferredSize(new Dimension(200, 20));
        setHealthBar(2);
        life.add(player_2_healthBar);

        // game

        BufferedImage myPicture = ImageIO.read(new File("background.png"));
        picLabel = new JLabel(new ImageIcon(myPicture));

        game.add(picLabel);

        main.add(life, BorderLayout.NORTH);
        main.add(game, BorderLayout.CENTER);
        main.add(interaction, BorderLayout.SOUTH);

        // add the player buttons

        interaction.add(question[currentQuestionIndex]);

        JPanel answerPanel = new JPanel(new GridLayout(2, 2));

        for (int i = 0; i < answers[0].length; i++) {
            answerPanel.add(answers[currentAnswerIndex][i]);
        }

        currentQuestionIndex++;
        currentAnswerIndex++;

        interaction.add(answerPanel);

        // add main to frame
        myFrame.add(main, BorderLayout.CENTER);

        // makes the frame visible
        myFrame.setVisible(true);

        // doesn't allow the frame to be resized
        myFrame.setResizable(false);

        // compacts the frame
        myFrame.pack();

    }

    public void setHealthBar(int player) {
        BufferedImage healthPicture = null;
        Image scaledImage = null;

        if (player == 1) {
            try {
                healthPicture = ImageIO.read(new File("healthBars/main/" + player_1_health + ".png"));
                scaledImage = healthPicture.getScaledInstance(220, 80, Image.SCALE_DEFAULT);
            } catch (IOException e) {
                System.out.println("invalid image file");
            }
            player_1_healthBar = new JLabel(new ImageIcon(scaledImage));
        } else if (player == 2) {
            try {
                healthPicture = ImageIO.read(new File("healthBars/enemy/" + player_2_health + ".png"));
                scaledImage = healthPicture.getScaledInstance(220, 80, Image.SCALE_DEFAULT);
            } catch (IOException e) {
                System.out.println("invalid image file");
            }
            player_2_healthBar = new JLabel(new ImageIcon(scaledImage));
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        boolean userWinsRound = false;

        if (source instanceof JButton && player_2_health != 0 && !gameOver){


            JButton button = (JButton) source;


            if (button.getText().equals("<html>" + correctAnswers[currentQuestionIndex - 1] + "</html>")) {

                BufferedImage healthPicture = null;
                Image scaledImage = null;

                player_2_health = player_2_health - 20;

                if (player_2_health <= 0) {
                    gameOver = true;
                    userWinsRound = true;
                    try {
                        healthPicture = ImageIO.read(new File("healthBars/enemy/" + 0 + ".png"));
                        scaledImage = healthPicture.getScaledInstance(220, 80, Image.SCALE_DEFAULT);
                    } catch (IOException ex) {
                        System.out.println("enemy invalid image file");
                    }
                    player_2_healthBar.setIcon(new ImageIcon(scaledImage));
    
                }
                else {
                    try {
                        healthPicture = ImageIO.read(new File("healthBars/enemy/" + player_2_health + ".png"));
                        scaledImage = healthPicture.getScaledInstance(220, 80, Image.SCALE_DEFAULT);
                    } catch (IOException ex) {
                        System.out.println("enemy invalid image file");
                    }
                    player_2_healthBar.setIcon(new ImageIcon(scaledImage));
    
                    userWinsRound = true;
                }

            } else {
                BufferedImage healthPicture = null;
                Image scaledImage = null;

                player_1_health = player_1_health - 50;

                if (player_1_health <= 0) {
                    gameOver = true;
                    try {
                        healthPicture = ImageIO.read(new File("healthBars/main/" + 0 + ".png"));
                        scaledImage = healthPicture.getScaledInstance(220, 80, Image.SCALE_DEFAULT);
                    } catch (IOException ex) {
                        System.out.println("enemy invalid image file");
                    }
                    player_1_healthBar.setIcon(new ImageIcon(scaledImage));
    
                }
                else {
                    try {
                        healthPicture = ImageIO.read(new File("healthBars/main/" + player_1_health + ".png"));
                        scaledImage = healthPicture.getScaledInstance(220, 80, Image.SCALE_DEFAULT);
                    } catch (IOException ex) {
                        System.out.println("main image file");
                    }
                    player_1_healthBar.setIcon(new ImageIcon(scaledImage));
                }
            }

            if (currentQuestionIndex == 4) {
                gameOver = true;
                if (player_1_health > player_2_health) {
                    userWinsRound = true;
                }
            }

            URL gif = null;
            Icon playingGif = null;
            if (!gameOver) {
                if (userWinsRound) {
                    gif = getClass().getResource("win.gif");
                    playingGif = new ImageIcon(gif);
                    picLabel.setIcon(playingGif);
                } else {
                    gif = getClass().getResource("lose.gif");
                    playingGif = new ImageIcon(gif);
                    picLabel.setIcon(playingGif);
                }
                
                question[0].setText(question[currentQuestionIndex].getText());
    
                for (int i = 0; i < answers[0].length; i++) {
                    answers[0][i].setText(answers[currentAnswerIndex][i].getText());
                }
    
                currentAnswerIndex++;
                currentQuestionIndex++;
            }
            else {
                if (userWinsRound) {
                    gif = getClass().getResource("winned.gif");
                    playingGif = new ImageIcon(gif);
                    picLabel.setIcon(playingGif);
                } else {
                    gif = getClass().getResource("gameOver.gif");
                    playingGif = new ImageIcon(gif);
                    picLabel.setIcon(playingGif);
                }

    
            }

            

        }
    }
}


