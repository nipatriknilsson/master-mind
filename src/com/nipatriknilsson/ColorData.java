/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nipatriknilsson;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.shape.Circle;

/**
 *
 * @author user
 * 
 * Class to store color information and to get styles to set to widgets.
 * Also keep track of what is selected.
 */

public class ColorData {

    private boolean checked;
    private int classcolor;
    private int classcolortext;
    private String latinname;

    ColorData ( int htmlhexcolor, int htmlhextext, String latinname ) {
        this.classcolor = htmlhexcolor;
        this.classcolortext = htmlhextext;
        this.latinname = latinname;
        this.checked = true;
    }

    ColorData () {
        this.classcolor = -1;
        this.classcolortext = -1;
        this.latinname = "";
        this.checked = false;
    }

    public String getHexBgColor() {
        return String.format("%06x", classcolor);
    }

    public String getHexFgColor() {
        return String.format("%06x", classcolortext);
    }

    public String getName() {
        return latinname;
    }
    
    public void setName( String latinname ) {
        this.latinname = latinname;
    }
    
    /*
    * Set style to button with default color
    */
    public static void setStyle ( Button button )
    {
        setStyle ( button, null );
    }

    /*
    * Set style to button with color
    */
    public static void setStyle ( Button button, String colorBackground )
    {
        button.setStyle ( getStyle ( colorBackground, null ) );
        double r = 15;
        button.setShape ( new Circle ( r ) );
        button.setMinSize ( 2 * r, 2 * r );
        button.setMaxSize ( 2 * r, 2 * r );
    }
    
    /*
    * Create a JavaFX style string with colors as input
    */
    public static String getStyle ( String bgColor, String fgColor ) {
        String s = "";
        
        if ( bgColor != null && bgColor != "" )
        {
            s += "-fx-background-color: #" + bgColor + ";";
        }
        else
        {
            s += "-fx-background-color: #f4f4f4;";
        }
        
        if ( fgColor != null && fgColor != "" )
        {
            s += "-fx-text-fill: #" + fgColor + ";";
        }

        s += "-fx-line-spacing: 0em; -fx-font-family: san-serif,'courier new',arial; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 5px;";
        
        s += "-fx-border-color: #000000;";
        
        s += "-fx-opacity: 1.0";

        return s;
    }

    /*
    * Get JavaFX style string
    */
    public String getStyle () {
        return getStyle ( getHexBgColor(), getHexFgColor() );
    }
    
    public int getRGB ()
    {
        return classcolor;
    }
    
    public void setRGB ( int color )
    {
        classcolor = color;
    }

    public boolean isChecked ()
    {
        return checked;
    }
}
