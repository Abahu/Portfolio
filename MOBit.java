package dm.com.realblacksmith;

public class MOBit extends EntityElement
{
    private double hardness;
    private double toughness;
    private double temperature = 20;
    private double thickness;

    private int matID;
    public MOBit(int mat, Vector2D pos, double thick)
    {
        super(new Animation(Entry.game.materials[mat].getMatTempPatterns(), -1));
        matID = mat;
        thickness = thick;
        hardness = Entry.game.materials[matID].getHardness();
        toughness = Entry.game.materials[matID].getToughness();
        position = pos.clone();
        this.pos = position.getData();
        size = Entry.game.ratio.clone();
    }
    public MOBit(int mat, int[] pos, double thick)
    {
        this(mat, new Vector2D(Entry.game.pixelCoordinates(pos)), thick);
    }

    public int[] getMapCoordinates()
    {
        return Entry.game.blockCoordinates(position.getData());
    }

    public double getToughness()
    {
        return toughness;
    }

    public void setTemperature(double nTemperature)
    {
        temperature = nTemperature;
    }

    public void setHardness(int h)
    {

    }

    public void setImage(int index)
    {
        animation.setFrame(index);
    }

    //TODO Temperatures and colour changing, volume, hardness, toughness
    public void tempUpdate(double temp, Vector2D start)
    {
        // The divide by FPS for the time
        temperature += 100 * (temp - temperature) / Math.pow((start.dist(position)), .5) / Entry.game.FPS;
        if (temperature > temp)
            temperature = temp;
    }

    @Override
    public void update()
    {
        int f = (int) temperature / 100;
        if (f > animation.getLength())
            f = animation.getLength() - 1;
        if (f < 0)
            f = 0;
        animation.setFrame(f);
        super.update();
        // TODO set toughness and hardness based on temperatures
    }
}
