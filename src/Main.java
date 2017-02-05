/**
 * Created by MiroslavK on 21/04/16.
 */
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import javax.swing.JFrame;

/*
    Main class for running the application.
*/
public class Main extends JFrame implements Runnable {
    public Player player;
    public Screen screen;
    public ArrayList<Textures> textures;
    private static final long serialVersionUID = 1L;
    public int mapWidth = 15;
    public int mapHeight = 15;
    private Thread thread;
    private boolean running;
    private BufferedImage image;
    public int[] pixels;
    public static int[][] map =
            {
                    {1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 2, 2, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2},
                    {1, 1, 1, 1, 1, 1, 1, 4, 4, 4, 4, 4, 4, 4, 4}
            };


    public Main() {
        player = new Player(1.5, 1.5, 0, 1, 0.66, 0);
        textures = new ArrayList<Textures>();
        textures.add(Textures.wood);
        textures.add(Textures.brick);
        textures.add(Textures.bluestone);
        textures.add(Textures.stone);
        addKeyListener(player);
        thread = new Thread(this);
        image = new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
        // Spojuje data mezi pixels a image - kdyz se zmeni data v pixels, projevi se
        // v image.
        pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
        screen = new Screen(map, mapWidth, mapHeight, textures, 640, 480);
        setSize(640, 480);
        setResizable(false);
        setTitle("3D Engine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBackground(Color.black);
        setLocationRelativeTo(null);
        setVisible(true);
        start();
    }

    private synchronized void start() {
        running = true;
        thread.start();
    }
    public synchronized void stop() {
        running = false;
        try {
            thread.join();
        } catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void render() {
        // We will use a buffer strategy 3 for better performance of the Graphics object
        BufferStrategy bs = getBufferStrategy();
        if(bs == null) {
            createBufferStrategy(3);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
        bs.show();
    }

    public void run() {
        long lastTime = System.nanoTime();
        final double period = 1000000000.0 / 60.0; // 1/60 seconds in nanoseconds
        double delta = 0;
        // Move focus to the window
        requestFocus();
        while(running) {
            // Current system time in nanoseconds
            long now = System.nanoTime();
            delta = delta + ((now-lastTime) / period);
            lastTime = now;
            // Update each 1/60 seconds
            while (delta >= 1)
            {
                // Update screen and player.
                screen.update(player, pixels);
                player.update(map);
                delta--;
            }
            render();
        }
    }

    public static void main(String [] args) {

        Main game = new Main();
    }

}