package Raytracer;

import Math.*;
import static Math.Vec3d.*;

public class Pixel {
    private Vec3d color;
    private double depth;

    public Pixel(Vec3d color, double depth){
        this.color = color;
        this.depth = depth;
    }

    public void average(Pixel pixel){
        this.color = add(scale(pixel.getColor(), 0.5), scale(this.color, 0.5));
        this.depth = (this.depth + pixel.getDepth()) * 0.5;
    }

    public double getDepth() {
        return depth;
    }

    public Vec3d getColor() {
        return color;
    }
}
