package player;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ClientAndServerLogin.SceneManagment;
import client.ClientUI;
import config.ConnectedClient;
import config.Player;

import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class MenuController implements Initializable{
	
	@FXML
	private Button exitBtn;
	@FXML
	private Button readyBtn;
	
	@FXML
	private Label playersReady;
	
	private static Player player;
	
	
	public void onClickReady(ActionEvent event) throws Exception {
		ArrayList<Player> clickOnReady_arr = new ArrayList<>();
		clickOnReady_arr.add(new Player("PlayerClickedOnReady"));
		//clickOnReady_arr.add(String.valueOf(player.getPlayerId()));
		clickOnReady_arr.add(player);
		ClientUI.chat.accept(clickOnReady_arr);
	}
	
	public void onClickExit(ActionEvent event) throws Exception {
		
	}
	
	
	public static void start(String pId) throws IOException {
		player = new Player(pId);
		SceneManagment.createNewStage("/player/MenuGUI.fxml", null, "Menu").show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
	}

}
