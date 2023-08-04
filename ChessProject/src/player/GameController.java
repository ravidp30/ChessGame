package player;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import ClientAndServerLogin.SceneManagment;
import config.Board;
import config.Piece;
import config.Player;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;



public class GameController implements Initializable {
    
    private static Player player;
    private static Player opponent;
    private Board board;
    private ImageView[][] imageViews = new ImageView[8][8];
    private int squareSize = 54;
    private Piece firstPieceSelected;
    private Piece piece;
    private ArrayList<Piece> pieces = new ArrayList<>();
    private LinkedList<Piece> pieceL = new LinkedList<>();
  
    @FXML
    private Label ChessHeadLineLbl;

    @FXML
    private Pane chessboardPane;

    public static void start(Player player_temp, Player opponent_temp) throws IOException {
        player = player_temp;
        opponent = opponent_temp;
        SceneManagment.createNewStage("/player/GameGUI.fxml", null, "Game").show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChessHeadLineLbl.setText("Chess Game:\nYou (id: " + player.getPlayerId() + ") VS opponent (id: " + opponent.getPlayerId() + ")");
        
        try {
			drawChessboard();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
    
    private void drawChessboard() throws IOException {
        
        
        
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Rectangle square = new Rectangle(x * squareSize, y * squareSize, squareSize, squareSize);
                
                Color color;
                if ((x + y) % 2 == 0) {//set the board
                    color = Color.WHITE;
                } else {
                    color = Color.GREEN;
                }
                
               
                square.setFill(color);
                
             final int finalX = x; // Create a final variable for x
               final int finalY = y; // Create a final variable for y
                
             // Attach a click event handler to each square
                square.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        handleClickOnMoveTo(square);
                    }
                });  
               

               
                chessboardPane.getChildren().add(square);
                
                //setup pieces
                if (y == 6 && (x >= 0 && x < 8)) {//soldier White
                	setUpPiece(x, y, "soldierW", true);
                	System.out.println("soldierW position: x:" + x +" ||  y:" + y );
                }
                
                if (y == 7 && (x ==0  || x == 7)) {//Rook White
                	setUpPiece(x, y, "RookW", true);
                	System.out.println("RookW position: x" + x +" ||  y:" + y );
                }
                
                if (y == 7 && (x ==1  || x == 6)) {//Knight White
                  	setUpPiece(x, y, "KnightW", true);
                	System.out.println("KnightW position: x" + x +" ||  y:" + y );
                	
                }
                
                if (y == 7 && (x ==2  || x == 5)) {//Bishop White
                	setUpPiece(x, y, "BishopW", true);
                	System.out.println("BishopW position: x" + x +" ||  y:" + y );
                }
                
                if (y == 7 && x ==3) {//King White
                	setUpPiece(x, y, "KingW", true);
                	System.out.println("KingW position: x" + x +" ||  y:" + y );
                }
                
                if (y == 7 && x ==4) {//Queen White
                	setUpPiece(x, y, "QueenW", true);
                	System.out.println("QueenW position: x" + x +" ||  y:" + y );
                }
                
               
                
            }
        }
        
        chessboardPane.setPrefWidth(8 * squareSize);
        chessboardPane.setPrefHeight(8 * squareSize);
        board = new Board(8 * squareSize, 8 * squareSize, pieces);
    }
    
    public void setUpPiece(int x, int y, String name, boolean isWhite) {
    	
    	
        piece = new Piece(x, y, name, true);
        
        imageViews[x][y] = new ImageView();
        imageViews[x][y].setFitWidth(squareSize);
        imageViews[x][y].setFitHeight(squareSize);
        
        Image image = new Image(getClass().getResourceAsStream("/player/" + name + ".png"));
        imageViews[x][y].setImage(image);
        System.out.println("123");
        imageViews[x][y].setLayoutX(x * squareSize);
        imageViews[x][y].setLayoutY(y * squareSize);
        
     // Attach a click event handler to each Image
       /* imageViews[x][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleClickOnPiece(imageViews[x][y]);
                System.out.println("ASas: " + piece.getX() + " " + piece.getY());
            }
        });*/
        
        
        chessboardPane.getChildren().add(imageViews[x][y]);
        
        pieces.add(piece);
        piece.setX(x);
    	piece.setY(y);

    }
    
   /* private void handleClickOnPiece(ImageView imageViews) {
    	System.out.println("from clicked: x = " + imageViews.getX() + ", y = " + imageViews.getY());
    	currPiece=board.getPiece((int)imageViews.getX(), (int)imageViews.getY());
    	
    	System.out.println("test: " + currPiece.getX() + " " + currPiece.getY());
       
    }*/
    
   /* private void handleClickOnMoveTo(int x, int y) {
    	try {
        System.out.println("to clicked: x = " + x + ", y = " + y);
        //check if EMPTY \ KILL
        //.....
        movePiece(currPiece.getX(), currPiece.getY(), x, y);
        piece.setX(x);
        piece.setY(y);
    	}
    	catch(NullPointerException e) {
    		System.out.println("error");
    	}
    }*/
    
    private void handleClickOnMoveTo(Rectangle cell) {
    	
    	
    	
        int x = (int)cell.getX() / squareSize;
        int y =	(int)cell.getY() / squareSize;
        
        piece = board.getPiece(x,y);
        
        if(piece != null && piece.isWhite()) { // our piece
        	firstPieceSelected = piece;
        	System.out.println("old piece: "+ piece.getX()+","+ piece.getY());
        	
        }
        
        else if(piece == null && firstPieceSelected != null) {//move our piece to empty place
        	System.out.println("check available- from:" + firstPieceSelected.getX()+", " +  firstPieceSelected.getY()+"  |  to:" +   (int)x+", " +   (int)y);
        	movePiece(firstPieceSelected.getX(), firstPieceSelected.getY(), x,y);
        	
        	//firstPieceSelected = null;
        }
        else {
        	System.out.println("not our");
        }
        
        System.out.println("Clicked on square at coordinates: (" + x + ", " + y + ")");
    }


    
    public void movePiece(int oldX, int oldY, int newX, int newY) {
    	System.out.println("old piece: "+ oldX+","+ oldY);
    	System.out.println("new piece: "+ newX+ ","+ newY);
    	board.Move(oldX, oldX, newX, newY );
    	imageViews[oldX][oldY].setLayoutX(newX * squareSize);
    	imageViews[oldX][oldY].setLayoutY(newY * squareSize);
       


    }
   }
