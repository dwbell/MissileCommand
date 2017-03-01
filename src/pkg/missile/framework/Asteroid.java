package pkg.missile.framework;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

public class Asteroid extends VectorObject {

    private float falling;                                  //Holds  -9.8m/s^2 to terminal velocity values
    private boolean isAlive;                          //Is it alive?
    private int distFallen;                              //Distance fallen
    private final int TERMINAL_V = 11000;   //Terminal velocity 

    public Asteroid(int spawnX, int spawnY, Matrix3x3f viewport, RelativeMouseInput mouse, KeyboardInput keyboard) {
        super(spawnX, spawnY, viewport, mouse, keyboard);
        this.falling = 0;
        this.isAlive = true;
        this.distFallen = 0;

        initialize();
    }

    private void initialize() {
        //Asteroid Shape (8 sided)
        polygon = new Vector2f[]{new Vector2f(400, -800), new Vector2f(-400, -800), new Vector2f(-800, -400),
            new Vector2f(-800, 400), new Vector2f(-400, 800), new Vector2f(400, 800), new Vector2f(800, 400), new Vector2f(800, -400)};
        //Translation variables
        this.tx = spawnX;
        this.ty = spawnY;    //Start above screen to gain momentum
        this.velocity = new Vector2f();

        //World initialize
        this.world = new Matrix3x3f();

    }

    @Override
    public void processInput(Vector2f m) {

        //Check if mouse within bounds, if so, kill it
        if (mouse.buttonDown(MouseEvent.BUTTON1)) {
            if (m.x < this.tx + 800 && m.x > this.tx - 800 && m.y < this.ty + 800 && m.y > this.ty - 800) {
                this.isAlive = false;
            }
        }

    }

    @Override
    public void updateObjects(float delta, Matrix3x3f viewport, float width, float height) {

        //Checking terminal velocity
        if (distFallen < TERMINAL_V) {
            //Acceleration of gravity
            ty -= (falling += Math.pow(9.80665f, 2) * delta);
            distFallen++;
        } else {
            //Terminal was reached, so stop increasing fall speed.
            ty -= falling;
        }

        if (ty < (-1 * (height / 2))) {
            this.isAlive = false;
        }

        Matrix3x3f mat = Matrix3x3f.translate(tx, ty);
        velocity = mat.mul(new Vector2f());

        //Translations
        world = Matrix3x3f.translate(velocity);
        //Multiply by viewport scalar
        world = world.mul(viewport);
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        Vector2f S = world.mul(polygon[polygon.length - 1]);
        Vector2f P = null;
        for (int i = 0; i < polygon.length; ++i) {
            P = world.mul(polygon[i]);
            g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
            S = P;
        }
    }

    public void setWind(float wind) {
        this.tx += wind;
    }

    public boolean isAlive() {
        return this.isAlive;
    }
}
