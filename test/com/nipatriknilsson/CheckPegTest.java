/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nipatriknilsson;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author user
 * 
 * Unit tests for business logic
 */
public class CheckPegTest {
    
    void doTest ( int goal0, int goal1, int goal2, int goal3, 
                  int guess0, int guess1, int guess2, int guess3,
                  int black, 
                  int white )
    {
        ColorData[] colordataGoal =  new ColorData[] 
        {
            new ColorData ( goal0, 0, "" ),
            new ColorData ( goal1, 0, "" ),
            new ColorData ( goal2, 0, "" ),
            new ColorData ( goal3, 0, "" ),
        };
        
        ColorData[] colordataGuess =  new ColorData[] 
        {
            new ColorData ( guess0, 0, "" ),
            new ColorData ( guess1, 0, "" ),
            new ColorData ( guess2, 0, "" ),
            new ColorData ( guess3, 0, "" ),
        };
        
        CheckPeg checkPeg = new CheckPeg ( colordataGoal, colordataGuess );
        assertSame ( checkPeg.countBlack, black );
        assertSame ( checkPeg.countWhite, white );
    }
    
    @Test
    public void test_000 () {
        doTest ( 1, 2, 3, 4, //Goal
                 1, 2, 3, 4, //Guess
                 4,          //Number black
                 0 );        //Number white
    }

    @Test
    public void test_001 () {
        doTest ( 1, 2, 3, 4, //Goal
                 2, 1, 3, 4, //Guess
                 2,          //Number black
                 2 );        //Number white
    }

    @Test
    public void test_002 () {
        doTest ( 1, 2, 3, 4, //Goal
                 1, 1, 3, 4, //Guess
                 3,          //Number black
                 0 );        //Number white
    }

    @Test
    public void test_003 () {
        doTest ( 1, 2, 3, 4, //Goal
                 1, 1, 1, 4, //Guess
                 2,          //Number black
                 0 );        //Number white
    }

    @Test
    public void test_004 () {
        doTest ( 1, 2, 3, 4, //Goal
                 1, 1, 1, 1, //Guess
                 1,          //Number black
                 0 );        //Number white
    }

    @Test
    public void test_005 () {
        doTest ( 1, 2, 3, 4, //Goal
                 5, 6, 7, 8, //Guess
                 0,          //Number black
                 0 );        //Number white
    }

    @Test
    public void test_006 () {
        doTest ( 1, 2, 3, 4, //Goal
                 1, 2, 1, 2, //Guess
                 2,          //Number black
                 0 );        //Number white
    }

    @Test
    public void test_007 () {
        doTest ( 1, 2, 3, 4, //Goal
                 3, 3, 1, 2, //Guess
                 0,          //Number black
                 3 );        //Number white
    }

    @Test
    public void test_008 () {
        doTest ( 1, 2, 3, 4, //Goal
                 5, 6, 1, 2, //Guess
                 0,          //Number black
                 2 );        //Number white
    }

    @Test
    public void test_009 () {
        doTest ( 1, 1, 3, 4, //Goal
                 5, 6, 1, 2, //Guess
                 0,          //Number black
                 1 );        //Number white
    }

    @Test
    public void test_010 () {
        doTest ( 1, 1, 1, 4, //Goal
                 5, 6, 1, 2, //Guess
                 1,          //Number black
                 0 );        //Number white
    }

    @Test
    public void test_011 () {
        doTest ( 1, 1, 1, 1, //Goal
                 5, 6, 3, 2, //Guess
                 0,          //Number black
                 0 );        //Number white
    }

    @Test
    public void test_012 () {
        doTest ( 1, 2, 2, 1, //Goal
                 5, 2, 1, 2, //Guess
                 1,          //Number black
                 2 );        //Number white
    }

    @Test
    public void test_013 () {
        doTest ( 1, 2, 2, 1, //Goal
                 5, 2, 2, 2, //Guess
                 2,          //Number black
                 0 );        //Number white
    }

    @Test
    public void test_014 () {
        doTest ( 2, 1, 2, 1, //Goal
                 1, 2, 2, 2, //Guess
                 1,          //Number black
                 2 );        //Number white
    }

    @Test
    public void test_015 () {
        doTest ( 2, 2, 2, 1, //Goal
                 1, 2, 2, 2, //Guess
                 2,          //Number black
                 2 );        //Number white
    }

    @Test
    public void test_016 () {
        doTest ( 3, 2, 2, 1, //Goal
                 1, 2, 2, 2, //Guess
                 2,          //Number black
                 1 );        //Number white
    }

    @Test
    public void test_017 () {
        doTest ( 3, 2, 3, 1, //Goal
                 1, 3, 2, 2, //Guess
                 0,          //Number black
                 3 );        //Number white
    }

    @Test
    public void test_018 () {
        doTest ( 3, 2, 3, 1, //Goal
                 2, 3, 2, 2, //Guess
                 0,          //Number black
                 2 );        //Number white
    }

    @Test
    public void test_019 () {
        doTest ( 3, 2, 3, 1, //Goal
                 2, 3, 2, 3, //Guess
                 0,          //Number black
                 3 );        //Number white
    }
}
