package Light;

import Math.*;

public class PointLight extends Light {

    Vec3d position;

    public PointLight(Vec3d position, Vec3d color) {
        super(color);
        this.position = position;
    }


}
