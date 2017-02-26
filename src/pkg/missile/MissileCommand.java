package pkg.missile;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Random;
import pkg.missile.framework.*;

public class MissileCommand extends SimpleFramework {

    private double timer;
    private double spawnTimer;
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
        appWorldWidth = 53000.0f;   //Maintain wide aspect ratio for satellite orbit
        appWorldHeight = 30000f;    //Meters to typical satellite orbit
    }

    @Override
    protected void initialize() {
        super.initialize();

        //Single asteroid for initialization
        timer = 0.0d;
        //Set to spawn a new asteroid every ~2 seconds intially
        spawnTimer = 2.0d;

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
        //mouseCursor.updateWorld(getViewportTransform());
        asteroidsUpdate(delta);

    }

    public void asteroidsUpdate(float delta) {
        //Timer to hold clock information
        timer += delta;
        //Check if appropiate amount of time has passed
        if (timer > spawnTimer) {
            //Setting asteroid spawn points, within screen bounds and accounting for asteroid size
            int spawnX = (int) (-1 * (appWorldWidth / 2) + 800) + (int) (Math.random() * ((appWorldWidth / 2 - 800) - (-1 * (appWorldWidth / 2) + 800) + 1));
            int spawnY = (int) (appWorldHeight / 2) + 1000;
            //Add asteroid with new random spawn point
            asteroids.add(new Asteroid(spawnX, spawnY, getViewportTransform(), mouse, null));
            //Reset timer to 0, to wait for next interval
            timer = 0;
            //Increase asteroid spawn times, and sets a cap
            if (spawnTimer > .28d) {
                spawnTimer -= .03;
            }
        }
        System.out.println(spawnTimer);

        //Loop through all asteroids and update them accordingly
        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).updateObjects(delta, getViewportTransform(), appWorldWidth, appWorldHeight);

            //Remove asteroids past bottom of screen
            if (!asteroids.get(i).isAlive()) {
                asteroids.remove(i);
            }
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
