package Light;

import Math.*;

public class DirectionalLight extends Light{

    public Vec3d direction;

    public DirectionalLight(Vec3d direction, Vec3d color, double intensity) {
        super(color, intensity);
        this.direction = direction;
    }
}
