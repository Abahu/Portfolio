/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DicePoker;

import java.util.Calendar;
import java.util.Random;
import ShapesLib.*;


/**
 *
 * @author DM
 */
public abstract class Dice
{
    Random generator;
    final double PIP_SIZE_PERCENT = .12;
    public static final double FACE_SIZE_PERCENT = .10;
    int[] pos = new int[2];
    double radius;
    Circle pip;
    java.awt.Graphics2D graphicsContextHandle;
    
    int value;
    float lineThickness = 2.0f;
    
    boolean clicked = false;
    boolean isPressed = false;
    
    
    public Dice()
    {
        this(new int[] {0,0}, 36.0d);
    }
    
    public Dice(int[] newPos)
    {
        this(newPos, 36.0d);
    }
    
    public Dice(int[] newPos, double newRad)
    {
        //super();
        initPRNG();
        pos[0] = newPos[0];
        pos[1] = newPos[1];
        radius = newRad;// = Entry.size[0] * FACE_SIZE_PERCENT;
        pip = new Circle(0,0,(int)(radius * PIP_SIZE_PERCENT));
        graphicsContextHandle = null;
    }
    
    public boolean isClicked()
    {
        return clicked;
    }
    
    public int getValue()
    {
        return value;
    }
    
    public void setValue(int newValue) //Multiplayer purposes only
    {
        value = newValue;
    }
    
    public void toggleClick()
    {
        clicked = !clicked;
    }
    
    public void click()
    {
        clicked = true;
    }
    
    public void unclick()
    {
        clicked = false;
    }
    
    protected final void initPRNG()
    {
        Math.random();
        Math.random();
        Math.random(); // Call Math.random() in order to induce prng
        generator = new Random((long)(Calendar.MILLISECOND * Calendar.SECOND / Math.random())); //Randomised seed
        generator.nextInt();
        generator.nextInt();
        generator.nextInt(); //Call random to randomised prng
    }
    
    protected void roll(int maxValue)
    {
        if (clicked || value == 0)
            value = generator.nextInt(maxValue) + 1;
    }
    
    public void reset()
    {
        value = 0;
        clicked = false;
    }
    
    public void graphicsUpdate(java.awt.Graphics2D g)
    {
        graphicsContextHandle = g;
    }
    
    public void setLineThickness(float newLineThickness)
    {
        lineThickness = newLineThickness;
    }
    
    public void displayUpdate()
    {
        radius = Entry.dank.getSize().width * FACE_SIZE_PERCENT;
        
        pip = new Circle(0,0,(int)(radius * PIP_SIZE_PERCENT));
        
    }
    
    public int[] getPos()
    {
        return pos;
    }
    
    public void setPos(int[] newPos)
    {
        pos = newPos;
    }
    
    public void setPos(int pos1, int pos2)
    {
        pos[0] = pos1;
        pos[1] = pos2;
    }
    
    public void setRadius(double rad)
    {
        radius = rad;
    }
    
    public void update()
    {
        displayUpdate();
        draw(graphicsContextHandle);
    }
    
    public abstract void draw(java.awt.Graphics2D g);
}
