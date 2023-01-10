package Math;

import Math.Vec3d;

import static Math.Vec3d.*;

public class Ray {

    public Vec3d origin;
    public Vec3d direction;

    public Ray(Vec3d origin_, Vec3d dir){
        origin = origin_;
        direction = dir;
    }

    public Vec3d at(double t){
        return add(origin, scale(direction, t));
    }

}
