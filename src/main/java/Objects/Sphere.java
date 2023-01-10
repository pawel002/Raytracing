package Objects;

import Math.Vec3d;
import Math.Ray;

import static Math.Vec3d.*;

public class Sphere extends Solid{

    private float radius;

    public Sphere(Vec3d position, float radius_, Vec3d color, float reflectivity, float emission) {
        super(position, color, reflectivity, emission);
        radius = radius_;
    }

    @Override
    public double calculateIntersection(Ray ray) {
        Vec3d oc = subtract(ray.origin, position);
        double a = lengthSquared(ray.direction);
        double b = dot(oc, ray.direction);
        double c = lengthSquared(oc) - radius*radius;
        double dis = b*b - a*c;
        if(dis < 0){
            return -1;
        }
        return (-b - Math.sqrt(dis) ) / a;
    }

    @Override
    public Vec3d getNormalAt(Vec3d point) {
        return normalize(subtract(point, position));
    }
}
