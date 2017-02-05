import java.util.ArrayList;
import java.awt.Color;

/*
    The class used for rendering, that is updating the pixels array based on the information from Screen.
*/
public class Screen {
    private int[][] map;
    private int mapWidth, mapHeight, screenWidth, screenHeight;
    private ArrayList<Textures> textures;

    public Screen(int[][] m, int mapW, int mapH, ArrayList<Textures> tex, int w, int h) {
        map = m;
        mapWidth = mapW;
        mapHeight = mapH;
        textures = tex;
        screenWidth = w;
        screenHeight = h;
    }

    public int[] update(Player player, int[] pixels) {

        // Load variables from Screen instance
        double xPlayer, yPlayer, xDirection, yDirection, xScreenPlane, yScreenPlane;
        xPlayer = player.getxPlayer();
        yPlayer = player.getyPlayer();
        xDirection = player.getxDirection();
        yDirection = player.getyDirection();
        xScreenPlane = player.getxScreenPlane();
        yScreenPlane = player.getyScreenPlane();

        // Paint floor and ceiling
        for(int n=0; n<pixels.length/2; n++) {
            if(pixels[n] != Color.DARK_GRAY.getRGB()) pixels[n] = Color.DARK_GRAY.getRGB();
        }
        for(int i=pixels.length/2; i<pixels.length; i++){
            if(pixels[i] != Color.gray.getRGB()) pixels[i] = Color.gray.getRGB();
        }

        // For each vertical column of pixels on the screen
        for(int x = 0; x< screenWidth; x=x+1) {
            // Coordinate of the current column, from -1 on the lft to 1 on the right.
            double cameraX =  x / ((double)(screenWidth)/2)-1;
            // Direction vector of the current ray being cast.
            double xRay = xDirection + xScreenPlane * cameraX;
            double yRay = yDirection + yScreenPlane * cameraX;
            // Alpha is the angle between ray and the x axis.
            double tanAlpha = yRay/xRay;
            // We want to compute the distance between player and first wall.
            double wallCameraDist;
            // Coordinates of the player on the map.
            int xPlayerMap = (int) xPlayer;
            int yPlayerMap = (int) yPlayer;

            // To use the same naming convention as in the article.
            double Px = xPlayer;
            double Py = yPlayer;

            // Step length in the x, y direction when computing horizontal, vertical intersections.
            double horXa, horYa, vertXa, vertYa;
            // Precise coordinates of the horizontal, vertical intersection.
            double Ax, Ay, Bx, By;

            // Calculate the initial intersections
            // Horizontal intersections
            // If the ray is facing up
            if (yRay > 0)
            {
                horYa = 1;
                Ay = yPlayerMap+1;

            }
            // If the ray is facing down
            else
            {
                horYa = -1;
                Ay = yPlayerMap;
            }
            // If ray faces left or right, tan alpha close to zero, Ax big, horizontal won't be chosen.
            horXa = horYa/tanAlpha;
            Ax = Px + (Ay-Py)/tanAlpha;
            // Vertical intersections
            // If the ray is facing right
            if (xRay > 0)
            {
                vertXa = 1;
                Bx = xPlayerMap+1;
            }
            // If the ray is facing left
            else
            {
                vertXa = -1;
                Bx = xPlayerMap;
            }
            // If ray faces up or down, alpha is 90 or -90, tanalpha is big, By is big and vertical won't be chosen.
            vertYa = (vertXa)*tanAlpha;
            By = Py - (Px-Bx)*tanAlpha;

            //Loop to find where the ray hits a wall
            //Number of texture to display
            int texNum;
            boolean horizontal;
            //This loop runs for each ray with the setup above
            while(true) {
                horizontal = (Math.sqrt((Ax-Px)*(Ax-Px)+(Ay-Py)*(Ay-Py)) < Math.sqrt((Bx-Px)*(Bx-Px)+(By-Py)*(By-Py)));
                // Check horizontal intersection
                if (horizontal) {

                    if(yRay > 0) {
                        xPlayerMap = (int) Ax;
                        yPlayerMap = (int) Ay; // Should be same.
                    }
                    else {
                        xPlayerMap = (int) Ax;
                        yPlayerMap = (int) Ay - 1;
                    }
                    if(xPlayerMap > mapWidth-1) xPlayerMap = mapWidth-1;
                    if(yPlayerMap > mapHeight-1) yPlayerMap = mapHeight-1;
                    if(xPlayerMap < 0) xPlayerMap = 0;
                    if(yPlayerMap < 0) yPlayerMap = 0;
                    texNum = map[xPlayerMap][yPlayerMap];
                    if(texNum > 0) break;
                    Ax += horXa;
                    Ay += horYa;

                }
                else {
                    if(xRay > 0) {
                        yPlayerMap = (int) By;
                        xPlayerMap = (int) Bx;
                    } else {
                        yPlayerMap = (int) By;
                        xPlayerMap = (int) Bx - 1;
                    }
                    if(xPlayerMap > mapWidth-1) xPlayerMap = mapWidth-1;
                    if(yPlayerMap > mapHeight-1) yPlayerMap = mapHeight-1;
                    if(xPlayerMap < 0) xPlayerMap = 0;
                    if(yPlayerMap < 0) yPlayerMap = 0;
                    texNum = map[xPlayerMap][yPlayerMap];
                    if (texNum > 0) break;
                    Bx += vertXa;
                    By += vertYa;
                }
            }
            //Calculate distance to the wall.
            if(horizontal)
                wallCameraDist = Math.sqrt((Px-Ax)*(Px-Ax)+(Py-Ay)*(Py-Ay));
            else
                wallCameraDist = Math.sqrt((Px-Bx)*(Px-Bx)+(Py-By)*(Py-By));
            //Now calculate the height of the wall in pixels based on the distance from the player.
            int lineHeight = (wallCameraDist > 0) ? Math.abs((int)(screenHeight / wallCameraDist)) : screenHeight;
            //calculate lowest and highest pixel to fill in current stripe.
            int drawStart = screenHeight /2-lineHeight/2;
            if(drawStart < 0)
                drawStart = 0;
            int drawEnd = screenHeight /2+lineHeight/2;
            if(drawEnd >= screenHeight)
                drawEnd = screenHeight - 1;
            // To test just with colors.
//            int color;
//            switch(texNum)
//            {
//                case 1:  color = Color.RED.getRed();  break; //red
//                case 2:  color = Color.GREEN.getGreen();  break; //green
//                case 3:  color = Color.BLUE.getBlue();   break; //blue
//                case 4:  color = Color.WHITE.getRGB();  break; //white
//                default: color = Color.YELLOW.getRGB(); break; //yellow
//            }
//
//            for(int y=drawStart; y<drawEnd; y++){
//                pixels[x + y*(screenWidth)] = color;
//            }
            // Calculate offset on the texture.
            double offset;
            if(horizontal) {
                offset = Math.abs(Ax)-Math.floor(Math.abs(Ax));

            } else {
                offset = Math.abs(By)-Math.floor(Math.abs(By));
            }
            // Calculate x coordinate on the texture
            int texWidth = textures.get(texNum-1).SIZE;
            int texX = (int)(offset * texWidth);
            // Verify if we need to flip the texture
            if((horizontal && xRay < 0 && yRay > 0) ||
                    (horizontal && xRay > 0 && yRay < 0) ||
                    (!horizontal && xRay > 0 && yRay > 0) ||
                    (!horizontal && xRay < 0 && yRay < 0)) texX = texWidth - texX - 1;
            // Calculate y coordinate on texture
            for(int y=drawStart; y<drawEnd; y++) {
                int texY = (((y*2 - screenHeight + lineHeight) << 6) / lineHeight) / 2;
                int color;
                if(horizontal) color = textures.get(texNum-1).pixels[texX + (texY * texWidth)];
                // Make y side color darker: R, G and B byte each divided by two with a "shift" and an "and"
                else color = (textures.get(texNum-1).pixels[texX + (texY * texWidth)]>>1) & 8355711;

                pixels[x + y*(screenWidth)] = color;
            }
        }
        return pixels;
    }
}
