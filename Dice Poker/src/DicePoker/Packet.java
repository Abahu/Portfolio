/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DicePoker;

import java.nio.ByteBuffer;

/**
 *
 * @author DM
 */
public class Packet 
{
    public long time;
    
    /**
     * 0x00: byte indicating packet type. 
     * Types:   0x00: dice update, 0x01: handshake, 0x02: handshake done, 
     *          0x03: game result, 0x04: music streaming selection, 0x05: client is ready
     *          0xFF: disconnect
     * 0x01-0x04: integer value of the first die
     * 0x05-0x08: second die
     * 0x09-0x0C: third die
     * 0x0D-0x10: fourth die
     * 0x11-0x14: fifth die
     * 0x15-0x18: integer value of the current stage
     * 
     */
    public byte[] data;
    public static final int PACKET_SIZE = 0xFF;
    
    public Packet()
    {
        time = System.nanoTime() / 1000000;
        data = new byte[PACKET_SIZE];
    }
    
    public Packet(ByteBuffer buff)
    {
        this();
        buff.position(0);
        buff.get(data);
    }
    
    public Packet(byte[] buff)
    {
        this();
        data = buff;
    }
}
