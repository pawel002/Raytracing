package Objects;

import Math.Vec3d;
import Math.Ray;
import Math.*;

public abstract class Solid {

    protected Vec3d position;
    protected Vec3d color;
    protected double reflectivity;
    protected double roughness;
    protected double albedo;
    protected double lambertian;
    protected double blinn;
    protected int blinnExp;

    public Solid(Vec3d position, Vec3d color, double reflectivity, double roughness, double albedo, double lambertian, double blinn, int blinnExp) {
        this.position = position;
        this.color = color;
        this.reflectivity = reflectivity;
        this.roughness = roughness;
        this.albedo = albedo;
        this.lambertian = lambertian;
        this.blinn = blinn;
        this.blinnExp = blinnExp;
    }

    public double getAlbedo(){
        return albedo;
    }

    public double getLambertian(){
        return lambertian;
    }

    public double getBlinn(){
        return blinn;
    }

    public int getBlinnExp(){
        return blinnExp;
    }
    public abstract HitInfo calculateIntersection(Ray ray);

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
