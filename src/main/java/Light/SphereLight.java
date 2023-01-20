package Light;

import Math.*;

public class SphereLight extends Light{

    public Vec3d position;
    public double radius;

    public SphereLight(Vec3d position, double radius, Vec3d color, double intensity) {
        super(color, intensity);
        this.radius = radius;
        this.position = position;
    }
}
