package pkg.missile;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import pkg.missile.framework.*;

public class MissileCommand extends SimpleFramework {

    private ArrayList<Asteroid> asteroids = new ArrayList<>();

    //  private static VectorObject asteroid;
    public MissileCommand() {
        appBackground = Color.WHITE;
        appBorder = Color.LIGHT_GRAY;
        appFont = new Font("Courier New", Font.PLAIN, 14);
        appBorderScale = .95f;
        appFPSColor = Color.BLACK;
        appWidth = 896;
        appHeight = 504;
        appMaintainRatio = true;
        appSleep = 10L;
        appTitle = "Missile Command";
        appWorldWidth = 53000.0f;   //Maintain wide aspect ratio
        appWorldHeight = 30000f;    //Meters to typical satellite orbit
    }

    @Override
    protected void initialize() {
        super.initialize();
        //asteroid = new Asteroid(getViewportTransform(), mouse, null);
        asteroids.add(new Asteroid(getViewportTransform(), mouse, null));
    }

    public void disableCursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("");
        Point point = new Point(0, 0);
        String name = "Crosshair";
        Cursor cursor = tk.createCustomCursor(image, point, name);
        setCursor(cursor);
    }

    @Override
    protected void processInput(float delta) {
        super.processInput(delta);
        //mouseCursor.processInput(getWorldMousePosition());

        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).processInput(getWorldMousePosition());
        }

    }

    @Override
    protected void updateObjects(float delta) {
        super.updateObjects(delta);
        //mouseCursor.updateWorld(getViewportTransform());

        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).updateWorld(getViewportTransform());
        }

    }

    @Override
    protected void render(Graphics g) {
        super.render(g);
        //mouseCursor.render(g);

        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).render(g);
        }
    }

    @Override
    protected void terminate() {
        super.terminate();
    }

    public static void main(String[] args) {
        launchApp(new MissileCommand());
    }
}
