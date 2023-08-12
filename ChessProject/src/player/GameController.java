package player;

import java.awt.Button;
import java.awt.image.BufferedImage;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Line;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
    private Piece secondPieceSelected;
    //private Piece piece;
    private static GameController instance;
    private King king;
    private Soldier soldier;
    private Queen queen;
    private Knight knight;
    private Bishop bishop;
    private Rook rook;
    private Piece lastChosenPiece;
    private int oldX;
    private int oldY;
    private String EatOrNot;
    private static Stage currStage;
    //private Piece tempPieceToMove = null;
    private int newXLastOpponent;
    private int newYLastOpponent;
    private int CountChess;
    private boolean continueTurn = true;
    private Image Cloud ;
    private ImageView CloudImageView ;
    private HBox hbox;
    private String[] pieceImagePaths  = {
	        "/player/QueenW.png",
	        "/player/KnightW.png",
	        "/player/BishopW.png",
	        "/player/RookW.png"
	    };
    //private ArrayList<Piece> pieces = new ArrayList<>();
//    private LinkedList<Piece> pieceL = new LinkedList<>();
    private ArrayList<Piece> Kpieces = new ArrayList<>();
    private ArrayList<Piece> Spieces = new ArrayList<>();
    private ArrayList<Piece> Qpieces = new ArrayList<>();
    private ArrayList<Piece> Rpieces = new ArrayList<>();
    private ArrayList<Piece> Bpieces = new ArrayList<>();
    private ArrayList<Piece> KNpieces = new ArrayList<>();
    
    private Board opponentBoard;

    
    
    private ListView<Rectangle> rectangleListOptions;
    @FXML
    private Pane backGroundPane;
    @FXML
    private Pane addPiecesBar;
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
        currStage = SceneManagment.createNewStage("/player/GameGUI.fxml", null, "Game");
        currStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	board = new Board(8 * squareSize, 8 * squareSize, null);
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
        //Cloud image
        Cloud = new Image(getClass().getResourceAsStream("/player/Cloud.png"));
        CloudImageView = new ImageView(Cloud);
        CloudImageView.setFitWidth(50);
        CloudImageView.setFitHeight(50);
        CloudImageView.toFront();
        CloudImageView.setVisible(false);
        //Hbox of pieces tool bar 
         hbox = new HBox(10); // Set spacing between slots
     	 hbox.setLayoutX(250);
		 hbox.setLayoutY(100);
		 backGroundPane.getChildren().add(hbox);
         for (int i = 0; i < 4; i++) {
             ImageView HboxPiecesChoosing = new ImageView(new Image(pieceImagePaths[i]));
             HboxPiecesChoosing.setFitWidth(50);
             HboxPiecesChoosing.setFitHeight(50);
             
             // Set an action when the image is clicked
             int slotIndex = i;
             HboxPiecesChoosing.setOnMouseClicked(event -> handlePieceClickOnHBOX(slotIndex));
             hbox.getChildren().add(HboxPiecesChoosing);  
         }
         	hbox.setVisible(false);
         	
        //setting up Bar choosing new player
    /*    addPiecesBar.setStyle("-fx-background-color: black;");
        double spacing = 80; // Adjust as needed
        addImageToAddPiecesBar(addPiecesBar, "/player/QueenW.png", 50);
        addImageToAddPiecesBar(addPiecesBar, "/player/KnightW.png", 50 + spacing);
        addImageToAddPiecesBar(addPiecesBar, "/player/BishopW.png", 50 + 2 * spacing);
        addImageToAddPiecesBar(addPiecesBar, "/player/RookW.png", 50 + 3 * spacing);
        addPiecesBar.setVisible(false); 
        
      */ 
        changePlayerTurn(playerTurn, new Player("NotInCheck"));
        try {
			drawChessboard();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
    
    private void handlePieceClickOnHBOX(int slotIndex) {//Queen knight bishop rook
    	board.removePiece(lastChosenPiece.getX(),lastChosenPiece.getY());
		deleteOpponentPicture(lastChosenPiece);
    	switch(slotIndex) {
    	case 0:
    		setUpPiece(lastChosenPiece.getX(), lastChosenPiece.getY(), "QueenW", true);
    		break;
    	case 1:
    		setUpPiece(lastChosenPiece.getX(), lastChosenPiece.getY(), "KnightW", true);
    		break;
    	case 2:
    		setUpPiece(lastChosenPiece.getX(), lastChosenPiece.getY(), "BishopW", true);
    	case 3:
    		setUpPiece(lastChosenPiece.getX(), lastChosenPiece.getY(), "RookW", true);
    		break;
    	default:
    			System.out.println("Error");
    	}
		hbox.setVisible(false);
		TurnContinueAfterMovement();
		continueTurn = true;

	}
    
    public void onPlayerClickExitGame(ActionEvent event) throws Exception {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit Game");
        alert.setHeaderText("Are you sure you want to exit the game?");
        
        ButtonType exitButton = new ButtonType("Exit Game");
        ButtonType cancelButton = new ButtonType("Stay In The Game");
        alert.getButtonTypes().setAll(exitButton, cancelButton);
        
        alert.showAndWait().ifPresent(response -> {
            if (response == exitButton) {
                // User clicked "Exit Game"
                System.out.println("Exiting the game...");
                
                ArrayList<Player> playerExitedFromGame = new ArrayList<>();
                playerExitedFromGame.add(new Player("PlayerExitedFromActiveGame"));
                playerExitedFromGame.add(player);
                ClientUI.chat.accept(playerExitedFromGame);
                
    			/*((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
    			try {
					MenuController.start(player.getPlayerId());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
                
                exitActiveGame(1);
                
            }
        });

    }
    
    // endStatus: 0 - none , 1 - lost - 2 - won
    public static void exitActiveGame(int endStatus) {
    	
    	Platform.runLater(() -> {

    		currStage.getScene().getWindow().hide(); // hiding primary window
			try {
				MenuController.start(player.getPlayerId(), endStatus);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
    	});
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
    }
    
    public void setUpPiece(int x, int y, String name, boolean isWhite) {
    	Piece piece = null;
    	switch (name) {
    				//-------WHITE------
    	
        case "KingW":
        	piece = new King(x, y, name, isWhite);
            break;
        case "QueenW":
        	piece = new Queen(x, y, name, isWhite);
        	break;
        case "RookW":
        	piece = new Rook(x, y, name, isWhite);
        	break; 
        case "BishopW":
        	piece = new Bishop(x, y, name, isWhite);
        	break; 
        case "KnightW":
        	piece = new Knight(x, y, name, isWhite);
        	break;
        case "soldierW":
        	piece = new Soldier(x, y, name, isWhite);
        	break; 
        	
        				//-------BLACK----------
        	
        case "soldierB":
        	piece = new Soldier(x, y, name, isWhite);
        	break; 
        case "KingB":
        	piece = new King(x, y, name, isWhite);
        	break;
        case "RookB":
        	piece = new Rook(x, y, name, isWhite);
        	break;
        case "KnightB":
        	piece = new Knight(x, y, name, isWhite);
        	break;
        case "BishopB":
        	piece = new Bishop(x, y, name, isWhite);
        	break;
        case "QueenB":
        	piece = new Queen(x, y, name, isWhite);
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
        
        board.addPiece(piece);

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
    	
    	if(playerTurn.getPlayerId().equals(player.getPlayerId()) && continueTurn) {
    	
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
		        
		        firstPieceSelected = board.getPiece(x,y);
		        //first click + our piece
		        if(firstPieceSelected != null && firstPieceSelected.isWhite()) { // our piece
			        oldX = firstPieceSelected.getX();
			        oldY = firstPieceSelected.getY();
		        	switch (firstPieceSelected.getname()) {
		        	
				        case "KingW":
				        	king=(King) firstPieceSelected;
				        	Kpieces=king.Move(board);
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
    public boolean checkForMate(HashMap<Piece, ArrayList<Piece>> piecesInMap) {
    	//Board newBoard;
    	
		System.out.println(1);
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				System.out.println(board.getPiece(x, y));
			}
		}
		System.out.println(1);
    	
    	
    	int newX;
    	int newY;
    	
    	int availableToMove=0;
    	
    	
        for (Piece keyPiece : piecesInMap.keySet()) {
        	if(keyPiece != null && keyPiece.isWhite()) {
        		//System.out.println("123 " + keyPiece);
	            ArrayList<Piece> moveOptions = new ArrayList<>();
	            moveOptions = piecesInMap.get(keyPiece);
	
	            // Now you can work with keyPiece and valueList
	            /*System.out.println("------------------------------------------");
	            System.out.println("Key Piece: " + keyPiece);
	            System.out.println("Value List: " + moveOptions);
	            System.out.println("------------------------------------------");*/
	            
	            for(int i = 0; i < moveOptions.size(); i++) {
	            	//newBoard = opponentBoard;
	            	
	            	ArrayList<Piece> piecesTemp = new ArrayList<>();
	            	piecesTemp = board.getPieces();
	            	//ArrayList<Piece> piecesTemp = board.getPieces();
	            	
	            	Board newBoard = new Board(8 * squareSize, 8 * squareSize, piecesTemp);

		            newX = moveOptions.get(i).getX();
		            newY = moveOptions.get(i).getY();
		            
		            availableToMove = newBoard.MoveCheck(keyPiece.getX(), keyPiece.getY(), newX, newY);//check if available to move
		            

		        	if(availableToMove == 1) {//available to move the image (EMPTY SPACE)
		        		
		        		newBoard.setPieceXY(keyPiece, newX, newY);
  		
		        	}
		        	else if(availableToMove == 2 && !newBoard.getPiece(newX, newY).getname().equals("KingB")) { // move to black piece (eating)
	        		
		        		newBoard.removePiece(newX, newY);
 		
		        		newBoard.setPieceXY(keyPiece, newX, newY);

		        		
		        	}
		        	

		        	if(!isChessOnMe(newBoard)){
		            	System.out.println("no chess on ,e");
		            	return false;
		        	}

		        }
        	}
    	
        }
        
        return true;
    	

    }
    



    //function that move the specific piece 
    public void movePiece(int newX, int newY) {
    	
    	
    	newXLastOpponent = newX;
    	newYLastOpponent = newY;
    	int availableToMove=0;
    	
    	availableToMove=board.MoveCheck(oldX, oldY, newX, newY);//check if available to move
    
        
    	if(availableToMove == 1) {//available to move the image (EMPTY SPACE)
    		
    		ChangePiqtureLocation(oldX,oldY,newX, newY);
    		board.setPieceXY(firstPieceSelected, newX, newY);
    		EatOrNot = "NotEating";
    		
    	}
    	else if(availableToMove == 2) { // move to black piece (eating)
    		
    		board.removePiece(newX, newY);
    		board.setPieceXY(firstPieceSelected, newX, newY);
    		
    		deleteOpponentPicture(secondPieceSelected);
    		ChangePiqtureLocation(oldX,oldY,newX, newY);
    		EatOrNot = "Eating";

    		
    	}
    	

         for(int j = 0; j < rectangleListOptions.getItems().size(); j++) {
         	rectangleListOptions.getItems().get(j).setFill(null);
         	rectangleListOptions.getItems().get(j).setStroke(null);
         }
         rectangleListOptions.getItems().clear();
    	
         lastChosenPiece = board.getPiece(newX, newY);
    }
    
    public void deleteOpponentPicture(Piece pieceToRemove) {
    	chessboardPane.getChildren().remove(imageViews[pieceToRemove.getX()][pieceToRemove.getY()]);
    	imageViews[pieceToRemove.getX()][pieceToRemove.getY()] = null;
    	System.out.println("image was deleted");
	}

	//function that change the piece picture to new location
    public void ChangePiqtureLocation(int oldX, int oldY, int newX, int newY) {
    	
        imageViews[oldX][oldY].setLayoutX((double)newX * squareSize);
        imageViews[oldX][oldY].setLayoutY((double)newY * squareSize);
        imageViews[newX][newY] = imageViews[oldX][oldY];
        imageViews[newX][newY].toFront();
        imageViews[oldX][oldY]=null;
        
   }
    
    
    public void ChangePieceLocationForOponent(Piece oldPiece, Piece newPiece, Piece eatingOrNot) {
    	
    	synchronized (board) {
    		Platform.runLater(() -> {
    		try {		
    			
    			// set the pieces places to the opposite side (for the opponent)
    			oldPiece.setX(7-oldPiece.getX());
    			oldPiece.setY(7-oldPiece.getY());
    			newPiece.setX(7-newPiece.getX());
    			newPiece.setY(7-newPiece.getY());    			
    			
    			if(eatingOrNot.getname().equals("Eating")) {
    				deleteOpponentPicture(newPiece);
    				board.removePiece(newPiece.getX(),newPiece.getY());//
    			}
			
    			board.setPieceXY(oldPiece, newPiece.getX(), newPiece.getY());
    	        imageViews[oldPiece.getX()][oldPiece.getY()].setLayoutX((double)newPiece.getX() * squareSize);
    	        imageViews[oldPiece.getX()][oldPiece.getY()].setLayoutY((double)newPiece.getY() * squareSize);
    	        imageViews[newPiece.getX()][newPiece.getY()] = imageViews[oldPiece.getX()][oldPiece.getY()];
    	        imageViews[newPiece.getX()][newPiece.getY()].toFront();
    	        imageViews[oldPiece.getX()][oldPiece.getY()]=null;
    	        
    	        
    	        
    	        // old piece's and new piece's have differenet names only if the player changed his piece from soldier to differenet piece
    	        if(!oldPiece.getname().equals(newPiece.getname())) { 
    	        	
    	        	
    	        	board.removePiece(newPiece.getX(),newPiece.getY());

    	    		deleteOpponentPicture(newPiece);
    	    		
    	    		// changing the name to black piece
    	    		String newPieceNameChangeToBlack = (newPiece.getname()).substring(0, (newPiece.getname()).length() - 1) + "B";
    	    		

    	    		setUpPiece(newPiece.getX(),newPiece.getY(), newPieceNameChangeToBlack, false);

    	        	
    	        }
    	        
    	        
    	        
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
    
    public HashMap<Piece, ArrayList<Piece>> setUpPiecesHasMap() {
        HashMap<Piece, ArrayList<Piece>> piecesInMap = new HashMap<>();
        ArrayList<Piece> moveOptions;
        
    	for(int x = 0; x < 8; x++) {
        	for(int y = 0; y < 8; y++) {
       		Piece tempPiece = null;
       		moveOptions = new ArrayList<>();
        		try {
            		//Piece tempPiece = new Piece(x, y, board.getPiece(x, y).getname(), board.getPiece(x, y).isWhite());
            		 
	        		switch (board.getPiece(x, y).getname()) {
			
						//-------WHITE------
			
				        case "KingW":
				        	tempPiece = new King(x, y, "KingW", true);
				        	moveOptions = ((King)tempPiece).Move(board);
				            break;
				        case "QueenW":
				        	tempPiece = new Queen(x, y, "QueenW", true);
				        	moveOptions = ((Queen)tempPiece).Move(board);
				        	break;
				        case "RookW":
				        	tempPiece = new Rook(x, y, "RookW", true);
				        	moveOptions = ((Rook)tempPiece).Move(board);
				        	break; 
				        case "BishopW":
				        	tempPiece = new Bishop(x, y, "BishopW", true);
				        	moveOptions = ((Bishop)tempPiece).Move(board);
				        	break; 
				        case "KnightW":
				        	tempPiece = new Knight(x, y, "KnightW", true);
				        	moveOptions = ((Knight)tempPiece).Move(board);
				        	break;
				        case "soldierW":
				        	tempPiece = new Soldier(x, y, "soldierW", true);
				        	moveOptions = ((Soldier)tempPiece).Move(board);
				        	break; 	
				        default:
				        	break;
	        		}
	        		
	        		piecesInMap.put(tempPiece, moveOptions);
        	
        		}catch (NullPointerException e) {}
        		
        	}
    	}
    	
    	return piecesInMap;
    }
    
    public boolean isChess(Board board1) {

    	ArrayList<Piece> moveOptions = new ArrayList<>();

    	Piece currPiece;

    	
    	for(int x = 0; x < 8; x++) {
    		for(int y = 0; y < 8; y++) {
    			try {
	    			currPiece = board1.getPiece(x, y);
	    			if(currPiece.isWhite()){
	    				
    					switch (currPiece.getname()) {

	    					case "KingW":
	    						moveOptions = ((King) currPiece).Move(board1);
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
        secondPieceSelected = board.getPiece(x,y); // the place to move to
        
        movePiece(x,y);

        
        if(isChessOnMe(board)) { //Chess On ME
        	moveBack();
        	cloudImage(true);
        }
        

        
        else {   
        	
        	
	        // if soldier is at the end
	        Piece soldierPiece = board.getPiece(x,y);
	        if(soldierPiece != null && soldierPiece.getname().equals("soldierW") && soldierPiece.getY() == 0) {
	        	/*if (!backGroundPane.getChildren().contains(hbox)) { //if the Hbox is not added to parent yet
	        		backGroundPane.getChildren().add(hbox);
	        	}*/
	        	continueTurn = false;
	        	hbox.setVisible(true);
	            //Line line = new Line(soldierPiece.getX()*squareSize, soldierPiece.getY()*squareSize, 130, 130);

	            //hbox.getChildren().add(line);
	        }
	        else {
	        	TurnContinueAfterMovement();
	        }
        	

        }
   

	}
	
	public void TurnContinueAfterMovement() {
		
		if(secondPieceSelected != null) {
			lastChosenPiece = board.getPiece(secondPieceSelected.getX(), secondPieceSelected.getY());
		}
		
    	cloudImage(false);
        boolean inCheck = isChess(board);
        
        if(inCheck) {
        	popUpCheck("chess");   
        }
        
        /*System.out.println("@@@@@@@@@@");
        System.out.println(firstPieceSelected.getname());
        System.out.println(lastChosenPiece.getname());
        System.out.println("@@@@@@@@@@");*/

   
		// send to the server the piece was changed (old piece and new piece) and if eaten
		ArrayList<Piece> updatePieceMoce_arr= new ArrayList<>();
		updatePieceMoce_arr.add(new Piece(0, 0, "PieceWasMoved", true));
		updatePieceMoce_arr.add(new Piece(0, 0, EatOrNot, true));
		updatePieceMoce_arr.add(new Piece(oldX, oldY, firstPieceSelected.getname(),firstPieceSelected.isWhite())); // old piece
		updatePieceMoce_arr.add(lastChosenPiece); // new piece
		updatePieceMoce_arr.add(new Piece(0, 0, player.getPlayerId(), true)); // player (playerId in piece's name)
		ClientUI.chat.accept(updatePieceMoce_arr);
    	
    	
    	 firstPieceSelected=null;
        
        
    	
    	SendToServerChangePlayerTurn(inCheck);  // send to the opponent also if there is check on him
	}
	
	
	/*public HashMap<Piece, ArrayList<Piece>> buildHashMapForPieces(){
		
		HashMap<Piece, ArrayList<Piece>> piecesInMap = new HashMap<>();
		
		
		for(int x = 0; x < 8; x++) {
			
		}
		
		
		
		return piecesInMap;
	}*/
	
	
	
//	private void showHbox(boolean show) {
//		if (show) {
//			if (!chessboardPane.getChildren().contains(hbox)) { //if the hbox is not added to parent yet
//				chessboardPane.getChildren().add(hbox);
//				hbox.setVisible(true);
//			}
//			
//		}
//		else {
//			try {
//				chessboardPane.getChildren().remove(hbox);		
//				hbox.setVisible(false);
//				System.out.println("UnShow");
//			}catch (NullPointerException e) {
//			}
//			
//		}
//		
//	    		 
//	}

	private void cloudImage(boolean use) {
		if(use) {
		Piece kingFound =  new Piece(0,0,"KingW",true);
		int newPositionX=0;
		int newPositionY=0;
		for (int x=0; x<8; x++)
			for(int y=0; y<8 ; y++)
				if(board.getPiece(x, y)!=null)//search for the KingW
				{
					if(board.getPiece(x, y).getname().equals(kingFound.getname())) {
						 newPositionX=x;
						 newPositionY=y;
					}
					
				}
		
		 CloudImageView.setLayoutX(newPositionX * squareSize -35);
		 CloudImageView.setLayoutY(newPositionY *squareSize -30);  
		 CloudImageView.setVisible(true);
		 if (!chessboardPane.getChildren().contains(CloudImageView)) { //if the image is not added to parent yet
			 chessboardPane.getChildren().add(CloudImageView);
			}
	    		 
		}
	    else {
	    
	    	try {
	    		 chessboardPane.getChildren().remove(CloudImageView);		
	    		 }catch (NullPointerException e) {
	    		 }
	    	CloudImageView.setVisible(false);     
	    	}
	 }
	

	private void moveBack() {
		// firstPieceSelected
		// piece
		
		if(secondPieceSelected == null) { // moving to empty place and check
			
			board.setPieceXY(lastChosenPiece, oldX, oldY);
			ChangePiqtureLocation(newXLastOpponent, newYLastOpponent,oldX, oldY);
			
		}
		else { // after eating and check
			
			ChangePiqtureLocation(lastChosenPiece.getX(),lastChosenPiece.getY(), oldX, oldY);//firstPieceSelected.ggetX..\Y
			
			
	        imageViews[lastChosenPiece.getX()][lastChosenPiece.getY()] = new ImageView();
	        imageViews[lastChosenPiece.getX()][lastChosenPiece.getY()].setFitWidth(squareSize);
	        imageViews[lastChosenPiece.getX()][lastChosenPiece.getY()].setFitHeight(squareSize);//
	             
	        
	        Image image = new Image(getClass().getResourceAsStream("/player/" + secondPieceSelected.getname() + ".png"));
	        
	        imageViews[lastChosenPiece.getX()][lastChosenPiece.getY()].setImage(image);
	        imageViews[lastChosenPiece.getX()][lastChosenPiece.getY()].setLayoutX(lastChosenPiece.getX() * squareSize);
	        imageViews[lastChosenPiece.getX()][lastChosenPiece.getY()].setLayoutY(lastChosenPiece.getY() * squareSize);
	        
	        
	        chessboardPane.getChildren().add(imageViews[lastChosenPiece.getX()][lastChosenPiece.getY()]);
	        
	        
	        board.addPiece(secondPieceSelected);
	        //pieces.add(secondPieceSelected);
	        
	        board.setPieceXY(lastChosenPiece, oldX, oldY);

			
        	lastChosenPiece = null;
		}
		
		popUpCheck("unvailable move");
		
	
		
		
	}

	private boolean isChessOnMe(Board newBoard) {//d
		
		
		ArrayList<Piece> tempPieces = new ArrayList<>();
		Piece tempPiece;
		String modifiedString = null;
		//changing the board 
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				try {			
					tempPiece = newBoard.getPiece(x, y);
	
					if(tempPiece.getname().endsWith("W")) {
						//System.out.println("white: " + tempPiece.getX() + "," + tempPiece.getY());
						modifiedString = (tempPiece.getname()).substring(0, (tempPiece.getname()).length() - 1) + "B";
						//System.out.println("white to black: " + modifiedString);
						//System.out.println(modifiedString + " , " + tempPiece.getX() + " " + tempPiece.getY());
						
						switch (modifiedString) {
					        case "soldierB":
					        	tempPiece = new Soldier(7-x, 7-y, modifiedString, false);
					        	//piecesInMap.put(tempPiece, ((Soldier)tempPiece).Move(newBoard));
					        	break; 
					        case "KingB":
					        	tempPiece = new King(7-x, 7-y, modifiedString, false);
					        	//piecesInMap.put(tempPiece, ((King)tempPiece).Move(newBoard));
					        	break;
					        case "RookB":
					        	tempPiece = new Rook(7-x, 7-y, modifiedString, false);
					        	//piecesInMap.put(tempPiece, ((Rook)tempPiece).Move(newBoard));
					        	break;
					        case "KnightB":
					        	tempPiece = new Knight(7-x,7-y, modifiedString, false);
					        	//piecesInMap.put(tempPiece, ((Knight)tempPiece).Move(newBoard));
					        	break;
					        case "BishopB":
					        	tempPiece = new Bishop(7-x,7-y, modifiedString, false);
					        	//piecesInMap.put(tempPiece, ((Bishop)tempPiece).Move(newBoard));
					        	break;
					        case "QueenB":
					        	tempPiece = new Queen(7-x, 7-y, modifiedString, false);
					        	//piecesInMap.put(tempPiece, ((Queen)tempPiece).Move(newBoard));
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
					        	//piecesInMap.put(tempPiece, ((King)tempPiece).Move(newBoard));
					            break;
					        case "QueenW":
					        	tempPiece = new Queen(7-x, 7-y, modifiedString, true);
					        	//piecesInMap.put(tempPiece, ((Queen)tempPiece).Move(newBoard));
					        	break;
					        case "RookW":
					        	tempPiece = new Rook(7-x, 7-y, modifiedString, true);
					        	//piecesInMap.put(tempPiece, ((Rook)tempPiece).Move(newBoard));
					        	break; 
					        case "BishopW":
					        	tempPiece = new Bishop(7-x, 7-y, modifiedString, true);
					        	//piecesInMap.put(tempPiece, ((Bishop)tempPiece).Move(newBoard));
					        	break; 
					        case "KnightW":
					        	tempPiece = new Knight(7-x, 7-y, modifiedString, true);
					        	//piecesInMap.put(tempPiece, ((Knight)tempPiece).Move(newBoard));
					        	break;
					        case "soldierW":
					        	tempPiece = new Soldier(7-x, 7-y, modifiedString, true);
					        	//piecesInMap.put(tempPiece, ((Soldier)tempPiece).Move(newBoard));
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
					ArrayList<Piece> piecesArr = new ArrayList<>();
					piecesArr = board.getPieces();
					if(!checkForMate(setUpPiecesHasMap())) {
						popUpCheck("check on me");
					}
					else {
						popUpCheck("mate on me");
					}
					
					board = new Board(8 * squareSize, 8 * squareSize, piecesArr);

					
					
					
					
					
					
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
