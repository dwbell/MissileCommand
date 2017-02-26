package pkg.missile.framework;

import java.awt.Color;
import java.awt.Graphics;

public class Asteroid extends VectorObject {

    private float falling;

    public Asteroid(Matrix3x3f viewport, RelativeMouseInput mouse, KeyboardInput keyboard) {
        super(viewport, mouse, keyboard);
        this.falling = 0;
        initialize();
    }

    private void initialize() {
        //Asteroid Shape (8 sided)
        polygon = new Vector2f[]{new Vector2f(400, -800), new Vector2f(-400, -800), new Vector2f(-800, -400),
            new Vector2f(-800, 400), new Vector2f(-400, 800), new Vector2f(400, 800), new Vector2f(800, 400), new Vector2f(800, -400)};
        this.tx = 0;
        this.ty = 16000;    //Start above screen to gain momentum
        this.velocity = new Vector2f();

        //World initialize
        this.world = new Matrix3x3f();

    }

    @Override
    public void processInput(Vector2f m) {

    }

    @Override
    public void updateWorld(float delta, Matrix3x3f viewport, float width, float height) {
        //Setting a "terminal velocity"
        if (ty > 5000) {
            //Estimate of gravities acceleration
            ty -= (falling += Math.pow(9.80665f, 2) * delta);
        } else {
            ty -= falling;
        }
        System.out.printf("Falling %f\n", falling);
        System.out.printf("ty %f", ty);
        Matrix3x3f mat = Matrix3x3f.translate(0.0f, ty);
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
}
