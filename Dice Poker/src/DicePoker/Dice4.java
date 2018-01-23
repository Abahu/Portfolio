/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DicePoker;

import java.awt.Color;
import ShapesLib.*;

/**
 *
 * @author DM
 */
public class Dice4 extends Dice
{
    EquiTriangle face;
    //Constructor
    public Dice4()
    {
        super();
    }
    public Dice4(int[] newPos)
    {
        super(newPos);
    }
    
    public void roll()
    {
        roll(4);
    }
    
    @Override
    public void draw(java.awt.Graphics2D g)
    {
        //I helped Jon with this, so that's why this appeared all at once
        Circle foo = new Circle(pos[0], pos[1], (int) radius/2);
        //foo.draw(g);
        face = new EquiTriangle(new Circle(pos[0],pos[1], (int)radius));
        face.setLineThickness(lineThickness);
        face.draw(g);
        
        pip.setIsFilled(true);
        pip.setColour(Color.BLACK);
        
        //Point p = new Point(pos[0], pos[1]);
        //p.draw(g);
        //value = 4;
        switch (value)
        {
            case 1:
                pip.setX(pos[0]);
                pip.setY(pos[1]);
                pip.draw(g);
                break;
            case 2:
                pip.setX(pos[0]);
                pip.setY((int)(pos[1]-radius*.18));
                pip.draw(g);
                pip.setY((int)(pos[1]+radius*.12));
                pip.draw(g);
                break;
            case 3:
                pip.setX(pos[0]);
                pip.setY((int)(pos[1]-radius*.275*3/5));
                pip.draw(g);
                pip.setX((int)(pos[0]-radius*.225*3/5));
                pip.setY((int)(pos[1]+radius*.150*3/5));
                pip.draw(g);
                pip.setX((int)(pos[0]+radius*.225*3/5));
                pip.draw(g);
                break;
            case 4:
                pip.setRadius((int)(radius * PIP_SIZE_PERCENT * .9));
                pip.setX(pos[0]);
                pip.setY((int)(pos[1]-radius*.255*3/5));
                pip.draw(g);
                pip.setY((int)(pos[1]+radius*.255*3/5));
                pip.draw(g);
                pip.setY(pos[1]);
                pip.setX((int)(pos[0]-radius*.255*3/5));
                pip.draw(g);
                pip.setX((int)(pos[0]+radius*.255*3/5));
                pip.draw(g);
                break;
        }
    }
}
