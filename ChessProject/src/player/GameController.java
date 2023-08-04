package player;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import ClientAndServerLogin.SceneManagment;
import config.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class GameController implements Initializable {
    
    private static Player player;
    private static Player opponent;
    
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
        
        drawChessboard();
    }
    
    private void drawChessboard() {
        int squareSize = 64;
        
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Rectangle square = new Rectangle(x * squareSize, y * squareSize, squareSize, squareSize);
                
                if ((x + y) % 2 == 0) {
                    square.setFill(Color.WHITE.darker());
                } else {
                    square.setFill(Color.GREEN.darker());
                }
                
                chessboardPane.getChildren().add(square);
            }
        }
    }

}
