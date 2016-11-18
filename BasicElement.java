package dm.com.realblacksmith;

import android.graphics.Bitmap;

public class BasicElement extends Element
{
    @Override
    public int getType()
    {
        return Element.TYPE_BASIC;
    }

    public BasicElement(Bitmap bit, double[] nSize, double[] nPos)
    {
        refID = Entry.game.refCount++;
        Entry.game.refCount +=1 ;
        bitmap = bit;
        size = nSize;
        pos = nPos;
    }

    public void update()
    {}
}
