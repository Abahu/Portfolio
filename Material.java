package dm.com.realblacksmith;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileInputStream;
import java.nio.ByteBuffer;

public class Material
{
    public static final int MAT_LENGTH = 17;

    public static final int MAT_STEEL_1040 = 0;
    public static final int MAT_STEEL_1050 = 1;
    public static final int MAT_STEEL_1060 = 2;
    public static final int MAT_STEEL_1070 = 3;
    public static final int MAT_STEEL_1080 = 4;
    public static final int MAT_BRONZE_15 = 5;
    public static final int MAT_BRONZE_20 = 6;
    public static final int MAT_WOOD_OAK = 7;
    public static final int MAT_WOOD_PINE = 8;
    public static final int MAT_WOOD_ASH = 9;
    public static final int MAT_WOOD_EBONY = 10;
    public static final int MAT_COPPER = 11;
    public static final int MAT_BRASS = 12;
    public static final int MAT_SILVER_14K = 13;
    public static final int MAT_SILVER_18K = 14;
    public static final int MAT_GOLD_14K = 15;
    public static final int MAT_GOLD_18K = 16;

    private double density = 1;
    private int hardness;
    private double toughness;

    private double annealTemp;
    private double plasticTemp;
    private double meltingTemp;
    private double weldingTemp;
    private double flashPoint;
    private double workHardeningVal;
    private double vSound = 300;
    private double maxRateOfExpansion = vSound;
    private boolean doesQuenchHarden;


    private Bitmap[] MatTempPatterns;

    public Material()
    {

    }

    /**
     * A Material Temperature Pattern (MTP) is a set of instructions
     * made to declare how a material looks and works at different
     * temperatures. For example, it would detail the hardness and
     * colour of 1040 steel at 600 C.
     */
    private class MTP
    {
        // Main stuff
        private double tempStart = 0;
        private double tempEnd = 0;
        private boolean temperedOnly = false;
        private int tempColour = 0xFFFFFFFF;

        private int hardness = 0;
        private double toughness = 0;

        private int animationIndex = 0;


        public boolean withinRange(double temp, boolean wasTempered)
        {
            if (!wasTempered && temperedOnly)
                return false;
            else
                return (temp >= tempStart && temp <= tempEnd);
        }

        /**
         * Called by an MOBit in its update routine
         * @param bit should always be "this"
         */
        public void set(MOBit bit)
        {
            bit.set
        }
    }
    // TODO Material loading

    public static Material parseResource(int index)
    {
        String path = Resources.getSystem().getString(index);
        try
        {
            //FileInputStream f = Entry.game.getResources().openRawResource(R.raw.);
            Uri uri = Uri.parse("android.resource://"+Entry.game.getContext().getPackageName()+"/raw/MAT_"+index);
            FileInputStream f = new FileInputStream(uri.getPath());
            byte[] buffer = new byte[f.available()];
            f.read(buffer);
            ByteBuffer bb = ByteBuffer.wrap(buffer);
            Material ret = new Material();

            // Begin parsing
            ret.setDensity(bb.getDouble());
            ret.setHardness(bb.getInt());
            ret.setToughness(bb.getDouble());
            ret.setAnnealTemp(bb.getDouble());
            ret.setPlasticTemp(bb.getDouble());
            ret.setMeltingTemp(bb.getDouble());
            ret.setWeldingTemp(bb.getDouble());
            ret.setFlashPoint(bb.getDouble());
            ret.setWorkHardeningVal(bb.getDouble());
            ret.setvSound(bb.getDouble());
            ret.setMaxRateOfExpansion(bb.getDouble());
            ret.setDoesQuenchHarden((bb.get() == 0));
            // Bitmap parsing
            int remainder = bb.remaining()/4;
            int[] temp = new int[remainder];
            for (int i = 0; i < remainder; i++)
                bb.get(temp[i]);
            ret.setMatTempPatterns(temp);

            return ret;
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public double getDensity() {
        return density;
    }

    public void setDensity(double density) {
        this.density = density;
    }

    public int getHardness() {
        return hardness;
    }

    public void setHardness(int hardness) {
        this.hardness = hardness;
    }

    public double getToughness() {
        return toughness;
    }

    public void setToughness(double toughness) {
        this.toughness = toughness;
    }

    public double getAnnealTemp() {
        return annealTemp;
    }

    public void setAnnealTemp(double annealTemp) {
        this.annealTemp = annealTemp;
    }

    public double getPlasticTemp() {
        return plasticTemp;
    }

    public void setPlasticTemp(double plasticTemp) {
        this.plasticTemp = plasticTemp;
    }

    public double getMeltingTemp() {
        return meltingTemp;
    }

    public void setMeltingTemp(double meltingTemp) {
        this.meltingTemp = meltingTemp;
    }

    public double getWeldingTemp() {
        return weldingTemp;
    }

    public void setWeldingTemp(double weldingTemp) {
        this.weldingTemp = weldingTemp;
    }

    public double getWorkHardeningVal() {
        return workHardeningVal;
    }

    public double getFlashPoint() {
        return flashPoint;
    }

    public void setFlashPoint(double flashPoint) {
        this.flashPoint = flashPoint;
    }


    public void setWorkHardeningVal(double workHardeningVal) {
        this.workHardeningVal = workHardeningVal;
    }

    public double getvSound() {
        return vSound;
    }

    public void setvSound(double vSound) {
        this.vSound = vSound;
    }

    public double getMaxRateOfExpansion() {
        return maxRateOfExpansion;
    }

    public void setMaxRateOfExpansion(double maxRateOfExpansion) {
        this.maxRateOfExpansion = maxRateOfExpansion;
    }

    public boolean isDoesQuenchHarden() {
        return doesQuenchHarden;
    }

    public void setDoesQuenchHarden(boolean doesQuenchHarden) {
        this.doesQuenchHarden = doesQuenchHarden;
    }

    public Bitmap[] getMatTempPatterns() {
        return MatTempPatterns;
    }

    public void setMatTempPatterns(Bitmap[] matTempPatterns) {
        MatTempPatterns = matTempPatterns;
    }

    private void f1(byte[] d, byte[] f)
    {
        // fills our doot with the proper pixels and whatnot
        for (int j = 0; j < d.length; j+=4)
            for (int i = 0; i < 4; i++)
                d[j] = f[i];
    }
    private void f1(int[] d, int[] f)
    {
        // fills our doot with the proper pixels and whatnot
        for (int j = 0; j < d.length; j+=4)
            for (int i = 0; i < 4; i++)
                d[j] = f[i];
    }
    private int[] f2(int c)
    {
        // creates the argb from the int
        int[] ret = new int[4];
        ret[0] = c >> 24;
        ret[1] = (c << 8) >> 24;
        ret[2] = (c << 16) >> 24;
        ret[3] = (c << 24) >> 24;
        return ret;
    }
    public void setMatTempPatterns(byte[][] colours)
    {
        if (colours.length != 0 && colours[0].length == 4)
        {
            MatTempPatterns = new Bitmap[colours.length];
            int l = (int)Math.ceil(Entry.game.ratio[0]);
            // Creates the properly size pixel array
            byte[] doot = new byte[l*(int) Math.ceil(Entry.game.ratio[1])];
            for (int i = 0; i < MatTempPatterns.length; i++)
            {
                // Doot doot!
                f1(doot, colours[i]);
                MatTempPatterns[i] = BitmapFactory.decodeByteArray(doot, 0, l);

            }
        }
    }

    public void setMatTempPatterns(int[] colours)
    {
        if(colours.length != 0)
        {
            MatTempPatterns = new Bitmap[colours.length];
            int w = (int) Math.ceil(Entry.game.ratio[0]);
            int h = (int) Math.ceil(Entry.game.ratio[1]);
            for(int i = 0; i < colours.length; i++)
            {
                MatTempPatterns[i] = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
                MatTempPatterns[i].eraseColor(colours[i]);
            }
        }
    }
}
