/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DicePoker;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JToggleButton;

/**
 *
 * @author DM
 */
public class DiceButton extends JToggleButton
{
    Dice6 val;
    boolean clicked;
    boolean clickable;
    boolean var_19_2000;
    
    public DiceButton()
    {
        super();
        clickable = false;
        var_19_2000 = false;
        val = null;
    }
    
    public DiceButton(Dice6 newDice)
    {
        //super();
        clickable = false;
        var_19_2000 = false;
        val = newDice;
    }
    
    public void setClickable(boolean val)
    {
        clickable = val;
    }
    
    public boolean getClickable()
    {
        return clickable;
    }
    
    public boolean getClicked()
    {
        return clicked;
    }
    
    public void setValue(int newValue) //For multiplayer only
    {
        val.setValue(newValue);
    }
    
    public void setClicked(boolean value)
    {
        clicked = value;
    }
    
    public void toggleClicked()
    {
        if (clickable)
        {
            val.toggleClick();
            clicked = !clicked;
        }
        else
        {
            var_19_2000 = !var_19_2000;
            if (var_19_2000)
                this.doClick();
        }
    }
    
    public void roll()
    {
        val.roll();
        clicked = false;
        val.unclick();
        this.setSelected(false);
    }
    
    public void setDieContext(Dice6 die)
    {
        val = die;
    }
    
    public int getValue()
    {
        return val.getValue();
    }
    
    public void reset()
    {
        val.reset();
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        
        val.draw(g2);
    }
}
