package Engine;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

import Math.Vec3d;

public class Display {

    private final int m_width;
    private final int m_height;
    private final int[] m_pixels;
    private final BufferedImage m_image;

    public Display(int width, int height) {
        m_width = width;
        m_height = height;
        m_image = new BufferedImage(m_width, m_height, BufferedImage.TYPE_INT_RGB);
        m_pixels = ((DataBufferInt) m_image.getRaster().getDataBuffer()).getData();
    }

    public void clear() {
        Arrays.fill(m_pixels, 0x000000);
    }

    public void drawPixelVec3f(int x, int y, Vec3d v) {
        if (x < 0 || x > m_width || y < 0 || y > m_height)
            return;

        // Calculate the hexadecimal color from the vector parameters
        long red = (long) (v.x * 255.0f);
        long green = (long) (v.y * 255.0f);
        long blue = (long) (v.z * 255.0f);
        long hex_value = ((red << 16) | (green << 8) | blue);

        m_pixels[x + y * m_width] = (int) hex_value;
    }

    public void drawPixelInt(int x, int y, int color) {
        if (x < 0 || x >= m_width || y < 0 || y >= m_height)
            return;
        m_pixels[x + y * m_width] = color;
    }

    public int get_width() {
        return m_width;
    }

    public int get_height() {
        return m_height;
    }

    public int[] get_pixels() {
        return m_pixels;
    }

    public int get_pixel(int i) {
        return m_pixels[i];
    }

    public BufferedImage get_image() {
        return m_image;
    }

}

