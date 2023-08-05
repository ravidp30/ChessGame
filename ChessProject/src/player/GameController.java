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
import client.ClientUI;
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
    private int squareSize = 64;
    private Piece firstPieceSelected;
    private Piece piece;
    private Piece tempPiece;
    private static GameController instance;

    private ArrayList<Piece> pieces = new ArrayList<>();
    private LinkedList<Piece> pieceL = new LinkedList<>();
  
    @FXML
    private Label ChessHeadLineLbl;

    @FXML
    private Pane chessboardPane;
    
    public GameController() {
    	instance = this;
    }
    
    public static GameController getInstance() {
    	return instance;
    }
    

    public static void start(Player player_temp, Player opponent_temp) throws IOException {
        player = player_temp;
        System.out.println(player.getStatus() + "asdasdsadsadsa");
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
    	
    	synchronized (board) {
    	
        int x = (int)cell.getX() / squareSize;
        int y =	(int)cell.getY() / squareSize;
        
        piece = board.getPiece(x,y);
        //first click + our piece
        if(piece != null && piece.isWhite()) { // our piece
        	firstPieceSelected = piece;//save the first Click on the piece
        	return;
        }
        //second click
        else if(piece == null && firstPieceSelected != null) {//move our piece to empty place
        	movePiece(firstPieceSelected,piece,x,y);
            firstPieceSelected=null;

        	//	System.out.println("piece before change: " + piece.getname() + " | " + piece.getX() + ","+ piece.getY());
        //	System.out.println("firstPieceSelected before change: " + firstPieceSelected.getname() + " | " + firstPieceSelected.getX() + ","+ firstPieceSelected.getY());

        	
        	//System.out.println("/n piece after change: " + piece.getname() + " | " + piece.getX() + ","+ piece.getY());
        	//System.out.println("firstPieceSelected after change: " + firstPieceSelected.getname() + " | " + firstPieceSelected.getX() + ","+ firstPieceSelected.getY());          	//firstPieceSelected = null;
        }
        else {
        	System.out.println("not our");
        }
    	}
        
    }


    //function that move the specific piece 
    public void movePiece(Piece firstPieceSelected ,Piece piece , int newX, int newY) {
    	
    	int check=0;
    	int oldX, oldY;
    	Piece tempPiece=null;
    	oldX=firstPieceSelected.getX();
    	oldY=firstPieceSelected.getY();
    	tempPiece=new Piece(newX,newY,firstPieceSelected.getname(),firstPieceSelected.isWhite());
        check=board.MoveCheck(firstPieceSelected,tempPiece);//check if available to move
        
    	if(check==1) {//available to move the image (KILL OR EMPTY SPACE)
    		ChangePiqtureLocation(oldX,oldY,tempPiece);
    	}
    	
    	// send to the server the piece was changed (old piece and new piece)
    	ArrayList<Piece> updatePieceMoce_arr= new ArrayList<>();
    	updatePieceMoce_arr.add(new Piece(0, 0, "PieceWasMoved", true));
    	updatePieceMoce_arr.add(new Piece(oldX, oldY, firstPieceSelected.getname(),firstPieceSelected.isWhite())); // old piece
    	updatePieceMoce_arr.add(tempPiece); // new piece
    	updatePieceMoce_arr.add(new Piece(0, 0, player.getPlayerId(), true)); // player (playerId in piece's name)
    	ClientUI.chat.accept(updatePieceMoce_arr);
    	
    	return;
    }
    
    //function that change the piece picture to new location
    public void ChangePiqtureLocation(int oldX, int oldY, Piece piece) {
    	
        imageViews[oldX][oldY].setLayoutX((double)piece.getX() * squareSize);
        imageViews[oldX][oldY].setLayoutY((double)piece.getY() * squareSize);
        imageViews[piece.getX()][piece.getY()] = imageViews[oldX][oldY];
        imageViews[piece.getX()][piece.getY()].toFront();
        imageViews[oldX][oldY]=null;
        
     /*   System.out.println("--------------------\n");
        for (int i=0 ; i<8; i++)
        	for (int j=0 ; j<8; j++) {
        		if(imageViews[i][j]!=null) 
        			System.out.println(" image in:" + (double)imageViews[i][j].getLayoutX() + "," + (double)imageViews[i][j].getLayoutY());
        		if(board.getPiece(i, j)!=null) {
        			System.out.println("piece in i,j: "+ board.getPiece(i,j).getname() + " in location: "+ i+"," + j);
        		}
        	}*/
   }
    
    
    public void ChangePieceLocationForOponent(Piece oldPiece, Piece newPiece) {
    	
    	synchronized (board) {
    	
    	board.getPiece(oldPiece.getX(), oldPiece.getY()).setX(newPiece.getX());
    	board.getPiece(oldPiece.getX(), oldPiece.getY()).setY(newPiece.getY());
    	
    	
    	
        imageViews[oldPiece.getX()][oldPiece.getY()].setLayoutX((double)newPiece.getX() * squareSize);
        imageViews[oldPiece.getX()][oldPiece.getY()].setLayoutY((double)newPiece.getY() * squareSize);
        imageViews[newPiece.getX()][newPiece.getY()] = imageViews[oldPiece.getX()][oldPiece.getY()];
        imageViews[newPiece.getX()][newPiece.getY()].toFront();
        imageViews[oldPiece.getX()][oldPiece.getY()]=null;
        
    	}
        

   }
    
    
    
}
