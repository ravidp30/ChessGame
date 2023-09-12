package ClientAndServerLogin;

import java.awt.Button;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import config.ConnectedClient;
import config.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;
import server.EchoServer;
import server.ServerUI;

public class ServerController implements Initializable {
	
	private static ServerController instance;

	private static EchoServer serverCommunication;

	public static ObservableList<ConnectedClient> connectedClients = FXCollections.observableArrayList(); // clients
																											// connected
	// table

	@FXML
	private TableView<ConnectedClient> tableView = new TableView<>(); // clients connected table

	@FXML
	private TableColumn<ConnectedClient, String> ipColumn; // clients connected table
	@FXML
	private TableColumn<ConnectedClient, String> usernameColumn; // clients connected table
	
	@FXML
    private Button btnConnect;

    @FXML
    private Button btnDisconnect;
    
    @FXML
    private Button btnExit;
    
	@FXML
	private Label lblStatus;
	@FXML
	private Label lblMessage;
	@FXML
	private Label txtIPAddress;

	// Connection Detail Variables
	@FXML
	private TextField txtPort = new TextField();

	@FXML
	private TextField txtServerIP = new TextField();

	
	//private clientPinger pinger = new clientPinger();
	
	public ServerController() {
		instance = this;
	}

	/**
	 * Retrieves the instance of the ServerPortFrameController.
	 *
	 * @return The instance of the ServerPortFrameController
	 */
	public static ServerController getInstance() {
	    return instance;
	}

	/**
	 * Adds a connected client to the list of connected clients using their hostname and IP.
	 *
	 * @param userHostName The hostname of the connected client
	 * @param userIP The IP address of the connected client
	 */
	public static void addConnectedClient(String userHostName, String userIP, ConnectionToClient client, Player player) {
		 synchronized (connectedClients) {
	    try {
	        // Create a new instance of ConnectedClient with the provided hostname and IP
	        connectedClients.add(new ConnectedClient(userHostName, userIP, client, player));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		 }
	}

	/**

	 Retrieves the list of connected clients.
	 @return the ObservableList of ConnectedClient objects representing the connected clients
	 */
	public static ObservableList<ConnectedClient> getConnectedClients() {
		synchronized (connectedClients) {
		return connectedClients;
		}
	}

	/**
	 * Removes a connected client from the list of connected clients.
	 *
	 * @param client The connected client to be removed
	 */
	public static void removeConnectedClient(ConnectedClient client) {
	    try {
	        // Remove the specified connected client from the list
	        connectedClients.remove(client);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}


	/**
	 * Handles the Connect button click event.
	 *
	 * @param event The action event triggered by the Connect button
	 */
	public void connectBtn(ActionEvent event) throws Exception {
	    if (getPort().trim().isEmpty()) {
	        // Check if any required fields are empty
	        lblMessage.setText("[Error] Missing fields!");
	    } else {
	        lblMessage.setText("");
	        // Connect to the MySQL database

	            lblStatus.setTextFill(Color.rgb(93, 210, 153));
	            lblStatus.setText("Connected");
	            setVisabilityForUI(true);
	            // Start the server
	            serverCommunication = ServerUI.runServer(getPort());
	    }
	}

	/**
	 * Set the visibility of UI components when Connecting and Disconnecting.
	 *
	 * @param isVisible True if the components should be visible/enabled, false otherwise
	 */
	private void setVisabilityForUI(boolean isVisible) {
	    this.txtPort.setDisable(isVisible);
	}

	/**

	 Handles the disconnection process of the server.

	 Sends a disconnection message to all connected clients and closes the server.

	 Updates the UI components and clears the connectedClients table.
	 */
	public void disconnectBtn() {
	    try {
	        // Send message to clients only if the server is on. If the server is not connected, the connection is null
	        serverCommunication.sendToAllClients("server is disconnected");
	    } catch (NullPointerException e) {
	        // Server is not connected, exit the program
	        System.exit(0);
	    }
	    
	    lblStatus.setTextFill(Color.rgb(254, 119, 76));
	    lblStatus.setText("Disconnected");
	    lblMessage.setText("");
	    
	    // Clear the connectedClients table
	    for(int idx = 0; idx < connectedClients.size(); idx++) {
	    	connectedClients.remove(idx);
	    }
	    
	    try {
	        serverCommunication.close(); // Close the server
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    setVisabilityForUI(false); // Set the visibility and enable/disable state of UI components
	}






	/**

	 Retrieves the entered port number from the port field.
	 @return The entered port number as a string.
	 */
	private String getPort() {
		return txtPort.getText();
	}

	/**

	 Starts the application by creating and showing the server GUI stage.
	 @param primaryStage The primary stage of the application.
	 @throws Exception If an exception occurs during the process.
	 */
	public void start(Stage primaryStage) throws Exception {
		System.out.println("asd");
		SceneManagment.createNewStage("/ClientAndServerLogin/ServerGUI.fxml", null, "Server").show();
		System.out.println("asd");
	}

	/**

	 Handles the action event for the exit button.
	 @param event The action event triggered by the exit button.
	 @throws Exception If an exception occurs during the process.
	 */
	public void exitBtn(ActionEvent event) throws Exception {
	    //System.out.println("exit Academic Tool");
	    disconnectBtn(); // Disconnect from the server and perform necessary cleanup
	    System.exit(0); // Exit the program
	    System.gc(); // Perform garbage collection
	}

	
	/**
	 * Loads default values into the UI text fields.
	 */
	public void loadInfo() {
	    txtPort.setText("5555"); // Set default port number
	    
	    try {
	        txtServerIP.setText(InetAddress.getLocalHost().getHostAddress()); // Set default server IP address
	        txtServerIP.setDisable(true); // Disable editing of server IP address field
	    } catch (UnknownHostException e) {
	        //System.out.println("Error: " + e.getMessage());
	    }
	}


	

	/** Removes the client from the table after it disconnects.
		@param ip The IP address of the client.
		@param clientName The name of the client.
	*/
	public static void removeConnectedClientFromTable(String ip, String clientName, String playerid) {
		
		// playerid == -1 only if something gone wrong. if all as planned it will remove the specific player from the connected players list
		
		if(!playerid.equals("-1")) {
			for(int idx = 0; idx < connectedClients.size(); idx++) {
				if(connectedClients.get(idx).getIp().equals(ip)) {
					if(connectedClients.get(idx).getClientname().equals(clientName) && connectedClients.get(idx).getPlayer().getPlayerId().equals(playerid)) {
						removeConnectedClient(connectedClients.get(idx));
					}
				}
			}
		}
		else {
			for(int idx = 0; idx < connectedClients.size(); idx++) {
				if(connectedClients.get(idx).getIp().equals(ip)) {
					if(connectedClients.get(idx).getClientname().equals(clientName)) {
						removeConnectedClient(connectedClients.get(idx));
					}
				}
			}
		}
	}

	/**

	 Initializes the controller.

	 @param location The location used to resolve relative paths for the root object, or null if the location is not known.

	 @param resources The resources used to localize the root object, or null if the root object was not localized.
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	    loadInfo();
	    lblStatus.setText("Disconnected");
	    lblMessage.setTextFill(Color.rgb(254, 119, 76));
	    usernameColumn.setCellValueFactory(new PropertyValueFactory<ConnectedClient, String>("clientname"));
	    ipColumn.setCellValueFactory(new PropertyValueFactory<ConnectedClient, String>("ip"));
	   
	    tableView.setItems(connectedClients);
	}

	public static void setPlayerStatus(Player player, int i) {
		synchronized (connectedClients) {
		for(int idx = 0; idx < connectedClients.size(); idx++) {
			if(connectedClients.get(idx).getPlayer().getPlayerId().equals(player.getPlayerId())) {
				connectedClients.get(idx).getPlayer().setStatus(i);
				System.out.println("asdasd::: " + connectedClients.get(idx).getPlayer().getStatus());
				break;
				
			}
		}
		
		for(int idx = 0; idx < connectedClients.size(); idx++) {
			System.out.println("-----------");
			System.out.println("id: " + connectedClients.get(idx).getPlayer().getPlayerId());
			System.out.println("status: " + connectedClients.get(idx).getPlayer().getStatus());
			System.out.println("name: " + connectedClients.get(idx).getClient().getName());
			System.out.println("address: " + connectedClients.get(idx).getClient().getInetAddress());
			System.out.println("-----------");
				
			
		}
		}
		
	}
	
	public static void setPlayerName(Player player, String name) {
		synchronized (connectedClients) {
		for(int idx = 0; idx < connectedClients.size(); idx++) {
			if(connectedClients.get(idx).getPlayer().getPlayerId().equals(player.getPlayerId())) {
				connectedClients.get(idx).getPlayer().setPlayerName(name);
				break;
				
			}
		}
		}
	}
	
	
	
	
}