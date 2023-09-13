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
	
	private static GameTimer gameTimerPlayer1;
	private static GameTimer gameTimerPlayer2;
	
	private static Boolean gameOn = false;
	//private static Thread ThreadTimer;
	
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
				// 1 - Eating or NotEating
				// 2 - old piece
				// 3 - new piece
				// 4 - current playerId - piece's name
				ArrayList<Piece> pieceMoved_arr_toOponent = new ArrayList<>();
				pieceMoved_arr_toOponent.add(new Piece(0, 0, "OponentPieceWasMoved", true));
				pieceMoved_arr_toOponent.add(arrayListPiece.get(1));
				pieceMoved_arr_toOponent.add(arrayListPiece.get(2));
				pieceMoved_arr_toOponent.add(arrayListPiece.get(3));	
				
				if(arrayListPiece.get(4).getname().equals(player1id)) {

					try {
						player2.sendToClient(pieceMoved_arr_toOponent);
						gameTimerPlayer1.pause();
						gameTimerPlayer2.resume();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					try {
						player1.sendToClient(pieceMoved_arr_toOponent);
						gameTimerPlayer2.pause();
						gameTimerPlayer1.resume();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

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
								ServerController.setPlayerName(connectedClients.get(i).getPlayer(), arrayListPlayer.get(1).getPlayerName());
								
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
					if(playersReady == 2 && !gameOn) {
						gameOn = true;
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
									
									startGame();
									
							       /* gameTimerPlayer1 = new GameTimer("timer1", 60 * 5); // Set the initial timer duration to 5 minutes
							        gameTimerPlayer1.start();
							        gameTimerPlayer2 = new GameTimer("timer2", 60 * 5);
							        gameTimerPlayer2.start();
							        gameTimerPlayer2.pause();
							        */
							        
									
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
					// 2 - in check or not
					
					ArrayList<Player> playerNextTurn_arr = new ArrayList<>();
					playerNextTurn_arr.add(new Player("ChangePlayerTurnForOpponent"));
					playerNextTurn_arr.add(arrayListPlayer.get(1));
					playerNextTurn_arr.add(arrayListPlayer.get(2));
					
					player1.sendToClient(playerNextTurn_arr);
					player2.sendToClient(playerNextTurn_arr);
					
					break;
					
				case "PlayerExitedFromActiveGame":
                	// 1 - the player exited the game
                	
                	ArrayList<Player> exitedfromgame_arr = new ArrayList<>();
                	exitedfromgame_arr.add(new Player("OpponentExitedFromYourGame"));
                	exitedfromgame_arr.add(arrayListPlayer.get(1));
                	
                	if(player1id.equals(arrayListPlayer.get(1).getPlayerId())) {
                		player2.sendToClient(exitedfromgame_arr);
                	}
                	else {
                		player1.sendToClient(exitedfromgame_arr);
                	}
                	
                	player1id = null;
                	player2id = null;
                	player1 = null;
                	player2 = null;
                	playeridCounter = 0;
                	playersReady = 0;
                	gameOn = false;
                	
                	client.sendToClient("exited from the game succesfully");
                	
                	break;
                	
				case "PlayerWonWithMate":
					// 1 - the player who won with mate
					
					if(player1id.equals(arrayListPlayer.get(1).getPlayerId())) {
						player1.sendToClient("GameEndedPlayerWon");
					}
					else {
						player2.sendToClient("GameEndedPlayerWon");
					}
					
                	player1id = null;
                	player2id = null;
                	player1 = null;
                	player2 = null;
                	playeridCounter = 0;
                	playersReady = 0;
                	gameOn = false;
					
					client.sendToClient("message sent to the opponent that he won");
					
					
					
					
					
					break;
					
            }
    	
        }catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
        }
        
	}
	
	
	public static void startGame() {
		
        Thread printThread = new Thread(() -> {
            for (int i = 1; i <= 3; i++) { // Print for 3 seconds
                System.out.println("Printing something - Second " + i);
                try {
                    Thread.sleep(1000); // Sleep for 1 second
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        printThread.start();
        
		
	    gameTimerPlayer2 = new GameTimer("timer1", 60 * 5); // Set the initial timer duration to 5 minutes
	    gameTimerPlayer2.start();
	    gameTimerPlayer1 = new GameTimer("timer2", 60 * 5);
	    gameTimerPlayer1.start();
	    gameTimerPlayer1.pause();
	    
	    gameOn = true;
	    
	    
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
						// 3 - playerid
						ServerController.removeConnectedClientFromTable(arrayListStr.get(1), arrayListStr.get(2), arrayListStr.get(3)); // call function to remove the client from the table
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
	
    public static void notifyTimerUpdate(String timerName, int remainingTime) {
    	
    	
        // Create a message indicating a timer update and include the remaining time
        ArrayList<String> timerUpdateMessage = new ArrayList<>();
        timerUpdateMessage.add("TimerUpdate");
        timerUpdateMessage.add(timerName);
        timerUpdateMessage.add(Integer.toString(remainingTime));

        // Send the timer update message to all connected clients
        
        try {
        	
	        if(gameOn) {
		        if(timerName.equals("timer1")) {
					player2.sendToClient(timerUpdateMessage);
					if(gameTimerPlayer2.getTime() == 0) {
						player2.sendToClient("GameEndedPlayerWon");
						gameTimerPlayer2.setTime(5);
						gameTimerPlayer1.setTime(5);
					}
		        }
		        else {
		        	player1.sendToClient(timerUpdateMessage);
					if(gameTimerPlayer2.getTime() == 0) {
						player1.sendToClient("GameEndedPlayerWon");
						gameTimerPlayer2.setTime(5);
						gameTimerPlayer1.setTime(5);
					}
		        }
		    }
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
    }
    
}
