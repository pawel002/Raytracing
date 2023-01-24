package Objects;

import Math.Vec3d;
import Math.Ray;
import Math.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Math.Vec3d.*;
import static java.lang.Math.*;
import static java.lang.Math.sqrt;

public class Sphere extends Solid{

    private final double radius;
    private BufferedImage texture;

    public Sphere(Vec3d position, double radius_, Vec3d color, double reflectivity, double roughness, double albedo, double lambertian, double blinn, int blinnExp) {
        super(position, color, reflectivity, roughness,  albedo,  lambertian,  blinn, blinnExp);
        this.radius = radius_;
        texture = null;
    }

    @Override
    public HitInfo calculateIntersection(Ray ray) {
        Vec3d oc = subtract(ray.origin, position);
        double a = lengthSquared(ray.direction);
        double b = dot(oc, ray.direction);
        double c = lengthSquared(oc) - radius*radius;
        double dis = b*b - a*c;
        if(dis < 0){
            return new HitInfo(-1, null, null, null);
        }
        double t = (-b - Math.sqrt(dis)) / a;
        Vec3d normal = normalize(subtract(ray.at(t), position));

        return new HitInfo(t, this, normal, getColor(normal));
    }

    private Vec3d getColor(Vec3d normal){

        if (texture == null)
            return color;

        double u = 0.5 * acos(normal.x / sqrt(normal.x * normal.x + normal.y * normal.y)) / Math.PI;
        u = normal.y > 0 ? 0.5 - u : 0.5 + u;

        double v = 0.5 - atan(normal.z / sqrt(normal.x* normal.x + normal.y* normal.y)) / Math.PI;

        try {
            return fromInt(texture.getRGB((int)(u*(texture.getWidth()-1)), (int)(v*(texture.getHeight()-1))));
        } catch (Exception e) {
            return new Vec3d(0.8, 0 , 0.9);
        }
    }

    public void loadTexture(String filename){
        new Thread("Texture loader") {
            @Override
            public void run() {
                try {
                    System.out.println("Loading sphere texture " + filename + "...");
                    texture = ImageIO.read(new File("src/main/resources/textures/" + filename));
                    System.out.println("Texture loaded.");
                } catch (IOException | IllegalArgumentException ex) {
                    try {
                        texture = ImageIO.read(new File("src/main/resources/textures/error_texture.jpg"));
                    } catch (IOException | IllegalArgumentException ex2) {
                        ex2.printStackTrace();
                        System.exit(-1);
                    }
                    ex.printStackTrace();
                }
            }
        }.start();
    }


}
