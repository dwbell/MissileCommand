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

    private int score = 0;

    /*Asteroids variables*/
    private double astTimer;                                                                         //Adds nanoseconds from delta to accumlate real time
    private final double SPAWN_TIMER = 2.0d;                                       //Spawn every ~2 seconds to start
    private final double SPAWN_TIMER_CAP = .5d;                               //Cap set to spawn at ~.28 seconds
    private final double SPAWN_DECREMENT = .04d;                          //Spawn time decrement
    private final float WIND_SPEED = 30.0f;                                              //Maximum wind change
    private double spawnTimer;                                                                 //Current interval between each new asteroid spawn
    private ArrayList<Asteroid> asteroids = new ArrayList<>();      //Container for all asteroids

    /*Wind variables*/
    private float wind;                                                                                     //Holds current wind status
    private double windTimer;                                                                   //Adds nanoseconds from delta to accumlate real time

    /*Mouse Cursor*/
    private MouseCursor crossHair;

    /*City Buildings*/
    private ArrayList<Building> buildings = new ArrayList<>();
    private int buildingSpawnX = -24000;
    private int buildingSpawnY = -15000;
    private final int NUM_BUILDINGS = 17;

    public MissileCommand() {
        appBackground = Color.BLACK;
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

    /*
    Name; initialize
    Param: N/A
    Desc: Initializes variables, and sets up the buildings list.
    */
    @Override
    protected void initialize() {
        super.initialize();
        astTimer = 0.0d;
        windTimer = 0.0d;
        spawnTimer = SPAWN_TIMER;
        this.wind = 0.0f;
        this.crossHair = new MouseCursor((int) appWorldWidth / 2, (int) appWorldHeight / 2, getViewportTransform(), mouse, null);

        //Initialize the buildings
        for (int i = 0; i < NUM_BUILDINGS; i++) {
            buildings.add(new Building(buildingSpawnX, buildingSpawnY, getViewportTransform(), null, null));
            buildingSpawnX += 3000;
        }

        disableCursor();

    }

    /*
    Name; disableCursor
    Param: N/A
    Desc: Disables the default cursor to allow for the crosshair one to be drawn
    */
    public void disableCursor() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image image = tk.createImage("");
        Point point = new Point(0, 0);
        String name = "Crosshair";
        Cursor cursor = tk.createCustomCursor(image, point, name);
        setCursor(cursor);
    }

    /*
    Name; processInput
    Param: float delta
    Desc: Updates the cursor, to be set to a crosshair, also
    allows for processing of hit detection via the asteroids method.
    */
    @Override
    protected void processInput(float delta) {
        super.processInput(delta);
        crossHair.processInput(getWorldMousePosition());

        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).processInput(getWorldMousePosition());
        }
    }

    /*
    Name; updateObjects
    Param: float delta
    Desc: This method calls invokes specific class methods to update the objects. 
    */
    @Override
    protected void updateObjects(float delta) {
        crossHair.updateObjects(delta, getViewportTransform(), appWorldWidth, appWorldHeight);

        asteroidsUpdate(delta);
        buildingsUpdate(delta);
        checkCollisions();

    }

    /*
    Name; asteroidUpdate
    Param: float delta
    Desc: Does all of the work for the various asteroid objects. 
    Uses two timers: astTimer, which decreases time intervals to 
    spawn new asteroids. And wind timer which is set to operate
    at a specified time interval to update wind. There is also
    bottom bounds collision detection to remove asteroids
    past the bottom of the screen. Score updating is done
    via this method as well, simply because the asteroids are a
    crucial object in this program.
    */
    public void asteroidsUpdate(float delta) {
        //Timer to hold clock information
        astTimer += delta;
        //Check if appropiate amount of time has passed

        if (astTimer > spawnTimer) {
            //Spawn random number of asteroid (1-3) at a time
            int rng = (int) (Math.random() * 3) + 1;
            for (int i = 0; i < (rng); i++) {
                //Setting asteroid spawn points, within screen bounds and accounting for asteroid size
                int spawnX = (int) (-1 * (appWorldWidth / 2) + 800) + (int) (Math.random() * ((appWorldWidth / 2 - 800) - (-1 * (appWorldWidth / 2) + 800) + 1));
                //Spawn Y 900-5500 meters above screen to give a more random feel to asteroids
                int spawnY = (int) (appWorldHeight / 2) + (900 + (int) (Math.random() * ((5500 - 900) + 1)));
                //Add asteroid with new random spawn point
                asteroids.add(new Asteroid(spawnX, spawnY, getViewportTransform(), mouse, null));
            }
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
            wind = (float) (-1 * (WIND_SPEED)) + (float) (Math.random() * ((WIND_SPEED) - (-1 * (WIND_SPEED)) + 1));;
            //Reset timer and wait for next interval
            windTimer = 0;
        }

        //Loop through all asteroids and update them accordingly
        for (int i = 0; i < asteroids.size(); i++) {
            //Update all asteroid with world information
            asteroids.get(i).updateObjects(delta, getViewportTransform(), appWorldWidth, appWorldHeight);
            //Set wind on asteroids
            asteroids.get(i).setWind(wind);
            //Remove "dead" asteroids
            if (!asteroids.get(i).isAlive()) {
                asteroids.remove(i);
            }

            //Check if asteroid was shot down via clicking and apply score
            if (asteroids.get(i).isShotDown()) {
                if (buildings.size() > 0) {
                    score = ((score + 1) + buildings.size());
                }else {
                    score = score + 1;
                }
                asteroids.remove(i);
            }
        }
    }

    /*
    Name; building update
    Param: float delta
    Desc: Updates the buildings, basically just allowing scaling via the viewport ratio
    */
    public void buildingsUpdate(float delta) {
        for (int i = 0; i < buildings.size(); i++) {
            buildings.get(i).updateObjects(delta, getViewportTransform(), appWorldWidth, appWorldHeight);
        }

    }
    /*
    Name; checkCollision
    Param: N/A
    Desc: Checks all asteroids against a buildings left and right x values, as well as
    an asteroid y value to so see if it passes through and collides with a building.
    If it does, then remove that building.
    */
    public void checkCollisions() {
        for (int i = 0; i < asteroids.size(); i++) {
            for (int j = 0; j < buildings.size(); j++) {
                if (asteroids.get(i).getX() < buildings.get(j).getRightBound() && asteroids.get(i).getX() > buildings.get(j).getLeftBound() && asteroids.get(i).getY() < (-1 * (appWorldHeight / 2) + 200)) {
                    buildings.remove(j);
                }
            }
        }
    }

    /*
    Name; render
    Param: Graphics g
    Desc: Draws up the wind, the score, and building multiplier. 
    Will also make the render method calls to update the cursor,
    asteroids, and buildings. 
    */
    @Override
    protected void render(Graphics g) {
        super.render(g);
        
        g.setColor(Color.GREEN);
        String w = String.format("%.2f", wind);
        g.drawString("Wind: " + w, 20, 20);
        g.drawString("Score: " + score, 20, 40);
        g.drawString("Building Multiplier: " + buildings.size(), 20, 60);

        //Render the mouse crosshair
        crossHair.render(g);
        
        //Render asteroids
        for (int i = 0; i < asteroids.size(); i++) {
            asteroids.get(i).render(g);
        }
        //Render buildings
        for (int i = 0; i < buildings.size(); i++) {
            buildings.get(i).render(g);
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
