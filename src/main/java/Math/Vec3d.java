package Math;

import static java.lang.Math.*;

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
        x = y = z = f;
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

    public void translate(Vec3d v){
        x += v.x;
        y += v.y;
        z += v.z;
    }

    public Vec3d rotate(double yaw, double pitch){
        double yd = toRadians(yaw);
        double pd = toRadians(pitch);

        double newY = y*cos(pd) - z * sin(pd);
        double newZ = y*sin(pd) + z * cos(pd);

        double newX = x * cos(yd) + newZ * sin(yd);
        newZ = -x*sin(yd) + newZ * cos(yd);

        return new Vec3d(newX, newY, newZ);
    }

    public static Vec3d scale(Vec3d a, double b){
        return new Vec3d(a.x * b, a.y * b, a.z * b);
    }

    public static Vec3d scale(Vec3d a, Vec3d b){
        return new Vec3d(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    public static Vec3d divide(Vec3d a, Vec3d b) {
        return new Vec3d(a.x / b.x, a.y / b.y, a.z / b.z);
    }

    public static Vec3d divide(Vec3d v, double f) {
        return new Vec3d(v.x / f, v.y / f, v.z / f);
    }

    public static double length(Vec3d v) {
        return Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z);
    }

    public static double lengthSquared(Vec3d v) {
        return v.x * v.x + v.y * v.y + v.z * v.z;
    }

    public static Vec3d normalize(Vec3d v) {
        double inv = 1 / length(v);
        return new Vec3d(v.x * inv, v.y * inv, v.z * inv);
    }

    public static Vec3d fromInt(int rgb) {
        int b = (rgb)&0xFF;
        int g = (rgb>>8)&0xFF;
        int r = (rgb>>16)&0xFF;

        return new Vec3d((double) r /255.0, (double)g /255.0, (double)b /255.0);
    }

    public String toString(){
        return String.format("{%.2f, %.2f, %.2f}", x, y, z);
    }


    public static Vec3d refract(Vec3d uv, Vec3d n, double ratio) {
        double cos_theta = min(dot(inverse(uv), n), 1.0);
        Vec3d perpendicular = scale(add(uv, scale(n, cos_theta)), ratio);
        Vec3d parallel = scale(n, -sqrt(abs(1.0 - lengthSquared(perpendicular))));
        return add(perpendicular, parallel);
    }

}
