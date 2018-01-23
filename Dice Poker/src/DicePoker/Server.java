/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DicePoker;

import static DicePoker.Game.debug;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 *
 * @author DM
 */
public class Server implements Runnable
{
    Socket socket;
    boolean doOnce = true;
    InputStream din = null;
    OutputStream dout = null;
    Packet lastPacket = null;
    
    public void run()
    {
        if (socket != null)
        {
            try
            {
                debug("Starting new server thread");
                din = socket.getInputStream();
                dout = socket.getOutputStream();
                doOnce = false;
                Packet pack = new Packet();
                while(Entry.theGame.running)
                {
                    din.read(pack.data);
                    pack.time = System.nanoTime() / 1000000;
                    Entry.theGame.update(pack);
                    lastPacket = pack;
                }
            }
            catch (IOException e)
            {
                //Close the thread
                System.out.println("Error in establishing server IO stream");
            }
        }
        close(); //If we somehow aren't instantiated correctly or we get kicked out of the loop, close up the thread.
    }
    
    public void setSocket(Socket sock)
    {
        socket = sock;
    }
    
    public Packet getLastUpdate()
    {
        return lastPacket;
    }
    
    public void updateOut(Packet out)
    {
        try
        {
            debug("Sending packet of type "+out.data[0]);
            dout.write(out.data);
            dout.flush();
        }
        catch (IOException | NullPointerException ex)
        {
            debug(ex.toString());
        }
    }
    
    public void close()
    {
        try 
        {
            // Sends the closing error code
            
            din.close();
            dout.close();
            
            socket.close();
        } 
        catch (Throwable ex) 
        {
            System.out.println(ex);
        }
    }
    
    public Server()
    {
        socket = null;
    }
    
    public Server(Socket sock)
    {
        socket = sock;
    }
}
