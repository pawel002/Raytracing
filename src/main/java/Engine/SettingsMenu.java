package Engine;

import Raytracer.Skybox;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;

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
        sceneComboBox.addItem("name1");
        sceneComboBox.addItem("name2");
        sceneComboBox.addItem("name3");
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

        // add options for changing scene

        JPanel renderingPanel = new JPanel();
        renderingPanel.add(new JLabel("RENDERING IMAGE"));
        add(renderingPanel);

        JPanel renderPanel = new JPanel();
        renderPanel.setLayout(new GridLayout(1, 4));

        JPanel renderHeight = new JPanel();
        renderHeight.add(new JLabel("Height: "));
        renderHeightVal = new JTextField("1080");
        renderHeight.add(renderHeightVal);

        JPanel renderWidth = new JPanel();
        renderWidth.add(new JLabel("Width: "));
        renderWidthVal = new JTextField("1920");
        renderWidth.add(renderWidthVal);

        JPanel renderAliasing = new JPanel();
        renderAliasing.add(new JLabel("Aliasing (spp): "));
        renderAliasingVal = new JTextField("5");
        renderAliasing.add(renderAliasingVal);

        JPanel renderDepth = new JPanel();
        renderDepth.add(new JLabel("Max depth: "));
        renderDepthVal = new JTextField("5");
        renderDepth.add(renderDepthVal);

        renderPanel.add(renderHeight);
        renderPanel.add(renderWidth);
        renderPanel.add(renderAliasing);
        renderPanel.add(renderDepth);

        add(renderPanel);

        buttonRenderImage = new JButton("Render Image");
        add(buttonRenderImage);

        buttonRenderImage.addActionListener(e -> {
            try {
                viewport.renderToImage(Integer.parseInt(renderWidthVal.getText()),
                                       Integer.parseInt(renderHeightVal.getText()),
                                       Integer.parseInt(renderDepthVal.getText()));
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(viewport, ex.toString(), "Could not save image", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

}
