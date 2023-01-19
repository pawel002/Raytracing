package Light;

import Math.*;

public abstract class Light {

    public Vec3d color;

    public Light(Vec3d color){
        this.color = color;
    }

    public Vec3d getIllumination(Vec3d point){

        return point;
    }
}
