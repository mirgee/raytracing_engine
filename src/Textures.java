/**
 * Created by MiroslavK on 21/04/16.
 */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/*
    Class for storing and loading information about textures.
*/
public class Textures {

    public ArrayList<Textures> textures;

    public static Textures wood = new Textures("../res/wood.png", 64);
    public static Textures brick = new Textures("../res/redbrick.png", 64);
    public static Textures bluestone = new Textures("../res/bluestone.png", 64);
    public static Textures stone = new Textures("../res/greystone.png", 64);

    public int[] pixels;
    private String loc;
    public final int SIZE;

    public Textures(String location, int size) {
        textures = new ArrayList<Textures>();

        loc = location;
        SIZE = size;
        pixels = new int[SIZE * SIZE];
        load();
    }

    private void load() {
        try {
            BufferedImage image = ImageIO.read(new File(loc));
            int w = image.getWidth();
            int h = image.getHeight();
            image.getRGB(0, 0, w, h, pixels, 0, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
