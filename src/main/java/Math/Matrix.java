package Math;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Matrix {

    public double[][] matrix;

    public Matrix(int size){
        matrix = new double[size][size];
    }

    public static Matrix RotXMatrix(double angleX){
        Matrix mat = new Matrix(3);
        mat.matrix = new double[][]{
                {1, 0, 0},
                {0, cos(angleX), -sin(angleX)},
                {0, sin(angleX), cos(angleX)},
        };
        return mat;
    }

    public static Matrix RotYMatrix(double angleY){
        Matrix mat = new Matrix(3);
        mat.matrix = new double[][]{
                {cos(angleY), 0, sin(angleY)},
                {0, 1, 0},
                {-sin(angleY), 0, cos(angleY)},
        };
        return mat;
    }

    public static Matrix RotZMatrix(double angleZ){
        Matrix mat = new Matrix(3);
        mat.matrix = new double[][]{
                {cos(angleZ), -sin(angleZ), 0},
                {sin(angleZ), cos(angleZ), 0},
                {0, 0, 1},
        };
        return mat;
    }

    public static Vec3d multiply(Matrix mat, Vec3d v){
        double[][] x = mat.matrix;
        return new Vec3d(x[0][0]*v.x + x[0][1]*v.y + x[0][2]*v.z,
                x[1][0]*v.x + x[1][1]*v.y + x[1][2]*v.z,
                x[2][0]*v.x + x[2][1]*v.y + x[2][2]*v.z);

    }

}
