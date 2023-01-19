package Light;

import Math.*;

public class DirectionalLight extends Light{

    public Vec3d direction;

    public DirectionalLight(Vec3d direction, Vec3d color) {
        super(color);
        this.direction = direction;
    }
}
