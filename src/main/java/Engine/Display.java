package Engine;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import Math.Vec3d;

public class Display {

    private int width;
    private int height;
    private final int[] pixels;
    private final BufferedImage image;

    public Display(int width_, int height_) {
        width = width_;
        height = height_;
        image = new BufferedImage(width, height+1, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
    }

    public void clear() {
        Arrays.fill(pixels, 0x000000);
    }

    public void drawPixelVec3f(int x, int y, Vec3d v) {
        if (x < 0 || x > width || y < 0 || y > height)
            return;

        // Calculate the hexadecimal color from the vector parameters
        long red = (long) (v.x * 255.0f);
        long green = (long) (v.y * 255.0f);
        long blue = (long) (v.z * 255.0f);
        long hex_value = ((red << 16) | (green << 8) | blue);

        pixels[x + y * width] = (int) hex_value;
    }

    public void drawPixelInt(int x, int y, int color) {
        if (x < 0 || x >= width || y < 0 || y >= height)
            return;
        pixels[x + y * width] = color;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int[] getPixels() {
        return pixels;
    }

    public int getPixel(int i) {
        return pixels[i];
    }

    public BufferedImage getImage() {
        return image;
    }

}

