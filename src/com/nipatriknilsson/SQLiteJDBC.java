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
    }

    void close ()
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
    
    private void connect () throws ClassNotFoundException, SQLException
    {
        try
        {
            Class.forName( "org.sqlite.JDBC" );
            conn = DriverManager.getConnection("jdbc:sqlite:mastermind.db");

            System.out.println ( "Opened database connection successfully" );

            Statement localstmt = conn.createStatement();
            localstmt.execute ( "create table if not exists history ( id datetime default current_timestamp, event text)" );
            localstmt.close ();

            localstmt = conn.createStatement();
            localstmt.execute ( "create table if not exists highscore ( user text not null, score integer)" );
            localstmt.close ();

            System.out.println ( "Table History created successfully" );
        }
        catch ( Exception e )
        {
            System.out.println ( "Error executing sql: " + e.getMessage() );
        }
    }
    
    static public void logHistory ( String event )
    {
        SQLiteJDBC jdbc = new SQLiteJDBC();
        
        try
        {
            jdbc.connect ();
            
            String s = "insert into history (event) values ('" + event + "')";
            System.out.println ( s );
            jdbc.stmt = jdbc.conn.createStatement();
            jdbc.stmt.executeUpdate ( s );
            jdbc.close ();

            System.out.println ( "LogHistory executed successfully" );
        }
        catch ( Exception e )
        {
            System.out.println ( "Error executing sql: " + e.getMessage() );
            jdbc.close ();
        }
    }

    static public void setLog ( ObservableList<String> ol )
    {
        ol.clear();

        SQLiteJDBC jdbc = new SQLiteJDBC();
        
        try
        {
            jdbc.connect ();
            jdbc.stmt = jdbc.conn.createStatement();
            jdbc.rs = jdbc.stmt.executeQuery ( "select datetime(id,'localtime'),event from history order by id desc limit 200" );
            
            boolean isempty = true;
            while ( jdbc.rs.next () )
            {
                ol.add ( jdbc.rs.getString ( 1 ) + " "+ jdbc.rs.getString ( 2 ) );
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
    
    static public void setHighScore ( ObservableList<String> ol )
    {
        ol.clear();

        SQLiteJDBC jdbc = new SQLiteJDBC();
        
        try
        {
            jdbc.connect ();
            jdbc.stmt = jdbc.conn.createStatement();
            jdbc.rs = jdbc.stmt.executeQuery ( "select * from highscore order by score asc limit 10" );

            boolean isempty = true;
            while ( jdbc.rs.next () )
            {
                ol.add ( jdbc.rs.getString ( 2 ) + " "+ jdbc.rs.getString ( 1 ) );
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
