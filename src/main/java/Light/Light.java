package Light;

import Math.*;

public abstract class Light {

    public Vec3d color;
    public double intensity;

    public Light(Vec3d color, double intensity){
        this.color = color;
        this.intensity = intensity;
    }

    public Vec3d getIllumination(Vec3d point){

        return point;
    }
}
