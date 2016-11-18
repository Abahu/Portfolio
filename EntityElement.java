package dm.com.realblacksmith;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;

public class EntityElement extends BasicElement {
    // Assumes SI units
    protected Animation animation;
    protected Vector2D iPosition;
    protected Vector2D position;
    protected Vector2D iVelocity;
    protected Vector2D velocity;
    protected Vector2D iAcceleration;
    protected Vector2D acceleration;
    protected Vector2D friction;
    protected float mass;
    protected long lastTime;
    protected boolean isStatic;
    protected Vector2D cameraFocus;
    protected boolean isInitialising = true;

    public EntityElement()
    {

    }
    public EntityElement(Animation anim) {
        super(anim.getImage(),
                new double[]{anim.getImage().getWidth(), anim.getImage().getHeight()},
                new double[]{0, 0});
        animation = anim;
        lastTime = System.nanoTime();
        friction = Entry.game.friction;
        // Values used by the game
        position = new Vector2D();
        velocity = new Vector2D();
        acceleration = new Vector2D();
        // Initial values for resetting
        iPosition = new Vector2D();
        iVelocity = new Vector2D();
        iAcceleration = new Vector2D();
        mass = 1;
    }

    public boolean getIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean b) {
        isStatic = b;
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void setCameraFocus(Vector2D v) {
        cameraFocus = v;
    }

    public void setForce(Vector2D force) {
        acceleration = force.m(1 / mass);
    }

    public void modForce(Vector2D force) {
        acceleration = acceleration.add(force.m(1 / mass));
    }

    public Vector2D getForce() {
        return acceleration.m(mass);
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public void setFriction(Vector2D FA) {
        friction = FA;
    }

    public void setPosition(Vector2D newPosition) {
        position = newPosition;
        if (isInitialising)
            iPosition = newPosition.clone();
    }

    public void setVelocity(Vector2D newVelocity) {
        velocity = newVelocity;
        if (isInitialising)
            iVelocity = newVelocity.clone();
    }

    public void setAcceleration(Vector2D newAcceleration) {
        acceleration = newAcceleration;
        if (isInitialising)
            iAcceleration = newAcceleration.clone();
    }

    public void setAnimation(Animation newAnimation) {
        animation = newAnimation;
    }

    public double getDistTo(EntityElement e) {
        return position.dist(e.getPosition());
    }

    public double getDistTo(Vector2D posV) {
        return position.dist(posV);
    }

    public void reset()
    {
        position = iPosition.clone();
        velocity = iVelocity.clone();
        acceleration = iAcceleration.clone();
        lastTime = System.nanoTime();
    }

    public int getType()
    {
        return Element.TYPE_ENTITY;
    }

    // TODO add map update functionality?
    @Override
    public void update() {
        // TODO Update position in Scene's Map
        isInitialising = false;
        if (isEnabled)
        {
            super.update();
            animation.update();
            bitmap = animation.getImage();
            if (velocity.mag() != 0 || acceleration.mag() != 0) {
                long n = System.nanoTime();
                double delta = (double) (n - lastTime) / 1000000000;
                position = position.add(velocity.m(delta).add(
                        acceleration.m(delta * delta / 2)
                ));
                velocity = velocity.add(acceleration.m(delta));
                for (int i = 0; i <= 1; i++) {
                    double mod = -1 * Math.signum(velocity.a(i));
                    velocity.modElement(mod * friction.a(i) * delta, i);
                    if (velocity.a(i) * mod > 0) {
                        velocity.setElement(0, i);
                        acceleration.setElement(0, i);
                    }
                }
                if (position.a(0) < 0) {
                    position.setElement(0, 0);
                    acceleration.setElement(0, 0);
                    velocity.setElement(0, 0);
                } else if (position.a(0) > Entry.game.size[0] - size[0]) {
                    position.setElement(Entry.game.size[0] - size[0], 0);
                    acceleration.setElement(0, 0);
                    velocity.setElement(0, 0);
                }
                if (position.a(1) < 0) {
                    position.setElement(0, 1);
                    acceleration.setElement(0, 1);
                    velocity.setElement(0, 1);
                } else if (position.a(1) > Entry.game.size[1] - size[1]) {
                    position.setElement(Entry.game.size[1] - size[0], 1);
                    acceleration.setElement(0, 1);
                    velocity.setElement(0, 1);
                }

                pos = position.getData();

                System.out.println(System.nanoTime() - n);
                lastTime = System.nanoTime();
            }
        }
    }

    // For debugging
    @Override
    public void draw(Canvas canvas)
    {
        super.draw(canvas);
    }
}
