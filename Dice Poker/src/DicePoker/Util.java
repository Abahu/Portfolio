/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DicePoker;

import java.nio.ByteBuffer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author DM
 */
public abstract class Util 
{
    private static final int BUFFER_ALLOC = 65536;
    public static final String TEXT_PATH = "Output\\data.txt";
    public static byte[] load(String filePath)
    {
        try
        {
            FileInputStream in = new FileInputStream(new File(filePath));
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            return data;
        }
        catch (IOException ex)
        {
            System.out.println(ex);
        }
        
        return null;
    }
    public static void save(String filePath, byte[] data)
    {
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                String fp = file.getPath();
                fp = fp.substring(0,fp.lastIndexOf(File.separator));
                File f = new File(fp);
                f.mkdirs();
                file.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(file);
            out.write(data);
            out.flush();
            out.close();
        }
        catch (IOException ex)
        {
            System.out.println(ex);
        }
    }
}
