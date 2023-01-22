package Objects;

import Math.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static java.lang.Math.max;
import static java.lang.Math.min;

import static Math.Vec3d.*;
import static java.lang.System.out;

public class Mesh extends Solid {

    private List<Vec3d> vertices = new ArrayList<>();
    private List<Triangle> triangles = new ArrayList<>();

    private Vec3d boundingCenter;
    private double boundingRadius;

    public Mesh(String filename, Vec3d position, Vec3d color, double reflectivity, double roughness){
        super(position, color, reflectivity, roughness);

        // load object
        File file = new File("src/main/resources/meshes/" + filename);
        Scanner myReader = null;
        try {
            myReader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] dividedData = data.split("[ /]");
            if(dividedData[0].equals("v")){
                Vec3d vertex = new Vec3d(Double.parseDouble(dividedData[1]), Double.parseDouble(dividedData[2]), Double.parseDouble(dividedData[3]));
                vertices.add(vertex);
            } else if (dividedData[0].equals("f")) {
                int i1 = Integer.parseInt(dividedData[1]) - 1;
                int i2 = Integer.parseInt(dividedData[4]) - 1;
                int i3 = Integer.parseInt(dividedData[7]) - 1;
                triangles.add(new Triangle(vertices.get(i1), vertices.get(i2), vertices.get(i3), color, reflectivity, roughness));
                if(13 == dividedData.length){
                    int i4 = Integer.parseInt(dividedData[10]) - 1;
                    triangles.add(new Triangle(vertices.get(i1), vertices.get(i3), vertices.get(i4), color, reflectivity, roughness));
                }
            }
        }
        double minX=1, minY=1, minZ=1, maxX=-1, maxY=-1, maxZ=-1;
        // compute AABB bounding box
        for(Vec3d vertex : vertices){
            minX = min(minX, vertex.x);
            minY = min(minY, vertex.y);
            minZ = min(minZ, vertex.z);

            maxX = max(maxX, vertex.x);
            maxY = max(maxY, vertex.y);
            maxZ = max(maxZ, vertex.z);
        }

        boundingCenter = new Vec3d((minX + maxX)/2, (minY + maxY)/2,(minZ + maxZ)/2);
        boundingRadius = length(new Vec3d((minX - maxX)/2, (minY - maxY)/2,(minZ - maxZ)/2));

    }

    @Override
    public HitInfo calculateIntersection(Ray ray) {

        Vec3d oc = subtract(ray.origin, boundingCenter);
        double a = lengthSquared(ray.direction);
        double b = dot(oc, ray.direction);
        double c = lengthSquared(oc) - boundingRadius*boundingRadius;
        if(b*b - a*c < 0){
            return new HitInfo(-1, null, null, null);
        }

        HitInfo hitInfo = new HitInfo(99999, null, null, color);
        HitInfo temp;
        for(Triangle triangle : triangles){
            temp = triangle.calculateIntersection(ray);
            if(temp.t > 0 && temp.t < hitInfo.t)
                hitInfo = temp;
        }
        return hitInfo;
    }

}
