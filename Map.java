package dm.com.realblacksmith;

public class Map
{
    private class MapList
    {
        private long[] refIDData = {};

        public void clear()
        {
            refIDData = new long[0];
        }

        public void appendRef(long refID)
        {
            if (refIDData.length != 0)
            {
                // Create a temporary array
                long[] temp = new long[refIDData.length + 1];
                // Fill the temporary array
                System.arraycopy(refIDData, 0, temp, 0, refIDData.length);
                // Append the final element
                temp[temp.length - 1] = refID;
                // Set the data to the temp array
                refIDData = temp;
            }
            else
                refIDData = new long[] {refID};
        }

        public long last()
        {
            return refIDData[refIDData.length - 1];
        }

        public long first()
        {
            return refIDData[0];
        }

        public long getE(int index)
        {
            if (index >= 0 && index < refIDData.length)
            {
                return refIDData[index];
            }
            else
                return -1;
        }

        public void removeEByIndex(int index)
        {
            if (index >= 0 && index < refIDData.length)
            {
                if (refIDData.length == 1)
                {
                    refIDData = new long[0];
                }
                else if (refIDData.length != 0)
                {
                    long[] temp = new long[refIDData.length - 1];
                    if (index == 0)
                    {
                        System.arraycopy(refIDData, 1, temp, 0, temp.length);
                        refIDData = temp;
                    }
                    else if (index == refIDData.length - 1)
                    {
                        System.arraycopy(refIDData, 0, temp, 0, temp.length);
                        refIDData = temp;
                    }
                    else
                    {
                        byte mod = 0;
                        for (int i = 0; i < refIDData.length; i++)
                        {
                            if (i == index)
                                mod = -1;
                            else
                                temp[i + mod] = refIDData[i];
                        }
                    }
                }
            }
        }

        private int findRefID(long refID)
        {
            for(int i = 0; i < refIDData.length; i++)
                if (refIDData[i] == refID)
                    return i;
            return -1;
        }

        public void removeRefID(long refID)
        {
            int index = findRefID(refID);
            if (index != -1)
                removeEByIndex(index);
        }

        public long[] getData()
        {
            return refIDData;
        }
    }

    private MapList[][] data;

    /**
     * Makes a map based on how many actual square hits there should be.
     * @param size
     */
    public Map(int[] size)
    {
        data = new MapList[size[0]][size[1]];
        for (int i = 0; i < data.length; i++)
            for (int j = 0; j < data[0].length ; j++)
                data[i][j] = new MapList();
    }

    /**
     * Makes a map based on the scene size.
     * @param size
     */
    public Map(double[] size)
    {
        this(Game.blockCoordinates(size));
    }

    public void addRefID(long refID, int x, int y)
    {
        data[x][y].appendRef(refID);
    }

    public void removeRefID(long refID, int x, int y)
    {
        data[x][y].removeRefID(refID);
    }

    public void clearRefID(long refID)
    {
        for (MapList[] m : data)
            for (MapList l : m)
                l.removeRefID(refID);
    }

    public void clearPoint(int[] index)
    {
        data[index[0]][index[1]].clear();
    }

    public void clear()
    {
        int[] size = new int[2];
        if (data.length != 0 && data[0].length != 0)
            size = new int[] {data.length, data[0].length};
        data = new MapList[size[0]][size[1]];
    }

    public long[] refIDsAtPoint(int[] p)
    {
        if (p[0] >= 0 && p[0] < data.length && p[1] >= 0 && p[0] < data[0].length)
            return data[p[0]][p[1]].getData();
        else
            return new long[] {}; // Empty instead of null to reduce errors at the cost of memory. Trashed anyway.
    }
}
