package Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

public class Engine extends Canvas implements Runnable {

    // Engine variables
    private boolean running;

    // Java objects
    private Dimension dimension;
    private JFrame jframe;
    private Thread thread;
    private Display display;
    private Raytracer raytracer;

    public Engine(int width, int height, int scale, boolean isResizable) {

        display = new Display(width, height);
        raytracer = new Raytracer(display);
        dimension = new Dimension(width * scale, height * scale);
        setPreferredSize(dimension);

        jframe = new JFrame();
        jframe.setResizable(isResizable);
        jframe.add(this);
        jframe.pack();
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setLocationRelativeTo(null);
        jframe.setVisible(true);
        running = false;
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        System.exit(0);
    }

    @Override
    public void run() {
        float loopTime;
        requestFocus();
        while (running) {
            loopTime = System.nanoTime();
            render();
            String loopTimeEnd = String.format("%.2f", (System.nanoTime() - loopTime) / 1000000.0f);
            System.out.println(loopTimeEnd);
        }
        stop();
    }

    public void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }
        display.clear();

        raytracer.render();

        Graphics g = bs.getDrawGraphics();
        g.drawImage(display.get_image(), 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

}
