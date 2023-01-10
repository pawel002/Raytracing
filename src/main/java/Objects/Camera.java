package Objects;
import Math.Vec3d;
import Math.Ray;

import static Math.Vec3d.*;

public class Camera {

    public Vec3d origin;
    public Vec3d lowerLeft;
    public Vec3d lookAt;
    public Vec3d up;
    public Vec3d hor;
    public Vec3d ver;
    double viewportHeight;
    double viewportWidth;
    public double cameraSpeed=0.1;

    public Camera(Vec3d lookFrom_, Vec3d lookAt_, Vec3d up_, double fov_, double aspect_){
        lookAt = lookAt_;
        up = up_;
        origin = lookFrom_;

        double h = Math.tan(fov_/2);
        viewportHeight = 2.0 * h;
        viewportWidth = aspect_ * viewportHeight;
        evaluateCamera();
    }

    public Ray getRay(double s, double t){
        return new Ray(origin, subtract(add(lowerLeft, add(scale(hor, s), scale(ver, t))), origin));
    }

    public void moveForward(){
        origin = add(origin, new Vec3d(0.1, 0,0));
    }

    public void moveBackward(){
        origin = subtract(origin, new Vec3d(0.1, 0,0));
    }

    public void moveUp(){
        origin = add(origin, new Vec3d(0, 0,0.1));
    }

    public void moveDown(){
        origin = subtract(origin, new Vec3d(0, 0,0.1));
    }

    public void moveRight(){
        origin = subtract(origin, new Vec3d(0, 0.1,0));
    }

    public void moveLeft(){
        origin = add(origin, new Vec3d(0, 0.1,0));
    }

    public void evaluateCamera(){
        lookAt = add(origin, new Vec3d(1, 0,0));
        Vec3d w = normalize(subtract(origin, lookAt));
        Vec3d u = normalize(cross(up, w));
        Vec3d v = cross(w, u);

        hor = scale(u, viewportWidth);
        ver = scale(v, viewportHeight);
        lowerLeft = subtract(origin, add(scale(hor, 0.5), add(scale(ver, 0.5), w)));
    }


}
