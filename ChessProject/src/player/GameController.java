package player;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.swing.JPanel;

import ClientAndServerLogin.SceneManagment;
import config.Player;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class GameController implements Initializable {
    
    private static Player player;
    private static Player opponent;
    
    LinkedList<Piece> pieceL = new LinkedList<>();
    
    @FXML
    private Label ChessHeadLineLbl;

    @FXML
    private Pane chessboardPane; // Assuming you have a Pane in your FXML file to hold the chessboard

    public static void start(Player player_temp, Player opponent_temp) throws IOException {
        player = player_temp;
        opponent = opponent_temp;
        SceneManagment.createNewStage("/player/GameGUI.fxml", null, "Game").show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ChessHeadLineLbl.setText("Chess Game:\nYou (id: " + player.getPlayerId() + ") VS opponent (id: " + opponent.getPlayerId() + ")");
        
        int squareSize = 64;
        
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                boolean White = true;

                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        if (White)
                            g.setColor(Color.WHITE.darker());
                        else
                            g.setColor(Color.GREEN.darker());

                        g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
                        White = !White;
                    }
                    White = !White;
                }
                // Your piece rendering code can be added here
            }
        };

        SwingNode swingNode = new SwingNode();
        swingNode.setContent(panel);

        chessboardPane.getChildren().add(swingNode);
    }

}
