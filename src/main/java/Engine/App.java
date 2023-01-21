package Engine;


import com.sun.prism.image.ViewPort;

import javax.swing.*;

// main app class
public class App {

    JFrame frame;

    public App(int width_, int height_){

        frame = new JFrame("RayTracing");
        frame.setSize(width_, height_);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JDialog settings = new JDialog(frame, "Settings");

        Viewport viewport = new Viewport(frame, settings);
        frame.add(viewport);

        SettingsMenu settingsMenu = new SettingsMenu(viewport);

        settings.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        settings.setSize(350, 600);
        settings.add(settingsMenu);
        settings.setVisible(true);
        
        frame.setVisible(true);

        viewport.runLoop();
    }
}
