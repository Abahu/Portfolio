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
public class LovelessRelationshipException extends Exception
{
    public LovelessRelationshipException() {}

    //Constructor that accepts a message
    public LovelessRelationshipException(String message)
    {
       super(message);
    }
}
