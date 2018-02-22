/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nipatriknilsson;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/**
 *
 * @author user
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private AnchorPane anchorPaneBackgroud;
    @FXML
    private GridPane gridPaneBoard;

    Random randomGenerator = new Random ();

    final int rows = 14;
    final int columns = 4;
    
    final int columnoffset = 1;

    final int checkResultColumn = columns + columnoffset;
    int rowToGuess = 0;

    ColorData[] colorData =  new ColorData[] 
    {
        new ColorData ( 0xff0000, 0x000000, "Red" ),
        new ColorData ( 0x00ff00, 0x000000, "Green" ),
        new ColorData ( 0x0000ff, 0xffffff, "Blue" ),
        new ColorData ( 0xffff00, 0x000000, "Yellow" ),
        new ColorData ( 0xff00ff, 0xffffff, "Magenta" ),
        new ColorData ( 0xffa500, 0x000000, "Orange" ),
        new ColorData ( 0x000000, 0xffffff, "Black" ),
        new ColorData ( 0xffffff, 0x000000, "White" )
    };
    @FXML
    private CheckMenuItem menuItemColor0;
    @FXML
    private CheckMenuItem menuItemColor1;
    @FXML
    private CheckMenuItem menuItemColor2;
    @FXML
    private CheckMenuItem menuItemColor3;
    @FXML
    private CheckMenuItem menuItemColor4;
    @FXML
    private CheckMenuItem menuItemColor5;
    @FXML
    private CheckMenuItem menuItemColor6;
    @FXML
    private CheckMenuItem menuItemColor7;
    
    Button[][] boardPegs = new Button[rows][4];
    Button[] boardChecks = new Button[rows];
    int[] intcolorselected = new int [ columns ];
    int[] intcolorcurrent = new int [ columns ];
    
    private void initializeButton ( Button button ) {
        initializeButton ( button, null );
    }

    private void initializeButton ( Button button, String colorBackground ) {
        ColorData.setStyle ( button, colorBackground );
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        for ( int r = 0; r < rows; r ++ )
        {
            for ( int c = 0; c < columns; c ++ )
            {
                final int cconst = c;
                boardPegs[r][c] = new Button();
                boardPegs[r][c].setOnMouseClicked ( ( new EventHandler<MouseEvent>() {
                    public void handle ( MouseEvent  event ) {
                    onMouseClickedPeg ( event, cconst );
                    }
                }));

                initializeButton ( boardPegs[r][c] );
                boardPegs[r][c].setDisable(true);

                gridPaneBoard.add ( boardPegs[r][c], c + columnoffset, rows - r - 1 );
            }
            
            boardChecks [ r ] = new Button( "Check" );

            boardChecks [ r ].setOnMouseClicked ( ( new EventHandler<MouseEvent>() {
                public void handle ( MouseEvent  event ) {
                onMouseClickedCheck ( event );
                }
            }));

            gridPaneBoard.add ( boardChecks [ r ], 5, rows - r - 1 );
        }

        for ( int c = 0; c < columns; c ++ )
        {
            boardPegs [ 0 ][ c ].setDisable( false );
            intcolorselected [ c ] = -1;
        }

        setMenuItem ( menuItemColor0, colorData [ 0 ] );
        setMenuItem ( menuItemColor1, colorData [ 1 ] );
        setMenuItem ( menuItemColor2, colorData [ 2 ] );
        setMenuItem ( menuItemColor3, colorData [ 3 ] );
        setMenuItem ( menuItemColor4, colorData [ 4 ] );
        setMenuItem ( menuItemColor5, colorData [ 5 ] );
        setMenuItem ( menuItemColor6, colorData [ 6 ] );
        setMenuItem ( menuItemColor7, colorData [ 7 ] );

        menuItemColor0.setSelected ( true );
        menuItemColor1.setSelected ( true );
        menuItemColor2.setSelected ( true );
        menuItemColor3.setSelected ( true );
        menuItemColor4.setSelected ( true );
        menuItemColor5.setSelected ( true );
        menuItemColor6.setSelected ( true );
        menuItemColor7.setSelected ( true );

        gridPaneBoard.setStyle("-fx-border-color: #000000; -fx-border-width: 1px;");
        
        setControlWidgets ();
    }    

    private ContextMenu createContextMenu ( ContextMenu contextMenu, Button button, ColorData colorData, int column  ) {
        if ( contextMenu == null ) {
            contextMenu = new ContextMenu ();
        }

        MenuItem menuItem = new MenuItem ( colorData.getName () );
        menuItem.setStyle ( colorData.getStyle() );
        menuItem.setOnAction( new EventHandler <ActionEvent> () {
            public void handle ( ActionEvent e ) {
                initializeButton ( button, colorData.getBgColor() );
                intcolorselected [ column ] = colorData.getRGB ();

                setControlWidgets ();
            };
        });
        
        contextMenu.getItems().add(menuItem);
        
        return contextMenu;
    }

    private void setMenuItem ( MenuItem menuitem, ColorData colordata )
    {
        menuitem.setText ( colordata.getName() );
        menuitem.setStyle ( colordata.getStyle () );
    }
    
    private void onMouseClickedPeg(MouseEvent event, int column ) {
        ContextMenu contextMenu = null;
        
        for ( int i = 0; i < colorData.length; i ++ )
        {
            contextMenu = createContextMenu ( contextMenu, (Button)event.getSource(), colorData[i], column );
        }

        contextMenu.show ( (Button)event.getSource(), event.getScreenX(), event.getScreenY() );
    }

    @FXML
    private void onMenuItemGameNew(ActionEvent event) {
    }

    @FXML
    private void onMenuItemGameExit(ActionEvent event) {
        System.exit ( 0 );
    }

    @FXML
    private void onMenuItemColor0(ActionEvent event) {
    }

    @FXML
    private void onUpdateMenuItemColor0(Event event) {
    }

    @FXML
    private void onMenuItemColor1(ActionEvent event) {
    }

    @FXML
    private void onUpdateMenuItemColor1(Event event) {
    }

    @FXML
    private void onMenuItemColor2(ActionEvent event) {
    }

    @FXML
    private void onUpdateMenuItemColor2(Event event) {
    }

    @FXML
    private void onMenuItemColor3(ActionEvent event) {
    }

    @FXML
    private void onUpdateMenuItemColor3(Event event) {
    }

    @FXML
    private void onMenuItemColor4(ActionEvent event) {
    }

    @FXML
    private void onUpdateMenuItemColor4(Event event) {
    }

    @FXML
    private void onMenuItemColor5(ActionEvent event) {
    }

    @FXML
    private void onUpdateMenuItemColor5(Event event) {
    }

    @FXML
    private void onMenuItemColor6(ActionEvent event) {
    }

    @FXML
    private void onUpdateMenuItemColor6(Event event) {
    }

    @FXML
    private void onMenuItemColor7(ActionEvent event) {
    }

    @FXML
    private void onUpdateMenuItemColor7(Event event) {
    }

    private void setControlWidgets ()
    {
        setControlWidgets ( false );
    }
    
    private void setControlWidgets ( boolean check )
    {
        if ( check )
        {
            boolean ok = true;
            
            for ( int c = 0; c < columns; c ++ )
            {
                if ( intcolorcurrent [ c ] == -1 )
                {
                    ok = false;
                    break;
                }
                
                if ( intcolorselected [ c ] != intcolorcurrent [ c ] )
                {
                    ok = false;
                    break;
                }
            }
            
            if ( ok )
            {
                for ( int c = 0; c < columns; c ++ )
                {
                    intcolorcurrent [ c ] = -1;
                }

                if ( ++rowToGuess < rows )
                {
                    
                }
            }
        }

        boolean checkDisabled = false;
        
        for ( int r = 0; r < rows; r ++ )
        {
            boardChecks [ r ].setDisable ( r != rowToGuess );
            boardChecks [ r ].setVisible ( r == rowToGuess );
        }
        
        for ( int c = 0; c < columns; c ++ )
        {
            if ( intcolorselected [ c ] == -1 )
            {
                checkDisabled = true;
            }
        }

        boardChecks [ rowToGuess ].setDisable ( checkDisabled );
        boardChecks [ rowToGuess ].setVisible ( true );
    }

    private void onMouseClickedCheck(Event event) {
        setControlWidgets ( true );
    }
    
    private void inititateNewColors ()
    {
        int r = randomGenerator.nextInt ( 16 );
        
        for ( int c = 0; c < columns; c++ )
        {
            for ( int i = 0; i < 8; i++)
            {
                if ( colorData[i].isInUse () )
                {




                }
            }
        }
    }
}
