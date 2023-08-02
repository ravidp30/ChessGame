package player;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ClientAndServerLogin.SceneManagment;
import config.Player;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class GameController implements Initializable {
	
	private static Player player;
	private static Player opponent;
	
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
		
	}



}
