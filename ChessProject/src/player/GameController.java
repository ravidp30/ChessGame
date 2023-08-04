package player;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;

import ClientAndServerLogin.SceneManagment;
import config.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class GameController implements Initializable {
	
	private static Player player;
	private static Player opponent;
	
	JFrame frame;
	
	LinkedList<Piece> pieceL= new LinkedList<>();
	
	
	
	@FXML
	private Label ChessHeadLineLbl;

	public static void start(Player player_temp, Player opponent_temp) throws IOException {
		player = player_temp;
		opponent = opponent_temp;
		SceneManagment.createNewStage("/player/GameGUI.fxml", null, "Game").show();
		
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ChessHeadLineLbl.setText("Chess Game: \nYou: (id: " + player.getPlayerId() + ") VS opponent (id: " + opponent.getPlayerId() + ")");
		
		frame= new JFrame(); 
		frame.setBounds(10, 10, 600, 600);
		int squareSize = 64;
		JPanel panel = new JPanel() {
            @Override
            public void paint(Graphics g) {
                boolean White = true;
    
                for (int y = 0; y < 8; y++) {
                    for (int x = 0; x < 8; x++) {
                        if (White)
                            g.setColor(Color.WHITE.darker());
                       
                        else
                            g.setColor(Color.GREEN.darker());
    
                        g.fillRect(x * squareSize, y * squareSize, squareSize, squareSize);
                        White = !White; // Toggle the WHITE boolean for the next square in the same row
                    }
                    White = !White; // Toggle the WHITE boolean for the next row
                }
             /*   for (Piece piece : pieceL) {
                    try {
                        BufferedImage img = ImageIO.read(new File(piece.getImagePath()));
                        g.drawImage(img, piece.getX() * squareSize, piece.getY() * squareSize, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/
               
            }
        };
        
		frame.add(panel);
		
		
	        

		frame.setDefaultCloseOperation(3);
		frame.setVisible(true);
	}
		
		
	



}
