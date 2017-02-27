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

    /*Asteroids variables*/
    private double astTimer;                                                                         //Adds nanoseconds from delta to accumlate real time
    private final double SPAWN_TIMER = 2.0d;                                       //Spawn every ~2 seconds to start
    private final double SPAWN_TIMER_CAP = .3d;                               //Cap set to spawn at ~.28 seconds
    private final double SPAWN_DECREMENT = .04d;                          //Spawn time decrement
    private double spawnTimer;                                                                 //Current interval between each new asteroid spawn
    private ArrayList<Asteroid> asteroids = new ArrayList<>();      //Container for all asteroids

    /*Wind variables*/
    private float wind;                                                                                    //Holds current wind status
    private double windTimer;                                                                  //Adds nanoseconds from delta to accumlate real time

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
        appWorldWidth = 53000.0f;   //Maintain wide aspect ratio w.r.t. satellite orbit altitude
        appWorldHeight = 30000f;    //Meters to typical satellite orbit (~300 km)
    }

    @Override
    protected void initialize() {
        super.initialize();
        astTimer = 0.0d;
        windTimer = 0.0d;
        spawnTimer = SPAWN_TIMER;
        this.wind = 0.0f;

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
        astTimer += delta;
        //Check if appropiate amount of time has passed
        if (astTimer > spawnTimer) {
            //Setting asteroid spawn points, within screen bounds and accounting for asteroid size
            int spawnX = (int) (-1 * (appWorldWidth / 2) + 800) + (int) (Math.random() * ((appWorldWidth / 2 - 800) - (-1 * (appWorldWidth / 2) + 800) + 1));
            int spawnY = (int) (appWorldHeight / 2) + 1000;
            //Add asteroid with new random spawn point
            asteroids.add(new Asteroid(spawnX, spawnY, getViewportTransform(), mouse, null));
            //Reset timer to 0, to wait for next interval
            astTimer = 0;
            //Increase asteroid spawn times, and sets a cap
            if (spawnTimer > SPAWN_TIMER_CAP) {
                spawnTimer -= SPAWN_DECREMENT;
            }
        }

        //Timer to hold clock information
        windTimer += delta;
        //Check if appropiate amount of time has passed (~15 seconds)
        if (windTimer > 15.0d) {
            //Setting wind to a random number between -30 and 30 inclusive
            wind = (float) (-1 * (30)) + (float) (Math.random() * ((30) - (-1 * (30)) + 1));
            //Reset timer and wait for next interval
            windTimer = 0;
        }

        //Loop through all asteroids and update them accordingly
        for (int i = 0; i < asteroids.size(); i++) {
            //Update all asteroid with world information
            asteroids.get(i).updateObjects(delta, getViewportTransform(), appWorldWidth, appWorldHeight);
            //Set wind on asteroids
            asteroids.get(i).setWind(wind);
            //Remove asteroids past bottom of viewport
            if (!asteroids.get(i).isAlive()) {
                asteroids.remove(i);
            }
        }
    }

    @Override
    protected void render(Graphics g) {
        super.render(g);

        g.drawString("Wind: " + String.valueOf(wind), 20, 40);

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
