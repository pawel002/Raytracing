package Engine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.*;
import java.util.Arrays;

import Light.Light;
import Math.Vec3d;
import Objects.*;
import Raytracer.Camera;
import Raytracer.Scene;
import Raytracer.Skybox;
import Raytracer.Renderer.*;
import Light.*;
import static Raytracer.Renderer.*;

import static Math.Vec3d.lengthSquared;
import static Math.Vec3d.scale;
import static Raytracer.Renderer.renderScene;
import static java.lang.Math.*;
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

    private Font font;

    private boolean running;


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
        running = true;

        // get scene elements
        scene = new Scene();
        camera = scene.getCamera();
        skybox = scene.getSkybox();


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
        font = new Font("Consolas", Font.PLAIN, getWidth()/50);

        tempBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        currBuffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

        // set average buffer
        averageBuffer = new long[3 * getWidth() * getHeight()];
        averagedScenes = 1;

        while (running){
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

        g.setColor(java.awt.Color.WHITE);
        g.setFont(font);
        FontMetrics fm = g.getFontMetrics();

        if(!cursorCaptured){
            String str1 = "Use WSAD, Space and Shift to move";
            Rectangle2D str1Bounds = fm.getStringBounds(str1, g);
            g.drawString(str1, 5,20);

            String str2 = "T - progressive rendering, Y - turn off, R - clear buffer";
            Rectangle2D str2Bounds = fm.getStringBounds(str2, g);
            g.drawString(str2, 5,20 + (int) str1Bounds.getHeight());

            String str3 = "Look around using mouse";
            Rectangle2D str3Bounds = fm.getStringBounds(str3, g);
            g.drawString(str3, 5,20 + (int) (str1Bounds.getHeight() + str2Bounds.getHeight()));

            String str4 = "Change properties in the settings window";
            Rectangle2D str4Bounds = fm.getStringBounds(str4, g);
            g.drawString(str4, 5,20 + (int) (str1Bounds.getHeight() + str2Bounds.getHeight() + str3Bounds.getHeight()));

            String str5 = "Don't move while rendering or with progressive rendering on!";
            Rectangle2D str5Bounds = fm.getStringBounds(str5, g);
            g.drawString(str5, 5,20 + (int) (str1Bounds.getHeight() + str2Bounds.getHeight() + str3Bounds.getHeight() + str4Bounds.getHeight()));

            String str6 = "Switch scenes with lowest resolution to prevent bugs!";
            Rectangle2D str6Bounds = fm.getStringBounds(str6, g);
            g.drawString(str6, 5,20 + (int) (str1Bounds.getHeight() + str2Bounds.getHeight() + str3Bounds.getHeight() + str4Bounds.getHeight() + str5Bounds.getHeight()));
        }


        if(!skybox.isLoaded()){
            String skyboxStr = "Loading Skybox...";
            Rectangle2D skyboxStrBounds = fm.getStringBounds(skyboxStr, g);
            g.drawString(skyboxStr, (int) (getWidth()/2  - skyboxStrBounds.getWidth()/2),(int) (getHeight()/2  - skyboxStrBounds.getHeight()/2));
        }
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
            settings.setVisible(true);
            setCursor(Cursor.getDefaultCursor());
        }
    }

    public void renderToImage(int width, int height, int maxDepth, int antiAliasing) throws IOException {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        BufferedImage tempImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        long[] imageAverageBuffer = new long[3*width*height];

        System.out.println("Rendering to image...");

        renderScene(image.getGraphics(), scene, width, height, maxDepth, 1);

        copyCurrBuffer(image, imageAverageBuffer, width * height);
        int imageAveragedScenes = 1;

        while (imageAveragedScenes < antiAliasing){
            imageAveragedScenes ++;
            renderScene(tempImage.getGraphics(), scene, width, height, maxDepth, 1);
            averageBuffers(image, tempImage, imageAverageBuffer, width * height, imageAveragedScenes);
        }

        File imgFile = new File("output.png");
        ImageIO.write(image, "PNG", new FileOutputStream(imgFile));
        System.out.println("Image saved.");

        Desktop.getDesktop().open(imgFile);
    }

    public void setResolution(int newResolution){
        resolution = newResolution;
    }

    public void setMouseSens(double newMouseSens){
        mouseSens = newMouseSens;
    }

    public void setCamSpeed(double newCamSpeed){
        camSpeed = newCamSpeed;
    }

    public void setMaxDepth(int newMaxDepth){
        maxDepth = newMaxDepth;
    }

    public void setFov(double newFov){
        camera.setFov(newFov);
    }

    public Scene getScene(){
        return scene;
    }
}
