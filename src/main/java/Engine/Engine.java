package Engine;

import Raytracer.Raytracer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

public class Engine extends JFrame implements Runnable {

    // Engine variables
    private boolean running;

    // Java objects
    private Dimension dimension;
    private Thread thread;
    private Display display;
    private Raytracer raytracer;

    public Engine(int width, int height, int scale, boolean isResizable) {

        display = new Display(width, height);
        raytracer = new Raytracer(display);
        dimension = new Dimension(width * scale, height * scale);
        setPreferredSize(dimension);

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    System.out.println("FORWARD - DOWN");
                    raytracer.moveCamF();
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    System.out.println("LEFT - DOWN");
                    raytracer.moveCamL();
                } else if (e.getKeyCode() == KeyEvent.VK_D) {
                    System.out.println("RIGHT - DOWN");
                    raytracer.moveCamR();
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    System.out.println("BACKWARD - DOWN");
                    raytracer.moveCamB();
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    System.out.println("UP - DOWN");
                    raytracer.moveCamU();
                } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    System.out.println("DOWN - DOWN");
                    raytracer.moveCamD();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_D) {
                    System.out.println("RIGHT - UP");
                } else if (e.getKeyCode() == KeyEvent.VK_A) {
                    System.out.println("LEFT - UP");
                } else if (e.getKeyCode() == KeyEvent.VK_W) {
                    System.out.println("FORWARD - UP");
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    System.out.println("BACKWARD - UP");
                } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    System.out.println("UP - UP");
                } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    System.out.println("DOWN - UP");
                }
            }
        });

        this.setTitle("Raytracing");
        this.setResizable(isResizable);
        this.pack();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
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
            String loopTimeEnd = String.format("%.2f", (System.nanoTime() - loopTime));
//            System.out.println(loopTimeEnd);
        }
        stop();
    }

    public void render(){
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(2);
            return;
        }

        display.clear();
        raytracer.render();

        Graphics g = bs.getDrawGraphics();
        g.drawImage(display.getImage(), 0, 0, getWidth(), getHeight(), null);
        g.dispose();
        bs.show();
    }

}
