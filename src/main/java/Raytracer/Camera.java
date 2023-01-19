package Raytracer;
import Math.Vec3d;
import Math.Ray;

import static Math.Vec3d.*;
import static java.lang.Math.*;
import static java.lang.System.out;

public class Camera {

    public Vec3d origin;
    public double yaw;
    public double pitch;
    public double fov;
    public Vec3d eyeConst;
    double h;
    double aspectRatio;

    Vec3d horizontal;
    Vec3d vertical;
    Vec3d LowerLeft;


    public Camera(Vec3d origin){
        this.origin = origin;
        yaw = pitch = 0;
        fov = 60;
        updateCamera();
    }

    public void move(Vec3d v){
        origin.translate(new Vec3d(-v.x * cos(toRadians(yaw)) + v.y*sin(toRadians(yaw)), -v.x * sin(toRadians(yaw)) - v.y*cos(toRadians(yaw)), v.z));
    }

    public void setAspectRatio(double ar){
        aspectRatio = ar;
    }

    public void setYaw(double y){
        yaw = y;
    }

    public void setPitch(double p){
        pitch = p;
    }

    public void setFov(double f){
        fov = f;
    }

    public void updateCamera(){

        h = Math.tan(Math.toRadians(fov/2));
        double viewportHeight = 2.0 * h;
        double viewportWidth = aspectRatio * viewportHeight;

        double p = toRadians(pitch);
        double y = toRadians(yaw);

        // assuming that up is always (0,0,1)
        Vec3d w = new Vec3d(cos(p)*cos(y), cos(p)*sin(y), sin(p));
        Vec3d u = normalize(cross(new Vec3d(0,0,1), w));
        Vec3d v = cross(w, u);


        horizontal = scale(u, viewportWidth);
        vertical = scale(v, viewportHeight);
        LowerLeft = subtract(origin, add(scale(horizontal, 0.5), add(scale(vertical, 0.5), w)));

    }

    public Ray castRay(double u, double v){
        Vec3d dir = subtract(add(LowerLeft, add(scale(horizontal, u), scale(vertical, v))), origin);
        return new Ray(origin, dir);
    }

    public Vec3d getEyeConst(){
        return eyeConst;
    }
}
