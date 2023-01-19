package Objects;

import Math.Vec3d;
import Math.Ray;

import static Math.Vec3d.*;

public class Plane extends Solid{

    private final Vec3d normal;

    public Plane(Vec3d position, Vec3d normal_, Vec3d color, double reflectivity, double roughness_) {
        super(position, color, reflectivity, roughness_);
        normal = inverse(normal_);
    }

    @Override
    public double calculateIntersection(Ray ray) {
        double a = dot(normal, ray.direction);
        if (a > 1e-2) {
            Vec3d p = subtract(position, ray.origin);
            return dot(p, normal) / a;
        }
        return -1;
    }

    @Override
    public Vec3d getNormalAt(Vec3d point) {
        return normal;
    }
}

