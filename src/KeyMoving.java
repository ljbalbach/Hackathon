import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;

public class KeyMoving extends JPanel {
   private static final int MAX_WIDTH = ( int )Toolkit.getDefaultToolkit().getScreenSize().getWidth();
   private static final int MAX_HEIGHT = ( int )Toolkit.getDefaultToolkit().getScreenSize().getHeight();
   private static final Rectangle SCREEN_DIMENSIONS = new Rectangle( 0, 0, MAX_WIDTH, MAX_HEIGHT );
  
   private BufferedImage bi;
   private BufferedImage[] male1 = new BufferedImage[9];
   private boolean[] keys = new boolean[4];
   JFrame frame;
   int myX = 0;
   int myY = 0;
   Canvas canvas;
   BufferStrategy bufferStrategy;
   boolean running = true;
   int count = 0;
  
  
   public KeyMoving(String filePath) {
        try {
            bi = ImageIO.read(new File(filePath));
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
               if(evt.getKeyCode() == KeyEvent.VK_ESCAPE)
                  System.exit(0);
           }
       });
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       frame.pack();
       frame.setResizable(false);
       frame.setVisible(true);
       canvas.createBufferStrategy(2);
       bufferStrategy = canvas.getBufferStrategy();
       canvas.requestFocus();

       Paint();
   }
   @Override
   public Dimension getPreferredSize() {
       return new Dimension(bi.getWidth(), bi.getHeight());
   }
  
   public void Paint() {
       Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
      
       Paint(g);
       bufferStrategy.show();
   }
  
   protected void Paint(Graphics2D g2d) {
       canvas.update(g2d);
       g2d.drawImage( ( (Image)bi ).getScaledInstance( 1920, 1080, Image.SCALE_SMOOTH ), myX, myY, this);

       g2d.dispose();
   }
}

