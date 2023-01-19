package Math;

import Math.Ray;

import java.awt.*;
import java.util.Random;
import java.util.Scanner;

import Math.*;
import static Math.Vec3d.*;
import static java.lang.System.out;

public class Utils {

    public static Vec3d randomInSphere(){
        Random r = new Random();
        Vec3d v = new Vec3d(2.0 * r.nextDouble() - 1, 2.0 * r.nextDouble() - 1, 2.0 * r.nextDouble() -1);
        while (lengthSquared(v) > 1)
            v = new Vec3d(2.0 * r.nextDouble() - 1, 2.0 * r.nextDouble() - 1, 2.0 * r.nextDouble() -1);
        return v;
    }
}
