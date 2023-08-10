package player;

import java.awt.Button;
import java.awt.image.BufferedImage;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.scene.effect.DropShadow;

import javax.imageio.ImageIO;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ClientAndServerLogin.SceneManagment;
import client.ClientUI;
import config.Bishop;
import config.Board;
import config.King;
import config.Knight;
import config.Piece;
import config.Player;
import config.Queen;
import config.Rook;
import config.Soldier;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;



public class GameController implements Initializable {
    private boolean opponentFound =false;
    private static Player player;
    private static Player opponent;
    private static Player playerTurn;
    private Board board;
    private ImageView[][] imageViews = new ImageView[8][8];
    private int squareSize = 64;
    private Piece firstPieceSelected;
    private Piece piece;
    private static GameController instance;
    private King king;
    private Soldier soldier;
    private Queen queen;
    private Knight knight;
    private Bishop bishop;
    private Rook rook;
    private Piece lastOpponentPiece;
    private int oldX;
    private int oldY;
    private String EatOrNot;
    private Piece tempPieceToMove = null;
    
    

    private ArrayList<Piece> pieces = new ArrayList<>();
//    private LinkedList<Piece> pieceL = new LinkedList<>();
    private ArrayList<Piece> Kpieces = new ArrayList<>();
    private ArrayList<Piece> Spieces = new ArrayList<>();
    private ArrayList<Piece> Qpieces = new ArrayList<>();
    private ArrayList<Piece> Rpieces = new ArrayList<>();
    private ArrayList<Piece> Bpieces = new ArrayList<>();
    private ArrayList<Piece> KNpieces = new ArrayList<>();

    
    
    private ListView<Rectangle> rectangleListOptions;
    @FXML
    private Pane backGroundPane;
    @FXML
    private TextField txtChat;
    @FXML
    private Label labelChatArea;
    @FXML
    private Label ChatLabel;
    @FXML
    private Button goodLuck;

    @FXML
    private Label ChessHeadLineLbl;
    @FXML
    private Label lblTurnStatus;

    @FXML
    private Pane chessboardPane;
    @FXML
	private Button exitBtn;
    
    
    public GameController() {
    	instance = this;
    }
    
    public static GameController getInstance() {
    	return instance;
    }
    public void onClickExit(ActionEvent event) throws Exception {
	////quit function @@@@@@@@@@@@@@@@@@@@@@@@@
    }
    public void onPlayerClickSend(ActionEvent event) throws Exception {
    	String myText = txtChat.getText();
    	txtChat.setText("");
    	sendMessage(myText);
    }
    
    public void sendMessage(String msg) {
    	
    	String textInside = labelChatArea.getText();
    	labelChatArea.setText(textInside + "\n(You): " + msg);
    	
    	// send the message to the server (to the opponent)
    	ArrayList<String> messageText_arr = new ArrayList<>();
    	messageText_arr.add("OponentSentMessage");
    	messageText_arr.add(msg);
    	messageText_arr.add(player.getPlayerId());
    	ClientUI.chat.accept(messageText_arr);
    }
    
    public void sendGoodLuck(ActionEvent event) throws Exception {
    	sendMessage("Good luck  :-)");
    }

    public static void start(Player player_temp, Player opponent_temp, Player playerStarting) throws IOException {
        player = player_temp;
      //  System.out.println(player.getStatus() + "asdasdsadsadsa");
        opponent = opponent_temp;
        playerTurn = playerStarting;
        //changePlayerTurn(playerTurn);
        SceneManagment.createNewStage("/player/GameGUI.fxml", null, "Game").show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChessHeadLineLbl.setText("Chess Game:\nYou (id: " + player.getPlayerId() + ") VS opponent (id: " + opponent.getPlayerId() + ")");
        ChessHeadLineLbl.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        backGroundPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.5); -fx-border-color: black; -fx-border-width: 1px;");
        labelChatArea.setStyle("-fx-text-fill: white; -fx-font-size: 15px;-fx-border-color: blue; -fx-border-width: 2px; -fx-font-weight: bold;");
        ChatLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold;");
        lblTurnStatus.setStyle("-fx-text-fill: #FF8080; -fx-font-weight: bold; -fx-font-size: 25px;");
        // Create a Timeline animation for flickering
        KeyFrame hideKeyFrame = new KeyFrame(Duration.seconds(0.5), event -> lblTurnStatus.setVisible(false));
        KeyFrame showKeyFrame = new KeyFrame(Duration.seconds(1), event -> lblTurnStatus.setVisible(true));
        Timeline flickerTimeline = new Timeline(hideKeyFrame, showKeyFrame);
        flickerTimeline.setCycleCount(Animation.INDEFINITE);
        flickerTimeline.play();
    
        
  
        changePlayerTurn(playerTurn, new Player("NotInCheck"));
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
                	 color = Color.rgb(173, 216, 230); // Light blue color 
                } else {
                	color = Color.rgb(0, 102, 204);   // Darker blue color
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
                }
                
                if (y == 7 && (x ==0  || x == 7)) {//Rook White
                	setUpPiece(x, y, "RookW", true);
                }
                
                if (y == 7 && (x ==1  || x == 6)) {//Knight White
                  	setUpPiece(x, y, "KnightW", true);
                	
                }
                
                if (y == 7 && (x ==2  || x == 5)) {//Bishop White
                	setUpPiece(x, y, "BishopW", true);
                }
                
                if (y == 7 && x ==3) {				//King White
                	setUpPiece(x, y, "KingW", true);
                }
                
                if (y == 7 && x ==4) {				//Queen White
                	setUpPiece(x, y, "QueenW", true);
                }
                
                //-------------Set mirror Board --------------
                
                if (y == 1) {//soldier Black
                	setUpPiece(x, y, "soldierB", false);
                }
                if (y == 0 && x ==4) {//King Black
                	setUpPiece(x, y, "KingB", false);
                }
                if (y == 0 && (x ==7  || x == 0)) {//Rook Black
                	setUpPiece(x, y, "RookB", false);
                }
                if (y == 0 && (x ==6  || x == 1)) {//Knight Black
                	setUpPiece(x, y, "KnightB", false);
                }
                if (y == 0 && (x ==2  || x == 5)) {//Bishop Black
                	setUpPiece(x, y, "BishopB", false);
                }
                if (y == 0 && x ==3) {//Queen Black
                	setUpPiece(x, y, "QueenB", false);
                }
               
            }
        }
        
        chessboardPane.setPrefWidth(8 * squareSize);
        chessboardPane.setPrefHeight(8 * squareSize);
        board = new Board(8 * squareSize, 8 * squareSize, pieces);
    }
    
    public void setUpPiece(int x, int y, String name, boolean isWhite) {
    	switch (name) {
    				//-------WHITE------
    	
        case "KingW":
        	piece = new King(x, y, name, true);
            break;
        case "QueenW":
        	piece = new Queen(x, y, name, true);
        	break;
        case "RookW":
        	piece = new Rook(x, y, name, true);
        	break; 
        case "BishopW":
        	piece = new Bishop(x, y, name, true);
        	break; 
        case "KnightW":
        	piece = new Knight(x, y, name, true);
        	break;
        case "soldierW":
        	piece = new Soldier(x, y, name, true);
        	break; 
        	
        				//-------BLACK----------
        	
        case "soldierB":
        	piece = new Soldier(x, y, name, false);
        	break; 
        case "KingB":
        	piece = new King(x, y, name, false);
        	break;
        case "RookB":
        	piece = new Rook(x, y, name, false);
        	break;
        case "KnightB":
        	piece = new Knight(x, y, name, false);
        	break;
        case "BishopB":
        	piece = new Bishop(x, y, name, false);
        	break;
        case "QueenB":
        	piece = new Queen(x, y, name, false);
        	break;
        default:
            System.out.println("Invalid choice");
    }
        
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
    	
    	if(playerTurn.getPlayerId().equals(player.getPlayerId())) {
    	
	    	synchronized (board) {
	    		
	    		// clear the prev choices
	    		try {
	            for(int j = 0; j < rectangleListOptions.getItems().size(); j++) {
	            	rectangleListOptions.getItems().get(j).setFill(null);
	            	rectangleListOptions.getItems().get(j).setStroke(null);
	            }
	            rectangleListOptions.getItems().clear();
	    		}catch(NullPointerException e) {}
	    	
		        int x = (int)cell.getX() / squareSize;
		        int y =	(int)cell.getY() / squareSize;
		        
		        piece = board.getPiece(x,y);
		        //first click + our piece
		        if(piece != null && piece.isWhite()) { // our piece
		        	firstPieceSelected = piece;//save the first Click on the piece
		        	switch (firstPieceSelected.getname()) {
		        	
				        case "KingW":
				        	king=(King) firstPieceSelected;
				        	Kpieces=king.Move();
				        	MoveOptions(Kpieces,king);
				            break;
				            
				        case "QueenW":
				        	queen=(Queen) firstPieceSelected;
				        	Qpieces=queen.Move(board);
				        	MoveOptions(Qpieces,queen);
				        	break;
				        	
				        case "RookW":
				        	rook=(Rook) firstPieceSelected;
				        	Rpieces=rook.Move(board);
				        	MoveOptions(Rpieces,rook);
				           	break; 
				           	
				        case "BishopW":
				        	bishop=(Bishop) firstPieceSelected;
				        	Bpieces=bishop.Move(board);
				        	MoveOptions(Bpieces,bishop);
				        	break; 
				        	
				        case "KnightW":
				        	knight=(Knight) firstPieceSelected;
				        	KNpieces=knight.Move(board);
				        	MoveOptions(KNpieces,knight);
				        	
				        	break;
				        	
				        case "soldierW":
				        	soldier=(Soldier) firstPieceSelected;
				        	Spieces=soldier.Move(board);
				        	MoveOptions(Spieces,soldier);
				        	break; 
				        default:
				            System.out.println("Invalid choice");
		        	}
		        	return;
		        }
		        
		        //---------------- second click ---------------------
		        
		       
		        else {
		        	System.out.println("not our");
		        }
	    	}
    	}
    	else {
    		System.out.println("not your turn");
    	}
        
    }


    //function that move the specific piece 
    public Piece movePiece(Piece firstPieceSelected ,Piece piece , int newX, int newY) {
    	
    	lastOpponentPiece = board.getPiece(newX, newY);
    	
    	int check=0;
    	//int oldX, oldY;
    	oldX=firstPieceSelected.getX();
    	oldY=firstPieceSelected.getY();
    	//tempPiece=new Piece(newX,newY,firstPieceSelected.getname(),firstPieceSelected.isWhite());
    	if(piece == null) { // move to empty place
    		tempPieceToMove = new Piece(newX,newY, "empty place", true);
    	}
    	else { // move to not empty place (move to white or black piece)
    		tempPieceToMove = new Piece(newX,newY,firstPieceSelected.getname(),firstPieceSelected.isWhite());
    	}
    	
        check=board.MoveCheck(firstPieceSelected,tempPieceToMove);//check if available to move
        
        //isCheckOnMe(); // send to the opponent message to check if there is check from his side
        
        
        //System.out.println(isChess());
        
    	if(check == 1) {//available to move the image (EMPTY SPACE)
    		ChangePiqtureLocation(oldX,oldY,tempPieceToMove);
    		EatOrNot = "NotEating";
    	}
    	else if(check == 2) { // move to black piece (eating)
    		deleteOpponentPicture(tempPieceToMove);
    		ChangePiqtureLocation(oldX,oldY,tempPieceToMove);
    		board.setPieceXY(firstPieceSelected, tempPieceToMove.getX(), tempPieceToMove.getY()); // change the x and y of the piece for the new x y
    		EatOrNot = "Eating";
    		
    	}
    	

         for(int j = 0; j < rectangleListOptions.getItems().size(); j++) {
         	rectangleListOptions.getItems().get(j).setFill(null);
         	rectangleListOptions.getItems().get(j).setStroke(null);
         }
         rectangleListOptions.getItems().clear();
    	
    	
    	return board.getPiece(newX,newY);
    }
    
    public void deleteOpponentPicture(Piece pieceToRemove) {
    	chessboardPane.getChildren().remove(imageViews[pieceToRemove.getX()][pieceToRemove.getY()]);
    	imageViews[pieceToRemove.getX()][pieceToRemove.getY()] = null;
    	System.out.println("image was deleted");
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
    
    // check after new move of me, if there is chess on me
    // pieceNewMovement is the current piece movement that not done yet
    // pieceOldMovement is the old piece place
    public boolean isChessOnMeAfterNewMove(Piece pieceOldMovement, Piece pieceNewMovement) {
    	
    	return false;
    }
    
    public void ChangePieceLocationForOponent(Piece oldPiece, Piece newPiece, Piece eatingOrNot) {
    	
    	synchronized (board) {
    		Platform.runLater(() -> {
    		try {		
    			
    			oldPiece.setX(7-oldPiece.getX());
    			oldPiece.setY(7-oldPiece.getY());
    			newPiece.setX(7-newPiece.getX());
    			newPiece.setY(7-newPiece.getY());
    			
    		//System.out.println("check: " + isChess(board));
    			
    			
    			if(eatingOrNot.getname().equals("Eating")) {
    				deleteOpponentPicture(newPiece);
    			}	
    			board.setPieceXY(oldPiece, newPiece.getX(), newPiece.getY());
    	        imageViews[oldPiece.getX()][oldPiece.getY()].setLayoutX((double)newPiece.getX() * squareSize);
    	        imageViews[oldPiece.getX()][oldPiece.getY()].setLayoutY((double)newPiece.getY() * squareSize);
    	        imageViews[newPiece.getX()][newPiece.getY()] = imageViews[oldPiece.getX()][oldPiece.getY()];
    	        imageViews[newPiece.getX()][newPiece.getY()].toFront();
    	        imageViews[oldPiece.getX()][oldPiece.getY()]=null;
    	        
    	        
    	        
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		});
       }
  }


    //function that showing all possibles moves 
    public void MoveOptions(ArrayList<Piece> options , Piece piece) {	
    	rectangleListOptions = new ListView<>();
    	Piece tempPiece;
    	 for( Piece p: options) {
    		 
    		 tempPiece = board.getPiece(p.getX(), p.getY());
//    		 int currXPosition=tempPiece.getX();//save the current X position 
//             int currYPosition=tempPiece.getY();//save the current Y position 
    		 // mark a square only if there are no pieces in the square to move or black piece
    		 if(tempPiece == null || !tempPiece.isWhite()) {		 
    			 Rectangle squareOption = new Rectangle( p.getX() * squareSize,p.getY() * squareSize, squareSize, squareSize);
    			 rectangleListOptions.getItems().add(squareOption);
                 squareOption.setFill(Color.TRANSPARENT);
                 squareOption.setStrokeWidth(4.0);
                 SetHighLight(squareOption);
                 
                 // Attach a click event handler to each square
                 squareOption.setOnMouseClicked(new EventHandler<MouseEvent>() {
                     @Override
                     public void handle(MouseEvent event) {
                         handleClickOnMoveToSecond(squareOption);
                     }
                 });  
                 
                 chessboardPane.getChildren().add(squareOption);
                 //System.out.println("clear options to move: " + p.getX() + ","+p.getY());
                 
        		 if(tempPiece == null){	// empty place 
        			 squareOption.setStroke(Color.BLACK);
 
        		 }
                 
        		 else if(!tempPiece.isWhite() ){	// Opponent piece 
        			 squareOption.setStroke(Color.RED);
        			 squareOption.toFront(); //
        		 
        		 }
    		 }	
    	 }
    }
   /* public List<Rectangle> MoveOptions(ArrayList<Piece> options , Piece piece) {
		List<Rectangle> square =  (List<Rectangle>) new Rectangle();
    	 int i=0;
    	 for( Piece p: options) {    
    		 Rectangle newSquare = new Rectangle( p.getX() * squareSize,p.getY()*  squareSize, squareSize, squareSize);
    		  square.add(newSquare);
                 Color color;
                 color = Color.LIGHTBLUE;
                 square.get(i).setFill(color);
                 chessboardPane.getChildren().add(square.get(i));
                 System.out.println(p.getname() + "move option: " + p.getX() + ","+p.getY());
                 }
    	 return square;
             }
    */
    
    public boolean isChess(Board board1) {

    	ArrayList<Piece> moveOptions = new ArrayList<>();

    	Piece currPiece;

    	int cnt = 0;
    	
    	for(int x = 0; x < 8; x++) {
    		for(int y = 0; y < 8; y++) {
    			try {
    				cnt ++;
	    			currPiece = board1.getPiece(x, y);
	    			if(currPiece.isWhite()){
	    				
    					switch (currPiece.getname()) {

	    					case "KingW":
	    						moveOptions = ((King) currPiece).Move();
	    						break;
	
	    					case "QueenW":
	    						moveOptions = ((Queen) currPiece).Move(board1);
	    						break;
	
	    					case "RookW":
	    						moveOptions = ((Rook) currPiece).Move(board1);
	    						break; 
	
	    					case "BishopW":
	    						moveOptions = ((Bishop) currPiece).Move(board1);
	    						break; 
	
	    					case "KnightW":
	    						moveOptions = ((Knight) currPiece).Move(board1);
	    						break;
	
	    					case "soldierW":
	    						moveOptions = ((Soldier) currPiece).Move(board1);
	    						break; 
	    					default:
	    						System.out.println("no way");
	    				}
    					//System.out.println("\n\ncurr piece: " + currPiece.getname());
    					for(Piece optionsToMove : moveOptions) {
    						//System.out.println(optionsToMove.getname());
    						if(optionsToMove.getname().equals("KingB")) {
    							return true;
    						}
    					}
	    			}
    			}catch (NullPointerException e) {
    			}
    		}
    	}
    	return false;
    }

	private void handleClickOnMoveToSecond(Rectangle squareOption) {
        int x = (int)squareOption.getX() / squareSize;
        int y =	(int)squareOption.getY() / squareSize;
        piece = board.getPiece(x,y); // the place to move to
        
        piece = movePiece(firstPieceSelected,piece,x,y);
        
        System.out.println("opponent checked on me : " + isChessOnMe());
        if(isChessOnMe()) {
        	moveBack();
        }
        
        else {   
        	
        	
	        System.out.println("i did check on opponent: " + isChess(board));
	        
	        boolean inCheck = isChess(board);
	        
	        if(inCheck) {
	        	popUpCheck("chess");
	        }
	        
	        
			// send to the server the piece was changed (old piece and new piece) and if eaten
			ArrayList<Piece> updatePieceMoce_arr= new ArrayList<>();
			updatePieceMoce_arr.add(new Piece(0, 0, "PieceWasMoved", true));
			updatePieceMoce_arr.add(new Piece(0, 0, EatOrNot, true));
			updatePieceMoce_arr.add(new Piece(oldX, oldY, firstPieceSelected.getname(),firstPieceSelected.isWhite())); // old piece
			updatePieceMoce_arr.add(tempPieceToMove); // new piece
			updatePieceMoce_arr.add(new Piece(0, 0, player.getPlayerId(), true)); // player (playerId in piece's name)
			ClientUI.chat.accept(updatePieceMoce_arr);
	    	
	    	
	    	 firstPieceSelected=null;
	        
	        
        	
        	SendToServerChangePlayerTurn(inCheck);  // send to the opponent also if there is check on him
        	//System.out.println("new move: " + piece.getX() + ", " + piece.getY());

        }

	}
	
	private void moveBack() {
		// firstPieceSelected
		// piece

		
		try {
			System.out.println(lastOpponentPiece.getname() + "," + lastOpponentPiece.getX() + "," + lastOpponentPiece.getY());
		}catch (NullPointerException e) {
			System.out.println("empty");
		}
		System.out.println(firstPieceSelected.getname() + "," + oldX + "," + oldY);
		
		if(lastOpponentPiece == null) { // moving to empty place and check
			
			board.setPieceXY(firstPieceSelected, oldX, oldY);
			ChangePiqtureLocation(firstPieceSelected.getX(),firstPieceSelected.getY(),new Piece(oldX, oldY, firstPieceSelected.getname(), true));
			
		}
		else { // after eating and check
			
			ChangePiqtureLocation(firstPieceSelected.getX(),firstPieceSelected.getY(),new Piece(oldX, oldY, firstPieceSelected.getname(), true));
			
			
	        imageViews[lastOpponentPiece.getX()][lastOpponentPiece.getY()] = new ImageView();
	        imageViews[lastOpponentPiece.getX()][lastOpponentPiece.getY()].setFitWidth(squareSize);
	        imageViews[lastOpponentPiece.getX()][lastOpponentPiece.getY()].setFitHeight(squareSize);
	        
	        
	        
	        
	        Image image = new Image(getClass().getResourceAsStream("/player/" + lastOpponentPiece.getname() + ".png"));
	        
	        imageViews[lastOpponentPiece.getX()][lastOpponentPiece.getY()].setImage(image);
	        imageViews[lastOpponentPiece.getX()][lastOpponentPiece.getY()].setLayoutX(lastOpponentPiece.getX() * squareSize);
	        imageViews[lastOpponentPiece.getX()][lastOpponentPiece.getY()].setLayoutY(lastOpponentPiece.getY() * squareSize);
	        
	        
	        chessboardPane.getChildren().add(imageViews[lastOpponentPiece.getX()][lastOpponentPiece.getY()]);
	        
	        
	        
	        pieces.add(lastOpponentPiece);
	        
	        board.setPieceXY(firstPieceSelected, oldX, oldY);
        	popUpCheck("unvailable move");

			
			
		}
		
		
	
		
		
	}

	private boolean isChessOnMe() {
		
		
		ArrayList<Piece> tempPieces = new ArrayList<>();
		Piece tempPiece;
		String modifiedString = null;
		//changing the board 
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				try {			
					tempPiece = board.getPiece(x, y);
	
					if(tempPiece.getname().endsWith("W")) {
						//System.out.println("white: " + tempPiece.getX() + "," + tempPiece.getY());
						modifiedString = (tempPiece.getname()).substring(0, (tempPiece.getname()).length() - 1) + "B";
						//System.out.println("white to black: " + modifiedString);
						//System.out.println(modifiedString + " , " + tempPiece.getX() + " " + tempPiece.getY());
						
						switch (modifiedString) {
					        case "soldierB":
					        	tempPiece = new Soldier(7-x, 7-y, modifiedString, false);
					        	break; 
					        case "KingB":
					        	tempPiece = new King(7-x, 7-y, modifiedString, false);
					        	break;
					        case "RookB":
					        	tempPiece = new Rook(7-x, 7-y, modifiedString, false);
					        	break;
					        case "KnightB":
					        	tempPiece = new Knight(7-x,7-y, modifiedString, false);
					        	break;
					        case "BishopB":
					        	tempPiece = new Bishop(7-x,7-y, modifiedString, false);
					        	break;
					        case "QueenB":
					        	tempPiece = new Queen(7-x, 7-y, modifiedString, false);
					        	break;
					        default:
					            System.out.println("Invalid choice");
					    }
					
					}
					
					else if (tempPiece.getname().endsWith("B")){
						//System.out.println("black: " + tempPiece.getX() + "," + tempPiece.getY());
						modifiedString = (tempPiece.getname()).substring(0, (tempPiece.getname()).length() - 1) + "W";
						//System.out.println("black to white: " + modifiedString);
						
						
						switch (modifiedString) {
		    				//-------WHITE------
		    	
					        case "KingW":
					        	tempPiece = new King(7-x, 7-y, modifiedString, true);
					            break;
					        case "QueenW":
					        	tempPiece = new Queen(7-x, 7-y, modifiedString, true);
					        	break;
					        case "RookW":
					        	tempPiece = new Rook(7-x, 7-y, modifiedString, true);
					        	break; 
					        case "BishopW":
					        	tempPiece = new Bishop(7-x, 7-y, modifiedString, true);
					        	break; 
					        case "KnightW":
					        	tempPiece = new Knight(7-x, 7-y, modifiedString, true);
					        	break;
					        case "soldierW":
					        	tempPiece = new Soldier(7-x, 7-y, modifiedString, true);
					        	break; 
					        default:
					            System.out.println("Invalid choice");
						}
										
						
					}
					tempPieces.add(tempPiece);
				}catch (Exception e) {
					//tempPieces.add(new Piece(x, y, "empty", true));
					//System.out.println("empty: " + x + "," + y);
				}
			}
		}
		/*int cnt = 0;
		for(Piece a : tempPieces) {
			cnt++;
			System.out.println(a.getname() + ", " + a.getX() + ", " + a.getY());
		}
		System.out.println(cnt);*/	
		
		Board tempBoard = new Board(8 * squareSize, 8 * squareSize, tempPieces);
		
		
		/*for(int i = 0; i<8; i++) {
			for(int j = 0; j<8; j++) {
				if(tempBoard.getPiece(i, j) != null)
					System.out.println(tempBoard.getPiece(i, j).getname() + ", " + tempBoard.getPiece(i, j).getX() + ", " + tempBoard.getPiece(i, j).getY());
			}
		}*/
		

		

		return isChess(tempBoard);
		

	}

	// send to the server the current player turn and if the opponent inCheck or not
	public void SendToServerChangePlayerTurn(boolean inCheck) {
		ArrayList<Player> playerTurnChange_arr = new ArrayList<>();
		playerTurnChange_arr.add(new Player("ChangePlayerTurn"));
		playerTurnChange_arr.add(opponent);
		if(inCheck) {
			playerTurnChange_arr.add(new Player("InCheck"));
		}
		else {
			playerTurnChange_arr.add(new Player("NotInCheck"));
		}
		ClientUI.chat.accept(playerTurnChange_arr);
	}
	
	
	// inCheck.getplayerId() = "InCheck" / "NotInCheck"
	public void changePlayerTurn(Player newPlayerTurn, Player inCheck) {
		Platform.runLater(() -> {
			/*if(isChess(board)) {
				System.out.println(123123);
			}*/
			playerTurn = newPlayerTurn;
			if(playerTurn.getPlayerId().equals(player.getPlayerId())) {
				lblTurnStatus.setText("Your Turn");
				if(inCheck.getPlayerId().equals("InCheck")) {
					popUpCheck("check on me");
				}
			}
			else {
				lblTurnStatus.setText("Opponent's Turn");
			}
		});
	}
	

	public void getMessageFromOponent(String message) {
		
		Platform.runLater(() -> {
		
	    	String textInside;
	    	
	    	textInside = labelChatArea.getText();
	    	
	    	labelChatArea.setText(textInside + "\n(Oponent): " + message);
    	
		});
    	
	}
	
	public void SetHighLight (Rectangle squareOption) {
		DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(8.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(0.0);
        dropShadow.setColor(Color.BLUE); // You can adjust the color as needed
        squareOption.setEffect(dropShadow);
	}
	
	//pop-up window - check is on.
	public void popUpCheck(String string){
		
		 String message = "<html><h1 style='font-size: 24px; color: #007bff;'>Check!</h1>"
                 + "<p style='font-size: 18px; color: #333;'>"
                  + player.getPlayerId()+ string + ".</p></html>";

		 JLabel messageLabel = new JLabel(message);
		 JOptionPane.showMessageDialog(
				 null,
				 messageLabel,
				 "Check",
				 JOptionPane.INFORMATION_MESSAGE
);
	}

}
