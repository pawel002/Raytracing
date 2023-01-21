package Objects;

import Math.Vec3d;
import Math.Ray;
import Math.*;

import static Math.Vec3d.*;

public class Sphere extends Solid{

    private final double radius;

    public Sphere(Vec3d position, double radius_, Vec3d color, double reflectivity, double roughness) {
        super(position, color, reflectivity, roughness);
        radius = radius_;
    }

    @Override
    public HitInfo calculateIntersection(Ray ray) {
        Vec3d oc = subtract(ray.origin, position);
        double a = lengthSquared(ray.direction);
        double b = dot(oc, ray.direction);
        double c = lengthSquared(oc) - radius*radius;
        double dis = b*b - a*c;
        if(dis < 0){
            return new HitInfo(-1, null, null);
        }
        double t = (-b - Math.sqrt(dis)) / a;
        return new HitInfo(t, this, normalize(subtract(ray.at(t), position)));
    }

    @Override
    public Vec3d getNormalAt(Vec3d point) {
        return normalize(subtract(point, position));
    }
}
