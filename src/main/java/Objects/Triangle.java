package Objects;

import Math.*;
import static Math.Vec3d.*;

public class Triangle extends Solid{

    private static final double EPS = 0.0000001;
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

        normal = normalize(cross(edges[0], inverse(edges[1])));
    }

    @Override
    public HitInfo calculateIntersection(Ray ray) {
        double a, f, u, v;
        Vec3d edge1, edge2, h, s, q;
        edge1 = subtract(vertices[1], vertices[0]);
        edge2 = subtract(vertices[2], vertices[0]);
        h = cross(ray.direction, edge2);
        a = dot(edge1, h);
        if(a > -EPS && a < EPS) return new HitInfo(-1, null, null, null);

        f = 1.0 / a;
        s = subtract(ray.origin, vertices[0]);
        u = f * dot(s, h);
        if(u < 0.0 || u > 1.0) return new HitInfo(-1, null, null, null);

        q = cross(s, edge1);
        v = f * dot(ray.direction, q);
        if (v < 0.0 || u + v > 1.0) return new HitInfo(-1, null, null, null);

        return new HitInfo(f * dot(edge2, q), this, normal, color);
    }
}
