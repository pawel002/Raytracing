package Math;

public class Vec3d {
    public double x;
    public double y;
    public double z;

    public Vec3d(double x_, double y_, double z_){
        x = x_;
        y = y_;
        z = z_;
    }

    public Vec3d(double f){
        x = y = z =f;
    }

    public static  Vec3d inverse(Vec3d v){
        return new Vec3d(-v.x, -v.y, -v.z);
    }

    public static Vec3d add(Vec3d a, Vec3d b){
        return new Vec3d(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Vec3d add(Vec3d a, double b){
        return new Vec3d(a.x + b, a.y + b, a.z + b);
    }

    public static Vec3d subtract(Vec3d a, Vec3d b){
        return new Vec3d(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Vec3d subtract(Vec3d a, double b){
        return new Vec3d(a.x - b, a.y - b, a.z - b);
    }

    public static double dot(Vec3d a, Vec3d b){
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public static Vec3d cross(Vec3d a, Vec3d b) {
        return new Vec3d(a.y * b.z - b.y * a.z, a.z * b.x - b.z * a.x, a.x * b.y - b.x * a.y);
    }

    public static Vec3d scale(Vec3d a, double b){
        return new Vec3d(a.x * b, a.y * b, a.z * b);
    }

    public static Vec3d divide(Vec3d a, Vec3d b) {
        return new Vec3d(a.x / b.x, a.y / b.y, a.z / b.z);
    }

    public static Vec3d divide(Vec3d v, double f) {
        return new Vec3d(v.x / f, v.y / f, v.z / f);
    }

    public static double length(Vec3d v) {
        return (double) Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
    }

    public static Vec3d normalize(Vec3d v) {
        return new Vec3d(v.x / length(v), v.y / length(v), v.z / length(v));
    }




}
