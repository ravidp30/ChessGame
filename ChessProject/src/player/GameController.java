package player;

import java.awt.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.scene.effect.DropShadow;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.image.Image;


public class GameController implements Initializable {
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
    private boolean continueTurn = true;
    private Image Cloud ;
    private ImageView CloudImageView ;
    private HBox hbox;
    private HBox yourPiecesKilledBox;
    private HBox oppPiecesKilledBox;

    private String[] pieceImagePaths  = {
	        "/player/QueenW.png",
	        "/player/KnightW.png",
	        "/player/BishopW.png",
	        "/player/RookW.png"
	    };
    ImageView customCursor;
    Image cursorImage;
    ImageView winnerView;
    Image winnerImage;
 
   
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
    private Pane addPiecesBar;
    @FXML
    private Pane myPane;
    @FXML
    private Pane oppPane;
    @FXML
    private TextField txtChat;
    @FXML
    private TextArea chatArea;
    @FXML
    private Label ChatLabel;
    @FXML
    private Label oppName;
    @FXML
    private Label yourName;
    @FXML
    private Button goodLuck;
    @FXML
    private Button sendGoodGame;
    @FXML
    private Button sendNiceMove;
    @FXML
    private Button sendThanks;
    
    @FXML
    private Label timerLabel;
    
    @FXML
    private Label timerOpponentLabel;
    
   // @FXML
    //private Label ChessHeadLineLbl;
    @FXML
    private Label lblTurnStatus;
    @FXML
    private Label OpponentLbl;
    @FXML
    private Label YouLbl;
    @FXML
    private Pane chessboardPane;
    @FXML
	private Button exitBtn;
    @FXML
  	private ImageView alarm;
   
    
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
    	
    	String textInside = chatArea.getText();
    	chatArea.setText(textInside + "\n(You): " + msg);
    	
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
    public void sendGoodGame(ActionEvent event) throws Exception {
    	sendMessage("Good Game");
    }
    public void sendNiceMove(ActionEvent event) throws Exception {
    	sendMessage("Nice Move");
    }
    public void sendThanks(ActionEvent event) throws Exception {
    	sendMessage("Thanks");
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
    	
    	timerLabel.setText("5 : 00");
    	timerOpponentLabel.setText("5 : 00");
    	alarm.setVisible(false);
        backGroundPane.setStyle("-fx-background-color: rgba(0, 0, 0, 1); -fx-border-width: 1px;");
        lblTurnStatus.setStyle("-fx-text-fill: #EE101F; -fx-font-weight: bold; -fx-font-size: 20px;");
        winnerImage = new Image("/player/Winner.gif");
        winnerView = new ImageView(winnerImage);
        winnerView.setVisible(false); // Initially hide the animation
        chessboardPane.getChildren().add(winnerView);       
        yourName.setText(player.getPlayerName());
        oppName.setText(opponent.getPlayerName());
        yourName.setStyle("-fx-font-weight: bold;");
        oppName.setStyle("-fx-font-weight: bold;");
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
     	 hbox.setLayoutX(300);
		 hbox.setLayoutY(65);
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
         	
         //Hbox of your killed pieces  
         yourPiecesKilledBox = new HBox(5); // Set spacing between slots
         yourPiecesKilledBox.setLayoutX(260);
         yourPiecesKilledBox.setLayoutY(670);
   		 backGroundPane.getChildren().add(yourPiecesKilledBox);
   		 
         //Hbox of opponent killed pieces  
   		oppPiecesKilledBox = new HBox(5); 
   		oppPiecesKilledBox.setLayoutX(260);	
   		oppPiecesKilledBox.setLayoutY(70);
		backGroundPane.getChildren().add(oppPiecesKilledBox);
		     
         	// Create an ImageView to represent the custom cursor
         	cursorImage = new Image("/player/arm.png");
            customCursor = new ImageView(cursorImage);
            customCursor.setVisible(false); // Initially hide the custom cursor
            chessboardPane.setCursor(javafx.scene.Cursor.NONE); 
            chessboardPane.getChildren().add(customCursor);/* your UI elements */
            // Show the custom cursor and move it with the mouse
            chessboardPane.setOnMouseMoved(event -> {
            	customCursor.setVisible(true);
            	customCursor.toFront();
            	customCursor.setTranslateX(event.getX());
            	customCursor.setTranslateY(event.getY()-10);

            });
            // Hide the custom cursor when the mouse leaves the scene
            chessboardPane.setOnMouseExited(event -> customCursor.setVisible(false));
     
        changePlayerTurn(playerTurn, new Player("NotInCheck"));
        try {
			drawChessboard();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        
    }
	
    /**
     * Handles the selection of a new chess piece from a graphical user interface (GUI)
     * component (HBOX) and performs the necessary actions based on the selected piece.
     *
     * @param slotIndex The index of the selected piece within the HBOX component.
     *                  Possible values: 0 for Queen, 1 for Knight, 2 for Bishop, 3 for Rook.
     */
    private void handlePieceClickOnHBOX(int slotIndex) {//Queen knight bishop rook
    	deleteOpponentPicture(lastChosenPiece);
    	board.removePiece(lastChosenPiece.getX(),lastChosenPiece.getY());
    	switch(slotIndex) {
    	case 0:
    		setUpPiece(lastChosenPiece.getX(), lastChosenPiece.getY(), "QueenW", true);
    		break;
    	case 1:
    		setUpPiece(lastChosenPiece.getX(), lastChosenPiece.getY(), "KnightW", true);
    		break;
    	case 2:
    		setUpPiece(lastChosenPiece.getX(), lastChosenPiece.getY(), "BishopW", true);
    		break;
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
    /**
     * Handles the event when a player clicks the "Exit Game" button in the user interface.
     *
     * @param event The ActionEvent representing the button click event.
     * @throws Exception If an exception occurs during the game exit process.
     */
    public void onPlayerClickExitGame(ActionEvent event) throws Exception {
    	Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit Game");
        alert.setHeaderText("Are you sure you want to exit the game?");
        ButtonType exitButton = new ButtonType("Exit Game");
        ButtonType cancelButton = new ButtonType("Stay In The Game");
        alert.getButtonTypes().setAll(exitButton, cancelButton);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-alert");
        alert.showAndWait().ifPresent(response -> {
            if (response == exitButton) {
                // User clicked "Exit Game"
                System.out.println("Exiting the game...");
                
                ArrayList<Player> playerExitedFromGame = new ArrayList<>();
                playerExitedFromGame.add(new Player("PlayerExitedFromActiveGame"));
                playerExitedFromGame.add(player);
                ClientUI.chat.accept(playerExitedFromGame);
                
    			
                
                exitActiveGame(1);
                
            }
        });

    }
    /**
     * Exits the active game and displays an appropriate menu based on the end status.
     *
     * @param endStatus An integer representing the end status of the game:
     *                  - 0: No specific end status (none).
     *                  - 1: The player has lost the game.
     *                  - 2: The player has won the game.
     */
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
    

    /**
     * Draws a chessboard with squares, assigns colors to squares based on their positions,
     * and sets up the initial chess pieces on the board.
     *
     * @throws IOException If an input/output exception occurs while drawing the chessboard.
     */
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
	
	/**
	 * Sets up a chess piece on the chessboard at the specified position with the given properties.
	 *
	 * @param x       The x-coordinate (column) where the piece is to be placed.
	 * @param y       The y-coordinate (row) where the piece is to be placed.
	 * @param name    The name of the piece, e.g., "KingW" for White King or "soldierB" for Black Soldier.
	 * @param isWhite A boolean indicating whether the piece is White (true) or Black (false).
	 */
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
        
        chessboardPane.getChildren().add(imageViews[x][y]);
        
        board.addPiece(piece);

    }
    
    /**
     * Handles the click event when a player clicks on a chessboard cell (Rectangle).
     *
     * @param cell The Rectangle representing the clicked chessboard cell.
     */
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
		        	castlingCheck();
		        	cursorImage=new Image("/player/" + firstPieceSelected.getname() +".png" ,cursorImage.getWidth(),cursorImage.getHeight(), true, false);
		        	customCursor.setImage(cursorImage);
		        	return;
		        }
		        
		       
		       
		        else {
		        	System.out.println("not our");
		        	cursorImage=new Image("/player/arm.png");
		        	customCursor.setImage(cursorImage);
		        }
	    	}
    	}
    	else {
    		System.out.println("not your turn");
    	}
    	
    }
    
    /**
     * Checks for a checkmate scenario in the chess game by analyzing all possible moves for the given pieces.
     * It creates new board states for each move and checks if the current player is in check after the move.
     *
     * @param piecesInMap A HashMap containing the pieces and their possible moves.
     * @return true if the current player is in checkmate, false otherwise.
     */
    public boolean checkForMate(HashMap<Piece, ArrayList<Piece>> piecesInMap) {
    	
    	//save the original board.
    	ArrayList<Piece> pieces = new ArrayList<>();
    	for (Piece originalPiece : board.getPieces()) {
    	    // Create new instances of the pieces and copy their properties
    	    Piece newPiece = new Piece(originalPiece.getX(), originalPiece.getY(), originalPiece.getname(), originalPiece.isWhite());
    	    pieces.add(newPiece);
    	}
    	
    	Piece key;
    	
    	for (HashMap.Entry<Piece, ArrayList<Piece>> entry : piecesInMap.entrySet()) {
	            key = new Piece(entry.getKey().getX(), entry.getKey().getY(), entry.getKey().getname(), entry.getKey().isWhite());           
	            ArrayList<Piece> moveOptions = new ArrayList<>();          
	            moveOptions.addAll(entry.getValue());            	            
	            for(Piece optionToMove : moveOptions) {
	             // Create a new set of pieces based on the original pieces
	                ArrayList<Piece> newPieces = new ArrayList<>();
	                for (Piece originalPiece : pieces) {
	                    newPieces.add(new Piece(originalPiece.getX(), originalPiece.getY(), originalPiece.getname(), originalPiece.isWhite()));
	                }           
	            	Board newBoard = new Board(8 * squareSize, 8 * squareSize, newPieces);	            		            		                   
	            	if(optionToMove.getname().equals("empty")) {//available to move the image (EMPTY SPACE)
	            		newBoard.setPieceXY(key, optionToMove.getX(), optionToMove.getY());        	         		
	            	}
	            	else if(!optionToMove.isWhite() && !newBoard.getPiece(optionToMove.getX(), optionToMove.getY()).getname().equals("KingB")) { // move to black piece (eating)        		
	            		newBoard.removePiece(optionToMove.getX(), optionToMove.getY());
	            		newBoard.setPieceXY(key, optionToMove.getX(), optionToMove.getY());       		
	            	}   	
	            	if(!isChessOnMe(newBoard)) {
	            		return false;
	            	}	            	
	            }	          
    	}
    	return true;
    }
    
    /**
     * Moves a specific chess piece to the specified new position on the chessboard.
     *
     * @param newX The new x-coordinate (column) where the piece is to be moved.
     * @param newY The new y-coordinate (row) where the piece is to be moved.
     */
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
     	cursorImage=new Image("/player/arm.png");
     	customCursor.setImage(cursorImage);
    }
    
    /**
     * Deletes the opponent's chess piece image from the chessboard view and adds it to the appropriate "killed pieces" box.
     *
     * @param pieceToRemove The chess piece to be removed from the chessboard.
     */
    public void deleteOpponentPicture(Piece pieceToRemove) {
    	ImageView pieceImageView = imageViews[pieceToRemove.getX()][pieceToRemove.getY()];
        if (pieceImageView != null) {

    	chessboardPane.getChildren().remove(imageViews[pieceToRemove.getX()][pieceToRemove.getY()]);
    	imageViews[pieceToRemove.getX()][pieceToRemove.getY()] = null;
    	pieceImageView.setFitWidth(20);
		pieceImageView.setFitHeight(20);
    	if(pieceToRemove.getname().contains("W")) {
    		yourPiecesKilledBox.getChildren().add(pieceImageView);
    	}
    	else 
    		oppPiecesKilledBox.getChildren().add(pieceImageView);

        }
    	System.out.println("image was deleted");
	}

    /**
     * Changes the location of a chess piece image on the chessboard view to a new position.
     *
     * @param oldX The old x-coordinate (column) of the piece's current position.
     * @param oldY The old y-coordinate (row) of the piece's current position.
     * @param newX The new x-coordinate (column) where the piece is to be moved.
     * @param newY The new y-coordinate (row) where the piece is to be moved.
     */   
    public void ChangePiqtureLocation(int oldX, int oldY, int newX, int newY) {
        imageViews[oldX][oldY].setLayoutX((double)newX * squareSize);
        imageViews[oldX][oldY].setLayoutY((double)newY * squareSize);
        imageViews[newX][newY] = imageViews[oldX][oldY];
        imageViews[newX][newY].toFront();
        imageViews[oldX][oldY]=null;
   }
    
    /**
     * Changes the location of an opponent's chess piece on the chessboard view to a new position and handles eating.
     * If the opponent's piece is eaten, it is removed from the board and the view. If the opponent's piece is promoted
     * (e.g., a pawn promotes to another piece), its name is changed to a corresponding black piece's name.
     *
     * @param oldPiece   The opponent's piece to be moved.
     * @param newPiece   The new position for the opponent's piece.
     * @param eatingOrNot The piece being eaten, or null if no piece is eaten.
     */
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
    	    		//deleteOpponentPicture(newPiece);
    	    		// changing the name to black piece
    	    		String newPieceNameChangeToBlack = (newPiece.getname()).substring(0, (newPiece.getname()).length() - 1) + "B";
    	    		deleteOpponentPicture(new Piece(newPiece.getX(),newPiece.getY(), newPieceNameChangeToBlack, false));
    	    		setUpPiece(newPiece.getX(),newPiece.getY(), newPieceNameChangeToBlack, false);
    	    		
    	        }	        
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
    		});
       }
  }


    /**
     * Displays all possible move options for a given chess piece by highlighting the available squares on the chessboard.
     *
     * @param options The list of possible move options for the chess piece.
     * @param piece   The chess piece for which move options are displayed.
     */
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
    
    /**
     * Sets up a HashMap containing chess pieces and their corresponding move options for the current state of the chessboard.
     *
     * @return A HashMap where the keys are chess pieces and the values are ArrayLists of possible move options for each piece.
     */
    public HashMap<Piece, ArrayList<Piece>> setUpPiecesHasMap() {
        HashMap<Piece, ArrayList<Piece>> piecesInMap = new HashMap<>();
        
        
    	for(int x = 0; x < 8; x++) {
        	for(int y = 0; y < 8; y++) {
       		Piece tempPiece = null;
       		ArrayList<Piece> moveOptions = new ArrayList<>();
       		
        		try {
            		 
	        		switch (board.getPiece(x, y).getname()) {
			
						//-------WHITE------
			
				        case "KingW":
				        	tempPiece = new King(x, y, "KingW", true);
				        	moveOptions.addAll(((King)tempPiece).Move(board));
				            break;
				        case "QueenW":
				        	tempPiece = new Queen(x, y, "QueenW", true);
				        	moveOptions.addAll(((Queen)tempPiece).Move(board));
				        	break;
				        case "RookW":
				        	tempPiece = new Rook(x, y, "RookW", true);
				        	moveOptions.addAll(((Rook)tempPiece).Move(board));
				        	break; 
				        case "BishopW":
				        	tempPiece = new Bishop(x, y, "BishopW", true);
				        	moveOptions.addAll(((Bishop)tempPiece).Move(board));
				        	break; 
				        case "KnightW":
				        	tempPiece = new Knight(x, y, "KnightW", true);
				        	moveOptions.addAll(((Knight)tempPiece).Move(board));
				        	break;
				        case "soldierW":
				        	tempPiece = new Soldier(x, y, "soldierW", true);
				        	moveOptions.addAll(((Soldier)tempPiece).Move(board));
				        	break; 	
				        default:
				        	break;
	        		}
	        		if(tempPiece != null) {
	        			piecesInMap.put(tempPiece, moveOptions);
	        		}
        	
        		}catch (NullPointerException e) {}
        		
        	}
    	}
    	
    	return piecesInMap;
    }
    
    /**
     * Checks if the given chessboard state results in a check condition for the white player.
     *
     * @param board1 The chessboard state to be checked.
     * @return True if the white player is in check, false otherwise.
     */
    public boolean isChess(Board board1) {

    	ArrayList<Piece> moveOptions = new ArrayList<>();

    	Piece currPiece;

    	
    	for(int x = 0; x < 8; x++) {
    		for(int y = 0; y < 8; y++) {
    			try {
    				moveOptions.clear();
	    			currPiece = board1.getPiece(x, y);
	    			if(currPiece.isWhite()){
	    				
    					switch (currPiece.getname()) {

	    					case "KingW":
	    						moveOptions.addAll(((King) currPiece).Move(board1));
	    						break;
	
	    					case "QueenW":
	    						moveOptions.addAll(((Queen) currPiece).Move(board1));
	    						break;
	
	    					case "RookW":
	    						moveOptions.addAll(((Rook) currPiece).Move(board1));
	    						break; 
	
	    					case "BishopW":
	    						moveOptions.addAll(((Bishop) currPiece).Move(board1));
	    						break; 
	
	    					case "KnightW":
	    						moveOptions.addAll(((Knight) currPiece).Move(board1));
	    						break;
	
	    					case "soldierW":
	    						moveOptions.addAll(((Soldier) currPiece).Move(board1));
	    						break; 
	    					default:
	    						System.out.println("no way");
	    				}
    					for(Piece optionsToMove : moveOptions) {
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
    
    /**
     * Handles the second click when a player selects a square to move a chess piece to. It processes the move, checks for
     * check conditions, handles promotion of a soldier to a higher piece, and updates the turn.
     *
     * @param squareOption The square that the player has selected to move the chess piece to.
     */
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
	        	continueTurn = false;
	        	hbox.setVisible(true);       
	        }
	        else {
	        	TurnContinueAfterMovement();
	        }       	
        }
        
        cursorImage=new Image("/player/arm.png");
    	customCursor.setImage(cursorImage);
	}
	
	
	/**
	 * Handles the continuation of the game turn after a player's move. It checks for check conditions, displays alerts, and
	 * sends information about the move to the server and the opponent.
	 */
	public void TurnContinueAfterMovement() {
		
		if(lastChosenPiece != null) {
			lastChosenPiece = board.getPiece(lastChosenPiece.getX(), lastChosenPiece.getY());
		}
		
    	cloudImage(false);
        boolean inCheck = isChess(board);
        
        if(inCheck) {
        	showAutoDisappearAlert("Check", "nice,\nYou did check", Duration.seconds(4));  
        }
     
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
	
	/**
	 * Displays or hides a cloud image over the position of the player's king when the king is in check.
	 *
	 * @param use A boolean flag indicating whether to display the cloud image (true) or hide it (false).
	 */
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
	
	/**
	 * Reverts a move if it's an invalid move or would result in the player's own king being in check.
	 * This method is called when the player attempts to make an invalid move.
	 * It restores the pieces to their previous positions and updates the user interface accordingly.
	 */
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
	        oppPiecesKilledBox.getChildren().remove((oppPiecesKilledBox.getChildren().size() - 1));

        	lastChosenPiece = null;
        	
        	cursorImage=new Image("/player/arm.png");
        	customCursor.setImage(cursorImage);
		}
		
		showAutoDisappearAlert("Unvaliable Move", "You cannot move there", Duration.seconds(2));
    	cursorImage=new Image("/player/arm.png");
    	customCursor.setImage(cursorImage);		
	}

	/**
	 * Checks if the current player is in a check (chess) position on their modified board.
	 *
	 * @param newBoard The modified chessboard with pieces swapped for the current player.
	 * @return true if the current player is in check, false otherwise.
	 */
	private boolean isChessOnMe(Board newBoard) {
		ArrayList<Piece> tempPieces = new ArrayList<>();
		Piece tempPiece;
		String modifiedString = null;
		//changing the board 
		for(int x = 0; x < 8; x++) {
			for(int y = 0; y < 8; y++) {
				try {			
					tempPiece = newBoard.getPiece(x, y);
					if(tempPiece.getname().endsWith("W")) {
	                    // Swap white pieces to black
						modifiedString = (tempPiece.getname()).substring(0, (tempPiece.getname()).length() - 1) + "B";		
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
	                    // Swap black pieces to white
						modifiedString = (tempPiece.getname()).substring(0, (tempPiece.getname()).length() - 1) + "W";										
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
					
				}
			}
		}		 
		// Create a temporary board with the modified pieces
		Board tempBoard = new Board(8 * squareSize, 8 * squareSize, tempPieces);	
	    // Check if the current player is in check on the modified board
		return isChess(tempBoard);	
	}
	
	/**
	 * Sends a message to the server indicating a change in the current player's turn and the opponent's check status.
	 *
	 * @param inCheck A boolean indicating whether the opponent is in check.
	 */
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
	
	/**
	 * Changes the current player's turn and updates the user interface accordingly. If the player is in check,
	 * it displays a check alert and checks for mate. If the player is in checkmate, it sends a message to the
	 * opponent indicating the game result.
	 *
	 * @param newPlayerTurn The player whose turn is changing to.
	 * @param inCheck A special player used to indicate if the current player is in check ("InCheck") or not ("NotInCheck").
	 */
	// inCheck.getplayerId() = "InCheck" / "NotInCheck"
	public void changePlayerTurn(Player newPlayerTurn, Player inCheck) {
		Platform.runLater(() -> {
			playerTurn = newPlayerTurn;
			if(playerTurn.getPlayerId().equals(player.getPlayerId())) {		
				lblTurnStatus.setText("Your Turn");
				lblTurnStatus.setLayoutX(14);
				lblTurnStatus.setLayoutY(493);
		     	cursorImage=new Image("/player/arm.png");
		     	customCursor.setImage(cursorImage);
		     	customCursor.toFront();
				if(inCheck.getPlayerId().equals("InCheck")) {
					
					if(!checkForMate(setUpPiecesHasMap())) {
						showAutoDisappearAlert("Check", "ohh\nYou are on check", Duration.seconds(4)); 
						
					}
					else {
						
						// *********** looser message HERE *****************
						sendBackToOpponentIsMate(); // send to the opponent that he won with mate
						showFinishPopup("You Lost!","Oops! You lost the game.");
					}	
				}
			}
			else {
				lblTurnStatus.setText("Opponent's Turn");
				lblTurnStatus.setLayoutX(14);
				lblTurnStatus.setLayoutY(157);
		     	cursorImage=new Image("/player/X.png");
		     	customCursor.setImage(cursorImage);
		     	customCursor.toFront();
			}
		});

	}
	
	/**
	 * Displays a custom confirmation dialog to inform the user that the game has finished and provides
	 * an option to start a new game or exit the application.
	 *
	 * @param title The title of the confirmation dialog.
	 * @param header The header text of the confirmation dialog.
	 */
	public void showFinishPopup(String title, String header) {
		        // Create an Alert with a custom content area
		        Alert alert = new Alert(AlertType.CONFIRMATION);
		        alert.setTitle(title);
		        alert.setHeaderText(header);
		        alert.setContentText("Start A new game?");
		        ButtonType yesButton = new ButtonType("YES");
			    ButtonType noButton = new ButtonType("NO");
			    alert.getButtonTypes().setAll(yesButton, noButton);
			    
			    // Apply custom styles to the dialog
			    alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
		        alert.getDialogPane().getStyleClass().add("custom-alert");
		        
		        // Show the alert and handle the user's response
			    alert.showAndWait().ifPresent(answer -> {
			        if (answer == yesButton) {
			            System.out.println("New game");
			            ArrayList<Player> playerExitedFromGame = new ArrayList<>();
		                playerExitedFromGame.add(new Player("PlayerExitedFromActiveGame"));
		                playerExitedFromGame.add(player);
		                ClientUI.chat.accept(playerExitedFromGame);   
		                exitActiveGame(1);
		                
			        }
			        
			    });
			    
		    }
	
	/**
	 * Sends a notification to the opponent that the player has won the game with a checkmate.
	 * This method is used to inform the opponent about the game result.
	 */
	public void sendBackToOpponentIsMate() {
		ArrayList<Player> mateSend_arr = new ArrayList<>();
		mateSend_arr.add(new Player("PlayerWonWithMate"));
		mateSend_arr.add(opponent);
		ClientUI.chat.accept(mateSend_arr);
	}
	
	/**
	 * Receives and displays a message from the opponent in the chat area.
	 * 
	 * @param message The message received from the opponent.
	 */
	public void getMessageFromOponent(String message) {	
		Platform.runLater(() -> {	
	    	String textInside;    	
	    	textInside = chatArea.getText();   	
	    	chatArea.setText(textInside + "\n(Oponent): " + message);
		});
	}
	
	/**
	 * Applies a highlight effect to a specified square option (Rectangle) to indicate a valid move.
	 * 
	 * @param squareOption The Rectangle representing the square to highlight.
	 */
	public void SetHighLight (Rectangle squareOption) {
		DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(8.0);
        dropShadow.setOffsetX(0.0);
        dropShadow.setOffsetY(0.0);
        dropShadow.setColor(Color.BLUE); // You can adjust the color as needed
        squareOption.setEffect(dropShadow);
	}
	
	/**
	 * Displays a confirmation alert window to notify the user that a check condition is met.
	 * 
	 * @param message The message to display in the alert window.
	 */
	public void popUpCheck(String string){
		Alert Check = new Alert(AlertType.CONFIRMATION);
		Check.setTitle("Check");
		Check.setHeaderText(string );
		Check.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
		Check.getDialogPane().getStyleClass().add("custom-alert");
        Check.showAndWait();
	}
	
	/**
	 * Checks if castling is a valid move for the White King and Rook and prompts the user with a confirmation dialog.
	 * Castling is a special chess move that involves the White King and Rook. This method checks if the conditions
	 * for castling are met and asks the user if they want to perform castling.
	 * 
	 * Conditions for castling:
	 * - The White King is on its initial position (3, 7).
	 * - The White Rook is on its initial position (0, 7).
	 * - The squares between the White King and Rook (1, 7) and (2, 7) are empty.
	 * 
	 * If the conditions are met, a confirmation dialog will ask the user if they want to perform castling.
	 * If the user confirms, the castling move will be executed.
	 */
	private void castlingCheck() {
		try {
			if(firstPieceSelected.getname().equals("KingW"))
	        	if(firstPieceSelected.getX()==3 && firstPieceSelected.getY()==7)
	        		if(board.getPiece(0, 7).getname().equals("RookW"))
		        		if(board.getPiece(1, 7) == null && board.getPiece(2, 7) == null)
		        				popUpCastling("Do Castling?");
	        		//if(yes) ->	
		}catch (NullPointerException e) {}
	}
	
	/**
	 * Displays a confirmation dialog to inquire whether the player wants to perform castling.
	 * If the player confirms castling, the method executes the necessary moves for castling.
	 *
	 * @param string The title of the confirmation dialog.
	 */
	public void popUpCastling (String string) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
	    alert.setTitle(string);
	    alert.setHeaderText("Do you want to do castling??");
	    
	    ButtonType yesButton = new ButtonType("YES");
	    ButtonType noButton = new ButtonType("NO");
	    alert.getButtonTypes().setAll(yesButton, noButton);
	    alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-alert");
	    alert.showAndWait().ifPresent(answer -> {
	        if (answer == yesButton) {
	            // User want to do Castling
	            System.out.println("doing castling...");
	            // moving the king
	            secondPieceSelected = board.getPiece(1,7); // the place to move to     	
	            movePiece(1,7);          
	            if(isChessOnMe(board)) { //Chess On ME
		        	cursorImage=new Image("/player/arm.png");
		        	customCursor.setImage(cursorImage);
	            	moveBack();
	            	cloudImage(true);
	            	System.out.println("cannot castling...");
	            	return;
	            }
	            else {
	            	// move the first piece for the opponent
		    		ArrayList<Piece> updatePieceMoce_arr= new ArrayList<>();
		    		updatePieceMoce_arr.add(new Piece(0, 0, "PieceWasMoved", true));
		    		updatePieceMoce_arr.add(new Piece(0, 0, EatOrNot, true));
		    		updatePieceMoce_arr.add(new Piece(oldX, oldY, firstPieceSelected.getname(),firstPieceSelected.isWhite())); // old piece
		    		updatePieceMoce_arr.add(lastChosenPiece); // new piece
		    		updatePieceMoce_arr.add(new Piece(0, 0, player.getPlayerId(), true)); // player (playerId in piece's name)
		    		ClientUI.chat.accept(updatePieceMoce_arr);
	        
		    		// // moving the Rook
		            firstPieceSelected = board.getPiece(0, 7); // the RookW
		            oldX = 0;
		            oldY = 7;
		            secondPieceSelected = board.getPiece(2,7); // the place to move to    
		            movePiece(2,7); 
		          
	            }
	            
	            // move the second piece for the opponent
	            // sending to the server that was and do thing in the client (opponent)
	    		ArrayList<Piece> updatePieceMoce_arr= new ArrayList<>();
	    		updatePieceMoce_arr.add(new Piece(0, 0, "PieceWasMoved", true));
	    		updatePieceMoce_arr.add(new Piece(0, 0, EatOrNot, true));
	    		updatePieceMoce_arr.add(new Piece(oldX, oldY, firstPieceSelected.getname(),firstPieceSelected.isWhite())); // old piece
	    		updatePieceMoce_arr.add(lastChosenPiece); // new piece
	    		updatePieceMoce_arr.add(new Piece(0, 0, player.getPlayerId(), true)); // player (playerId in piece's name)
	    		ClientUI.chat.accept(updatePieceMoce_arr);
	            
	    		SendToServerChangePlayerTurn(isChess(board));  // send to the opponent also if there is check on him
				
	        }
	        else return;
	    });
	}
	
	/**
	 * Displays an informational alert with a message that automatically disappears after a specified duration.
	 * 
	 * @param title The title of the alert.
	 * @param message The message to display in the alert.
	 * @param duration The duration for which the alert will be shown before automatically disappearing.
	 */
    public  void showAutoDisappearAlert(String title, String message, Duration duration) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("custom-alert");
        Timeline timeline = new Timeline(new KeyFrame(duration, event -> alert.hide()));

        alert.show();
        timeline.play();
    }
    
    /**
     * Moves the first selected chess piece to the specified position (x, y) on the chessboard.
     * Handles various game scenarios such as checkmate, pawn promotion, and turn continuation.
     *
     * @param x The x-coordinate of the target position.
     * @param y The y-coordinate of the target position.
     */
    public void moveFirstPiece(int x, int y) {
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
	        	continueTurn = false;
	        	hbox.setVisible(true);
	        }
	        else 
	        	TurnContinueAfterMovement();

        }
    }
    public void tenSecRemain() {
    	Platform.runLater(() -> {
    		alarm.setVisible(true);
    		System.out.println(122222);
    		alarm.toFront();
    	});
    }
    
    /**
     * Displays a message to indicate that the player has won the game. This method
     * updates the user interface to show the winning message, changes the text color
     * of the turn status label to green, and sets a bold font style. It also displays
     * a pop-up message to inform the player about their victory.
     */
	public void wonTheGameMessage() {
		
		Platform.runLater(() -> {
			winnerView.toFront();
			  winnerView.setVisible(true); 
			  showFinishPopup("You Won!","You did it, You won the game!!!");
			String chickenEmoji = "\uD83D\uDC14";
			lblTurnStatus.setStyle("-fx-text-fill: green; -fx-font-weight: bold; -fx-font-size: 25px;");
			lblTurnStatus.setText("Winner winner chiken dinner! " + chickenEmoji);
		});
	}
	
	
	public void lostNoTimeMessage() {
		
		Platform.runLater(() -> {
			//winnerView.toFront();
			  //winnerView.setVisible(true); 
			  showFinishPopup("You Lost!","End Of Time!!!");
			String cryingEmoji = "\uD83D\uDE22";
			lblTurnStatus.setStyle("-fx-text-fill: red; -fx-font-weight: bold; -fx-font-size: 25px;");
			lblTurnStatus.setText("Looser! " + cryingEmoji);
		});
	}

	public void updateTimer(String timeRemaining, String timeRemainingForOppponent) {
		
		Platform.runLater(() -> {
			
	        int minutes = Integer.parseInt(timeRemaining) / 60;
	        int seconds = Integer.parseInt(timeRemaining) % 60;
	        String formattedSeconds = String.format("%02d", seconds);
	        
			timerLabel.setText(minutes + " : " + formattedSeconds);
			
			int minutesOp = Integer.parseInt(timeRemainingForOppponent) / 60;
			int secondsOp  = Integer.parseInt(timeRemainingForOppponent) % 60;
			String formattedOpSeconds = String.format("%02d", secondsOp);
			
			timerOpponentLabel.setText(minutesOp + " : " + formattedOpSeconds);

		});
		
	}

	public Player getPlayer() {
		return player;
	}
	
	public Player getOpponent() {
		return opponent;
	}
	
	
	
}














/*
 * 
 * 
 * 
 *												Created by Ravid && Nadav 
 * 	
 * 
 * 
 * 
 */













