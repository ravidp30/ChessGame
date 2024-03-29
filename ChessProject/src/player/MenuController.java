package player;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import ClientAndServerLogin.SceneManagment;
import client.ClientUI;
import config.Player;

import java.util.ArrayList;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import javafx.scene.control.Label;

import javafx.scene.control.Alert.AlertType;

import javafx.scene.image.ImageView;

import javafx.scene.layout.Pane;

import javafx.scene.control.TextField;

public class MenuController implements Initializable{
	
	@FXML
	private Button exitBtn;
	@FXML
	private Button readyBtn;
	@FXML
	private TextField nameText;
	@FXML
	private Label playersReady;
	@FXML
	private ImageView imageBG;// background image 
	@FXML
	private Pane MenuPane;

	
	private static Player player;
	
	private static MenuController instance;
	
	private ActionEvent currEvent;
	    
	public MenuController() {
		instance = this;

	      
	}
	
	
	public void onClickReady(ActionEvent event) throws Exception {
		currEvent = event;
		if(player.getStatus() == 0 ) {
			if(nameText.getText().isEmpty()) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
		        alert.setTitle("name");
		        alert.setHeaderText("Please enter your name before ready");
		        alert.getDialogPane().getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
		        alert.getDialogPane().getStyleClass().add("custom-alert");
		        ButtonType exitButton = new ButtonType("Close");
		        alert.getButtonTypes().setAll(exitButton);
		        alert.show();
			}
			else {
				player.setPlayerName(nameText.getText()); // set player name
				ArrayList<Player> clickOnReady_arr = new ArrayList<>();
				clickOnReady_arr.add(new Player("PlayerClickedOnReady"));
				clickOnReady_arr.add(player);
				ClientUI.chat.accept(clickOnReady_arr);
			}
		}
		else {
			ArrayList<Player> clickOnUnReady_arr = new ArrayList<>();
			clickOnUnReady_arr.add(new Player("PlayerClickedOnUnReady"));
			clickOnUnReady_arr.add(player);
			ClientUI.chat.accept(clickOnUnReady_arr);
		}
	}
	
	public void onClickExit(ActionEvent event) throws Exception {
		
		if(player.getStatus() == 1) { // if the player is ready, update it in the lobby and than exit the application
			ArrayList<Player> clickOnUnReady_arr = new ArrayList<>();
			clickOnUnReady_arr.add(new Player("PlayerClickedOnUnReady"));
			clickOnUnReady_arr.add(player);
			ClientUI.chat.accept(clickOnUnReady_arr);
			player.setStatus(0);
		}
		ClientUI.chat.client.quit(player.getPlayerId());

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
	
	// endStatus: 0 - none , 1 - lost - 2 - won
	public static void start(String pId, int endStatus) throws IOException {
		
		player = new Player(pId);
		SceneManagment.createNewStage("/player/MenuGUI.fxml", null, "Menu").show();
		
		String message = "";
		if(endStatus == 2) {
			message = "Your opponent has left the game";
		}
		
		if(endStatus == 1) {
			message = "You left the game";
		}
		
		if(endStatus == 1 || endStatus == 2) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
	        alert.setTitle("The Game Was Ended");
	        alert.setHeaderText(message);
	        
	        ButtonType exitButton = new ButtonType("Close");
	        alert.getButtonTypes().setAll(exitButton);
	        alert.show();
		}

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

	public void startGame(Player opponent, Player playerStarting) throws IOException {
		player.setStatus(2); // in game
		System.out.println("Game Started!!!");
		System.out.println(player + " VS " + opponent);
		
        Platform.runLater(() -> {
			((Node) currEvent.getSource()).getScene().getWindow().hide(); // hiding primary window
	
	        try {
				GameController.start(player, opponent, playerStarting);
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
	
	

	
	
	
	
	
	
	
/*
 * 
 * 
 * 
 * 									Created by Ravid && Nadav
 * 
 * 
 * 
 */
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
