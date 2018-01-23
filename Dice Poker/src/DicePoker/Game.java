/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DicePoker;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
/**
 *
 * @author DM
 */
public class Game implements Runnable
{
    private Thread thread;
    private final MainFrame theMatrix;
    private final Dice6[] diceArray;
    private final DiceButton[] buttonArray;
    public  boolean running;
    private int state;
    private boolean finalRound = false;
    private Score.result p1Score;
    private Score.result p2Score;
    
    private boolean isMultiplayer = false;
    private boolean isHost = false;
    private Inet4Address IP;
    private Socket socket;
    private ServerSocket ss;
    private Server server;
    public  Thread serverThread;
    private boolean didHandshake = false;
    
    private static final int STATE_RESET = 0;
    private static final int STATE_MY_TURN = 1;
    private static final int STATE_OTHER_TURN = 2;
    private static final int STATE_FINISH = 3;
    
    public void run()
    {
        do
        {
            waitUntilInterrupt();
        } while (isMultiplayer && !handshake()); //If we're offline, this will break without handshake. Otherwise, it will break by handshake

        if(isMultiplayer)
        {
            theMatrix.setRollEnabled(false); // Both host and client start off unable to roll. Host gets to roll when client says they're ready
            
            if (isHost)
                waitUntilInterrupt();
            else
            {
                Packet pack = new Packet(new byte[] {0x05});
                server.updateOut(pack);
            }
        }
        
        theMatrix.setLocation(Entry.start.getLocation());
        theMatrix.setVisible(true);
        Entry.start.setVisible(false);
        
    }
    
    private void waitUntilInterrupt()
    {
        try
        {
            Thread.sleep(System.nanoTime()/1000000);
        }
        catch(InterruptedException ex)
        {
        }
    }
    
    private void pause(long time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch(InterruptedException ex)
        {
        }
    }
    
    private boolean handshake()
    {
        long startTime = System.nanoTime() / 1000000;
        pause(100);
        if (!isHost)
        {
            Packet pack = new Packet(new byte[] {0x01});
            //pause(100);
            server.updateOut(pack);
            System.out.println("Sent the first handshake packet!");
        }
        while (System.nanoTime()/1000000 - startTime < 5000)
        {
            if (didHandshake)
                return true;
        }
        System.out.println("Handshake unsuccessful");
        server.close();
        return false;
    }
    
    public void setThreadHandle(Thread handle)
    {
        thread = handle;
    }
    
    public void updateOther(Packet inputUpdate) // Internal update call
    {
        if (state == STATE_MY_TURN)
        {
            server.updateOut(inputUpdate);
        }
    }
    
    public void update(Packet inputUpdate) // External update call
    {
        ByteBuffer buff = ByteBuffer.wrap(inputUpdate.data);
        int type = (int) buff.get();
        debug("Received packet of type " + type);
        Packet pack;
        
        switch (type)
        {
            case 0x00: // Dice update
                int[] diceValues = new int[5];
                for (int i = 0; i < 5; i++)
                {
                    diceValues[i] = buff.getInt();
                    diceArray[i+5].setValue(diceValues[i]);
                }
                p2Score = Score.score(diceValues);
                theMatrix.setP2ScoreText(Score.NAMES_OF_SCORES[p2Score.type]);
                state = (isHost && finalRound) ? STATE_FINISH : STATE_MY_TURN;
                if (isHost)
                    finalRound = !finalRound;
                debug("Final round: " + finalRound);
                debug("State: " + state);
                theMatrix.setRollEnabled(true);
                if (state == STATE_MY_TURN)
                    for (int i = 0; i < 5; i++)
                        theMatrix.setDieEnabled(finalRound, i);
                
                if (state == STATE_FINISH)
                    rollDice();
                theMatrix.repaint();
                break;
                
            case 0x01: // Handshake
                if (isHost)
                    pack = new Packet(new byte[] {0x01}); // If we're the host, this is our first packet out.
                else
                    pack = new Packet(new byte[] {0x02}); // If we're the client, then we already sent a handshake packet. Let's be done.
                server.updateOut(pack);
                break;
                
            case 0x02: // Handshake done
                didHandshake = true;
                if (isHost)
                {
                    pack = new Packet(new byte[] {0x02});
                    server.updateOut(pack); // Let the client do its handshake as well!
                }
                break;
                
            case 0x03: // Game result
                break;
                
            case 0x04: // Streaming selection
                break;
                
            case 0x05: // Client is ready
                theMatrix.setRollEnabled(true);
                Entry.gameThread.interrupt();
                break;
                
            case -1: // Terminate
                stahp();
                break;
        }
    }
    
    /* //This was a beautiful lie
    public boolean cleaningOutMySocket() throws LovelessRelationshipException //Turn on multiplayer
    {
        Enumeration<NetworkInterface> nis;
        try 
        {
            nis = NetworkInterface.getNetworkInterfaces();
            
            NetworkInterface real = null; 
            boolean foundLove = false; //http://www.youtube.com/watch?v=HEXWRTEbj1I
            while(nis.hasMoreElements())
            {
                real = nis.nextElement();
                if (real.isPointToPoint()) //Won't the real IP please stand up
                {
                    foundLove = true; //We found love in a hopeless place
                    break;
                }
            }
            if (foundLove)
            {
                for (int i = 25565; i < 26000; i++)
                {
                    socket = new Socket(real.getInetAddresses().nextElement(), i);
                    
                }
            }
            else
            {
                char[] mess = new char[] {0x42, 0x61, 0x62, 0x79, 0x20, 0x64, 0x6f, 0x6e,
                0x27, 0x74, 0x20, 0x68, 0x75, 0x72, 0x74, 0x20, 0x6d, 0x65, 0x2c, 0x20,
                0x64, 0x64, 0x6e, 0x27, 0x74, 0x20, 0x68, 0x75, 0x72, 0x74, 0x20, 0x6d,
                0x65, 0x2c, 0x20, 0x6e, 0x6f, 0x20, 0x6d, 0x6f, 0x72, 0x65, 0x2e};
                throw new LovelessRelationshipException(new String(mess));
            }
        }
        catch (SocketException ex) 
        {
            return false;            
        }
        catch (IOException ex)
        {
            return false;
        }
        return true;
    }
    //*/
    
    public void stahp()
    {
        running = false;
        Entry.start.dispose();
        if (theMatrix != null)
            theMatrix.dispose();
        if (Entry.doDebug)
            Entry.debug.dispose();
        
        if (isMultiplayer)
        {
            if (server != null)
            {
                Packet pack = new Packet(new byte[] {(byte)0xFF});
                server.updateOut(pack);
                server.close();
            }
        }
    }
    
    public boolean startMultiplayer(boolean shouldHost, Inet4Address ip)
    {
        isMultiplayer = true;
        isHost = shouldHost;
        debug("Starting socket connection as " + ((isHost) ? "host" : "client"));
        if (isHost)
        {
            try 
            {
                ss = new ServerSocket(25565);
                
                socket = ss.accept();
            } 
            catch (IOException ex) 
            {
                debug("IOException! Failed to start server and/or establish socket!");
                System.out.println(ex);
                return false;
            }
        }
        else
        {
            try
            {
                if (ip != null)
                {
                    IP = ip;
                    socket = new Socket(IP, 25565);
                }
                else
                    return false;
            }
            catch (IOException ex)
            {
                debug("IOException! Failed to establish socket!");
                System.out.println(ex);
                return false;
            }
        }
        debug("Connection established");
        server = new Server(socket);
        serverThread = new Thread(server, "ServerThread");
        serverThread.start();
        return true;
    }
    
    public static void debug(String text)
    {
        if (Entry.doDebug)
            Entry.debug.debugText.setText(Entry.debug.debugText.getText() + text + '\n');
    }
    
    public static void debug(Object obj)
    {
        if (Entry.doDebug)
            Entry.debug.debugText.setText(Entry.debug.debugText.getText() + obj + '\n');
    }
    
    public static void debug(int val)
    {
        if (Entry.doDebug)
            Entry.debug.debugText.setText(Entry.debug.debugText.getText() + val + '\n');
    }
    
    public static void debug(double val)
    {
        if (Entry.doDebug)
            Entry.debug.debugText.setText(Entry.debug.debugText.getText() + val + '\n');
    }
    
    public static void debug(float val)
    {
        if (Entry.doDebug)
            Entry.debug.debugText.setText(Entry.debug.debugText.getText() + val + '\n');
    }
    
    public static void debug(boolean val)
    {
        if (Entry.doDebug)
            Entry.debug.debugText.setText(Entry.debug.debugText.getText() + val + '\n');
    }
    
    public static void debug(byte val)
    {
        if (Entry.doDebug)
            Entry.debug.debugText.setText(Entry.debug.debugText.getText() + val + '\n');
    }
    
    public static void debug(char val)
    {
        if (Entry.doDebug)
            Entry.debug.debugText.setText(Entry.debug.debugText.getText() + val + '\n');
    }
    
    public static void debug(long val)
    {
        if (Entry.doDebug)
            Entry.debug.debugText.setText(Entry.debug.debugText.getText() + val + '\n');
    }
    
    public void rollDice()
    {
        int[] diceValues = new int[5];
        
        if (isMultiplayer) // Multiplayer
        {
            switch (state)
            {
                case STATE_RESET:
                    for (DiceButton foo : buttonArray)
                    {
                        foo.reset();
                    }
                    theMatrix.setP1ScoreText("");
                    theMatrix.setP2ScoreText("");
                    debug("Reset");
                    if (isHost)
                        state = STATE_MY_TURN;
                    else
                    {
                        state = STATE_OTHER_TURN;
                        break;
                    }

                case STATE_MY_TURN:
                    // Client-side roll update
                    for (int i = 0; i < 5; i++)
                    {
                        buttonArray[i].roll();
                        diceValues[i] = buttonArray[i].getValue();
                    }
                    p1Score = Score.score(diceValues);
                    theMatrix.setP1ScoreText(Score.NAMES_OF_SCORES[p1Score.type]);
                    
                    // Multiplayer packet
                    try
                    {
                        ByteBuffer buff = ByteBuffer.allocateDirect(Packet.PACKET_SIZE);
                        buff.put((byte) 0x00);
                        for (DiceButton foo : buttonArray)
                        {
                            buff.putInt(foo.getValue());
                        }
                        Packet pack = new Packet(buff);
                        updateOther(pack);
                    }
                    catch (Exception ex)
                    {
                        debug(ex.toString());
                        debug(ex.getMessage());
                    }
                    // Update state
                    // Host is player 1. Client is player 2. If we're the client and it's the last round, the game is up.
                    state = (!isHost && finalRound) ? STATE_FINISH : STATE_OTHER_TURN;
                    theMatrix.setRollEnabled(false);
                    // After the prior state update, if we're the client (player 2) then it's the final round. Set it so
                    if (!isHost)
                        finalRound = !finalRound;
                    debug("Final round: " + finalRound);
                    debug("State: " + state);
                    
                    if (state == STATE_MY_TURN)
                        for (int i = 0; i < 5; i++)
                            theMatrix.setDieEnabled(false, i);
                    
                    break;
                    

                case STATE_OTHER_TURN: //Really, we shouldn't see this at all :^)
                    debug("Uh oh! The player rolled on the other person's turn!");
                    break;

                case STATE_FINISH:
                    int res = p1Score.isHigher(p2Score);
                    switch (res)
                    {
                        case 0: // Draw
                            theMatrix.setP1ScoreText("Draw!");
                            theMatrix.setP2ScoreText("Draw!");
                            break;
                            
                        case 1: // Player 1 wins
                            theMatrix.setP1ScoreText("Winner!");
                            theMatrix.setP2ScoreText("Lost!");
                            break;
                            
                        case 2: // Player 2 wins
                            theMatrix.setP1ScoreText("Lost!");
                            theMatrix.setP2ScoreText("Winner!");
                            break;
                    }
                    state = STATE_RESET;
                    break;

                default:
                    break;
            }
        }
        else // Singleplayer
        {
            switch (state)
            {
                case STATE_RESET:
                    for (DiceButton foo : buttonArray)
                        foo.reset();
                    theMatrix.setP1ScoreText("");
                    theMatrix.setP2ScoreText("");

                case STATE_MY_TURN:
                    for (int i = 0; i < 5; i++)
                    {
                        buttonArray[i].roll();
                        diceValues[i] = buttonArray[i].getValue();
                    }
                    p1Score = Score.score(diceValues);
                    theMatrix.setP1ScoreText(Score.NAMES_OF_SCORES[p1Score.type]);
                    state = STATE_OTHER_TURN;
                    
                    for (int i = 0; i < 5; i++)
                            theMatrix.setDieEnabled(false, i);
                    if (finalRound)
                        for (int i = 5; i < 10; i++)
                            theMatrix.setDieEnabled(true, i);
                    
                    break;

                case STATE_OTHER_TURN:
                    for (int i = 5; i < 10; i++)
                    {
                        buttonArray[i].roll();
                        diceValues[i-5] = buttonArray[i].getValue();
                    }
                    p2Score = Score.score(diceValues);
                    theMatrix.setP2ScoreText(Score.NAMES_OF_SCORES[p2Score.type]);
                    state = (finalRound) ? STATE_FINISH : STATE_MY_TURN;
                    finalRound = !finalRound;
                    
                    for (int i = 5; i < 10; i++)
                        theMatrix.setDieEnabled(false, i);
                    if (finalRound)
                        for (int i = 0; i < 5; i++)
                            theMatrix.setDieEnabled(true, i);
                    
                    if (state != STATE_FINISH)
                        break;

                case STATE_FINISH:
                    int s = p1Score.isHigher(p2Score);
                    if (s == 1) //Player 1 wins
                    {
                        theMatrix.setP1ScoreText("Winner!");
                    }
                    if (s == 2) //Player 2 wins
                    {
                        theMatrix.setP2ScoreText("Winner!");
                    }
                    if (s == 0) //Draw
                    {
                        theMatrix.setP1ScoreText("Draw!");
                        theMatrix.setP1ScoreText("Draw!");
                    }
                    state = STATE_RESET;
                    break;

                default:
                    break;
            }
        }
        if (!isHost && isMultiplayer)
        {
            if (state == STATE_RESET)
                rollDice();
        }
        theMatrix.repaint();
    }
    
    public MainFrame getTheMatrix()
    {
        return theMatrix;
    }
    
    public Game()
    {
        diceArray = new Dice6[10];
        for (int i = 0; i < 10; i++)
        {
            diceArray[i] = new Dice6();
            diceArray[i].setRadius(80.0f);
        }
        theMatrix = new MainFrame(diceArray);
        theMatrix.addWindowListener(
            new java.awt.event.WindowAdapter() 
            {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) 
                {
                    Entry.theGame.stahp();
                }
            });
        buttonArray = theMatrix.getButtonArray();
        running = true;
        state = STATE_RESET;
    }
}
