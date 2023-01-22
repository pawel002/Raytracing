package Objects;

import Light.Light;
import Math.*;

import java.util.ArrayList;
import java.util.List;

import static Math.Vec3d.*;


public class Parallelepiped extends Solid{

    public Vec3d[] vectors = new Vec3d[3];
    public Vec3d[] vertices = new Vec3d[8];
    public List<Triangle> triangles = new ArrayList<>();

    Vec3d boundingCenter;
    double boundingRadius;

    public Parallelepiped(Vec3d position, Vec3d v1, Vec3d v2, Vec3d v3, Vec3d color_, double reflectivity_, double roughness_){
        super(position, color_, reflectivity_, roughness_);
        vectors[0] = v1;
        vectors[1] = v2;
        vectors[2] = v3;

        vertices[0] = position;
        vertices[1] = add(position, v1);
        vertices[2] = add(position, v2);
        vertices[3] = add(position, v3);
        vertices[4] = add(vertices[1], v2);
        vertices[5] = add(vertices[1], v3);
        vertices[6] = add(vertices[2], v3);
        vertices[7] = add(vertices[4], v3);

        triangles.add(new Triangle(vertices[0], vertices[1], vertices[3], color_, reflectivity_, roughness_));
        triangles.add(new Triangle(vertices[1], vertices[5], vertices[3], color_, reflectivity_, roughness_));

        triangles.add(new Triangle(vertices[1], vertices[4], vertices[5], color_, reflectivity_, roughness_));
        triangles.add(new Triangle(vertices[5], vertices[4], vertices[7], color_, reflectivity_, roughness_));


        triangles.add(new Triangle(vertices[4], vertices[7], vertices[6], color_, reflectivity_, roughness_));
        triangles.add(new Triangle(vertices[2], vertices[4], vertices[6], color_, reflectivity_, roughness_));


        triangles.add(new Triangle(vertices[6], vertices[2], vertices[3], color_, reflectivity_, roughness_));
        triangles.add(new Triangle(vertices[3], vertices[2], vertices[0], color_, reflectivity_, roughness_));


        triangles.add(new Triangle(vertices[5], vertices[7], vertices[6], color_, reflectivity_, roughness_));
        triangles.add(new Triangle(vertices[6], vertices[3], vertices[5], color_, reflectivity_, roughness_));


        triangles.add(new Triangle(vertices[0], vertices[1], vertices[2], color_, reflectivity_, roughness_));
        triangles.add(new Triangle(vertices[1], vertices[4], vertices[2], color_, reflectivity_, roughness_));

        boundingCenter = add(position, scale(add(v1, add(v2, v3)), 0.5));
        boundingRadius = length(add(v1, add(v2, v3)));
    }

    @Override
    public HitInfo calculateIntersection(Ray ray) {
        // ray - bounding sphere intersection

        Vec3d oc = subtract(ray.origin, boundingCenter);
        double a = lengthSquared(ray.direction);
        double b = dot(oc, ray.direction);
        double c = lengthSquared(oc) - boundingRadius*boundingRadius;
        if(b*b - a*c < 0){
            return new HitInfo(-1, null, null,null);
        }

        // now we iterate over all triangles and find the closest intersection
        HitInfo hitInfo = new HitInfo(99999, null, null, null);
        HitInfo temp;
        for(Triangle triangle : triangles){
            temp = triangle.calculateIntersection(ray);
            if(temp.t > 0 && temp.t < hitInfo.t)
                hitInfo = temp;
        }
        return hitInfo;
    }
}
