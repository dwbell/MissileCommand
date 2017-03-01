package pkg.missile.framework;

import java.awt.Color;
import java.awt.Graphics;

public class Building extends VectorObject {

    private boolean isAlive;                                //Is it alive?
    private int leftBound;                                  //Bound left for collision
    private int rightBound;                              //Bound right for collision

    public Building(int spawnX, int spawnY, Matrix3x3f viewport, RelativeMouseInput mouse, KeyboardInput keyboard) {
        super(spawnX, spawnY, viewport, mouse, keyboard);
        this.isAlive = true;
        this.leftBound = spawnX - 1500;
        this.rightBound = spawnX + 1500;

        initialize();
    }

    /*
    Name; initialize
    Param: N/A
    Desc: Sets up the building vector array, its x,y location and the world
    matrix for transforms.
    */
    private void initialize() {
        //Building shape, centered to 0,0, that are 3000 units long, so 10 buildings can be evenly placed on screen
        polygon = new Vector2f[]{new Vector2f(-1500, 0), new Vector2f(-1500, 2000), new Vector2f(-500, 2000), new Vector2f(-500, 1000),
            new Vector2f(0, 1000), new Vector2f(0, 2500), new Vector2f(750, 2500), new Vector2f(750, 1300), new Vector2f(1500, 1300), new Vector2f(1500, 0)};

        //Translation variables
        this.tx = spawnX;
        this.ty = spawnY;

        //World initialize
        this.world = new Matrix3x3f();

    }

    /*
    Name; processInput
    Param: Vector2f m
    Desc: Not needed
    */
    @Override
    public void processInput(Vector2f m) {

    }

    /*
    Name; updateObjects
    Param: float delta: time, Matrix3x3f viewport: viewport scalar, 
    float width: appWorldWidth, float height: appWorldHeight
    Desc: Applies transforms to the buildings (scalar transforms)
    */
    @Override
    public void updateObjects(float delta, Matrix3x3f viewport, float width, float height) {

        //Translations
        world = Matrix3x3f.translate(this.tx, this.ty);
        //Multiply by viewport scalar
        world = world.mul(viewport);
    }

    /*
    Name; render
    Param: Graphics g
    Desc: Renders the buildings. 
    */
    @Override
    public void render(Graphics g) {
        g.setColor(Color.GREEN);
        Vector2f S = world.mul(polygon[polygon.length - 1]);
        Vector2f P = null;
        for (int i = 0; i < polygon.length; ++i) {
            P = world.mul(polygon[i]);
            g.drawLine((int) S.x, (int) S.y, (int) P.x, (int) P.y);
            S = P;
        }
    }

    /*
    Name; getLeftBound
    Param: N/A
    Desc: Getter to return buildings individiaul
    left bound x coordinate
    */
    public int getLeftBound() {
        return this.leftBound;
    }
    
    /*
    Name; getRightBound
    Param: N/A
    Desc: Getter to return buildings individiaul
    right bound x coordinate
    */
    public int getRightBound() {
        return this.rightBound;
    }
}
