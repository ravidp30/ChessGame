package ClientAndServerLogin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import player.MenuController;

public class ClientConnectController implements Initializable {
	
	private static String playerId = "-1";

	@FXML
	private TextField txtPort;

	@FXML
	private TextField txtServerIP;

	@FXML
	private Label lblMessage;

	@FXML
	private Button btnExit;
	@FXML
	private Button btnConnect;
	
	private static ActionEvent currEvent;

	/**

	 Retrieves the text value from the "Port" text field.
	 @return The text value from the "Port" text field
	 */
	public String getTxtPort() {
		return txtPort.getText();
	}

	/**

	 Retrieves the text value from the "Server IP" text field.
	 @return The text value from the "Server IP" text field
	 */
	public String getTxtServerIP() {
		return txtServerIP.getText();
	}

	/**
	 * Handles the event when the exit button is clicked.
	 *
	 * @param event The action event triggered by the exit button
	 * @throws Exception If an exception occurs during the execution
	 */
	public void getExitBtn(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
		System.exit(0);
		System.gc();
	}

	/**
	 * Handles the event when the connect button is clicked.
	 *
	 * @param event The action event triggered by the connect button
	 * @throws Exception If an exception occurs during the execution
	 */
	public void getConnectBtn(ActionEvent event) throws Exception {

		if (getTxtServerIP().equals("") || getTxtPort().equals("")) {
			lblMessage.setText("[Error] Missing fields!");
			lblMessage.setTextFill(Color.rgb(254, 119, 76));
		} else {

			// create the client and connect him to server
			// the use of client: ClientUI.chat.client
			// to send the server message, use: ClientUI.chat.accept(object)
			if (!ClientUI.connectClient(getTxtServerIP(), Integer.valueOf(getTxtPort()))) {
				JOptionPane.showMessageDialog(null, "Couldn't connect to server.", "Connect to Server",
						JOptionPane.INFORMATION_MESSAGE);
				System.exit(0);
				return;
			}

			currEvent = event;
			
			// Adding Client Info
			lblMessage.setText("");
			ArrayList<String> clientInfo = new ArrayList<>();
			clientInfo.add("ClientConnecting");
			clientInfo.add(InetAddress.getLocalHost().getHostAddress());
			clientInfo.add(InetAddress.getLocalHost().getHostName());
			ClientUI.chat.accept(clientInfo);
			
		}

	}
	/**

	 Starts the JavaFX application by creating and displaying the primary stage.
	 @param primaryStage the primary stage of the JavaFX application
	 @throws Exception if an exception occurs during the startup process
	 */
	public void start(Stage primaryStage) throws Exception {
		SceneManagment.createNewStage("/ClientAndServerLogin/ClientConnectGUI.fxml", null, "Client Connect Managment Tool").show();
	}

	/**

	 Initializes the user interface components and sets default values.

	 @param location The URL of the FXML file

	 @param resources The resource bundle for the FXML file (not used in this method)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    // Initialize the user interface components and set default values

	    // @param location The URL of the FXML file
	    // @param resources The resource bundle for the FXML file (not used in this method)

	    txtPort.setText("5555"); // Set the default port value to "5555"

	    try {
	        txtServerIP.setText(InetAddress.getLocalHost().getHostAddress());
	        // Set the default server IP address value to the local host address
	    } catch (UnknownHostException e) {
	        // If an exception occurs while retrieving the local host address, print the stack trace
	        e.printStackTrace();
	    }
	}

	public static void saveIdForPlayer(String pId) throws IOException {
        Platform.runLater(() -> {
			playerId = pId;
			((Node) currEvent.getSource()).getScene().getWindow().hide(); // hiding primary window
	
	        try {
				MenuController.start(playerId, 0);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        });
		
	}
}
