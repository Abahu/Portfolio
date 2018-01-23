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
public abstract class Score {
    private static final int NOTHING = 0;
    private static final int TWO_OF_A_KIND = 1;
    private static final int PAIR = 2;
    private static final int THREE_OF_A_KIND = 3;
    private static final int FIVE_STRAIGHT = 4;
    private static final int SIX_STRAIGHT = 5;
    private static final int FULL_HOUSE = 6;
    private static final int FOUR_OF_A_KIND = 7;
    private static final int FIVE_OF_A_KIND = 8;
    
    public static final String[] NAMES_OF_SCORES = new String[] {  "Bust",
                                                                    "Two of a kind",
                                                                    "Pair",
                                                                    "Three of a kind",
                                                                    "Five high straight",
                                                                    "Six high straight",
                                                                    "Full house",
                                                                    "Four of a kind",
                                                                    "Five of a kind"};
    
    public static class result
    {
        public int type;
        public int score;
        /**
         *  Returns whether or not this result is better than the passed result
         *  draw = 0, this = 1, other = 2
         *  @param other the score with which comparison is made.
         *  @return The game result: 0: draw, 1: player 1 wins, 2: player 2 wins
        */
        public int isHigher(result other)
        {
            if (other.type > type)
                return 2;
            else if (other.type < type)
                return 1;
            else
            {
                if (other.score > score)
                    return 2;
                else if (other.score < score)
                    return 1;
                else
                    return 0;
            }
        }
    }
    
    public static result score(int diceValues[])
    {
        int[] values = new int[6];
        result res = new result();
        
        for (int foo : diceValues)
            values[foo - 1]++;
        
        int max = 0;
        int maxIndex = 0;
        int min = 7;
        int minIndex = 0;
        for (int i = 0; i < 6; i++)
        {
            if (values[i] > max)
            {
                max = values[i];
                maxIndex = i;
            }
            if (values[i] < min && values[i] != 0)
            {
                min = values[i];
                minIndex = i;
            }
        }
        maxIndex++;
        minIndex++;
        if (max == 5)
        {
            res.type = FIVE_OF_A_KIND;
            res.score = 5 * maxIndex;
        }
        if (max == 4)
        {
            res.type = FOUR_OF_A_KIND;
            res.score = 4 * maxIndex;
        }
        if (max == 3)
        {
            if (min == 2)
            {
                res.type = FULL_HOUSE;
                res.score = (3 * maxIndex) + (2 * minIndex);
            }
            else
            {
                res.type = THREE_OF_A_KIND;
                res.score = 3 * maxIndex;
            }
        }
        if (max == 2)
        {
            int count = 0;
            int[] countIndex = new int[2];
            for (int i = 0; i < values.length; i++)
                if ( values[i] == 2)
                {
                    countIndex[count] = i;
                    count++;
                }
            if (count == 2)
            {
                res.type = PAIR;
            }
            else
            {
                res.type = TWO_OF_A_KIND;
            }
            res.score = 2*(countIndex[0]+countIndex[1]+2);
        }
        if (max == 1)
        {
            if (isFlush(true, values))
            {
                res.type = FIVE_STRAIGHT;
                res.score = 15;
            }
            else if(isFlush(false, values))
            {
                res.type = SIX_STRAIGHT;
                res.score = 20;
            }
            else
            {
                res.type = NOTHING;
                res.score = maxIndex + 1;
            }
        }
        
        return res;
    }
    
    private static boolean isFlush(boolean five, int[] values)
    {
        int mod = (five) ? 0 : 1;
        for (int i = 0; i < 5; i++)
            if (values[i + mod] == 0)
                return false;
        return true;
    }
}
