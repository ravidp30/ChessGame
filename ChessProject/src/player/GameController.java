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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;



public class GameController implements Initializable {
    
    private static Player player;
    private static Player opponent;
    private Board board;
    
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
        int squareSize = 54;
        
        Piece piece;
        ArrayList<Piece> pieces = new ArrayList<>();
        ImageView imageView = new ImageView();
        imageView.setFitWidth(squareSize);
        imageView.setFitHeight(squareSize);
        
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
            	
                Rectangle square = new Rectangle(x * squareSize, y * squareSize, squareSize, squareSize);
                
                
                if ((x + y) % 2 == 0) {
                    imageView.setStyle("-fx-background-color: white;");
                } else {
                    imageView.setStyle("-fx-background-color: green;");
                }
                
            	if(y == 6 && (x >= 0 && x < 8)) {
            		piece = new Piece(x, y, "soldier", true);
                    Image image = new Image(getClass().getResourceAsStream("/player/KingWhite.png"));
                    imageView.setImage(image);
            		
            		pieces.add(piece);
            	}
            	
                // Add the ImageView to the chessboardPane's children
                chessboardPane.getChildren().add(imageView);


                
            }
        }
        chessboardPane.setPrefWidth(8 * squareSize);
        chessboardPane.setPrefHeight(8 * squareSize);
        board = new Board(8 * squareSize, 8 * squareSize, pieces);
    }

}
