import org.junit.jupiter.api.Test;

import Math.*;
import static Math.Vec3d.*;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Vec3dTest {

    @Test
    public void Vec3dAddTest(){
        Vec3d v1 = new Vec3d(1, 2, 3);
        Vec3d v2 = new Vec3d(4, 3, 6);
        Vec3d v3 = add(v1, v2);
        assertEquals(v3.x, 5);
        assertEquals(v3.y, 5);
        assertEquals(v3.z, 9);

        Vec3d v4 = add(v1, 5);
        assertEquals(v4.x, 6);
        assertEquals(v4.y, 7);
        assertEquals(v4.z, 8);
    }

    @Test
    public void Vec3dInvTest(){
        Vec3d v1 = new Vec3d(1, 2, 3);
        Vec3d v2 = inverse(inverse(v1));
        assertEquals(v2.x, v1.x);
        assertEquals(v2.y, v1.y);
        assertEquals(v2.z, v1.z);
    }


    @Test
    public void Vec3dSubTest(){
        Vec3d v1 = new Vec3d(1, 2, 3);
        Vec3d v2 = new Vec3d(4, 3, 6);
        Vec3d v3 = subtract(v1, v2);
        assertEquals(v3.x, -3);
        assertEquals(v3.y, -1);
        assertEquals(v3.z, -3);

        Vec3d v4 = subtract(v1, 5);
        assertEquals(v4.x, -4);
        assertEquals(v4.y, -3);
        assertEquals(v4.z, -2);
    }

    @Test
    public void Vec3dDotTest(){
        Vec3d v1 = new Vec3d(1, 2, 3);
        Vec3d v2 = new Vec3d(4, 3, 6);
        double t = dot(v1, v2);
        assertEquals(t, 4 +2*3+3*6);
    }

    @Test
    public void Vec3dScaleTest(){
        Vec3d v1 = new Vec3d(1, 2, 3);
        Vec3d v2 = new Vec3d(4, 3, 6);
        Vec3d v3 = scale(v1, v2);
        assertEquals(v3.x, 4);
        assertEquals(v3.y, 6);
        assertEquals(v3.z, 18);

        Vec3d v4 = scale(v1, 5);
        assertEquals(v4.x, 5);
        assertEquals(v4.y, 10);
        assertEquals(v4.z, 15);
    }

    @Test
    public void Vec3dLenSqTest(){
        Vec3d v1 = new Vec3d(1, 2, 3);
        double t = lengthSquared(v1);
        assertEquals(t, 1+4+9);
    }

    @Test
    public void Vec3dLenTest(){
        Vec3d v1 = new Vec3d(1, 2, 3);
        double t = length(v1);
        assertTrue(abs(t - sqrt(1+4+9)) < 0.001);
    }

    @Test
    public void Vec3dNormalizeTest(){
        Vec3d v1 = new Vec3d(1, 2, 3);
        Vec3d v2 = normalize(v1);
        assertTrue(abs(length(v2) - 1) < 0.001);
    }


}
