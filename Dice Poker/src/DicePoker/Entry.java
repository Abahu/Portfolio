/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DicePoker;

/**
 *
 * @author DM
 */
public class Entry
{
    public static MainFrame dank;
    public static Game theGame;
    public static Start start;
    public static Thread gameThread;
    public static DebugOutput debug;
    
    public static boolean doDebug = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        theGame = new Game();
        gameThread = new Thread(theGame, "GameLoop");
        gameThread.start();
        
        start = new Start();
        if (doDebug)
        {
            debug = new DebugOutput();
            debug.setLocation(Entry.start.getLocation());
            debug.setVisible(true);
        }
        start.setVisible(true);
    }
}
