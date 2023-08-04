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
import config.Player;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
    private int currX;
    private int currY;    
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
    public void initialize(URL location, ResourceBundle resources) {//////hey
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
                
                int finalX = x; // Create a final variable for x
                int finalY = y; // Create a final�variable�for�y
             // Attach a click event handler to each square
                square.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        handleClickOnMoveTo(finalX, finalY);
                    }
                });                
                chessboardPane.getChildren().add(square);
                
                if (y == 6 && (x >= 0 && x < 8)) {//soldier White
                	
                	
                	placePiece(x, y, "soldierW", true);
                	
                }
                
                if (y == 7 && (x ==0  || x == 7)) {//Rook White
                	
                	placePiece(x, y, "RookW", true);

                }
                if (y == 7 && (x ==1  || x == 6)) {//Knight White
                	
                	placePiece(x, y, "KnightW", true);
                	
                }
                
                if (y == 7 && (x ==2  || x == 5)) {//Bishop White
                	
                	placePiece(x, y, "BishopW", true);

                }
                if (y == 7 && x ==3) {//King White
                	
                	placePiece(x, y, "KingW", true);
                	
                }
                if (y == 7 && x ==4) {//Queen White
                	
                	placePiece(x, y, "QueenW", true);
                	
                }
                
               
                
            }
        }
        
        chessboardPane.setPrefWidth(8 * squareSize);
        chessboardPane.setPrefHeight(8 * squareSize);
        board = new Board(8 * squareSize, 8 * squareSize, pieces);
    }
    
    public void placePiece(int x, int y, String name, boolean isWhite) {
    	
    	
        piece = new Piece(x, y, name, true);
        
        imageViews[x][y] = new ImageView();
        imageViews[x][y].setFitWidth(squareSize);
        imageViews[x][y].setFitHeight(squareSize);
        
        Image image = new Image(getClass().getResourceAsStream("/player/" + name + ".png"));
        imageViews[x][y].setImage(image);
        
        imageViews[x][y].setLayoutX(x * squareSize);
        imageViews[x][y].setLayoutY(y * squareSize);
        
     // Attach a click event handler to each square
        imageViews[x][y].setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleClickOnPiece(x, y);
            }
        });
        
        
        chessboardPane.getChildren().add(imageViews[x][y]);
        
        pieces.add(piece);
    	
    }
    
    private void handleClickOnPiece(int x, int y) {
    	System.out.println("from clicked: x = " + currX + ", y = " + currY);
        currX = x;
        currY = y;
    }
    
    private void handleClickOnMoveTo(int x, int y) {
        System.out.println("to clicked: x = " + x + ", y = " + y);
        movePiece(currX, currY,x,y);
    }
    
    public void movePiece(int oldX, int oldY, int newX, int newY) {
    	board.Move(oldX, oldX, newX, newY);
    	imageViews[oldX][oldY].setLayoutX(newX * squareSize);
    	imageViews[oldX][oldY].setLayoutY(newY * squareSize);
    	currX = newX;
    	currY = newY;
    }
   }
