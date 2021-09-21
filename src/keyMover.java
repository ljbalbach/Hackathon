import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.sound.sampled.*;

public class keyMover extends JPanel {
    private static final int MAX_WIDTH = ( int )Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int MAX_HEIGHT = ( int )Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final Rectangle SCREEN_DIMENSIONS = new Rectangle( 0, 0, MAX_WIDTH, MAX_HEIGHT );

    private BufferedImage[] backgrounds = new BufferedImage[9];
    private BufferedImage[] skr = new BufferedImage[9];
    private BufferedImage[] skl = new BufferedImage[9];
    private BufferedImage[] sku = new BufferedImage[9];
    private BufferedImage[] skd = new BufferedImage[9];
    private boolean[][] keys = new boolean[4][2];
    JFrame frame;
    double ratio = MAX_WIDTH/1000;
    int increment = 3;
    int myX = 0;
    int myY = 0;
    int x = MAX_WIDTH/8;
    int y = MAX_HEIGHT/2;
    Canvas canvas;
    BufferStrategy bufferStrategy;
    boolean running = true;
    int count = 0;
    int backgroundCount = 0;
    int multiplier = MAX_HEIGHT/250;
    boolean hasRunPuzzle;
    boolean hasRunFight;
    boolean doubleTap;
    
    
    public keyMover() {
        try {
            for( int i = 0; i < 9; i++ ) {
                backgrounds[i] = ImageIO.read(new File("background" + (i+1) + ".png" ));
            }
            for( int i = 0; i < 9; i++ ) {
                skr[i] = ImageIO.read(new File("SKR" + (i+1) + ".png"));
                skl[i] = ImageIO.read(new File("SKL" + (i+1) + ".png"));
                sku[i] = ImageIO.read(new File("SKU" + (i+1) + ".png"));
                skd[i] = ImageIO.read(new File("SKD" + (i+1) + ".png"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        frame = new JFrame("Basic Game");
        frame.setBounds( SCREEN_DIMENSIONS );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setUndecorated(true);
        JPanel panel = (JPanel) frame.getContentPane();
        panel.setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
        panel.setLayout(null);
        canvas = new Canvas();
        canvas.setBounds(0, 0, MAX_WIDTH, MAX_HEIGHT);
        canvas.setIgnoreRepaint(true);
        panel.add(canvas);
        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent evt) {
                moveIt(evt);
            }
            public void keyReleased(KeyEvent evt) {
                stopIt(evt);
            }
        });
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
    }
    public static void main(String[] args) {
        keyMover km = new keyMover();
        km.playAud();
        Thread lucasThread1 = new Thread() {
            @Override
            public void run() {
                while(km.running == true) {
                    km.Paint();
                    try{
                        Thread.sleep(35);
                    }
                    catch(InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        lucasThread1.start();
    }

    public void playAud(){
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("soundtrack.wav").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch(Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(backgrounds[0].getWidth(), backgrounds[0].getHeight());
    }
    
    public void Paint() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        update();
        Paint(g);
        bufferStrategy.show();
    }
    
    protected void Paint(Graphics2D g2d) {
        if(myX + (int)(backgrounds[0].getWidth()/ratio) <= MAX_WIDTH)
            keys[0][1] = true;
        else
            keys[0][1] = false;
        
        if(myX >= 0)
            keys[2][1] = true;
        else
            keys[2][1] = false;
        
        if(myY + (int)(backgrounds[0].getHeight()/ratio) <= MAX_HEIGHT)
            keys[3][1] = true;
        else
            keys[3][1] = false;
        
        if(myY >= 0)
            keys[1][1] = true;
        else
            keys[1][1] = false;

            
        if(x >= MAX_WIDTH && backgroundCount < 9){
            backgroundCount++;
            myX = 0;
            myY = 0;
            x = 50;
            y = MAX_HEIGHT/2;
        }

        
        if( backgroundCount == 5 && x >= MAX_WIDTH/2-10 && x <= MAX_WIDTH/2+10 && !hasRunPuzzle){
            hasRunPuzzle = true;
            stopper();
            Puzzles puzzle = new Puzzles();
            puzzle.runner();
        }
        if( backgroundCount == 8 && x >= MAX_WIDTH/2-10 && x <= MAX_WIDTH/2+10 && !hasRunFight){
            hasRunFight = true;
            stopper();
            Fight gameBoard = new Fight();
            //starts game
            try{
                gameBoard.startGame();
            } catch(IOException e){
                System.out.println(e.getMessage());
            }
        }
            //( (Image)backgrounds[backgroundCount] ).getScaledInstance( (int)(backgrounds[backgroundCount].getWidth()), (int)(backgrounds[backgroundCount].getHeight()), Image.SCALE_SMOOTH )
        canvas.update(g2d);
        g2d.drawImage( backgrounds[backgroundCount], myX, myY, (int)(backgrounds[backgroundCount].getWidth()*ratio), (int)(backgrounds[backgroundCount].getHeight()*ratio), this);
        
        if((!keys[0][0] && !keys[1][0] && !keys[2][0] && !keys[3][0]) || (keys[0][0] && !keys[1][0] && keys[2][0] && !keys[3][0]) || (!keys[0][0] && keys[1][0] && !keys[2][0] && keys[3][0]))
            g2d.drawImage(skr[0], x, y, skr[0].getWidth()*multiplier, skr[0].getHeight()*multiplier, this );
        else if(keys[0][0] && !keys[1][0] && !keys[2][0] && !keys[3][0])
            g2d.drawImage(skr[count], x, y, skr[count].getWidth() * multiplier, skr[count].getHeight() * multiplier, this);
        else if(keys[0][0] && keys[1][0] && !keys[2][0] && !keys[3][0])
            g2d.drawImage(skr[count], x, y, skr[count].getWidth() * multiplier, skr[count].getHeight() * multiplier, this);
        else if(!keys[0][0] && keys[1][0] && !keys[2][0] && !keys[3][0])
            g2d.drawImage(sku[count], x, y, sku[count].getWidth() * multiplier, sku[count].getHeight() * multiplier, this);
        else if(!keys[0][0] && keys[1][0] && keys[2][0] && !keys[3][0])
            g2d.drawImage(skl[count], x, y, skl[count].getWidth() * multiplier, skl[count].getHeight() * multiplier, this);
        else if(!keys[0][0] && !keys[1][0] && keys[2][0] && !keys[3][0])
            g2d.drawImage(skl[count], x, y, skl[count].getWidth() * multiplier, skl[count].getHeight() * multiplier, this);
        else if(!keys[0][0] && !keys[1][0] && keys[2][0] && keys[3][0])
            g2d.drawImage(skl[count], x, y, skl[count].getWidth() * multiplier, skl[count].getHeight() * multiplier, this);
        else if(!keys[0][0] && !keys[1][0] && !keys[2][0] && keys[3][0])
            g2d.drawImage(skd[count], x, y, skd[count].getWidth() * multiplier, skd[count].getHeight() * multiplier, this);
        else if(keys[0][0] && !keys[1][0] && !keys[2][0] && keys[3][0])
            g2d.drawImage(skr[count], x, y, skr[count].getWidth() * multiplier, skr[count].getHeight() * multiplier, this);
        if( count == 8 )
            count = 0;
        else if(doubleTap)
            count++;
        doubleTap = !doubleTap;
        g2d.dispose();
    }
    public void stopper(){
        for(int i = 0; i< keys.length;i++)
            keys[i][0] = false;
    }
    public void moveIt(KeyEvent evt) {
    
        if( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
            System.exit( 0 );
        if( evt.getKeyCode() == KeyEvent.VK_RIGHT ) {
            keys[0][0] = true;
        }
        if( evt.getKeyCode() == KeyEvent.VK_LEFT ) {
            keys[2][0] = true;
        }
        if( evt.getKeyCode() == KeyEvent.VK_UP ) {
            keys[1][0] = true;
        }
        if( evt.getKeyCode() == KeyEvent.VK_DOWN ) {
            keys[3][0] = true;
        }
        update();
    }
    public void stopIt(KeyEvent evt) {
    
        if( evt.getKeyCode() == KeyEvent.VK_RIGHT ) {
            keys[0][0] = false;
        }
        if( evt.getKeyCode() == KeyEvent.VK_LEFT ) {
            keys[2][0] = false;
        }
        if( evt.getKeyCode() == KeyEvent.VK_UP ) {
            keys[1][0] = false;
        }
        if( evt.getKeyCode() == KeyEvent.VK_DOWN ) {
            keys[3][0] = false;
        }
    }
    private void update()
    {
        if( keys[0][0] && !keys[0][1] )
            myX -= increment;
        if( keys[0][0] && keys[0][1] ){
            if( backgroundCount == 6 && x >= MAX_WIDTH );
            else
                x += increment;
        }
        
        if( keys[2][0] && !keys[2][1] )
            myX += increment;
        if( keys[2][0] && keys[2][1] && x > 0)
            x -= increment;
        
        if( keys[1][0] && !keys[1][1] )
            myY += increment;
        if( keys[1][0] && keys[1][1] && y > 0)
            y -= increment;
        
        if( keys[3][0] && !keys[3][1] )
            myY -= increment;
        if( keys[3][0] && keys[3][1] && (y + skr[0].getHeight()*multiplier) < MAX_HEIGHT )
            y += increment;
    }
}