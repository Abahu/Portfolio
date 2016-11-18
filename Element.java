package dm.com.realblacksmith;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;

// A drawable element
public abstract class Element
{
    public static final int TYPE_ELEMENT = 0;
    public static final int TYPE_BASIC = 1;
    public static final int TYPE_INTERACTIVE = 2;
    public static final int TYPE_ENTITY = 3;

    protected Bitmap bitmap;
    protected double[] pos;
    protected double depth = 0;
    protected double[] size = {60, 60}; // Default in case forgotten

    protected boolean isVisible = true;
    protected boolean isEnabled = true;

    protected float angle;

    protected long refID;

    /**
     * 0 = element, 1 = basic, 2 = interactive, 3 = entity
     * @return
     */
    public int getType()
    {
        return TYPE_ELEMENT;
    }

    public void setVisible(boolean b)
    {
        isVisible = b;
    }

    public boolean getVisible()
    {
        return isVisible;
    }

    public void setEnabled(boolean b)
    {
        isEnabled = b;
    }

    public boolean getEnabled()
    {
        return isEnabled;
    }

    public void setPos(double[] newPos)
    {
        pos = newPos;
    }

    public double getWidth()
    {
        return size[0];
    }

    public double getHeight()
    {
        return size[1];
    }

    public int getWidthBlock() { return (int) Math.round(size[0]/Entry.game.ratio[0]); }

    public int getHeightBlock() { return (int) Math.round(size[0]/Entry.game.ratio[1]); }

    public void setPosBlock(int[] blocks)
    {
        pos[0] = (double) blocks[0]*Entry.game.ratio[0];
        pos[1] = (double) blocks[1]*Entry.game.ratio[1];
    }

    public int[] getPosBlock()
    {
        return new int[] {(int) Math.round(pos[0]/Entry.game.ratio[0]), (int) Math.round(pos[1]/Entry.game.ratio[1])};
    }

    public abstract void update();

    public double getDepth()
    {
        return depth;
    }

    public void setDepth(double nd)
    {
        depth = nd;
    }

    public void setBitmap(Bitmap bit)
    {
        bitmap = bit;
    }

    protected Bitmap rotateBitmap(Bitmap source, float angle)
    {
        if (angle != 0.0f) {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        }
        else
            return source;
    }

    public void draw(Canvas canvas)
    {
        if (isVisible && bitmap != null)
            canvas.drawBitmap(rotateBitmap(bitmap, angle), (float) pos[0], (float)pos[1], null);
    }

    public long getRefID()
    {
        return refID;
    }

    public void setRefID(long nrid)
    {
        refID = nrid;
    }

    protected Resources getResources()
    {
        return Entry.game.getResources();
    }

    public void setAngle(float na)
    {
        angle = na;
    }

    public float getAngle()
    {
        return angle;
    }

    public void touchEvent(MotionEvent TouchEvent)
    {

    }
}
