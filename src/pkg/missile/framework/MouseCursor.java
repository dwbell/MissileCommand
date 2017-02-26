package pkg.missile.framework;

import java.awt.Color;
import java.awt.Graphics;

public class MouseCursor extends VectorObject {

    private Vector2f[] polycpy;

    public MouseCursor(int spawnX, int spawnY, Matrix3x3f viewport, RelativeMouseInput mouse, KeyboardInput keyboard) {
        super(spawnX, spawnY, viewport, mouse, keyboard);
        initialize();

    }

    private void initialize() {
        //Crosshair cursor
        this.polygon = new Vector2f[]{new Vector2f(-5, 0), new Vector2f(0, 0), new Vector2f(0, -5),
            new Vector2f(0, 0), new Vector2f(5, 0), new Vector2f(0, 0), new Vector2f(0, 5)};
        this.world = new Matrix3x3f();

    }

    @Override
    public void processInput(Vector2f m) {
        this.tx = m.x;
        this.ty = m.y;
        System.out.println(m.x);
        System.out.println(m.y);
    }

    @Override
    public void updateObjects(float delta, Matrix3x3f viewport, float width, float height) {
        this.viewport = viewport;
    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.BLACK);
        world = viewport.mul(Matrix3x3f.translate(tx, ty));
        drawPolygon(g, polygon);

    }

    protected void onWindowClosing() {
    }

    private void drawPolygon(Graphics g, Vector2f[] polygon) {
        Vector2f P;
        Vector2f S = polygon[polygon.length - 1];
        for (int i = 0; i < polygon.length; ++i) {
            P = polygon[i];
            g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
            S = P;
        }
    }
}
