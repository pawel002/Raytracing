package Light;

import Math.*;

public class PointLight extends Light {

    public Vec3d position;

    public PointLight(Vec3d position, Vec3d color, double intensity) {
        super(color, intensity);
        this.position = position;
    }


}
