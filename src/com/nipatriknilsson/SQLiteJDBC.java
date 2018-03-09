/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nipatriknilsson;
import java.sql.*;
import javafx.collections.ObservableList;

/**
 *
 * @author user
 */
public class SQLiteJDBC {
    
    //Tutorial
    //https://www.tutorialspoint.com/sqlite/sqlite_java.htm
    
    Connection conn = null;
    Statement stmt = null;
    ResultSet rs = null;
    
    private SQLiteJDBC()
    {
        //Make sure we only use it within this classes static members
        //make it Private
    }

    /*
    Clean up and close database
    */
    private void close ()
    {
        try
        {
            if ( rs != null )
            {
                rs.close();
                rs = null;
            }
        }
        catch ( Exception e )
        {
            System.out.println ( "Error closing statement: " + e.getMessage() );
        }

        try
        {
            if ( stmt != null )
            {
                stmt.close();
                stmt = null;
            }
        }
        catch ( Exception e )
        {
            System.out.println ( "Error closing statement: " + e.getMessage() );
        }
            
        try
        {
            if ( conn != null )
            {
                conn.close();
                conn = null;
            }
        }
        catch ( Exception e )
        {
            System.out.println ( "Error closing connection: " + e.getMessage() );
        }
    }
    
    /*
    Open database connection
    */
    private void connect () throws ClassNotFoundException, SQLException
    {
        try
        {
            Class.forName( "org.sqlite.JDBC" );
            conn = DriverManager.getConnection("jdbc:sqlite:mastermind.db");

            Statement localstmt = conn.createStatement();
            localstmt.execute ( "create table if not exists history ( id datetime default current_timestamp, event text)" );
            localstmt.close ();

            localstmt = conn.createStatement();
            localstmt.execute ( "create table if not exists highscore ( user text not null, time integer, score integer)" );
            localstmt.close ();
        }
        catch ( Exception e )
        {
            System.out.println ( "Error executing sql: " + e.getMessage() );
        }
    }
    
    /*
    Add to database and display
    */
    static public void addLog ( ObservableList<String> ol, String event )
    {
        SQLiteJDBC jdbc = new SQLiteJDBC();
        
        try
        {
            jdbc.connect ();
            
            String s = "insert into history (event) values ('" + event + "')";
            jdbc.stmt = jdbc.conn.createStatement();
            jdbc.stmt.executeUpdate ( s );
            jdbc.close ();

        }
        catch ( Exception e )
        {
            System.out.println ( "Error executing sql: " + e.getMessage() );
            jdbc.close ();
        }

        if ( ol != null )
        {
            setObservableLog ( ol );
        }
    }

    /*
    Add to database and display
    */
    static public void addHighScore ( ObservableList<String> ol, String name, int score, long time )
    {
        SQLiteJDBC jdbc = new SQLiteJDBC();
        
        try
        {
            jdbc.connect ();
            
            String s = "insert into highscore (user,score,time) values ('" + name + "'," + score + "," + time + ")";
            jdbc.stmt = jdbc.conn.createStatement();
            jdbc.stmt.executeUpdate ( s );
            jdbc.close ();
        }
        catch ( Exception e )
        {
            System.out.println ( "Error executing sql: " + e.getMessage() );
            jdbc.close ();
        }
        
        if ( ol != null )
        {
            setObservableHighScore ( ol );
        }
    }

    /*
    Sets list by reading data from the data base
    */
    static public void setObservableLog ( ObservableList<String> ol )
    {
        ol.clear();

        SQLiteJDBC jdbc = new SQLiteJDBC();
        
        try
        {
            jdbc.connect ();
            jdbc.stmt = jdbc.conn.createStatement();
            jdbc.rs = jdbc.stmt.executeQuery ( "select datetime(id,'localtime'),event from history order by id desc, ROWID desc limit 200" );
            
            boolean isempty = true;
            while ( jdbc.rs.next () )
            {
                String s = jdbc.rs.getString ( 1 ) + " "+ jdbc.rs.getString ( 2 );
                ol.add ( s.substring ( 11 ) );
                isempty = false;//sqlite jdbc doesn't support is empty set query
            }
            
            if ( isempty )
            {
                ol.add ( "Nothing to display!" );
            }

            jdbc.close ();
        }
        catch ( Exception e )
        {
            ol.clear ();
            ol.add ( "Error executing sql: " + e.getMessage() );
            jdbc.close ();
        }
    }
    
    /*
    Sets list by reading data from the data base
    */
    static public void setObservableHighScore ( ObservableList<String> ol )
    {
        ol.clear();

        SQLiteJDBC jdbc = new SQLiteJDBC();
        
        try
        {
            jdbc.connect ();
            jdbc.stmt = jdbc.conn.createStatement();
            jdbc.rs = jdbc.stmt.executeQuery ( "select * from highscore order by score asc, time asc limit 10" );

            boolean isempty = true;
            while ( jdbc.rs.next () )
            {
                ol.add ( ( jdbc.rs.getString ( 1 ) + "          " ).substring ( 0, 9 ) + " " + jdbc.rs.getString ( 3 ) + " tries in " + jdbc.rs.getString ( 2 ) + " seconds" );
                isempty = false;//sqlite jdbc doesn't support is empty set query
            }
            
            if ( isempty )
            {
                ol.add ( "Nothing to display!" );
            }
            
            jdbc.close ();
        }
        catch ( Exception e )
        {
            ol.clear ();
            ol.add ( "Error executing sql: " + e.getMessage() );
            jdbc.close ();
        }
    }
}    
