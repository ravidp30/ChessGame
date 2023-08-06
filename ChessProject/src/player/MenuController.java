package player;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ClientAndServerLogin.SceneManagment;
import client.ClientUI;
import config.ConnectedClient;
import config.Player;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
	
	private static MenuController instance;
	
	private ActionEvent currEvent;
	
	public MenuController() {
		instance = this;
	}
	
	
	public void onClickReady(ActionEvent event) throws Exception {
		currEvent = event;
		if(player.getStatus() == 0) {
			ArrayList<Player> clickOnReady_arr = new ArrayList<>();
			clickOnReady_arr.add(new Player("PlayerClickedOnReady"));
			clickOnReady_arr.add(player);
			ClientUI.chat.accept(clickOnReady_arr);
		}
		else {
			ArrayList<Player> clickOnUnReady_arr = new ArrayList<>();
			clickOnUnReady_arr.add(new Player("PlayerClickedOnUnReady"));
			clickOnUnReady_arr.add(player);
			ClientUI.chat.accept(clickOnUnReady_arr);
		}
	}
	
	public void onClickExit(ActionEvent event) throws Exception {
		////quit function @@@@@@@@@@@@@@@@@@@@@@@@@

	}
	
	public void onClickStartGame(ActionEvent event) throws Exception {
		if(player.getStatus() == 1) {
			ArrayList<Player> clickOnStartGame_arr = new ArrayList<>();
			clickOnStartGame_arr.add(new Player("PlayerClickedOnStartGame"));
			clickOnStartGame_arr.add(player);
			ClientUI.chat.accept(clickOnStartGame_arr);
		}
		else {
			System.out.println("You are not ready!");
		}
	}
	
	public static void start(String pId) throws IOException {
		player = new Player(pId);
		SceneManagment.createNewStage("/player/MenuGUI.fxml", null, "Menu").show();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ClientUI.chat.accept("UpdateCounterOfReadyPlayers");
		
	}

	public void setPlayerReady(int i) {
		Platform.runLater(() -> {
			player.setStatus(i);
			if(i == 0) {
				readyBtn.setText("Ready");
			}
			else {
				readyBtn.setText("UnReady");
			}
		});
	}

	public void startGame(Player opponent) throws IOException {
		player.setStatus(2); // in game
		
		System.out.println("Game Started!!!");
		System.out.println(player + " VS " + opponent);
		
        Platform.runLater(() -> {
			((Node) currEvent.getSource()).getScene().getWindow().hide(); // hiding primary window
	
	        try {
				GameController.start(player, opponent);
			} catch (IOException e) {
				e.printStackTrace();
			}
        });
		
		
	}


	public static MenuController getInstance() {
		return instance;
	}


	public void changePlayersReady(String cntReadyPlayers) {
		Platform.runLater(() -> {
			playersReady.setText(cntReadyPlayers + "/2");
		});
	}
	
	

}
