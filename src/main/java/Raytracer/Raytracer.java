package Raytracer;

import Engine.Display;
import Math.Vec3d;
import Math.Ray;
import Objects.Camera;
import Objects.Plane;
import Objects.Solid;
import Objects.Sphere;

import java.util.ArrayList;
import java.util.List;

import static Math.Vec3d.*;
import static java.lang.System.out;

public class Raytracer {
    Display display;
    public int width;
    public int height;
    public Camera camera;

    public List<Solid> objectList = new ArrayList<>();

    public Raytracer(Display display_){
        display = display_;
        width = display.getWidth();
        height = display.getHeight();
        camera = new Camera(new Vec3d(0,0,0), new Vec3d(5, 0,0), new Vec3d(0,0,1), Math.PI/3, 16.0/9.0);

        objectList.add(new Sphere(new Vec3d(5, 0,0), 1, new Vec3d(0.5, 0.5, 0.5), 1, 1));
        objectList.add(new Sphere(new Vec3d(5, 2,0), 1, new Vec3d(0.5, 0.5, 0.5), 1, 1));
        objectList.add(new Plane(new Vec3d(0, 0,-1), new Vec3d(0, 0, 1), new Vec3d(0.5, 0.5, 0.5), 1, 1));
    }

    public void moveCamF(){
        camera.moveForward();
    }

    public void moveCamB(){
        camera.moveBackward();
    }

    public void moveCamD(){
        camera.moveDown();
    }

    public void moveCamR(){
        camera.moveRight();
    }

    public void moveCamL(){
        camera.moveLeft();
    }

    public void moveCamU(){
        camera.moveUp();
    }

    public void render(){
        out.println(camera.origin);
        camera.evaluateCamera();
        for (int j = height-1; j >= 0; --j) {
            for (int i = 0; i < width; ++i) {
                double u = (double) i / (width - 1);
                double v = 1 - (double) j / (height - 1);
                Ray ray = camera.getRay(u, v);
                double t = 99999, temp;
                Solid hitSolid = null;
                for (Solid x : objectList) {
                    temp = x.calculateIntersection(ray);
                    if (temp >= 0.01 && temp <= t) {
                        t = temp;
                        hitSolid = x;
                    }
                }

                if (hitSolid != null)
                    display.drawPixelVec3f(i, j, scale(add(hitSolid.getNormalAt(ray.at(t)), new Vec3d(1.0)), 0.5));
                else {
                    display.drawPixelVec3f(i, j, new Vec3d((double) j / height, 0.5, 0.5));
                }
            }
        }
    }

}
