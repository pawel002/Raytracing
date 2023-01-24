package Raytracer;

import Light.Light;
import Light.*;
import Objects.Solid;

import java.util.ArrayList;
import java.util.List;

import Math.*;

import static Math.Vec3d.*;
import static Math.Utils.*;
import static java.lang.Math.*;

public class Scene {

    private Camera camera;
    private List<Solid> solids = new ArrayList<>();
    private List<Light> lights = new ArrayList<>();
    private Skybox skybox;
    private boolean mutex;

    public Pixel getRayColor(Ray incomingRay, int depth){

        if(depth == 0){
            return new Pixel(skybox.getColor(incomingRay.direction), -1);
        }

        mutex = true;
        // add info about normal to HITINFO - DONE
        HitInfo hitInfo = getHitInfo(incomingRay);
        double t = hitInfo.t;
        Solid solid = hitInfo.solid;


        if (solid != null){

            // color of the point hit by incoming ray
            Vec3d hitColor = getHitColor(hitInfo, incomingRay);

            if(solid.getReflectivity() == 0)
                return new Pixel(hitColor, t);

            // reflection color
            Vec3d reflectionColor = getReflectionColor(hitInfo, incomingRay, depth);

            // actual color that comes to the camera
            Vec3d color = add(scale(hitColor, 1 - solid.getReflectivity()), scale(reflectionColor, solid.getReflectivity()));

            mutex = false;
            return new Pixel(color, t);
        }

        mutex = false;
        return new Pixel(skybox.getColor(incomingRay.direction), -1);
    }

    private Vec3d getHitColor(HitInfo hitInfo, Ray incomingRay){

        double t = hitInfo.t;
        Solid solid = hitInfo.solid;
        Vec3d hitPoint = incomingRay.at(t);
        Vec3d normal = hitInfo.normal;
        Vec3d color = new Vec3d(0);

        // works only for points light sources
        for(int i=0; i<lights.size(); i++){

            Light lightSource = lights.get(i);

            if(lightSource instanceof PointLight) {

                Vec3d lightVec = normalize(subtract(((PointLight) lightSource).position, hitPoint));

                Ray lightRay = new Ray(hitPoint, lightVec);
                HitInfo lightHitInfo = getHitInfo(lightRay);

                if (lightHitInfo.solid != null) {
                    continue;
                }

                double distanceSquared = lengthSquared(lightVec);
                double lightIntensity = lightSource.intensity / distanceSquared;

                // Lambertian shading
                double lambertianIntensity = solid.getLambertian() * lightIntensity * max(0, dot(lightVec, normal));

                // Blinn - Phong (blinn-phong coef material??)
                Vec3d bisector = normalize(add(lightVec, inverse(normalize(incomingRay.direction))));
                double blinnPhongIntensity = solid.getBlinn() * lightIntensity * pow(max(0, dot(bisector, normal)), solid.getBlinnExp());

                Vec3d addColor = scale(scale(lightSource.color, hitInfo.color), lambertianIntensity + blinnPhongIntensity);
                color = add(color, addColor);

            } else if(lightSource instanceof DirectionalLight){

                Vec3d lightVec = normalize(inverse(((DirectionalLight) lightSource).direction));

                Ray lightRay = new Ray(hitPoint, lightVec);
                HitInfo lightHitInfo = getHitInfo(lightRay);

                if (lightHitInfo.solid != null) {
                    continue;
                }

                double distanceSquared = lengthSquared(lightVec);
                double lightIntensity = lightSource.intensity / distanceSquared;

                // Lambertian shading
                double lambertianIntensity = solid.getLambertian() * lightIntensity * max(0, dot(lightVec, normal));

                // Blinn - Phong (blinn-phong coef material??)
                Vec3d bisector = normalize(add(lightVec, inverse(normalize(incomingRay.direction))));
                double blinnPhongIntensity = solid.getBlinn() * lightIntensity * pow(max(0, dot(bisector, normal)), solid.getBlinnExp());

                Vec3d addColor = scale(scale(lightSource.color, hitInfo.color), lambertianIntensity + blinnPhongIntensity);
                color = add(color, addColor);
            }
        }

        // ambient light - albedo
        color = add(color, scale(hitInfo.color, solid.getAlbedo()));

        return new Vec3d(min(0.999,  color.x), min(0.999,  color.y), min(0.999,  color.z));
    }

    private Vec3d getReflectionColor(HitInfo hitInfo, Ray incomingRay, int depth){

        double t = hitInfo.t;;
        Solid solid = hitInfo.solid;
        Vec3d normal = hitInfo.normal;

        Vec3d dir = normalize(incomingRay.direction);
        Ray newRay = new Ray(incomingRay.at(t), add(subtract(dir, scale(normal, 2 * dot(dir, normal))), scale(randomInSphere(), solid.getRoughness())));

        return getRayColor(newRay, depth - 1).getColor();
    }

    private HitInfo getHitInfo(Ray ray){
        HitInfo hitInfo = new HitInfo(99999, null, null, null);

        for (int i=0; i<solids.size(); i++) {

            Solid solid = solids.get(i);

            HitInfo tempHitInfo = solid.calculateIntersection(ray);

            if (tempHitInfo.t >= 0.001 && tempHitInfo.t <= hitInfo.t) {
                hitInfo = tempHitInfo;
            }
        }

        return hitInfo;
    }

    public Scene(){
        camera = new Camera(new Vec3d(0,0,10));
        skybox = new Skybox("Sky.jpg");
    }

    public void addSolid(Solid solid){
        solids.add(solid);
    }

    public void addLight(Light light){
        lights.add(light);
    }

    public Camera getCamera() {
        return camera;
    }

    public Skybox getSkybox() {
        return skybox;
    }

    public boolean clearScene(){
        if(mutex)
            return false;
        solids.clear();
        lights.clear();
        return true;
    }

}
