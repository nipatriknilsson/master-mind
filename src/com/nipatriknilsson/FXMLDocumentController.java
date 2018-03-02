/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nipatriknilsson;

import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

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
    
    Button[][] boardPegs = new Button [ rows ][ columns ];
    Button[] boardChecks = new Button [ rows ];
    CheckBox[][] boardFeedback = new CheckBox [ rows ][ columns ];
    ColorData[] colordataSelected = new ColorData [ columns ];
    ColorData[] colordataCurrent = new ColorData [ columns ];
    @FXML
    private CheckMenuItem menuItemOptionMultiple;
    @FXML
    private MenuItem menuitemTest;
    @FXML
    private Tab tableViewLog;
    
    private TableColumn<TableView, String> tableViewHighScoreTime;
    @FXML
    private ListView<String> listViewHighScore;
    @FXML
    private ListView<String> listViewLog;

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
                boardPegs [ r ][ c ].setDisable(true);

                gridPaneBoard.add ( boardPegs[r][c], c + columnoffset, rows - r - 1 );
            }
            
            boardChecks [ r ] = new Button( "Check" );

            boardChecks [ r ].setOnMouseClicked ( ( new EventHandler<MouseEvent>() {
                public void handle ( MouseEvent  event ) {
                onMouseClickedCheck ( event );
                }
            }));

            gridPaneBoard.add ( boardChecks [ r ], 5, rows - r - 1 );
            
            GridPane gridpane = new GridPane ();
            for ( int c = 0; c < columns; c ++ )
            {
                boardFeedback [ r ][ c ] = new CheckBox ( "" );
                boardFeedback [ r ][ c ].setDisable ( true );
            }
            
            gridpane.add ( boardFeedback [ r ][ 0 ], 0, 0 );
            gridpane.add ( boardFeedback [ r ][ 1 ], 1, 0 );
            gridpane.add ( boardFeedback [ r ][ 2 ], 0, 1 );
            gridpane.add ( boardFeedback [ r ][ 3 ], 1, 1 );

            gridPaneBoard.add ( gridpane, 5, rows - r - 1 );
        }

        for ( int c = 0; c < columns; c ++ )
        {
            colordataSelected [ c ] = new ColorData ();
            colordataCurrent [ c ] = new ColorData ();
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
        
        SQLiteJDBC.logHistory ( "Started" );
        
        listViewHighScore.setStyle ( "-fx-font-family: 'courier new'; -fx-font-size: 12pt;" );
        listViewLog.setStyle ( "-fx-font-family: 'courier new'; -fx-font-size: 8pt;" );

        ObservableList<String> highScoreItems = listViewHighScore.getItems();
        SQLiteJDBC.setHighScore ( highScoreItems );

        ObservableList<String> logItems = listViewLog.getItems();
        SQLiteJDBC.setLog ( logItems );
        
        newGame ();
        
    }
    
    private void newGame ()
    {
        for ( int r = 0; r < ( rows - 1 ); r ++ )
        {
            for ( int c = 0; c < columns; c ++ )
            {
                boardPegs [ r ][ c ].setStyle ( null );
                boardFeedback [ r ][ c ].setStyle ( null );
            }
        }

        rowToGuess = 0;
        setControlWidgets ();
        
        inititateNewColors ();
        showSelection ();
        
    
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
                colordataCurrent [ column ].setRGB ( colorData.getRGB () );

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
        newGame ();
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
        boolean ok = true;

        if ( check )
        {
            for ( int c = 0; c < columns; c ++ )
            {
                if ( colordataCurrent [ c ].getRGB() == -1 )
                {
                    ok = false;
                    break;
                }
                
                if ( colordataSelected [ c ].getRGB() != colordataCurrent [ c ].getRGB() )
                {
                    ok = false;
                    break;
                }
            }
            
            for ( int c = 0; c < columns; c ++ )
            {
                colordataCurrent [ c ].setRGB ( -1 );
            }

            if ( ++rowToGuess < rows )
            {

            }
        }

        boolean checkDisabled = false;
        
        for ( int r = 0; r < ( rows - 1 ); r ++ )
        {
            boardChecks [ r ].setDisable ( r != rowToGuess );
            boardChecks [ r ].setVisible ( r == rowToGuess );

            for ( int c = 0; c < columns; c ++ )
            {
                boardPegs [ r ][ c ].setDisable ( r != rowToGuess || ( check && ok ) );
            }

            boardFeedback [ r ][ 0 ].getParent().setVisible ( false );
        }
        
        boardFeedback [ rows - 1  ][ 0 ].getParent().setVisible ( false );

            for ( int c = 0; c < columns; c ++ )
        {
            if ( colordataCurrent [ c ].getRGB () == -1 )
            {
                checkDisabled = true;
            }
        }

        boardChecks [ rowToGuess ].setDisable ( checkDisabled );
        boardChecks [ rowToGuess ].setVisible ( ! ( check && ok ) );

        for ( int r = 0; r < rowToGuess; r ++ )
        {
            boardFeedback [ r ][ 0 ].getParent().setVisible ( true );
        }
        
        if ( check && ok )
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("MasterMind");
            alert.setHeaderText("You bet the game in x tries");
            alert.setContentText("What do you want to do next?");

            ButtonType buttonPlayAgain = new ButtonType("Play again");
            ButtonType buttonExit = new ButtonType("Exit");

            alert.getButtonTypes().setAll ( buttonPlayAgain, buttonExit );

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == buttonPlayAgain)
            {
                // ... user chose "One"
            }
            else if ( result.get() == buttonExit )
            {
                System.exit ( 0 );
            }
            else
            {
                // ... user chose CANCEL or closed the dialog
            }            
        }
    }

    private void onMouseClickedCheck(Event event) {
        setControlWidgets ( true );
    }

    private int nextColor ( int steps )
    {
        while ( steps >= 0 )
        {
            for ( int i = 0; i < 8; i++)
            {
                if ( colorData [ i ].isInUse () )
                {
                    if ( steps == 0 )
                    {
                        return colorData [ i ].getRGB();
                    }

                    steps --;
                }
            }
        }
        
        return -1;
    }
    
    private void inititateNewColors ()
    {
        for ( int c = 0; c < columns; c++ )
        {
            colordataSelected [ c ].setRGB( nextColor ( randomGenerator.nextInt ( 16 ) ) );
        }
    }
    
    private void showSelection ()
    {
        for ( int c = 0; c < columns; c++ )
        {
            initializeButton ( boardPegs [ rows - 1 ][ c ], colordataSelected [ c ].getBgColor () );
            boardPegs [ rows - 1 ][ c ].setDisable ( true );
        }

        boardChecks [ rows - 1 ].setDisable ( true );
        boardChecks [ rows - 1 ].setVisible ( false );
    }

    @FXML
    private void onMenuItemTest(ActionEvent event) {
    }
}
