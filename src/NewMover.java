import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

public class NewMover extends JPanel {
    private static final int MAX_WIDTH = ( int ) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final int MAX_HEIGHT = ( int )Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    private static final Rectangle SCREEN_DIMENSIONS = new Rectangle( 0, 0, MAX_WIDTH, MAX_HEIGHT );
    
    private BufferedImage bi;
    private BufferedImage[] male1 = new BufferedImage[9];
    private boolean[] keys = new boolean[4];
    JFrame frame;
    int myX = 0;
    int myY = 0;
    boolean running = true;
    int count = 0;
    
    
    public NewMover() {
        try {
            bi = ImageIO.read(new File("Act1Scene1Final.png" ));
            for( int i = 0; i < 9; i++ ) {
                male1[i] = ImageIO.read(new File("Male2" + (i+1) + ".png"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        frame = new JFrame("Basic Game");
        frame.setBounds( SCREEN_DIMENSIONS );
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setUndecorated(true);
        //GraphicsPanel panel = new GraphicsPanel();
//        JPanel panel = (JPanel) frame.getContentPane();
        
        JPanel panel = new JPanel() {
            public void paintComponent(Graphics g) {
                System.out.println("Hi");
                super.paintComponent(g);
                painter(g);
            }
        };
        
        panel.setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
        panel.setLayout(null);
        frame.add(panel);
        frame.addKeyListener(new KeyAdapter() {
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
    }
    public static void main(String[] args) {
        NewMover nm = new NewMover();
        nm.start();
    }
    
    public void start(){
        while(running) {
            frame.repaint();
        }
    }
    
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(bi.getWidth(), bi.getHeight());
    }
    
    public void painter(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawImage( ( (Image)bi ).getScaledInstance( 2000, MAX_HEIGHT, Image.SCALE_SMOOTH ), myX, myY, this);
        if(!keys[0] && !keys[1] && !keys[2] && !keys[3])
            g2d.drawImage(male1[0], 200, MAX_HEIGHT/2, male1[0].getWidth()*3, male1[0].getHeight()*3, this );
        else
            g2d.drawImage(male1[count], 200, MAX_HEIGHT / 2, male1[count].getWidth() * 3, male1[count].getHeight() * 3, this);
        if( count == 8 )
            count = 0;
        else
            count++;
        g2d.dispose();
    }
    
    public void moveIt(KeyEvent evt) {
        
        if( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
            System.exit( 0 );
        if( evt.getKeyCode() == KeyEvent.VK_RIGHT ) {
            keys[0] = true;
        }
        if( evt.getKeyCode() == KeyEvent.VK_LEFT ) {
            keys[2] = true;
        }
        if( evt.getKeyCode() == KeyEvent.VK_UP ) {
            keys[1] = true;
        }
        if( evt.getKeyCode() == KeyEvent.VK_DOWN ) {
            keys[3] = true;
        }
        update();
    }
    public void stopIt(KeyEvent evt) {
        
        if( evt.getKeyCode() == KeyEvent.VK_RIGHT ) {
            keys[0] = false;
        }
        if( evt.getKeyCode() == KeyEvent.VK_LEFT ) {
            keys[2] = false;
        }
        if( evt.getKeyCode() == KeyEvent.VK_UP ) {
            keys[1] = false;
        }
        if( evt.getKeyCode() == KeyEvent.VK_DOWN ) {
            keys[3] = false;
        }
    }
    private void update()
    {
        if( keys[0] )
            myX -= 3;
        
        if( keys[2] )
            myX += 3;
        
        if( keys[1] )
            myY += 3;
        
        if( keys[3] )
            myY -= 3;
    }
}