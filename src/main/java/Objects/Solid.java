package Objects;

import Math.Vec3d;
import Math.Ray;

public abstract class Solid {

    protected Vec3d position;
    protected Vec3d color;
    protected float reflectivity;
    protected float emission;

    public Solid(Vec3d position_, Vec3d color_, float reflectivity_, float emission_) {
        position = position_;
        color = color_;
        reflectivity = reflectivity_;
        emission = emission_;
    }

    public abstract double calculateIntersection(Ray ray);

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

    public float getReflectivity() {
        return reflectivity;
    }

    public float getEmission() {
        return emission;
    }
}
