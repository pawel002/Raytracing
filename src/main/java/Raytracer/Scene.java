package Raytracer;

import Light.Light;
import Light.*;
import Objects.Solid;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Math.*;

import static Math.Vec3d.*;
import static Math.Utils.*;
import static java.lang.Math.*;
import static java.lang.System.out;

public class Scene {

    private Camera camera;
    private List<Solid> solids = new ArrayList<>();
    private List<Light> lights = new ArrayList<>();
    private Skybox skybox;
    private final Random rand = new Random();

    public Pixel getRayColor(Ray incomingRay, int depth, double currRefraction){

        if(depth == 0){
            return new Pixel(skybox.getColor(incomingRay.direction), -1);
        }

        // add info about normal to HITINFO - DONE
        HitInfo hitInfo = getHitInfo(incomingRay);
        double t = hitInfo.t;
        Solid solid = hitInfo.solid;


        if (solid != null){

            // if the solid is transparent
            if (solid.isTransparent()){

                Vec3d direction = normalize(incomingRay.direction);
                double cosTheta = min(dot(inverse(direction), hitInfo.normal), 1.0);
                double sinTheta = sqrt(1.0 - cosTheta*cosTheta);

                boolean cannotRefract = currRefraction / solid.getRefractionIndex() * sinTheta > 1.0;

                if (cannotRefract || reflectance(cosTheta, solid.getRefractionIndex()) > rand.nextDouble()){
                    return new Pixel(getReflectionColor(hitInfo, incomingRay, depth, currRefraction), t);
                }
                else {
                    direction = refract(direction, hitInfo.normal, currRefraction/solid.getRefractionIndex());
                    return getRayColor(new Ray(incomingRay.at(t), direction), depth, solid.getRefractionIndex());
                }

            }

            // color of the point hit by incoming ray
            Vec3d hitColor = getHitColor(hitInfo, incomingRay);

            if(solid.getReflectivity() == 0)
                return new Pixel(hitColor, t);

            // reflection color
            Vec3d reflectionColor = getReflectionColor(hitInfo, incomingRay, depth, currRefraction);

            // actual color that comes to the camera
            Vec3d color = add(scale(hitColor, 1 - solid.getReflectivity()), scale(reflectionColor, solid.getReflectivity()));

            return new Pixel(color, t);
        }

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

                Vec3d lightVec = subtract(((PointLight) lightSource).position, hitPoint);
                double distanceSquared = lengthSquared(lightVec);

                Ray lightRay = new Ray(hitPoint, lightVec);
                HitInfo lightHitInfo = getHitInfo(lightRay);

                double distanceNextHit = lengthSquared(subtract(lightRay.at(lightHitInfo.t), hitPoint));

                if (lightHitInfo.solid != null && distanceNextHit < distanceSquared) {
                    continue;
                }

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

                double lightIntensity = lightSource.intensity;

                // Lambertian shading
                double lambertianIntensity = solid.getLambertian() * lightIntensity * max(0, dot(lightVec, normal));

                // Blinn - Phong (blinn-phong coef material??)
                Vec3d bisector = normalize(add(lightVec, inverse(normalize(incomingRay.direction))));
                double blinnPhongIntensity = solid.getBlinn() * lightIntensity * pow(max(0, dot(bisector, normal)), solid.getBlinnExp());

                Vec3d addColor = scale(scale(lightSource.color, hitInfo.color), lambertianIntensity + blinnPhongIntensity);
                color = add(color, addColor);

            } else if(lightSource instanceof SphereLight SLight){

                double sphereSamples = 3;

                for(int j=0; j<sphereSamples; j++) {

                    Vec3d lightVec = subtract(add(SLight.position, scale(randomInSphere(), SLight.radius)), hitPoint);
                    double distanceSquared = lengthSquared(lightVec);

                    Ray lightRay = new Ray(hitPoint, lightVec);
                    HitInfo lightHitInfo = getHitInfo(lightRay);

                    double distanceNextHit = lengthSquared(subtract(lightRay.at(lightHitInfo.t), hitPoint));

                    if (lightHitInfo.solid != null && distanceNextHit < distanceSquared) {
                        continue;
                    }

                    double lightIntensity = SLight.intensity / sphereSamples / distanceSquared;

                    // Lambertian shading
                    double lambertianIntensity = solid.getLambertian() * lightIntensity * max(0, dot(lightVec, normal));

                    // Blinn - Phong (blinn-phong coef material??)
                    Vec3d bisector = normalize(add(lightVec, inverse(normalize(incomingRay.direction))));
                    double blinnPhongIntensity = solid.getBlinn() * lightIntensity * pow(max(0, dot(bisector, normal)), solid.getBlinnExp());

                    Vec3d addColor = scale(scale(SLight.color, hitInfo.color), lambertianIntensity + blinnPhongIntensity);
                    color = add(color, addColor);
                }
            }
        }

        // ambient light - albedo
        color = add(color, scale(hitInfo.color, solid.getAlbedo()));

        return new Vec3d(min(0.999,  color.x), min(0.999,  color.y), min(0.999,  color.z));
    }

    private Vec3d getReflectionColor(HitInfo hitInfo, Ray incomingRay, int depth, double currRefraction){

        double t = hitInfo.t;;
        Solid solid = hitInfo.solid;
        Vec3d normal = hitInfo.normal;

        Vec3d dir = normalize(incomingRay.direction);
        Ray newRay = new Ray(incomingRay.at(t), add(subtract(dir, scale(normal, 2 * dot(dir, normal))), scale(randomInSphere(), solid.getRoughness())));

        return getRayColor(newRay, depth - 1, currRefraction).getColor();
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

    private static double reflectance(double cosine, double ref) {
        // Use Schlick's approximation for reflectance.
        double r = (1-ref) / (1+ref);
        r = r * r;
        return r + (1-r)*pow((1 - cosine),5);
    }

    public Scene(){
        camera = new Camera(new Vec3d(0,0,0));
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

    public void clearScene(){
        solids.clear();
        lights.clear();
    }

}
