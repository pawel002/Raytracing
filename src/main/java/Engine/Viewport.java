package Engine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.lang.*;
import java.util.Arrays;

import Light.Light;
import Math.Vec3d;
import Objects.Parallelepiped;
import Objects.Plane;
import Objects.Sphere;
import Objects.Triangle;
import Raytracer.Camera;
import Raytracer.Scene;
import Raytracer.Skybox;
import Raytracer.Renderer.*;
import Light.*;
import static Raytracer.Renderer.*;

import static Math.Vec3d.lengthSquared;
import static Math.Vec3d.scale;
import static Raytracer.Renderer.renderScene;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.System.out;
import static java.lang.System.setOut;

public class Viewport extends JPanel {

    private BufferedImage currBuffer;
    private BufferedImage tempBuffer;
    private long[] averageBuffer;
    private int averagedScenes;
    private boolean resetRender;

    private JFrame container;
    private JDialog settings;
    private int resolution;
    private boolean cursorCaptured;
    private Vec3d cameraMotion;
    private int maxDepth;

    private Scene scene;

    private boolean progressiveRendering;
    private boolean cameraMoved;
    private Camera camera;
    private double mouseSens;
    private double camSpeed;
    private double cameraYaw;
    private double cameraPitch;

    private Skybox skybox;
    private Cursor blankCursor;

    private long timePerFrame;

    private Robot robot;


    public Viewport(JFrame container, JDialog settings){
        this.setFocusable(true);
        this.container = container;
        this.settings = settings;

        // set scene constants
        resolution = 4;
        mouseSens = 0.5;
        camSpeed = 5.0;
        maxDepth = 2;
        cursorCaptured = false;
        cameraMotion = new Vec3d(0,0,0);
        timePerFrame = -1;
        cameraMoved = false;
        progressiveRendering = false;

        // get scene elements
        scene = new Scene();
        camera = scene.getCamera();
        skybox = scene.getSkybox();

        // setup SCENE
        scene.addSolid(new Sphere(new Vec3d(-5,-5,5), 2, new Vec3d(0.8,0.5,0.5), 0.1, 0.2));
        scene.addSolid(new Sphere(new Vec3d(-5,-1,5), 2, new Vec3d(0.5,0.8,0.5), 0.2, 0));
        scene.addSolid(new Sphere(new Vec3d(-5,3,5), 2, new Vec3d(0.5,0.5,0.8), 0.3, 0));
        scene.addSolid(new Sphere(new Vec3d(-5,7,5), 2, new Vec3d(0.8,0.8,0.8), 0.4, 0));

        scene.addSolid(new Plane(new Vec3d(0,0,3), new Vec3d(0, 0, 1), new Vec3d(0.3,0.3,0.3), 0.01, 0));

        scene.addSolid(new Parallelepiped(new Vec3d(0, 0, 3), new Vec3d(0,0,3), new Vec3d(0,3,1), new Vec3d(3,0,1), new Vec3d(0.5), 1, 0));

        scene.addLight(new PointLight(new Vec3d(0, 20, 10), new Vec3d(0.8,0.8,0.6), 2));

        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0,0), "blank");

        createKeyListeners();
        createMouseListeners();

        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    public void runLoop(){
        tempBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        currBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        // set average buffer
        averageBuffer = new long[3 * getWidth() * getHeight()];
        averagedScenes = 1;

        while (true){
            long start = System.currentTimeMillis();

            handleCameraTranslation();

            handleCameraAngle();

            renderScene(tempBuffer.getGraphics(), scene, getWidth(), getHeight(), maxDepth, resolution);

            if ((cameraMotion.x == 0 && cameraMotion.y == 0 && cameraMotion.z == 0 && !cameraMoved) && !resetRender && progressiveRendering) {

                averagedScenes ++;
                averageBuffers(currBuffer, tempBuffer, averageBuffer, getWidth() * getHeight(), averagedScenes);

            } else {

                if(resetRender)
                    resetRender = false;

                currBuffer = tempBuffer;

                copyCurrBuffer(currBuffer, averageBuffer, getWidth() * getHeight());
                averagedScenes = 1;
            }

            repaint();

            timePerFrame = System.currentTimeMillis() - start;
        }
    }

    private void handleCameraTranslation(){
        if (lengthSquared(cameraMotion) != 0)
            camera.move(scale(cameraMotion, timePerFrame*camSpeed * 0.001));
    }

    private void handleCameraAngle(){
        if (cursorCaptured){
            camera.setYaw(cameraYaw);
            camera.setPitch(cameraPitch);
        }
    }

    @Override
    public void paintComponent(Graphics g){
        if (currBuffer != null)
            g.drawImage(currBuffer, 0, 0, this);

        // paiting options / hud
    }

    private void createKeyListeners(){
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_W) cameraMotion.x = 1;
                else if (e.getKeyCode() == KeyEvent.VK_D) cameraMotion.y = 1;
                else if (e.getKeyCode() == KeyEvent.VK_A) cameraMotion.y = -1;
                else if (e.getKeyCode() == KeyEvent.VK_S) cameraMotion.x = -1;
                else if (e.getKeyCode() == KeyEvent.VK_SPACE) cameraMotion.z = 1;
                else if (e.getKeyCode() == KeyEvent.VK_SHIFT) cameraMotion.z = -1;
                else if (e.getKeyCode() == KeyEvent.VK_R) resetRender = true;
                else if (e.getKeyCode() == KeyEvent.VK_T) progressiveRendering = true;
                else if (e.getKeyCode() == KeyEvent.VK_Y) progressiveRendering = false;
            }

            @Override
            public void keyReleased(KeyEvent e){
                if (e.getKeyCode() == KeyEvent.VK_W) cameraMotion.x = 0;
                else if (e.getKeyCode() == KeyEvent.VK_D) cameraMotion.y = 0;
                else if (e.getKeyCode() == KeyEvent.VK_A) cameraMotion.y = 0;
                else if (e.getKeyCode() == KeyEvent.VK_S) cameraMotion.x = 0;
                else if (e.getKeyCode() == KeyEvent.VK_SPACE) cameraMotion.z = 0;
                else if (e.getKeyCode() == KeyEvent.VK_SHIFT) cameraMotion.z = 0;
                else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    setCursorCaptured(false);
                }
            }
        });
    }

    private void createMouseListeners(){
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e){
                cameraMoved = false;
                if(cursorCaptured){
                    int mouseX = e.getXOnScreen() - (container.getX() + getWidth() / 2);
                    int mouseY = e.getYOnScreen() - (container.getY() + getHeight() / 2);
                    if (mouseY != 0 || mouseX != 0)
                        cameraMoved = true;
                    cameraYaw = cameraYaw + mouseX * mouseSens;
                    cameraPitch = min(90, max(-90, cameraPitch + mouseY * mouseSens));
                    robot.mouseMove(container.getX() + getWidth() / 2, container.getY() + getHeight() / 2);
                }
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e){
                if (!cursorCaptured) setCursorCaptured(true);
            }
        });
    }

    public void setCursorCaptured(boolean b) {
        cursorCaptured = b;

        if (cursorCaptured){
            if (settings.isVisible()){
                settings.setVisible(false);
            }

            setCursor(blankCursor);
            robot.mouseMove(container.getX() + container.getWidth()/2, container.getY() + container.getHeight()/2);

        } else {
            setCursor(Cursor.getDefaultCursor());
        }
    }
}
