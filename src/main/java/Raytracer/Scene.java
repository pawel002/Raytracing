package Raytracer;

import Objects.Solid;

import java.util.ArrayList;
import java.util.List;

import Math.*;

import static Math.Vec3d.*;
import static Math.Utils.*;

public class Scene {

    private Camera camera;
    private List<Solid> solids = new ArrayList<>();
    //    private List<LightSource> lights = new ArrayList<>();
    private Skybox skybox;

    public Pixel getRayColor(Ray ray, int depth){
        if(depth == 0){
            return new Pixel(skybox.getColor(ray.direction), -1);
        }

        double t = 99999, temp;
        Solid hitSolid = null;
        for (Solid x : solids) {
            temp = x.calculateIntersection(ray);
            if (temp >= 0.01 && temp <= t) {
                t = temp;
                hitSolid = x;
            }
        }

        if (hitSolid != null){
            if(hitSolid.getReflectivity() == 0){
                return new Pixel(hitSolid.getColor(), t);
            }
            Vec3d dir = normalize(ray.direction);
            Vec3d normal = hitSolid.getNormalAt(ray.at(t));
            Ray newRay = new Ray(ray.at(t), add(subtract(dir, scale(normal, 2 * dot(dir, normal))), scale(randomInSphere(), hitSolid.getRoughness())));

            Vec3d reflectionColor = getRayColor(newRay, depth - 1).getColor();
            Vec3d color = add(scale(hitSolid.getColor(), 1 - hitSolid.getReflectivity()), scale(reflectionColor, hitSolid.getReflectivity()));
            return new Pixel(color, t);
        }
        else
            return new Pixel(skybox.getColor(ray.direction), -1);
    }

    public Scene(){
        camera = new Camera(new Vec3d(0,0,1));
        skybox = new Skybox("Sky.jpg");
    }

    public void addSolid(Solid solid){
        solids.add(solid);
    }

    public Camera getCamera() {
        return camera;
    }

    public Skybox getSkybox() {
        return skybox;
    }
}
