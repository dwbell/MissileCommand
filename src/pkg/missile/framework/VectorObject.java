package pkg.missile.framework;

import java.awt.Graphics;

public abstract class VectorObject {

    protected Vector2f[] polygon;                         //Objects defined points
    protected Matrix3x3f world;                            //Objects transformation matrix
    protected Matrix3x3f viewport;                     //Viewport scaling matrix
    protected int width;                                           //Screen width
    protected int height;                                         //Screen height
    protected Vector2f velocity;                           //Vector for velocity
    protected float tx;                                               //Translation position x
    protected float ty;                                               //Translation position y
    protected RelativeMouseInput mouse;     //Mouse input
    protected KeyboardInput keyboard;         //Keyboard input
    protected float rot;                                            //Rotation
    protected float rotStep;                                  //Rotation Steps

    public VectorObject(Matrix3x3f viewport, RelativeMouseInput mouse, KeyboardInput keyboard) {
        this.viewport = viewport;
        this.mouse = mouse;
        this.keyboard = keyboard;
    }

    public abstract void processInput(Vector2f m);

    public abstract void updateWorld(float delta, Matrix3x3f viewport, float width, float height);

    public abstract void render(Graphics g);

}
