package Objects;

import Math.Vec3d;
import Math.Ray;
import Math.*;

import static Math.Vec3d.*;

public class Plane extends Solid{

    private final Vec3d normal;

    public Plane(Vec3d position, Vec3d normal, Vec3d color, double reflectivity, double roughness, double albedo, double lambertian, double blinn, int blinnExp) {
        super(position, color, reflectivity, roughness, albedo, lambertian, blinn, blinnExp);
        this.normal = normal;
    }

    @Override
    public HitInfo calculateIntersection(Ray ray) {
        double a = dot(inverse(normal), ray.direction);
        if (a > 1e-2) {
            Vec3d p = subtract(position, ray.origin);
            return new HitInfo(dot(p, inverse(normal)) / a, this, normal, color);
        }
        return new HitInfo(-1, null, null, null);
    }
}

