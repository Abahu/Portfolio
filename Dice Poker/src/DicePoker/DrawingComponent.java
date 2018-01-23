/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DicePoker;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;

class DrawingComponent extends JComponent 
{
    private Color boxColor;
    
    @Override
    public void paintComponent(Graphics g)
    {
        //Create drawing component
        Graphics2D g2 = (Graphics2D) g;
       
        BasicStroke myStroke = new BasicStroke(2.0f);
        g2.setStroke(myStroke);
        
        int[] pos = {200,200}; 
        Dice6 test = new Dice6(pos);
        //Dice4 test = new Dice4(pos);
        test.roll();
        test.draw(g2);
    }
    
    public void drawMethod()
    {
        
    }
    
    public void setBoxColor(Color newColour)
    {
        boxColor = newColour;
    }
}
