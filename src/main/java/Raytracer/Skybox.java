package Raytracer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import Math.Vec3d;
import static Math.Vec3d.*;
import static java.lang.Math.*;

public class Skybox {

    private BufferedImage skybox;
    private boolean loaded;

    public Skybox(String resourceName) {
        skybox = new BufferedImage(2,2, BufferedImage.TYPE_INT_RGB);
        loaded = false;

        new Thread("Skybox loader") {
            @Override
            public void run() {
                try {
                    System.out.println("Loading skybox " + resourceName + "...");
                    skybox = ImageIO.read(new File("src/main/resources/skybox/" + resourceName));
                    System.out.println("Skybox loaded.");
                    loaded = true;
                } catch (IOException | IllegalArgumentException ex) {
                    try {
                        skybox = ImageIO.read(new File("src/main/resources/skybox/error_skybox.jpg"));
                        loaded = true;
                    } catch (IOException | IllegalArgumentException ex2) {
                        ex2.printStackTrace();
                        System.exit(-1);
                    }
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public void reload(String resourceName) {
        skybox = new BufferedImage(2,2, BufferedImage.TYPE_INT_RGB);
        loaded = false;

        new Thread("Skybox loader") {
            @Override
            public void run() {
                try {
                    System.out.println("Loading skybox "+ resourceName +"...");
                    skybox = ImageIO.read(new File("src/main/resources/skybox/" + resourceName));
                    System.out.println("Skybox ready.");
                    loaded = true;
                } catch (IOException | IllegalArgumentException ex) {
                    try {
                        skybox = ImageIO.read(new File("src/main/resources/skybox/error_skybox.jpg"));
                        loaded = true;
                    } catch (IOException | IllegalArgumentException ex2) {
                        ex2.printStackTrace();
                        System.exit(-1);
                    }
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public void reloadFromFile(File file) {
        skybox = new BufferedImage(2,2, BufferedImage.TYPE_INT_RGB);
        loaded = false;

        new Thread("Skybox loader") {
            @Override
            public void run() {
                try {
                    System.out.println("Loading skybox "+ file.getName() +"...");
                    skybox = ImageIO.read(file);
                    System.out.println("Skybox ready.");
                    loaded = true;
                } catch (IOException | IllegalArgumentException ex) {
                    try {
                        skybox = ImageIO.read(new File("src/main/resources/skybox/error_skybox.jpg"));
                        loaded = true;
                    } catch (IOException | IllegalArgumentException ex2) {
                        ex2.printStackTrace();
                        System.exit(-1);
                    }
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    public Vec3d getColor(Vec3d dir) {

        Vec3d direction = normalize(dir);

        double u = 0.5 * acos(direction.x / sqrt(direction.x * direction.x + direction.y * direction.y)) / Math.PI;
        u = direction.y > 0 ? 0.5 + u : 0.5 - u;
        double v = 0.5 - atan(direction.z / sqrt(direction.x* direction.x + direction.y* direction.y)) / Math.PI;

        try {
            return fromInt(skybox.getRGB((int)(u*(skybox.getWidth()-1)), (int)(v*(skybox.getHeight()-1))));
        } catch (Exception e) {
            return new Vec3d(0.8, 0 , 0.9);
        }
    }

    public boolean isLoaded() {
        return loaded;
    }
}
