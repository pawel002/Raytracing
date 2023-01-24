package Engine;

import Light.DirectionalLight;
import Light.PointLight;
import Light.SphereLight;
import Objects.*;
import Raytracer.Scene;
import Raytracer.Skybox;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;

import Math.*;

import static java.lang.Math.PI;
import static java.lang.Math.asin;

public class SettingsMenu extends JPanel {

    private Viewport viewport;
    private JSlider resolutionSlider;
    private JSlider mouseSensSlider;
    private JSlider camSpeedSlider;
    private JSlider maxDepthSlider;
    private JSlider fovSlider;

    private JComboBox skyboxComboBox;
    private JComboBox sceneComboBox;
    private int selectedSkyboxIndex;

    private JTextField renderHeightVal;
    private JTextField renderWidthVal;
    private JTextField renderAliasingVal;
    private JTextField renderDepthVal;

    private JButton buttonRenderImage;
    private JButton buttonClearScene;

    public SettingsMenu(Viewport viewport){
        this.viewport = viewport;
        setLayout(new GridLayout(0, 1));

        JPanel settingsPanel = new JPanel();
        settingsPanel.add(new JLabel("SETTINGS"));
        add(settingsPanel);

        resolutionSlider = new JSlider(1,15,5);
        resolutionSlider.setMinorTickSpacing(1);
        resolutionSlider.setPaintTicks(true);
        JPanel resolutionPanel = new JPanel();
        resolutionPanel.add(new JLabel("Viewport resolution: "));
        resolutionPanel.add(resolutionSlider);
        add(resolutionPanel);

        mouseSensSlider = new JSlider(0,200,50);
        JPanel mouseSensPanel = new JPanel();
        mouseSensPanel.add(new JLabel("Mouse sensitivity: "));
        mouseSensPanel.add(mouseSensSlider);
        add(mouseSensPanel);

        camSpeedSlider = new JSlider(0,100,50);
        JPanel camSpeedPanel = new JPanel();
        camSpeedPanel.add(new JLabel("Camera speed: "));
        camSpeedPanel.add(camSpeedSlider);
        add(camSpeedPanel);

        maxDepthSlider = new JSlider(0,10,2);
        JPanel maxDepthPanel = new JPanel();
        maxDepthPanel.add(new JLabel("Max depth: "));
        maxDepthPanel.add(maxDepthSlider);
        add(maxDepthPanel);

        fovSlider = new JSlider(30,120,60);
        JPanel fovPanel = new JPanel();
        fovPanel.add(new JLabel("Fov: "));
        fovPanel.add(fovSlider);
        add(fovPanel);


        resolutionSlider.addChangeListener(e -> viewport.setResolution(resolutionSlider.getValue()));
        mouseSensSlider.addChangeListener(e -> viewport.setMouseSens(mouseSensSlider.getValue() / 100.0));
        camSpeedSlider.addChangeListener(e -> viewport.setCamSpeed(camSpeedSlider.getValue() / 10.0));
        maxDepthSlider.addChangeListener(e -> viewport.setMaxDepth(maxDepthSlider.getValue()));
        fovSlider.addChangeListener(e -> viewport.setFov(fovSlider.getValue()));


        JPanel skyboxPanel = new JPanel();
        skyboxPanel.add(new JLabel("Select Skybox: "));
        skyboxComboBox = new JComboBox<String>();
        skyboxComboBox.addItem("Sky");
        skyboxComboBox.addItem("Studio");
        skyboxComboBox.addItem("Outdoor");
        skyboxComboBox.addItem("Factory");
        skyboxComboBox.addItem("Castle");
        skyboxComboBox.addItem("Space");
        skyboxComboBox.addItem("Night");
        skyboxComboBox.addItem("NightHighRes");
        skyboxComboBox.addItem("Black");
        skyboxComboBox.addItem("Custom");
        skyboxPanel.add(skyboxComboBox);
        add(skyboxPanel);

        JPanel scenePanel = new JPanel();
        scenePanel.add(new JLabel("Select scene: "));
        sceneComboBox = new JComboBox<String>();
        sceneComboBox.addItem("Spheres");
        sceneComboBox.addItem("Cubes");
        sceneComboBox.addItem("Torus");
        sceneComboBox.addItem("Texture");
        sceneComboBox.addItem("Error Texture");
        sceneComboBox.addItem("Mesh");
        sceneComboBox.addItem("Phong Showcase");
        sceneComboBox.addItem("Lambertian Showcase");
        sceneComboBox.addItem("Reflectivity Showcase");
        sceneComboBox.addItem("Roughness Showcase");
        sceneComboBox.addItem("Albedo Showcase");
        sceneComboBox.addItem("Refraction Showcase");
        sceneComboBox.addItem("Many Items");
        sceneComboBox.addItem("Cornell Box");
        sceneComboBox.addItem("custom");
        scenePanel.add(sceneComboBox);
        add(scenePanel);

        skyboxComboBox.addItemListener(e -> {
            int selectedIndex = skyboxComboBox.getSelectedIndex();

            if (e.getStateChange() == ItemEvent.SELECTED && selectedIndex != selectedSkyboxIndex) {
                Skybox skybox = viewport.getScene().getSkybox();

                if (skybox.isLoaded()) {
                    if (selectedIndex < skyboxComboBox.getItemCount()-1) {
                        skybox.reload(skyboxComboBox.getSelectedItem() +".jpg");
                        selectedSkyboxIndex = selectedIndex;

                    } else {
                        JFileChooser fc = new JFileChooser("Choose a Skybox (HDRI)");
                        fc.setFileFilter(new FileFilter() {
                            @Override
                            public boolean accept(File f) {
                                return f.isDirectory() || f.getName().endsWith("jpg") || f.getName().endsWith("jpeg") || f.getName().endsWith("png") || f.getName().endsWith("bmp");
                            }

                            @Override
                            public String getDescription() {
                                return "Equirectangular images (*.jpg, *.png, *.bmp)";
                            }
                        });

                        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                        if (fc.showOpenDialog(viewport) == JFileChooser.APPROVE_OPTION) {
                            skybox.reloadFromFile(fc.getSelectedFile());
                            selectedSkyboxIndex = selectedIndex;
                        } else {
                            skyboxComboBox.setSelectedIndex(selectedSkyboxIndex);
                        }
                    }

                } else {
                    skyboxComboBox.setSelectedIndex(selectedSkyboxIndex);
                }
            }
        });

        sceneComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                Scene scene = viewport.getScene();
                switch (sceneComboBox.getSelectedIndex()) {
                    case 0: // Spheres
                        scene.clearScene();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        scene.addLight(new PointLight(new Vec3d(0, 20, 10), new Vec3d(0.8,0.8,0.6), 3));

                        scene.addSolid(new Sphere(new Vec3d(0,0,0), 2, new Vec3d(0.9), 0.3, 0.2, 0.5, 0.2, 0.4, 10));
                        scene.addSolid(new Sphere(new Vec3d(0,4,0), 2, new Vec3d(0.2, 0.9, 0.9), 0.3, 0, 0.5, 0.2, 0.4, 10));
                        scene.addSolid(new Sphere(new Vec3d(0,8,0), 2, new Vec3d(0.9, 0.2, 0.9), 0.3, 0, 0.5, 0.2, 0.4, 10));
                        scene.addSolid(new Sphere(new Vec3d(0,12,0), 2, new Vec3d(0.9,0.9,0.2), 0.3, 0, 0.5, 0.2, 0.4, 10));
                        break;
                    case 1: // Cubes
                        scene.clearScene();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        scene.addLight(new PointLight(new Vec3d(0, 20, 10), new Vec3d(0.8,0.8,0.6), 3));

                        scene.addSolid(new Parallelepiped(new Vec3d(-1, -1, -1), new Vec3d(2,0,0), new Vec3d(0,2,0), new Vec3d(0,0,2), new Vec3d(0.9), 0.3, 0.2, 0.5, 0.2, 0.4, 10));
                        scene.addSolid(new Parallelepiped(new Vec3d(-1, 3, -1), new Vec3d(2,0,0), new Vec3d(0,2,0), new Vec3d(0,0,2), new Vec3d(0.2, 0.9, 0.9), 0.3, 0, 0.5, 0.2, 0.4, 10));
                        scene.addSolid(new Parallelepiped(new Vec3d(-1, 7, -1), new Vec3d(2,0,0), new Vec3d(0,2,0), new Vec3d(0,0,2), new Vec3d(0.9, 0.2, 0.9), 0.3, 0, 0.5, 0.2, 0.4, 10));
                        scene.addSolid(new Parallelepiped(new Vec3d(-1, 11, -1), new Vec3d(2,0,0), new Vec3d(0,2,0), new Vec3d(0,0,2), new Vec3d(0.9,0.9,0.2), 0.3, 0, 0.5, 0.2, 0.4, 10));
                        break;
                    case 2: // Torus
                        scene.clearScene();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();
                        scene.addSolid(new Torus(new Vec3d(0,0,0), 2, 0.8, new Vec3d(0.5), 0.5, 0, 0.2, 0.5, 0.5, 10));
                        scene.addLight(new PointLight(new Vec3d(0, 20, 10), new Vec3d(0.8,0.8,0.6), 3));
                        break;
                    case 3: // Textures
                        scene.clearScene();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        scene.addLight(new PointLight(new Vec3d(0, 20, 10), new Vec3d(0.8,0.8,0.6), 3));
                        Sphere s = new Sphere(new Vec3d(0,4,0), 2, new Vec3d(0.2, 0.9, 0.9), 0.3, 0, 0.5, 0.2, 0.8, 30);
                        s.loadTexture("Earth.jpg");

                        Parallelepiped p = new Parallelepiped(new Vec3d(-1, 7, -1), new Vec3d(2,0,0), new Vec3d(0,2,0), new Vec3d(0,0,2), new Vec3d(0.9, 0.2, 0.9), 0.3, 0, 0.5, 0.2, 0.8, 30);
                        p.loadTexture("Crate.jpg");

                        scene.addSolid(s);
                        scene.addSolid(p);

                        break;
                    case 4: // error Textures
                        scene.clearScene();
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        scene.addLight(new PointLight(new Vec3d(0, 20, 10), new Vec3d(0.8,0.8,0.6), 3));
                        Sphere s1 = new Sphere(new Vec3d(0,4,0), 2, new Vec3d(0.2, 0.9, 0.9), 0.3, 0, 0.5, 0.2, 0.8, 30);
                        s1.loadTexture("x.jpg");

                        Parallelepiped p1 = new Parallelepiped(new Vec3d(-1, 7, -1), new Vec3d(2,0,0), new Vec3d(0,2,0), new Vec3d(0,0,2), new Vec3d(0.9, 0.2, 0.9), 0.3, 0, 0.5, 0.2, 0.8, 30);
                        p1.loadTexture("x.jpg");

                        scene.addSolid(s1);
                        scene.addSolid(p1);

                        break;
                    case 5: // Mesh
                        scene.clearScene();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();
                        scene.addSolid(new Mesh("monkey.obj", new Vec3d(0), PI/2, 0, 0, new Vec3d(0.8, 0.8, 0.8), 0.4, 0, 0.2, 0.5, 0.8, 20));

                        scene.addLight(new PointLight(new Vec3d(0, 20, 10), new Vec3d(0.8,0.8,0.6), 3));
                        break;
                    case 6: // Phong showcase
                        scene.clearScene();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        scene.addLight(new DirectionalLight(new Vec3d(0, -1, -1), new Vec3d(0.8,0.8,0.8), 0.5));
                        for(int i=1; i<10; i++){
                            scene.addSolid(new Sphere(new Vec3d(0,i*2.5,0), 1, new Vec3d(0.4, 0.4, 0.4), 0, 0, 0.4, 0.1, 0.8, i*10));
                        }
                        break;
                    case 7: // Lambertian showcase
                        scene.clearScene();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        scene.addLight(new DirectionalLight(new Vec3d(0, -1, -1), new Vec3d(0.8,0.8,0.8), 0.5));
                        for(int i=1; i<10; i++){
                            scene.addSolid(new Sphere(new Vec3d(0,i*2.5,0), 1, new Vec3d(0.4, 0.4, 0.4), 0, 0, 0.4, 0.1*i, 0.8, 10));
                        }
                        break;
                    case 8: // reflectivity showcase
                        scene.clearScene();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        scene.addLight(new DirectionalLight(new Vec3d(0, -1, -1), new Vec3d(0.8,0.8,0.8), 0.5));
                        for(int i=0; i<11; i++){
                            scene.addSolid(new Sphere(new Vec3d(0,i*2.5,0), 1, new Vec3d(0.4, 0.4, 0.4), i*0.1, 0, 1, 0, 0, 10));
                        }
                        break;
                    case 9: // reflectivity showcase
                        scene.clearScene();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        scene.addLight(new DirectionalLight(new Vec3d(0, -1, -1), new Vec3d(0.8,0.8,0.8), 0.5));
                        for(int i=0; i<10; i++){
                            scene.addSolid(new Sphere(new Vec3d(0,i*2.5,0), 1, new Vec3d(0.4, 0.4, 0.4), 0.5, 0.05*i, 1, 0, 0, 10));
                        }
                        break;
                    case 10: // albedo showcase
                        scene.clearScene();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        for(int i=0; i<11; i++){
                            scene.addSolid(new Sphere(new Vec3d(0,i*2.5,0), 1, new Vec3d(0.4), 0.2, 0, 0.1*i, 0, 0, 10));
                        }
                        break;
                    case 11: // refraction showcase
                        scene.clearScene();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        scene.addLight(new DirectionalLight(new Vec3d(0, -1, -1), new Vec3d(0.8,0.8,0.8), 0.5));
                        Sphere s11;

                        for(int i=0; i<11; i++) {
                            s11 = new Sphere(new Vec3d(0, i * 2.5, 0), 1, new Vec3d(0.4), 0.2, 0, 0.1, 0, 0, 10);
                            s11.makeTransparent(0.6 + i * 0.2);

                            scene.addSolid(s11);
                        }

                        break;
                    case 12: // many items
                        scene.clearScene();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        scene.addSolid(new Torus(new Vec3d(0,0,0), 2, 0.8, new Vec3d(0.5), 0.6, 0, 0.2, 0.5, 0.5, 10));
                        scene.addSolid(new Sphere(new Vec3d(10,8,0), 2, new Vec3d(0.2, 0.9, 0.9), 0.3, 0, 0.5, 0.2, 0.4, 10));
                        scene.addSolid(new Parallelepiped(new Vec3d(-1, 7, -1), new Vec3d(2,0,1), new Vec3d(0,2,1), new Vec3d(0,0,2), new Vec3d(0.9, 0.2, 0.9), 0.3, 0, 0.5, 0.2, 0.4, 10));

                        Sphere s2 = new Sphere(new Vec3d(5,5,0), 2, new Vec3d(0.2, 0.9, 0.9), 0.3, 0, 0.5, 0.2, 0.8, 30);
                        s2.loadTexture("Earth.jpg");
                        scene.addSolid(s2);

                        Parallelepiped p2 = new Parallelepiped(new Vec3d(7, 12, -1), new Vec3d(2,0,0), new Vec3d(0,2,0), new Vec3d(0,0,2), new Vec3d(0.9, 0.2, 0.9), 0.3, 0, 0.5, 0.2, 0.8, 30);
                        p2.loadTexture("Crate.jpg");
                        scene.addSolid(p2);

                        scene.addSolid(new Plane(new Vec3d(0,0,-3), new Vec3d(0,0,1), new Vec3d(0.8), 0.1, 0.2, 0.2, 0.4, 0.2, 40));

                        scene.addLight(new PointLight(new Vec3d(0, 20, 10), new Vec3d(0.8,0.8,0.6), 3));

                        break;
                    case 13: // cornell box
                        scene.clearScene();
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            throw new RuntimeException(ex);
                        }
                        scene.clearScene();

                        scene.addLight(new SphereLight(new Vec3d(0, 0, 0), 1, new Vec3d(0.6,0.6,0.6), 10));

                        scene.addSolid(new Triangle(new Vec3d(-10,-10,-10), new Vec3d(-10,10,-10),new Vec3d(10,-10,-10), new Vec3d(0.8), 0.1, 0.05, 0.2, 0.5, 0.2, 10));
                        scene.addSolid(new Triangle(new Vec3d(10,-10,-10), new Vec3d(-10,10,-10), new Vec3d(10,10,-10), new Vec3d(0.8), 0.1, 0.05, 0.2, 0.5, 0.2, 10));

                        scene.addSolid(new Triangle(new Vec3d(10,-10,-10), new Vec3d(-10,-10,10), new Vec3d(-10,-10,-10), new Vec3d(0.8, 0.2, 0.2), 0.1, 0.05, 0.2, 0.5, 0.2, 10));
                        scene.addSolid(new Triangle(new Vec3d(10,-10,10), new Vec3d(-10,-10,10), new Vec3d(10,-10,-10), new Vec3d(0.8, 0.2, 0.2), 0.1, 0.05, 0.2, 0.5, 0.2, 10));

                        scene.addSolid(new Triangle(new Vec3d(-10,-10,-10), new Vec3d(-10,-10,10), new Vec3d(-10,10,-10), new Vec3d(0.2,0.8,0.2), 0.1, 0.05, 0.2, 1, 0.2, 10));
                        scene.addSolid(new Triangle(new Vec3d(-10,10,-10), new Vec3d(-10,-10,10), new Vec3d(-10,10,10), new Vec3d(0.2,0.8,0.2), 0.1, 0.05, 0.2, 1, 0.2, 10));

                        scene.addSolid(new Triangle(new Vec3d(10,-10,-10), new Vec3d(10,10,-10), new Vec3d(10,-10,10), new Vec3d(0.2,0.2,0.8), 0.1, 0.05, 0.2, 1, 0.2, 10));
                        scene.addSolid(new Triangle(new Vec3d(10,10,-10), new Vec3d(10,10,10), new Vec3d(10,-10,10), new Vec3d(0.2,0.2,0.8), 0.1, 0.05, 0.2, 1, 0.2, 10));

                        scene.addSolid(new Triangle(new Vec3d(-10,10,10), new Vec3d(-10,-10,10), new Vec3d(10,-10,10),  new Vec3d(0.8), 0.1, 0.05, 0.2, 1, 0.5, 10));
                        scene.addSolid(new Triangle(new Vec3d(10,-10,10), new Vec3d(10,10,10), new Vec3d(-10,10,10),   new Vec3d(0.8), 0.1, 0.05, 0.2, 1, 0.2, 10));

                        scene.addSolid(new Sphere(new Vec3d(0,0,-7.5), 2.5, new Vec3d(0.4, 0.4, 0.4), 0.2, 0, 0.4, 0.5, 2, 30));
                        Sphere s13 = new Sphere(new Vec3d(3,3,-8.5), 1.5, new Vec3d(0.4, 0.4, 0.4), 0.2, 0, 0.4, 0.5, 2, 30);
                        s13.makeTransparent(1.5);

                        scene.addSolid(s13);

                        Parallelepiped p13 = new Parallelepiped(new Vec3d(5, -5, -10), new Vec3d(2,1,0), new Vec3d(-1,2,0), new Vec3d(0,0,7), new Vec3d(0.9, 0.2, 0.9), 0.3, 0, 0.5, 0.2, 0.8, 30);
                        scene.addSolid(p13);

                        break;
                    default:
                        break;
                }
            }
        });

        JPanel renderingPanel = new JPanel();
        renderingPanel.add(new JLabel("RENDERING IMAGE"));
        add(renderingPanel);

        JPanel renderPanel = new JPanel();
        renderPanel.setLayout(new GridLayout(1, 4));

        JPanel renderHeight = new JPanel();
        renderHeight.add(new JLabel("Image height: "));
        renderHeightVal = new JTextField("1080");
        renderHeightVal.setPreferredSize(new Dimension(50, 20));
        renderHeight.add(renderHeightVal);

        JPanel renderWidth = new JPanel();
        renderWidth.add(new JLabel("Image width: "));
        renderWidthVal = new JTextField("1920");
        renderWidthVal.setPreferredSize(new Dimension(50, 20));
        renderWidth.add(renderWidthVal);

        JPanel renderAliasing = new JPanel();
        renderAliasing.add(new JLabel("Aliasing (samples per pixel): "));
        renderAliasingVal = new JTextField("2");
        renderAliasingVal.setPreferredSize(new Dimension(50, 20));
        renderAliasing.add(renderAliasingVal);

        JPanel renderDepth = new JPanel();
        renderDepth.add(new JLabel("Max depth (ray reflections): "));
        renderDepthVal = new JTextField("5");
        renderDepthVal.setPreferredSize(new Dimension(50, 20));
        renderDepth.add(renderDepthVal);

        add(renderHeight);
        add(renderWidth);
        add(renderAliasing);
        add(renderDepth);

//        add(renderPanel);
        buttonClearScene = new JButton("Clear Scene");
        add(buttonClearScene);

        buttonRenderImage = new JButton("Render Image");
        add(buttonRenderImage);

        buttonRenderImage.addActionListener(e -> {
            try {
                viewport.renderToImage(Integer.parseInt(renderWidthVal.getText()),
                                       Integer.parseInt(renderHeightVal.getText()),
                                       Integer.parseInt(renderDepthVal.getText()),
                                       Integer.parseInt(renderAliasingVal.getText()));
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(viewport, ex.toString(), "Could not save image", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonClearScene.addActionListener(e -> {
            viewport.getScene().clearScene();
        });
    }

}
