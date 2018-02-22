/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nipatriknilsson;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;

/**
 *
 * @author user
 */
public class ColorData {

    private boolean checked;
    private int htmlhexcolor;
    private int htmlhextext;
    private String latinname;

    ColorData(int htmlhexcolor, int htmlhextext, String latinname) {
        this.htmlhexcolor = htmlhexcolor;
        this.htmlhextext = htmlhextext;
        this.latinname = latinname;
        this.checked = true;
    }

    public String getBgColor() {
        return String.format("%06x", htmlhexcolor);
    }

    public String getFgColor() {
        return String.format("%06x", htmlhextext);
    }

    public String getName() {
        return latinname;
    }
    
    public static void setStyle ( Button button )
    {
        setStyle ( button, null );
    }
    
    public static void setStyle ( Button button, String colorBackground )
    {
        button.setStyle ( getStyle ( colorBackground, null ) );
        double r = 15;
        button.setShape ( new Circle ( r ) );
        button.setMinSize ( 2 * r, 2 * r );
        button.setMaxSize ( 2 * r, 2 * r );
    }
    
    public static String getStyle ( String bgColor, String fgColor ) {
        String s = "";
        
        if ( bgColor != null && bgColor != "" )
        {
            s += "-fx-background-color: #" + bgColor + ";";
        }
        
        if ( fgColor != null && fgColor != "" )
        {
            s += "-fx-text-fill: #" + fgColor + ";";
        }

        s += "-fx-line-spacing: 0em; -fx-font-family: san-serif,'courier new',arial; -fx-font-size: 20px; -fx-font-weight: bold; -fx-padding: 5px;";
        
        s += "-fx-border-color: #000000;";

//        s += "-fx-alignment: center;";

        return s;
    }

    public String getStyle ( boolean empty ) {
        return getStyle ( getBgColor(), getFgColor() );
    }

    public String getStyle () {
        return getStyle ( getBgColor(), getFgColor() );
    }
    
    public int getRGB ()
    {
        return htmlhexcolor;
    }
    
    public boolean isInUse ()
    {
        return checked;
    }
}
