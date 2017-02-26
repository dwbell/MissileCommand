package pkg.missile.framework;

import java.awt.Color;
import java.awt.Graphics;

public class Asteroid extends VectorObject {

    public Asteroid(Matrix3x3f viewport, RelativeMouseInput mouse, KeyboardInput keyboard) {
        super(viewport, mouse, keyboard);
        initialize();
    }

    private void initialize() {
        //Asteroid Shape (8 sided)
        polygon = new Vector2f[]{new Vector2f(400, -800), new Vector2f(-400, -800), new Vector2f(-800, -400),
            new Vector2f(-800, 400), new Vector2f(-400, 800), new Vector2f(400, 800), new Vector2f(800, 400), new Vector2f(800, -400)};

       //World initialize
        this.world = new Matrix3x3f();

    }

    @Override
    public void processInput(Vector2f m) {
        this.tx = m.x;
        this.ty = m.y;
    }

    @Override
    public void updateWorld(Matrix3x3f viewport) {
        //Translations
        world = Matrix3x3f.translate(0,10000);
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
