/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nipatriknilsson;

/**
 *
 * @author user
 * 
 * Class to calculate black and white feedback pins.
 * 
 */
public class CheckPeg {

    int countBlack = 0;
    int countWhite = 0;
    private boolean[] usedGoal;
    private boolean[] usedGuess;

    CheckPeg ( ColorData goal [], ColorData guess [] )
    {
        if ( goal.length != guess.length )
        {
            throw new IllegalArgumentException ();
        }
        
        int columns = goal.length;

        usedGoal = new boolean [ columns ];
        usedGuess = new boolean [ columns ];

        for ( int c = 0; c < columns; c ++ )
        {
            usedGoal [ c ] = false;
            usedGuess [ c ] = false;
        }

        for ( int cGoal = 0; cGoal < columns; cGoal ++ )
        {
            if ( guess [ cGoal ].getRGB () == goal [ cGoal ].getRGB () )
            {
                countBlack ++;
                usedGoal [ cGoal ] = true;
                usedGuess [ cGoal ] = true;
            }
        }

        for ( int cGuess = 0; cGuess < columns; cGuess ++ )
        {
            if ( ! usedGuess [ cGuess ] )
            {
                for ( int cGoal = 0; cGoal < columns; cGoal ++ )
                {
                    if ( ! usedGoal [ cGoal ] )
                    {
                        if ( guess [ cGuess ].getRGB() == goal [ cGoal ].getRGB() )
                        {
                            countWhite ++;
                            usedGuess [ cGuess ] = true;
                            usedGoal [ cGoal ] = true;

                            break;
                        }
                    }
                }
            }
        }
    }
}
