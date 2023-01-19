package Raytracer;

import Math.Ray;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.util.Random;

import Math.*;
import static Math.Vec3d.*;
import static java.lang.System.out;

public class Renderer {

    public static void renderScene(Graphics buffer, Scene scene, int width, int height, int maxDepth, int resolution){
        Random rand = new Random();
        Camera cam = scene.getCamera();
        cam.setAspectRatio((double) width / height);
        cam.updateCamera();

        for(int x=0; x < width+resolution; x+= resolution){
            for(int y=0; y < height+resolution; y+=resolution){
                double[] v = new double[2];

                v[0] =  (x + resolution * rand.nextDouble()) / (width-1);
                v[1] =  (y + resolution * rand.nextDouble()) / (height-1);

                Pixel pixel = getPixelColor(scene, cam, v, maxDepth);

                buffer.setColor(pixelToColor(pixel));
                buffer.fillRect(width-x, height - y, resolution, resolution);
            }
        }
    }

    private static Color pixelToColor(Pixel pixel){
        Vec3d v = pixel.getColor();
        long red = (long) (v.x * 255.0f);
        long green = (long) (v.y * 255.0f);
        long blue = (long) (v.z * 255.0f);
        long hex_value = ((red << 16) | (green << 8) | blue);
        return new Color((int) hex_value);
    }

    private static Pixel getPixelColor(Scene scene, Camera cam, double[] v, int maxDepth){
        Ray ray = cam.castRay(v[0], v[1]);
        return scene.getRayColor(ray, maxDepth);
    }

    public static void averageBuffers(BufferedImage currBuffer, BufferedImage nextBuffer, long[] averageBuffer, int size, int numOfScenes){
        int[] currPixels = ((DataBufferInt) currBuffer.getRaster().getDataBuffer()).getData();
        int[] nextPixels = ((DataBufferInt) nextBuffer.getRaster().getDataBuffer()).getData();

        for(int i=0;i<size;i++){
            averageBuffer[3*i] += (nextPixels[i] & 0xFF);
            averageBuffer[3*i + 1] += ((nextPixels[i] >> 8) & 0xFF);
            averageBuffer[3*i + 2] += ((nextPixels[i] >> 16) & 0xFF);

            int r = (int) ((averageBuffer[3*i+2]/numOfScenes) % 255);
            int g = (int) ((averageBuffer[3*i+1]/numOfScenes) % 255);
            int b = (int) ((averageBuffer[3*i]/numOfScenes) % 255);

            currPixels[i] = (r << 16) | (g << 8) | b;
        }
    }

    public static void copyCurrBuffer(BufferedImage currBuffer, long[] averageBuffer, int size){
        int[] currPixels = ((DataBufferInt) currBuffer.getRaster().getDataBuffer()).getData();
        for(int i =0; i<size; i++){
            averageBuffer[3*i] = (currPixels[i]) & 0xFF;
            averageBuffer[3*i + 1] = (currPixels[i] >> 8) & 0xFF;
            averageBuffer[3*i + 2] = (currPixels[i] >> 16) & 0xFF;
        }
    }

}
