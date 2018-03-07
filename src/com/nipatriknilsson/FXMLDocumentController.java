/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nipatriknilsson;

import java.io.InputStream;
import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.web.HTMLEditor;

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
    Button[][] boardFeedback = new Button [ rows ][ columns ];
    ColorData[] colordataGoal = new ColorData [ columns ];
    ColorData[] colordataGuess = new ColorData [ columns ];
    @FXML
    private CheckMenuItem menuItemOptionMultiple;
    @FXML
    private Tab tableViewLog;
    
    private TableColumn<TableView, String> tableViewHighScoreTime;
    @FXML
    private ListView<String> listViewHighScore;
    @FXML
    private ListView<String> listViewLog;
    
    private long timeKeeper = 0;
    private TextArea textAreaInstructions;
    @FXML
    private HTMLEditor htmlInstruction;
    
    ObservableList<String> observableHighScore = null;
    ObservableList<String> observableLog = null;
    @FXML
    private MenuItem menuItemGameNew;
    @FXML
    private MenuBar menuBar;

    private void initializeButton ( Button button ) {
        initializeButton ( button, null );
    }

    private void initializeButton ( Button button, String colorBackground ) {
        ColorData.setStyle ( button, colorBackground );
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        observableHighScore = listViewHighScore.getItems();
        observableLog = listViewLog.getItems();
        
        SQLiteJDBC.setObservableHighScore ( observableHighScore );
        
        //Couldn't find a read only option to HTMLEdit
        htmlInstruction.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent t) {
            setHtmlEdit ();
        }});
        
        setHtmlEdit ();
        
        //Hide HTMLEdit controls
        Node[] nodes = htmlInstruction.lookupAll ( ".tool-bar" ).toArray ( new Node [ 0 ] );
        for(Node node : nodes)
        {
            node.setVisible ( false );
            node.setManaged ( false );
        }
        htmlInstruction.setVisible ( true );
        htmlInstruction.setDisable ( false );
        htmlInstruction.autosize();
        
        //Classic Mastermind background
        anchorPaneBackgroud.setStyle ( "-fx-background-color: #d6926b;" );

        //Initiate all Pegs, Check and Feedback squares
        for ( int r = 0; r < rows; r ++ )
        {
            for ( int c = 0; c < columns; c ++ )
            {
                final int cconst = c;
                boardPegs [ r ][ c ] = new Button();
                boardPegs [ r ][ c ].setOnMouseClicked ( ( new EventHandler<MouseEvent>() {
                    public void handle ( MouseEvent  event ) {
                        onMouseClickedPeg ( event, cconst );
                        if ( timeKeeper == 0 )
                        {
                            timeKeeper = System.currentTimeMillis () / 1000;
                        }
                    }
                }));

                initializeButton ( boardPegs [ r ][ c ] );
                boardPegs [ r ][ c ].setDisable(true);

                gridPaneBoard.add ( boardPegs [ r ][ c ], c + columnoffset, rows - r - 1 );
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
                boardFeedback [ r ][ c ] = new Button ( "" );
                boardFeedback [ r ][ c ].setDisable ( true );
                boardFeedback [ r ][ c ].setMinSize ( 12, 12 );
                boardFeedback [ r ][ c ].setMaxSize ( 12, 12 );
                boardFeedback [ r ][ c ].setAlignment ( Pos.CENTER );
                boardFeedback [ r ][ c ].setOpacity ( 1 );
            }
            
            gridpane.setHgap ( 4 );
            gridpane.setVgap ( 4 );
            gridpane.add ( boardFeedback [ r ][ 0 ], 0, 0 );
            gridpane.add ( boardFeedback [ r ][ 1 ], 1, 0 );
            gridpane.add ( boardFeedback [ r ][ 2 ], 0, 1 );
            gridpane.add ( boardFeedback [ r ][ 3 ], 1, 1 );
            gridpane.setMinSize ( 30, 30 );
            gridpane.setMaxSize ( 30, 30 );
            gridpane.setAlignment ( Pos.CENTER );

            gridPaneBoard.add ( gridpane, 5, rows - r - 1 );
        }

        for ( int c = 0; c < columns; c ++ )
        {
            colordataGoal [ c ] = new ColorData ();
            colordataGuess [ c ] = new ColorData ();
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
        
        listViewHighScore.setStyle ( "-fx-font-family: 'courier new'; -fx-font-size: 12pt;" );
        listViewLog.setStyle ( "-fx-font-family: 'courier new'; -fx-font-size: 8pt;" );

        SQLiteJDBC.addLog ( observableLog, "Started" );
        
        newGame ();
        updateMenus ();
    }
    
    private void newGame ()
    {
        timeKeeper = 0;
        
        for ( int r = 0; r < rows; r ++ )
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
        menuItem.setOnAction(new EventHandler <ActionEvent> () {
            public void handle ( ActionEvent e ) {
                initializeButton ( button, colorData.getHexBgColor() );
                colordataGuess [ column ].setRGB ( colorData.getRGB () );
                colordataGuess [ column ].setName ( colorData.getName () );

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
            contextMenu = createContextMenu ( contextMenu, (Button)event.getSource(), colorData [ i ], column );
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
    private void onMenuItemColor1(ActionEvent event) {
    }


    @FXML
    private void onMenuItemColor2(ActionEvent event) {
    }


    @FXML
    private void onMenuItemColor3(ActionEvent event) {
    }


    @FXML
    private void onMenuItemColor4(ActionEvent event) {
    }


    @FXML
    private void onMenuItemColor5(ActionEvent event) {
    }


    @FXML
    private void onMenuItemColor6(ActionEvent event) {
    }


    @FXML
    private void onMenuItemColor7(ActionEvent event) {
    }

    private void updateMenus ()
    {
//        menuItemGameNew.setDisable ( true );
        
        boolean[] menucolorchecked = new boolean [ 8 ];
        
//        menuItemColor0.isSelected ()
    }

    /*
    * Update all widgets
    */
    private void setControlWidgets ()
    {
        setControlWidgets ( false );
    }
    
    /*
    * Update all widgets with check option if user pressed Check button
    */
    private void setControlWidgets ( boolean check )
    {
        boolean ok = true;
        long time = System.currentTimeMillis () / 1000 - timeKeeper;
        
        String checkColors = "";
        String checkFeedback = "";

        if ( check )
        {
            for ( int c = 0; c < columns; c ++ )
            {
                if ( checkColors == "" )
                {
                    checkColors = colordataGuess [ c ].getName();
                }
                else
                {
                    checkColors += "," + colordataGuess [ c ].getName();
                }
            }

            for ( int c = 0; c < columns; c ++ )
            {
                if ( colordataGuess [ c ].getRGB() == -1 )
                {
                    ok = false;
                    break;
                }
                
                if ( colordataGoal [ c ].getRGB() != colordataGuess [ c ].getRGB() )
                {
                    ok = false;
                    break;
                }
            }
            
            int countBlack = 0;
            int countWhite = 0;
            boolean[] pegused = new boolean [ columns ];

            for ( int c = 0; c < columns; c ++ )
            {
                pegused [ c ] = false;
            }
            
            for ( int cGoal = 0; cGoal < columns; cGoal ++ )
            {
                if ( colordataGuess [ cGoal ].getRGB() == colordataGoal [ cGoal ].getRGB() )
                {
                    countBlack ++;
                    pegused [ cGoal ] = true;
                }
            }
            
            for ( int cGoal = 0; cGoal < columns; cGoal ++ )
            {
                for ( int cGuess = 0; cGuess < columns; cGuess ++ )
                {
                    if ( ! pegused [ cGuess ] )
                    {
                        if ( colordataGuess [ cGoal ].getRGB() == colordataGoal [ cGuess ].getRGB() )
                        {
                            countWhite ++;
                            pegused [ cGuess ] = true;
                        }
                    }
                }
            }

            for ( int c = 0; c < columns; c ++ )
            {
                if ( countWhite != 0 )
                {
                    boardFeedback [ rowToGuess ][ c ].setStyle ( "-fx-background-color: white;-fx-border-color: white;" );
                    boardFeedback [ rowToGuess ][ c ].setOpacity ( 1 );
                    countWhite --;
                    
                    checkFeedback += "W";
                }
                else if ( countBlack != 0 )
                {
                    boardFeedback [ rowToGuess ][ c ].setStyle ( "-fx-background-color: black;-fx-border-color: black;" );
                    boardFeedback [ rowToGuess ][ c ].setOpacity ( 1 );
                    countBlack --;
                    checkFeedback += "B";
                }
                else
                {
                    boardFeedback [ rowToGuess ][ c ].setOpacity ( 0.2 );
                    checkFeedback += "-";
                }
            }
            
            for ( int c = 0; c < columns; c ++ )
            {
                colordataGuess [ c ].setRGB ( -1 );
            }

            rowToGuess++;
        }

        boolean checkDisabled = false;
        
        for ( int r = 0; r < rows; r ++ )
        {
            boardChecks [ r ].setDisable ( r != rowToGuess );
            boardChecks [ r ].setVisible ( r == rowToGuess );

            for ( int c = 0; c < columns; c ++ )
            {
                boardPegs [ r ][ c ].setDisable ( r != rowToGuess || ( check && ok ) );
            }

            boardFeedback [ r ][ 0 ].getParent().setVisible ( false );
        }
        
        for ( int c = 0; c < columns; c ++ )
        {
            if ( colordataGuess [ c ].getRGB () == -1 )
            {
                checkDisabled = true;
            }
        }

        if ( rowToGuess < rows )
        {
            boardChecks [ rowToGuess ].setDisable ( checkDisabled );
            boardChecks [ rowToGuess ].setVisible ( ! ( check && ok ) );
        }

        for ( int r = 0; r < rowToGuess; r ++ )
        {
            boardFeedback [ r ][ 0 ].getParent().setVisible ( true );
        }
        
        if ( check )
        {
            SQLiteJDBC.addLog ( observableLog, "Guess: " + checkColors + "; " + checkFeedback );
        }
        
        if ( check && ok )
        {
            Alert alert = new Alert ( AlertType.CONFIRMATION );
            alert.setTitle ( "MasterMind" );
            alert.setHeaderText ( "You bet the game in " + ( rowToGuess ) + " tries. Total time was " + ( time ) + " seconds");
            
            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));

            TextField name = new TextField();
            name.setPromptText ( "Name" );

            gridPane.add ( new Label( "Enter your name to save it in the high score list:" ), 0, 0 );
            gridPane.add ( name, 0, 1 );

            alert.getDialogPane().setContent(gridPane);

            alert.setContentText ( "What do you want to do next?" );

            ButtonType buttonPlayAgain = new ButtonType ( "Play again" );
            ButtonType buttonExit = new ButtonType ( "Exit" );

            alert.getButtonTypes().setAll ( buttonPlayAgain, buttonExit );

            Optional<ButtonType> result = alert.showAndWait();
            
            ButtonType buttonPressed = result.get();
            
            String s = name.getCharacters().toString();
            s = s.trim();
            
            if ( s != "" )
            {
                SQLiteJDBC.addHighScore (observableLog, s, rowToGuess, time );
            }
            
            if ( buttonPressed == buttonPlayAgain)
            {
                newGame ();
            }
            else if ( result.get() == buttonExit )
            {
                System.exit ( 0 );
            }
            else
            {
                newGame ();
                // ... user chose CANCEL or closed the dialog
            }            
        }
    }

    private void onMouseClickedCheck(Event event) {
        setControlWidgets ( true );
    }

    private ColorData nextColor ( int steps )
    {
        while ( steps >= 0 )
        {
            for ( int i = 0; i < 8; i++)
            {
                if ( colorData [ i ].isChecked () )
                {
                    if ( steps == 0 )
                    {
                        return colorData [ i ];
                    }

                    steps --;
                }
            }
        }
        
        return null;
    }
    
    private void inititateNewColors ()
    {
        String colors = "";
        
        for ( int c = 0; c < columns; c++ )
        {
            ColorData colorData = nextColor ( randomGenerator.nextInt ( 16 ) );
            colordataGoal [ c ].setRGB( colorData.getRGB() );
            
            if ( colors == "" )
            {
                colors = colorData.getName();
            }
            else
            {
                colors += "," + colorData.getName();
            }
        }
        
        SQLiteJDBC.addLog ( observableLog, "Goal: " + colors );
    }
    
    private void showSelection ()
    {
//        for ( int c = 0; c < columns; c++ )
//        {
//            initializeButton (boardPegs [ rows - 1 ][ c ], colordataGoal [ c ].getBgColor () );
//            boardPegs [ rows - 1 ][ c ].setDisable ( true );
//        }

//        boardChecks [ rows - 1 ].setDisable ( true );
//        boardChecks [ rows - 1 ].setVisible ( false );
    }

    private void setHtmlEdit ()
    {
        try
        {
            InputStream inputstream = FXMLDocumentController.class.getResourceAsStream ( "/resources/instruction.html" );
            Scanner scanner = new Scanner ( inputstream).useDelimiter ( "\\A" );
            String content = scanner.hasNext() ? scanner.next() : "";
            htmlInstruction.setHtmlText( content );
        }
        catch ( Exception e )
        {
            htmlInstruction.setHtmlText( "Unable to load instructions!" );
        }
    }

    @FXML
    private void onMenuGameOptionMultiple(ActionEvent event) {
    }

    @FXML
    private void onMenuGameOptionRow14(ActionEvent event) {
    }

    @FXML
    private void onMenuGameOptionRow13(ActionEvent event) {
    }

    @FXML
    private void onMenuGameOptionRow12(ActionEvent event) {
    }

    @FXML
    private void onMenuGameOptionRow11(ActionEvent event) {
    }

    @FXML
    private void onMenuGameOptionRow10(ActionEvent event) {
    }

    @FXML
    private void onMenuGameOptionRow9(ActionEvent event) {
    }

    @FXML
    private void onMenuGameOptionRow8(ActionEvent event) {
    }

    @FXML
    private void onMenuGameOptionRow7(ActionEvent event) {
    }

    @FXML
    private void onMenuGameOptionRow6(ActionEvent event) {
    }
}
