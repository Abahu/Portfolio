package dm.com.realblacksmith;

import android.graphics.Bitmap;

public class BackgroundElement extends BasicElement
{
    public BackgroundElement(Bitmap bit)
    {
        super(bit, Entry.game.size, new double[] {0,0});
    }
}
