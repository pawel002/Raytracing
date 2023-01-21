package Math;

import Objects.Solid;
import com.sun.javafx.scene.text.TextLayout;

public class HitInfo {

    public double t;
    public Solid solid;
    public Vec3d normal;

    public HitInfo(double t, Solid solid, Vec3d normal){
        this.t = t;
        this.solid = solid;
        this.normal = normal;
    }
}
