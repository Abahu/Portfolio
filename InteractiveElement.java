package dm.com.realblacksmith;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

public class InteractiveElement extends Element
{
    @Override
    public void update() {}


    @Override
    public int getType()
    {
        return Element.TYPE_INTERACTIVE;
    }

    public InteractiveElement(Context context, Bitmap bit)
    {
        refID = Entry.game.refCount++;
        Entry.game.refCount += 1 ;
        bitmap = bit;
        size = new double[] {bitmap.getHeight(), bitmap.getWidth()};
    }
}
