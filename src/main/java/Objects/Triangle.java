package Objects;

import Math.*;
import static Math.Vec3d.*;

public class Triangle extends Solid{

    public Vec3d[] vertices = new Vec3d[3];
    public Vec3d[] edges = new Vec3d[3];
    public Vec3d normal;

    public Triangle(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d color_, double reflectivity_, double roughness_){
        super(new Vec3d(0,0,0), color_, reflectivity_, roughness_);
        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;

        edges[0] = subtract(v2, v1);
        edges[1] = subtract(v3, v2);
        edges[2] = subtract(v1, v3);

        normal = cross(edges[0], inverse(edges[1]));
    }

    @Override
    public double calculateIntersection(Ray ray) {

    }

    @Override
    public Vec3d getNormalAt(Vec3d point) {
        return normal;
    }
}
