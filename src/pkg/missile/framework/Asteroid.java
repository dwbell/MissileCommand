package pkg.missile.framework;

import java.awt.Color;
import java.awt.Graphics;

public class Asteroid extends VectorObject {

    private float falling;            //Holds  -9.8m/s^2 to terminal velocity values
    private boolean isAlive;    //Is it alive?

    public Asteroid(int spawnX, int spawnY, Matrix3x3f viewport, RelativeMouseInput mouse, KeyboardInput keyboard) {
        super(spawnX, spawnY, viewport, mouse, keyboard);
        this.falling = 0;
        this.isAlive = true;

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

    }

    @Override
    public void updateObjects(float delta, Matrix3x3f viewport, float width, float height) {
        //Setting a "fake terminal velocity"
        if (ty > 4000) {
            //Acceleration of gravity
            ty -= (falling += Math.pow(9.80665f, 2) * delta);
        } else {
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
        System.out.println(tx);
    }

    public boolean isAlive() {
        return this.isAlive;
    }
}
