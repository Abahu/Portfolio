/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DicePoker;

import java.awt.Graphics2D;
import ShapesLib.*;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.geom.Point2D;

/**
 *
 * @author DM
 */
public class Dice6 extends Dice
{
    Square face;
    
    public Dice6()
    {
        super();
        lineThickness = 2.5f;
    }
    public Dice6(int[] newPos)
    {
        super(newPos);
        lineThickness = 2.5f;
    }
    
    public void roll()
    {
        roll(6);
    }
    
    @Override
    public void draw(Graphics2D g)
    {
        Circle foo = new Circle(pos[0], pos[1], (int) radius);
        face = new Square(foo);
        face.setLineThickness(lineThickness);
        face.setPos((int)(pos[0] - radius), (int)(pos[1] - radius));
        face.setIsFilled(clicked);
        
        int[] c = face.getCentre();
        
        //START COLOURS :o
        Color glowing = new Color(0xFF,0xE6,0x23);
        Color glowingSoft = new Color(0xFF,0xF7,0xB7);
        Color ivory = new Color(0xF3,0xF4,0xE0);
        Color ivoryDark = new Color(0xCA,0xC6,0x95);
        Color onyx = new Color(0x0C,0x0B,0x10);
        Color onyxPurple = new Color(0x0E,0x0C,0x14);
        Color royalPurple = new Color(0x1F,0x04,0x43);
        Color grape = new Color(0x4C,0x04,0xEA);
        
        Point2D p1 = new Point2D.Float(c[0], 0.0f);
        Point2D p2 = new Point2D.Float(c[0], 80.0f);
        Point2D centre = new Point2D.Float(c[0], c[1]);

        GradientPaint gradMidnight = new GradientPaint(p1,
                                                    Color.BLACK,
                                                    p2,
                                                    Color.BLUE,
                                                    true);

        GradientPaint gradIvory = new GradientPaint(p1,
                                                    ivory,
                                                    p2,
                                                    ivoryDark,
                                                    true);

        GradientPaint gradOnyx = new GradientPaint( p1, 
                                                    royalPurple, 
                                                    p2, 
                                                    onyx, 
                                                    true);

        GradientPaint gradGlowing = new GradientPaint(  p1,
                                                        glowing,
                                                        p2,
                                                        glowingSoft,
                                                        true);
        
        RadialGradientPaint gradOnyxRad = new RadialGradientPaint(  centre,
                                                                    50.0f,
                                                                    new float[] {0.0f, 1.0f},
                                                                    new Color[] {grape, onyx});
        RadialGradientPaint gradMidnightRad = new RadialGradientPaint(  centre,
                                                                    50.0f,
                                                                    new float[] {0.0f, 1.0f},
                                                                    new Color[] {Color.BLUE, onyx});
        //END COLOURS :)
        
        if (clicked)
        {
            pip.setPaint(gradGlowing);
            face.setPaint(gradOnyxRad);
            face.draw(g);
        }
        else
        {
            pip.setPaint(gradMidnight);
            face.setPaint(gradMidnightRad);
            face.draw(g);
        }
        pip.setIsFilled(true);
        
        switch(value)
        {
            case 1:
                pip.setPos(c[0], c[1]);
                pip.draw(g);
                break;
                
            case 2:
                pip.setPos((int)(c[0] - foo.getWidth()*.25),(int)(c[1] + foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos((int)(c[0] + foo.getWidth()*.25),(int)(c[1] - foo.getWidth()*.25));
                pip.draw(g);
                break;
                
            case 3:
                pip.setPos((int)(c[0] + foo.getWidth()*.25),(int)(c[1] + foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos(c[0], c[1]);
                pip.draw(g);
                pip.setPos((int)(c[0] - foo.getWidth()*.25),(int)(c[1] - foo.getWidth()*.25));
                pip.draw(g);
                break;
                
            case 4:
                //pip.setPos((int)(c[0]),(int)(c[1]));
                pip.setPos((int)(c[0] - foo.getWidth()*.25),(int)(c[1] + foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos((int)(c[0] + foo.getWidth()*.25),(int)(c[1] - foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos((int)(c[0] + foo.getWidth()*.25),(int)(c[1] + foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos((int)(c[0] - foo.getWidth()*.25),(int)(c[1] - foo.getWidth()*.25));
                pip.draw(g);
                break;
                
            case 5:
                pip.setPos((int)(c[0] - foo.getWidth()*.25),(int)(c[1] + foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos((int)(c[0] + foo.getWidth()*.25),(int)(c[1] - foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos((int)(c[0] + foo.getWidth()*.25),(int)(c[1] + foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos((int)(c[0] - foo.getWidth()*.25),(int)(c[1] - foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos(c[0], c[1]);
                pip.draw(g);
                break;
                
            case 6:
                pip.setPos((int)(c[0] - foo.getWidth()*.25),(int)(c[1] + foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos((int)(c[0] + foo.getWidth()*.25),(int)(c[1] - foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos((int)(c[0] + foo.getWidth()*.25),(int)(c[1] + foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos((int)(c[0] - foo.getWidth()*.25),(int)(c[1] - foo.getWidth()*.25));
                pip.draw(g);
                pip.setPos((int)(c[0] - foo.getWidth()*.25), c[1]);
                pip.draw(g);
                pip.setPos((int)(c[0] + foo.getWidth()*.25), c[1]);
                pip.draw(g);
                break;
        }
    }
}
