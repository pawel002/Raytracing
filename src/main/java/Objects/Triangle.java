package Objects;

import Math.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static Math.Vec3d.*;
import static java.lang.Math.*;
import static java.lang.Math.sqrt;

public class Triangle extends Solid{

    private static final double EPS = 0.0000001;
    public Vec3d[] vertices = new Vec3d[3];
    public Vec3d[] edges = new Vec3d[3];
    public Vec3d normal;
    private BufferedImage texture;
    private double[][] textureCoords;


    public Triangle(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d color, double reflectivity, double roughness, double albedo, double lambertian, double blinn, int blinnExp){
        super(new Vec3d(0,0,0), color, reflectivity, roughness, albedo, lambertian, blinn, blinnExp);
        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;

        edges[0] = subtract(v2, v1);
        edges[1] = subtract(v3, v2);
        edges[2] = subtract(v1, v3);

        normal = normalize(cross(edges[0], inverse(edges[1])));
    }

    @Override
    public HitInfo calculateIntersection(Ray ray) {
        double a, f, u, v;
        Vec3d edge1, edge2, h, s, q;
        edge1 = subtract(vertices[1], vertices[0]);
        edge2 = subtract(vertices[2], vertices[0]);
        h = cross(ray.direction, edge2);
        a = dot(edge1, h);
        if(a > -EPS && a < EPS) return new HitInfo(-1, null, null, null);

        f = 1.0 / a;
        s = subtract(ray.origin, vertices[0]);
        u = f * dot(s, h);
        if(u < 0.0 || u > 1.0) return new HitInfo(-1, null, null, null);

        q = cross(s, edge1);
        v = f * dot(ray.direction, q);
        if (v < 0.0 || u + v > 1.0) return new HitInfo(-1, null, null, null);

        return new HitInfo(f * dot(edge2, q), this, normal, getColor(u, v));
    }

    private Vec3d getColor(double u, double v){

        if (texture == null)
            return color;

        double x1 = textureCoords[1][0] - textureCoords[0][0];
        double y1 = textureCoords[1][1] - textureCoords[0][1];

        double x2 = textureCoords[2][0] - textureCoords[0][0];
        double y2 = textureCoords[2][1] - textureCoords[0][1];

        double normalizedX = textureCoords[0][0] + u*x1 + v*x2;
        double normalizedY = textureCoords[0][1] + u*y1 + v*y2;

        try {
            return fromInt(texture.getRGB((int)(normalizedX*(texture.getWidth()-1)), (int)(normalizedY*(texture.getHeight()-1))));
        } catch (Exception e) {
            return new Vec3d(0.8, 0 , 0.9);
        }
    }

    public void loadTexture(String filename, double[][] vt){
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

        textureCoords = vt;
    }

}
