package handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import ClientAndServerLogin.ServerController;
import config.ConnectedClient;
import config.Player;
import javafx.collections.ObservableList;
import ocsf.server.ConnectionToClient;
import server.EchoServer;
import server.ServerUI;

public class MessageHandler_Server {

	
	private static int playeridCounter = 0;
	private static int playersReady = 0;
	
	private static ObservableList<ConnectedClient> connectedClients;
	
	/**
	 * Handles the received message and performs the necessary actions based on the message type.
	 *
	 * @param msg the received message object
	 * @param client the ConnectionToClient representing the client connection
	 */
	@SuppressWarnings("unchecked")
	public static void handleMessage(Object msg, ConnectionToClient client) {
		//System.out.println("Reached the handleMessage Method | Server");
	    MessageType messageType = getMessageType(msg);
	    if (messageType == null) {
	        return;
	    }

	    switch (messageType) {
	        case STRING:
	            handleStringMessage((String) msg, client);
	            break;
	        case ARRAY_LIST_STRING:
	            handleStringArrayListMessage((ArrayList<String>) msg, client);
	            break;
	        case MAP_STRING_ARRAYLIST_STRING:
	            handleMapStringKeyArrayListStringValueMessage((Map<String, ArrayList<String>>) msg, client);
	            break;
	        case MAP_STRING_STRING:
	            handleMapStringStringValueMessage((Map<String, String>) msg, client);
	            break;
	        case ARRAY_LIST_PLAYER:
	        	handlePlayerArrayListMessage((ArrayList<Player>) msg, client);
	        	break;
	        default:
	            //System.out.println("Message type does not exist");
	            break;
	    }
	}

	private static void handlePlayerArrayListMessage(ArrayList<Player> arrayList, ConnectionToClient client) {

        ArrayList<Player> arrayListPlayer = (ArrayList<Player>) arrayList;
		//System.out.println(arrayListStr);
        String messageType = arrayListPlayer.get(0).getPlayerId();
        try {
            switch (messageType) {
            
				case "PlayerClickedOnReady":
			    	// 1 - player
			    	if(playersReady == 0) {
			    		playersReady ++;
			    		System.out.println("Player" + arrayListPlayer.get(1).getPlayerId() + "is ready!");
			    		System.out.println("total players ready: " + playersReady);
			    		
			    		// set player status = 1 (ready)
			    		connectedClients = ServerController.getConnectedClients();
						for(int i = 0; i < connectedClients.size(); i++) {
							if(connectedClients.get(i).getPlayer().getPlayerId().equals(arrayListPlayer.get(1).getPlayerId())) {
								connectedClients.get(i).getPlayer().setStatus(1);
							}
						}    		
			    		client.sendToClient("First Ready");
			    		
			    		ArrayList<String> arr = new ArrayList<>();
			    		arr.add("ChangePlayersReady");
			    		arr.add(Integer.toString(playersReady));
						EchoServer.getInstance(5555).sendToAllClients(arr);
			    		
			    	}
			    	else if(playersReady == 1) {
			    		playersReady ++;
			    		System.out.println("Player" + arrayListPlayer.get(1).getPlayerId() + "is ready!");
			    		System.out.println("total players ready: " + playersReady);
			    		
						
			    		// set player status = 1 (ready)
			    		connectedClients = ServerController.getConnectedClients();
						for(int i = 0; i < connectedClients.size(); i++) {
							if(connectedClients.get(i).getPlayer().getPlayerId().equals(arrayListPlayer.get(1).getPlayerId())) {
								connectedClients.get(i).getPlayer().setStatus(1);
							}
						}    		
						client.sendToClient("Second Ready");
			    		
			    		ArrayList<String> arr = new ArrayList<>();
			    		arr.add("ChangePlayersReady");
			    		arr.add(Integer.toString(playersReady));
						EchoServer.getInstance(5555).sendToAllClients(arr);
						
						
			    	}
			    	else {
			    		System.out.println("total players ready: " + playersReady);
			    		System.out.println("too many");
			    		client.sendToClient("too many players are ready");
			    	}
			    	
			    	break;
			    	
				case "PlayerClickedOnUnReady":
					playersReady --;
		    		System.out.println("Player" + arrayListPlayer.get(1).getPlayerId() + "is Unready!");
		    		System.out.println("total players ready: " + playersReady);
							
		    		// set player status = 0 (unready)
		    		connectedClients = ServerController.getConnectedClients();
					for(int i = 0; i < connectedClients.size(); i++) {
						if(connectedClients.get(i).getPlayer().getPlayerId().equals(arrayListPlayer.get(1).getPlayerId())) {
							connectedClients.get(i).getPlayer().setStatus(0);
						}
					}    		
					client.sendToClient("UnReady");
		    		
		    		ArrayList<String> arr = new ArrayList<>();
		    		arr.add("ChangePlayersReady");
		    		arr.add(Integer.toString(playersReady));
					EchoServer.getInstance(5555).sendToAllClients(arr);
			    	
			    	break;
			    	
				case "PlayerClickedOnStartGame":
					// 1 - the player clicked on start game
					Player currPlayer = arrayListPlayer.get(1);
					currPlayer.setStatus(2);
					if(playersReady == 2) {
						ArrayList<Player> players_arr = new ArrayList<>();
						players_arr.add(new Player("GameStarting"));
						connectedClients = ServerController.getConnectedClients();
						for(int i = 0; i < connectedClients.size(); i++) {
							if(connectedClients.get(i).getPlayer().getStatus() == 1) { // all the ready players
								connectedClients.get(i).getPlayer().setStatus(2); // set the status of the 2 players that are ready to 2 (in game)
								// send every player (2 players total) the player who play against
								if(!connectedClients.get(i).getPlayer().getPlayerId().equals(currPlayer.getPlayerId())) {
									players_arr.add(currPlayer);
									connectedClients.get(i).getClient().sendToClient(players_arr);
									players_arr.set(1, connectedClients.get(i).getPlayer());
									client.sendToClient(players_arr);
									
									System.out.println("Game started between: \n" + currPlayer + " AND " + connectedClients.get(i).getPlayer());
									
									break;
								}
							}
						}    
					}
					else {
						System.out.println("2 players have to be ready before starting a game!!! - server");
						client.sendToClient("2 players have to be ready before starting a game!!! - player");
					}
					
					
					break;
            }
    	
        }catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
        }
		
	}

	/**
	 * Determines the MessageType based on the type of the received message object.
	 *
	 * @param msg the message object
	 * @return the MessageType
	 */
	private static MessageType getMessageType(Object msg) {
		//System.out.println("Reached the getMessageType Method | Server Handler");
	    if (msg instanceof String) {
	        return MessageType.STRING;
	    } else if (msg instanceof ArrayList) {
	        ArrayList<?> arrayList = (ArrayList<?>) msg;
	        if (!arrayList.isEmpty()) {
	            Object firstElement = arrayList.get(0);
	            if (firstElement instanceof String) {
	                return MessageType.ARRAY_LIST_STRING;
	            }
	            if (firstElement instanceof Player) {
	                return MessageType.ARRAY_LIST_PLAYER;
	            }
			}
	    } else if (msg instanceof Map) {
	        Map<?, ?> map = (Map<?, ?>) msg;
	        if (!map.isEmpty()) {
	            Object firstKey = map.keySet().iterator().next();
	            Object firstValue = map.get(firstKey);
	            if (firstKey instanceof String && firstValue instanceof ArrayList
	                    && ((ArrayList<?>) firstValue).get(0) instanceof String) {
	                return MessageType.MAP_STRING_ARRAYLIST_STRING;
	            } else if (firstKey instanceof String && firstValue instanceof String) {
	                return MessageType.MAP_STRING_STRING;
	            }
	        }
	    }
		return null;
	}

	/**
	 * Handles the received string message and performs the necessary actions.
	 *
	 * @param message the string message
	 * @param client the ConnectionToClient representing the client connection
	 */
    private static void handleStringMessage(String message, ConnectionToClient client) {
		//System.out.println("Reached the handleStringMessage Method");
        // Handle string messages
    	try {
	    	switch (message) {
			    case "UpdateCounterOfReadyPlayers":
			    	ArrayList<String> arr = new ArrayList<>();
			    	arr.add("ChangePlayersReady");
			    	arr.add(String.valueOf(playersReady));
			    	client.sendToClient(arr);
			    	break;
			    	
			    default:
	    	}
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	}
    }

	/**
	 * Handles the received ArrayList and performs the necessary actions based on the message type.
	 *
	 * @param arrayList the ArrayList containing the message and its parameters
	 * @param client the ConnectionToClient representing the client connection
	 *
	 * must client.sendToClient(obj); after handling the message from the client to get response from the server
	 */
    @SuppressWarnings("unchecked")
    private static void handleStringArrayListMessage(ArrayList<?> arrayList, ConnectionToClient client) {
		//System.out.println("Reached the handleStringArrayListMessage method | Server Handler");
            ArrayList<String> arrayListStr = (ArrayList<String>) arrayList;
			//System.out.println(arrayListStr);
            String messageType = arrayListStr.get(0);
            try {
	            switch (messageType) {
	                case "ClientConnecting":
	                    // Handle ClientConnecting message
						ServerController.addConnectedClient(arrayListStr.get(1), arrayListStr.get(2), client, new Player(String.valueOf(playeridCounter)));
						ArrayList<String> clientConnected_arr = new ArrayList<>();
						clientConnected_arr.add("connected");
						clientConnected_arr.add(String.valueOf(playeridCounter)); // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ the id of the player sent to him back
						client.sendToClient(clientConnected_arr);
						playeridCounter ++;
						
	                    break;
	                
	                case "ClientQuitting":
	                    // Handle ClientQuitting message
	                	
						// 1 - HostAddress
						// 2 - HostName
						// 3 - UserID
						// 4 - userLoginAs
						// 5 - isLogged
						ServerController.removeConnectedClientFromTable(arrayListStr.get(1), arrayListStr.get(2)); // call function to remove the client from the table
						client.sendToClient("quit");
						
	                    break;

				}
            }catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            }
        }

	/**
	 * Handles the received Map of String keys and ArrayList<String> values.
	 *
	 * @param map the Map of String keys and ArrayList<String> values
	 * @param client the ConnectionToClient representing the client connection
	 */
    private static void handleMapStringKeyArrayListStringValueMessage(Map<String, ArrayList<String>> map, ConnectionToClient client) {
        // Handle Map<String, ArrayList<String>> messages
    	

    }

	/**
	 * Handles the received Map of String keys and String values.
	 *
	 * @param msg the Map of String keys and String values
	 * @param client the ConnectionToClient representing the client connection
	 */
	private static void handleMapStringStringValueMessage(Map<String, String> msg, ConnectionToClient client) {
		// Handle Map<String, String> messages
		
	}
    
}
