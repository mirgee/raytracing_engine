/**
 * Created by MiroslavK on 21/04/16.
 */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
/*
    Class that stores and updates the information about player position and camera view.
*/

public class Player implements KeyListener {
    /*
        xPlayer, yPlayer:           Coordinates of the player.
        xDirection, yDirection:     Vector in the direction of the sceenPlane (orthogonal to it)
        xScreenPlane,yScreenPlane:  Vector normal to direction, pointing right, on the screen plane
     */
    private double xPlayer, yPlayer, xDirection, yDirection, xScreenPlane, yScreenPlane;
    /*
        l, r, u, d:                 Booleans to keep track of what key was pressed or released.
     */
    private boolean l, r, f, b;
    private final double MOVE_SPEED = .08;
    private final double ROTATION_SPEED = .045;

    public Player(double x, double y, double xd, double yd, double xp, double yp)
    {
        this.xPlayer = x;
        this.yPlayer = y;
        this.xDirection = xd;
        this.yDirection = yd;
        xScreenPlane = xp;
        yScreenPlane = yp;
    }


    @Override
    public void keyTyped(KeyEvent key) {

    }

    /*
    If a key was pressed, check if it was left, right, up or down arrow and set
    l, r, u, d to true.
    */
    @Override
    public void keyPressed(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_LEFT))
            l = true;
        if((key.getKeyCode() == KeyEvent.VK_RIGHT))
            r = true;
        if((key.getKeyCode() == KeyEvent.VK_UP))
            f = true;
        if((key.getKeyCode() == KeyEvent.VK_DOWN))
            b = true;
    }

    /*
    If a key was released, check if it was left, right, up or down arrow and set
    l, r, u, d to false.
    */
    @Override
    public void keyReleased(KeyEvent key) {
        if((key.getKeyCode() == KeyEvent.VK_LEFT))
            l = false;
        if((key.getKeyCode() == KeyEvent.VK_RIGHT))
            r = false;
        if((key.getKeyCode() == KeyEvent.VK_UP))
            f = false;
        if((key.getKeyCode() == KeyEvent.VK_DOWN))
            b = false;
    }

    /*

     */
    public void update(int[][] map) {
        if(f) {
            /*
                If going forward, update x and y position by adding a MOVE_SPEED fraction of direction,
                if we didn't hit a wall.
            */
            if(map[(int)(xPlayer + xDirection * MOVE_SPEED)][(int) yPlayer] == 0) {
                xPlayer += xDirection * MOVE_SPEED;
            }
            if(map[(int) xPlayer][(int)(yPlayer + yDirection * MOVE_SPEED)] == 0)
                yPlayer += yDirection * MOVE_SPEED;
            System.out.println("Px: " + xPlayer + ", Py: " + yPlayer);
            System.out.println("xDirection " + xDirection + ", yDirection: " + yDirection);
        }
        /*
                If going forward, update x and y position by subtracting a MOVE_SPEED fraction of direction,
                if we didn't hit a wall.
        */
        if(b) {
            if(map[(int)(xPlayer - xDirection * MOVE_SPEED)][(int) yPlayer] == 0)
                xPlayer -= xDirection *MOVE_SPEED;
            if(map[(int) xPlayer][(int)(yPlayer - yDirection * MOVE_SPEED)]==0)
                yPlayer -= yDirection *MOVE_SPEED;
            System.out.println("Px: " + xPlayer + ", Py: " + yPlayer);
            System.out.println("xDirection " + xDirection + ", yDirection: " + yDirection);
        }
        /*
                Rotate player and plane to the right by multiplying their vectors by rotation matrix.
         */
        if(r) {
            double oldxDir= xDirection;
            xDirection = xDirection *Math.cos(-ROTATION_SPEED) - yDirection *Math.sin(-ROTATION_SPEED);
            yDirection =oldxDir*Math.sin(-ROTATION_SPEED) + yDirection *Math.cos(-ROTATION_SPEED);
            double oldxPlane = xScreenPlane;
            xScreenPlane = xScreenPlane *Math.cos(-ROTATION_SPEED) - yScreenPlane *Math.sin(-ROTATION_SPEED);
            yScreenPlane =oldxPlane*Math.sin(-ROTATION_SPEED) + yScreenPlane *Math.cos(-ROTATION_SPEED);
        }
        /*
                Rotate player and plane to the left multiplying their vectors by rotation matrix.
         */
        if(l) {
            double oldxDir= xDirection;
            xDirection = xDirection *Math.cos(ROTATION_SPEED) - yDirection *Math.sin(ROTATION_SPEED);
            yDirection =oldxDir*Math.sin(ROTATION_SPEED) + yDirection *Math.cos(ROTATION_SPEED);
            double oldxPlane = xScreenPlane;
            xScreenPlane = xScreenPlane *Math.cos(ROTATION_SPEED) - yScreenPlane *Math.sin(ROTATION_SPEED);
            yScreenPlane =oldxPlane*Math.sin(ROTATION_SPEED) + yScreenPlane *Math.cos(ROTATION_SPEED);
        }
    }

    public double getxPlayer() {
        return this.xPlayer;
    }

    public double getyPlayer() {
        return this.yPlayer;
    }

    public double getxDirection() {
        return this.xDirection;
    }

    public double getyDirection() {
        return this.yDirection;
    }

    public double getxScreenPlane() {
        return this.xScreenPlane;
    }

    public double getyScreenPlane() {
        return this.yScreenPlane;
    }
}
