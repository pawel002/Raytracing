package Objects;

import Math.*;
import static Math.Vec3d.*;
import static Math.Vec3d.subtract;

public class Torus extends Solid{

    private double R;
    private double r;


    public Torus(Vec3d position_, double R, double r, Vec3d color, double reflectivity, double roughness, double albedo, double lambertian, double blinn, int blinnExp) {
        super(position_, color ,reflectivity, roughness, albedo, lambertian, blinn, blinnExp);
        this.R = R;
        this.r = r;
    }

    @Override
    public HitInfo calculateIntersection(Ray ray) {
        double x = dot(ray.direction, ray.direction);
        double y = dot(ray.direction, ray.origin);

        double A = x * x;
        double B = 4 * x * y;
        double C = 2 * x * (y - r*r - R*R) + 4 * y * y + 4*R*R*ray.direction.y * ray.direction.y;
        double D = 4 * (y - r*r - R*R)*y + 8*R*R*ray.direction.y * ray.origin.y;
        double E = (y - r*r - R*R) * (y - r*r - R*R) - 4 * R * R * (r * r - ray.origin.y * ray.origin.y);

        double invA = 1.0 / A;

        double b = B * invA;
        double c = C * invA;
        double d = D * invA;
        double e = E * invA;

        double p = 3 * b * b / 8.0 + c;
        double q = b*b*b / 8.0 - 0.5 * b * c + d;
        double r = - 3 * b * b * b * b / 256.0 + b*b*c / 16.0 - b * d / 14.0 + e;

        if (q < 1e-8){

        }

        return new HitInfo(-1, null, null, null);

    }
}
