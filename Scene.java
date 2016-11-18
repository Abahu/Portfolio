package dm.com.realblacksmith;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.DragEvent;
import android.view.MotionEvent;

public class Scene
{
    protected Element[] elements;

    protected EntityElement[] entities;

    protected Resources getResources()
    {
        return Entry.game.getResources();
    }

    protected double[] size;

    protected Vector2D cameraFocus;

    protected boolean isTouchable = false;

    protected Map map;

    public void update()
    {
        for (Element e : elements)
            e.update();
    }

    public void draw(Canvas canvas)
    {
        sortEByDepth();
        for (int i = 0; i < elements.length; i++)
            if (elements[i] != null)
                elements[i].draw(canvas);
    }

    protected void sortEByDepth()
    {
        qsDepth(elements, 0, elements.length - 1);
    }

    protected void sortEByDistTo(long eRefID)
    {
        qsDist(entities, 0, entities.length - 1, findEntityByRefID(eRefID));
    }

    protected void sortEByDistTo(Vector2D posV)
    {
        qsDist(entities, 0, entities.length -1, posV);
    }

    protected int findByRefID(long eRefID)
    {
        for (int i = 0; i < elements.length; i++)
            if (elements[i].getRefID() == eRefID)
                return i;

        return -1;
    }

    protected int findEntityByRefID(long eRefID)
    {
        for (int i = 0; i < entities.length; i++)
            if (entities[i].getRefID() == eRefID)
                return i;

        return -1;
    }

    private void qsDepth(Element[] arr, int low, int high)
    {
        if (arr == null || arr.length == 0)
            return;
        if (low >= high)
            return;

        int middle = low + (high - low)/2;

        double pivot = arr[middle].getDepth();

        int i = low, j = high;

        while (i <= j) {
            while (arr[i].getDepth() < pivot) {
                i++;
            }
            while (arr[j].getDepth() > pivot) {
                j--;
            }
            if (i <= j) {
                Element temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }

        if (low < j)
            qsDepth(arr, low, j);
        if (high > i)
            qsDepth(arr, i, high);
    }

    private void qsDist(EntityElement[] arr, int low, int high, int eCompIndex)
    {
        if (arr == null || arr.length == 0)
            return;
        if (low >= high)
            return;

        int middle = low + (high - low)/2;

        double pivot = arr[middle].getDistTo(arr[eCompIndex]);

        int i = low, j = high;

        while (i <= j) {
            while (arr[i].getDistTo(arr[eCompIndex]) < pivot) {
                i++;
            }
            while (arr[j].getDistTo(arr[eCompIndex]) > pivot) {
                j--;
            }
            if (i <= j) {
                EntityElement temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }

        if (low < j)
            qsDist(arr, low, j, eCompIndex);
        if (high > i)
            qsDist(arr, i, high, eCompIndex);
    }

    private void qsDist(EntityElement[] arr, int low, int high, Vector2D posV)
    {
        if (arr == null || arr.length == 0)
            return;
        if (low >= high)
            return;

        int middle = low + (high - low)/2;

        double pivot = arr[middle].getDistTo(posV);

        int i = low, j = high;

        while (i <= j) {
            while (arr[i].getDistTo(posV) < pivot) {
                i++;
            }
            while (arr[j].getDistTo(posV) > pivot) {
                j--;
            }
            if (i <= j) {
                EntityElement temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
                i++;
                j--;
            }
        }

        if (low < j)
            qsDist(arr, low, j, posV);
        if (high > i)
            qsDist(arr, i, high, posV);
    }

    public void appendE(Element e)
    {
        if (elements == null)
            elements = new Element[] {e};
        else
        {
            Element[] n = new Element[elements.length+1];
            System.arraycopy(elements, 0, n, 0, elements.length);
            n[n.length-1] = e;
            elements = n;
        }

        if (e.getType() == Element.TYPE_INTERACTIVE)
            for (int i = e.getPosBlock()[0]; i < e.getPosBlock()[0] + e.getWidthBlock(); i++)
                for (int j = e.getPosBlock()[1]; j < e.getPosBlock()[1] + e.getHeightBlock(); j++)
                    map.addRefID(e.getRefID(), i, j);
    }

    public void appendE(EntityElement e)
    {
        if (elements == null)
            elements = new Element[] {e};
        else
            if (e != null)
            {
                Element[] n = new Element[elements.length+1];
                System.arraycopy(elements, 0, n, 0, elements.length);
                n[n.length-1] = e;
                elements = n;
            }

        if (entities == null)
            entities = new EntityElement[] {e};
        else
            if (e != null)
            {
                EntityElement[] n = new EntityElement[entities.length+1];
                System.arraycopy(entities, 0, n, 0, entities.length);
                n[n.length-1] = e;
                entities = n;
            }
        for (int i = e.getPosBlock()[0]; i < e.getPosBlock()[0] + e.getWidthBlock(); i++)
            for (int j = e.getPosBlock()[1]; j < e.getPosBlock()[1] + e.getHeightBlock(); j++)
                map.addRefID(e.getRefID(), i, j);
    }

    public void setCameraFocus(Vector2D v)
    {
        cameraFocus = v;
    }

    public Scene(double[] newSize)
    {
        size = newSize;
        elements = new Element[0];
        cameraFocus = new Vector2D();
        cameraFocus.setElement(0, 0);
        cameraFocus.setElement(0, 1);
        map = new Map(size);
    }

    public void touchEvent(MotionEvent event)
    {
        long[] refs = map.refIDsAtPoint(Game.blockCoordinates(new double[] {event.getX(), event.getY()}));
        for (long ref : refs)
        {
            elements[findByRefID(ref)].touchEvent(event);
        }
    }

    public void dragEvent(DragEvent event)
    {
    }
}
