/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DicePoker;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * Start Multiplayer Thread
 * Exists solely for multiplayer to free up the EDT in Entry.start
 * @author DM
 */
public class SMT implements Runnable
{
    private boolean sh = true;
    private Inet4Address i = null;
    public void sm(boolean shouldHost, Inet4Address ip)
    {
        sh = shouldHost;
        i = ip;
    }
    public void run()
    {
        if (i == null)
        {
            try
            {
                i = (Inet4Address) Inet4Address.getLocalHost();
            }
            catch (UnknownHostException ex)
            {
                Game.debug(ex);
                return;
            }
        }
        Entry.theGame.startMultiplayer(sh, i);
        Entry.gameThread.interrupt();
    }
}
