package dm.com.realblacksmith;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MoldableObject extends EntityElement
{

    private MOBit[] bits;
    private Map coordinates;
    private int[] LMC;

    /**
     * Creates a primitive rectangular moldable object.
     * @param matID
     * @param pos
     * @param size Size in blocks
     */
    public MoldableObject(int matID, Vector2D pos, int[] size, double thickness)
    {
        position = pos.clone();
        this.pos = position.getData();
        LMC = Entry.game.blockCoordinates(position.getData());
        bits = new MOBit[size[0]*size[1]];
        coordinates = new Map(new int[] {2*size[0], 2*size[1]});

        this.size[0] = (double)(size[0]) * Entry.game.ratio[0];
        this.size[1] = (double)(size[1]) * Entry.game.ratio[1];

        for (int i = 0; i < size[0]; i++)
            for (int j = 0; j < size[1]; j++)
            {
                bits[i*size[1] + j] = new MOBit(matID, new int[] {LMC[0] + i, LMC[1] + j}, thickness);
                coordinates.addRefID(bits[i*size[1] + j].getRefID(), i + size[0], j + size[1]);
            }
    }

    public void setTemperature(double temp)
    {
        for(MOBit b : bits)
            b.setTemperature(temp);
    }

    public void tempUpdate(Vector2D start, double temp)
    {
        for (MOBit b : bits)
            b.tempUpdate(temp, start);
    }

    public int[] getLMC(int[] globalCoordinates)
    {
        return new int[] {globalCoordinates[0] - LMC[0], globalCoordinates[1] - LMC[1]};
    }

    public void markForDelete(int index)
    {
        coordinates.clearPoint(getLMC(bits[index].getMapCoordinates()));
    }

    private int findMOBitIndexByRefID(long refID)
    {
        for (int i = 0; i < bits.length; i++)
            if (bits[i].getRefID() == refID)
                return i;
        return -1;
    }



    private boolean findPointInArray(int[][] array, int[] point)
    {
        if (array.length != 0 && array[0].length == point.length)
            for (int[] p : array)
                if (p.equals(point))
                    return true;
        return false;
    }


    /**
     * Returns a double array with length 3. The first two indices are the map point. The last is the bit's toughness.
     * @param point
     * @param exclude
     * @return
     */
    private double[] weakestAdjacent(int[] point, int[][] exclude)
    {
        int dist = 3; // At most 3 units away from point
        double[] ret = new double[3];
        ret[2] = 10000;

        int k;
        for (int i = point[0] - dist; i < point[0] + dist; i++)
            for (int j = point[1] - dist; j < point[1] + dist; j++)
            {
                // Checks if this point is in the exclude list
                if (!findPointInArray(exclude, point))
                {
                    // Finds the bits index of the MOBit in question using the map coordinates
                    k = findMOBitIndexByRefID(coordinates.refIDsAtPoint(new int[]{i, j})[0]);
                    if (k >= 0 && bits[k].getToughness() < ret[2])
                    {
                        ret[2] = bits[k].getToughness();
                        ret[0] = i;
                        ret[1] = j;
                    }
                }
            }
        return ret;
    }

    // May not actually be used in final ver.
    public void fracture(int[] mapIndex, double force)
    {
        List<int[]> fractured = new ArrayList<>();
        List<int[]> exclude = new ArrayList<>();
        int[] p = mapIndex;

        while(!Entry.game.getPaused())
        {
            double[] ret = weakestAdjacent(p, (int[][])exclude.toArray());
            int[] np = new int[] {(int) ret[0], (int) ret[1]};
            exclude.add(np);
            while (exclude.size() > 5)
                exclude.remove(0);
            if (findPointInArray((int[][])fractured.toArray(), new int[] {(int)ret[0], (int)ret[1]}))
                break;
            if (ret[2] < 1)
                fractured.add(np);
        }

    }

    @Override
    public void draw(Canvas canvas)
    {
        if (isVisible)
            for (MOBit b : bits)
                b.draw(canvas);
    }

    @Override
    public void update()
    {
        for (MOBit b : bits)
            b.update();
    }
    //TODO temperature, colour switching, touch events

    @Override
    public void touchEvent(MotionEvent touchEvent)
    {
        // TEST TEMPERATURE
        tempUpdate(new Vector2D((int)touchEvent.getX(), (int)touchEvent.getY()), 800);
    }
}
