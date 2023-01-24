package Objects;

import Math.HitInfo;
import Math.Ray;
import Math.Vec3d;

import static Math.Solvers.solveQuartic;
import static Math.Vec3d.dot;
import static Math.Vec3d.normalize;
import static java.lang.Math.*;

public class Torus extends Solid{

    private double R;
    private double r;


    public Torus(Vec3d position_, double R, double r, Vec3d color, double reflectivity, double roughness, double albedo, double lambertian, double blinn, int blinnExp) {
        super(position_, color, reflectivity, roughness, albedo, lambertian, blinn, blinnExp);
        this.R = R;
        this.r = r;
    }

    @Override
    public HitInfo calculateIntersection(Ray ray) {
        double x = dot(ray.direction, ray.direction);
        double y = dot(ray.direction, ray.origin);
        double z = dot(ray.origin, ray.origin);

        double A = x * x;
        double B = 4 * x * y;
        double C = 2 * x * (z - r*r - R*R) + 4 * y * y + 4*R*R*ray.direction.z * ray.direction.z;
        double D = 4 * (z - r*r - R*R)*y + 8*R*R*ray.direction.z * ray.origin.z;
        double E = (z - r*r - R*R) * (z - r*r - R*R) - 4 * R * R * (r * r - ray.origin.z * ray.origin.z);

        double t=99999;
        double[] solution = solveQuartic(A, B, C, D, E);
        if (solution == null) return  new HitInfo(-1, null, null, null);
        for(double a : solution){
            if(a >= 0.1 && a<t) t=a;
        }
        if(t>9999) return new HitInfo(-1, null, null, null);

        Vec3d hp = ray.at(t);
        double u = R / sqrt(hp.x*hp.x + hp.y*hp.y);
        Vec3d normal = new Vec3d((1-u)*hp.x, (1-u)*hp.y, hp.z);
        return new HitInfo(t, this, normalize(normal), color);
    }
}
