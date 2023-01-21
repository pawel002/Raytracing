package Objects;

import Math.Vec3d;
import Math.Ray;
import Math.*;

public abstract class Solid {

    protected Vec3d position;
    protected Vec3d color;
    protected double reflectivity;
    protected double roughness;

    public Solid(Vec3d position_, Vec3d color_, double reflectivity_, double roughness_) {
        position = position_;
        color = color_;
        reflectivity = reflectivity_;
        roughness = roughness_;
    }

    public abstract HitInfo calculateIntersection(Ray ray);

    public abstract Vec3d getNormalAt(Vec3d point);

    public Vec3d getPosition() {
        return position;
    }

    public Vec3d getColor() {
        return color;
    }

    public Vec3d getTextureColor(Vec3d point) {
        return getColor();
    }

    public double getReflectivity() {
        return reflectivity;
    }

    public double getRoughness() {
        return roughness;
    }
}
