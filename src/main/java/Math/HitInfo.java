package Math;

import Objects.Solid;
import com.sun.javafx.scene.text.TextLayout;

public class HitInfo {

    public double t;
    public Solid solid;

    public HitInfo(double t, Solid solid){
        this.t = t;
        this.solid = solid;
    }
}
