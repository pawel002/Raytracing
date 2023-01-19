package Light;

import Math.*;

public class SphereLight extends Light{

    public Vec3d position;
    public double radius;

    public SphereLight(Vec3d position, double radius, Vec3d color) {
        super(color);
        this.radius = radius;
        this.position = position;
    }
}
