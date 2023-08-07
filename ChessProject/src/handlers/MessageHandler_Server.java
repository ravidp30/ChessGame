package handlers;

import java.io.IOException;
import java.util.Random;
import java.util.ArrayList;
import java.util.Map;

import ClientAndServerLogin.ServerController;
import config.ConnectedClient;
import config.Piece;
import config.Player;
import javafx.collections.ObservableList;
import ocsf.server.ConnectionToClient;
import server.EchoServer;
import server.ServerUI;

public class MessageHandler_Server {

	
	private static int playeridCounter = 0;
	private static int playersReady = 0;
	//ServerController serverController = new ServerController();
	private static ConnectionToClient player1;
	private static ConnectionToClient player2;
	private static String player1id;
	private static String player2id;
	private static String playerIdTurn;
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
	        case ARRAY_LIST_PIECE:
	        	handlePieceArrayListMessage((ArrayList<Piece>) msg, client);
	        	break;
	        default:
	            //System.out.println("Message type does not exist");
	            break;
	    }
	}

	private static void handlePieceArrayListMessage(ArrayList<Piece> arrayList, ConnectionToClient client) {

		
        ArrayList<Piece> arrayListPiece = (ArrayList<Piece>) arrayList;
		//System.out.println(arrayListStr);
        String messageType = arrayListPiece.get(0).getname();
        switch (messageType) {
		
			case "PieceWasMoved":
				// 1 - old piece
				// 2 - new piece
				// 3 - current playerId - piece's name
				ArrayList<Piece> pieceMoved_arr_toOponent = new ArrayList<>();
				pieceMoved_arr_toOponent.add(new Piece(0, 0, "OponentPieceWasMoved", true));
				pieceMoved_arr_toOponent.add(arrayListPiece.get(1));
				pieceMoved_arr_toOponent.add(arrayListPiece.get(2));	
				
				if(arrayListPiece.get(3).getname().equals(player1id)) {

					try {
						player2.sendToClient(pieceMoved_arr_toOponent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					try {
						player1.sendToClient(pieceMoved_arr_toOponent);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				/*connectedClients = ServerController.getConnectedClients();

				for(int i = 0; i < connectedClients.size(); i++) {
					
					System.out.println("id: " + connectedClients.get(i).getPlayer().getPlayerId());
					System.out.println("status: " + connectedClients.get(i).getPlayer().getStatus());
					System.out.println("address: " + connectedClients.get(i).getClient().getInetAddress());
					
					System.out.println("from: " + arrayListPiece.get(3).getname());
					
					System.out.println("client: " + connectedClients.get(i).getClient());

					//if(connectedClients.get(i).getPlayer().getStatus() == 2) {

						if(!connectedClients.get(i).getPlayer().getPlayerId().equals(arrayListPiece.get(3).getname())) {
							System.out.println(arrayListPiece.get(3).getname() + " " + connectedClients.get(i).getPlayer().getPlayerId());
						try {
							System.out.println(12 + " " + client);
							ArrayList<Piece> pieceMoved_arr_toOponent = new ArrayList<>();
							pieceMoved_arr_toOponent.add(new Piece(0, 0, "OponentPieceWasMoved", true));
							pieceMoved_arr_toOponent.add(arrayListPiece.get(1));
							pieceMoved_arr_toOponent.add(arrayListPiece.get(2));
							

							if (connectedClients.get(i).getClient() != null && connectedClients.get(i).getClient().isAlive()) {
								connectedClients.get(i).getClient().sendToClient(pieceMoved_arr_toOponent);
							} else {
							    System.out.println("@@@");
							
							}
							
							connectedClients.get(i).getClient().sendToClient(pieceMoved_arr_toOponent);
							break;
							
							

						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						}
					//}
				} */ 
			try {
				client.sendToClient("piece moved sucssefully");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
				
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
								ServerController.setPlayerStatus(connectedClients.get(i).getPlayer(), 1);
								//connectedClients.get(i).getPlayer().setStatus(1);//
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
								ServerController.setPlayerStatus(connectedClients.get(i).getPlayer(), 1);
								connectedClients.get(i).getPlayer().setStatus(1);//
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
							ServerController.setPlayerStatus(connectedClients.get(i).getPlayer(), 0);
							connectedClients.get(i).getPlayer().setStatus(0);//
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
					
			        Random random = new Random(); // random number (0 / 1) to choose who will start
			        double randomNumber = random.nextDouble();
					
					Player currPlayer = arrayListPlayer.get(1);
					//currPlayer.setStatus(2);
					if(playersReady == 2) {
						ArrayList<Player> players_arr = new ArrayList<>();
						players_arr.add(new Player("GameStarting"));
						
						connectedClients = ServerController.getConnectedClients();
						for(int i = 0; i < connectedClients.size(); i++) {
							if(connectedClients.get(i).getPlayer().getStatus() == 1) { // all the ready players
								ServerController.setPlayerStatus(connectedClients.get(i).getPlayer(), 2);
								//connectedClients.get(i).getPlayer().setStatus(2); // set the status of the 2 players that are ready to 2 (in game)
								// send every player (2 players total) the player who play against
								if(!connectedClients.get(i).getPlayer().getPlayerId().equals(currPlayer.getPlayerId())) {
									currPlayer.setStatus(2);
									

									
									System.out.println("Game started between: \n" + currPlayer + " AND " + connectedClients.get(i).getPlayer());
									player1id = currPlayer.getPlayerId();
									player1 = client;
									player2id = connectedClients.get(i).getPlayer().getPlayerId();
									player2 = connectedClients.get(i).getClient();
									
							        if(randomNumber == 0) {
							        	playerIdTurn = player1id;
							        }
							        else {
							        	playerIdTurn = player2id;
							        }
							        
							        players_arr.add(new Player(playerIdTurn)); // send the player that starting the game
							        players_arr.add(currPlayer);
									
									connectedClients.get(i).getClient().sendToClient(players_arr);
									players_arr.set(2, connectedClients.get(i).getPlayer());
									client.sendToClient(players_arr);
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
					
				case "ChangePlayerTurn":
					// 1 - Player next turn
					
					ArrayList<Player> playerNextTurn_arr = new ArrayList<>();
					playerNextTurn_arr.add(new Player("ChangePlayerTurnForOpponent"));
					playerNextTurn_arr.add(arrayListPlayer.get(1));
					player1.sendToClient(playerNextTurn_arr);
					player2.sendToClient(playerNextTurn_arr);
					
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
	            if (firstElement instanceof Piece) {
	                return MessageType.ARRAY_LIST_PIECE;
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
	                    
	                case "OponentSentMessage":
	                	// 1 - text
	                	// 2 - id player sender
	                	
	                	ArrayList<String> message_arr = new ArrayList<>();
	                	message_arr.add("GettingMessageFromPlayer");
	                	message_arr.add(arrayListStr.get(1));
	                	
	                	if(player1id.equals(arrayListStr.get(2))) {
	                		player2.sendToClient(message_arr);
	                	}
	                	else {
	                		player1.sendToClient(message_arr);
	                	}
	                	
	                	client.sendToClient("message sent");
	                	
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
