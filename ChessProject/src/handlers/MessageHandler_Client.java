package handlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import ClientAndServerLogin.ClientConnectController;
import config.Piece;
import config.Player;
import javafx.scene.Node;
import player.GameController;
import player.MenuController;


public class MessageHandler_Client {
	
	private static Player player;

	/**
	 * Handles the received message based on its type.
	 *
	 * @param msg the received message object
	 */
	@SuppressWarnings("unchecked")
	public static void handleMessage(Object msg) {
	    MessageType messageType = getMessageType(msg);
	    if (messageType == null) {
	        return;
	    }

	    switch (messageType) {
	        case STRING:
	            handleStringMessage((String) msg);
	            break;
	        case ARRAY_LIST_STRING:
	            handleStringArrayListMessage((ArrayList<String>) msg);
	            break;
	        case MAP_STRING_ARRAYLIST_STRING:
	            handleMapStringKeyArrayListStringValueMessage((Map<String, ArrayList<String>>) msg);
	            break;
	        case MAP_STRING_STRING:
	            handleMapStringStringValueMessage((Map<String, String>) msg);
	            break;
	            
	        case ARRAY_LIST_PLAYER:
	        	handlePlayerArrayListMessage((ArrayList<Player>) msg);
	            break;
	        case ARRAY_LIST_PIECE:
	        	handlePieceArrayListMessage((ArrayList<Piece>) msg);
	            break;
	        default:
	            //System.out.println("Message type does not exist");
	            break;
	    }
	}



	private static void handlePieceArrayListMessage(ArrayList<Piece> arrayList) {

		ArrayList<Piece> arrayListPiece = (ArrayList<Piece>) arrayList;
		//System.out.println(arrayListStr);
        String messageType = arrayListPiece.get(0).getname();
        switch (messageType) {
		
			case "OponentPieceWasMoved":
				// 1 - Eating or NotEating
				// 2 - old piece
				// 3 - new piece
				System.out.println("moved");
				GameController.getInstance().ChangePieceLocationForOponent(arrayListPiece.get(2), arrayListPiece.get(3), arrayListPiece.get(1));
				break;
		}
		
	}



	private static void handlePlayerArrayListMessage(ArrayList<Player> arrayList) {

        ArrayList<Player> arrayListPlayer = (ArrayList<Player>) arrayList;
		//System.out.println(arrayListStr);
        String messageType = arrayListPlayer.get(0).getPlayerId();
        try {
        	

	        switch (messageType) {
			
				case "GameStarting":
					
					// 1 - the player that starting the game
					// 2 - Opponent player
					
					MenuController.getInstance().startGame(arrayListPlayer.get(2), arrayListPlayer.get(1));
					
					break;
					
				case "ChangePlayerTurnForOpponent":
					// 1 - player with the current turn
					// 2 - in check or not
					GameController.getInstance().changePlayerTurn(arrayListPlayer.get(1), arrayListPlayer.get(2));
					
					
					break;
					
					
					
				case "OpponentExitedFromYourGame":
					// 1 - opponent player exited the game - if needed in the future
					GameController.exitActiveGame(2); // 2 because the player won automatically
					
					
					break;				
					
	        }
        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
				
				
	}
	



	/**
	 * Determines the MessageType based on the type of the message object.
	 *
	 * @param msg the message object
	 * @return the corresponding MessageType
	 */
	private static MessageType getMessageType(Object msg) {
		// Check if the message is a String.
		if (msg instanceof String) {
			// If it is, return the corresponding MessageType.
			return MessageType.STRING;
		}
		// Check if the message is an ArrayList.
		else if (msg instanceof ArrayList) {
			// Cast the message to an ArrayList.
			ArrayList<?> arrayList = (ArrayList<?>) msg;
			// Check if the ArrayList is not empty.
			if (!arrayList.isEmpty()) {
				// Get the first element of the ArrayList.
				Object firstElement = arrayList.get(0);
				// Check the type of the first element and return the corresponding MessageType.
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
		// Check if the message is a Map.
		else if (msg instanceof Map) {
			// Cast the message to a Map.
			Map<?, ?> map = (Map<?, ?>) msg;
			// Check if the Map is not empty.
			if (!map.isEmpty()) {
				// Get the first key and value from the Map.
				Object firstKey = map.keySet().iterator().next();
				Object firstValue = map.get(firstKey);
				// Check the types of the first key and value and return the corresponding MessageType.
				if (firstKey instanceof String && firstValue instanceof ArrayList
						&& ((ArrayList<?>) firstValue).get(0) instanceof String) {
					return MessageType.MAP_STRING_ARRAYLIST_STRING;
				} else if (firstKey instanceof String && firstValue instanceof String) {
					return MessageType.MAP_STRING_STRING;
				}
			}
		}
		}
			
		// If none of the above conditions are met, return null.
		return null;
	}


	/**
	 * Handles the message containing a string value.
	 *
	 * @param message the string message
	 */
	private static void handleStringMessage(String message) {
        // Handle string messages
    	
    	try {
	    	switch (message) {
	    	
	    	case "First Ready":
	    		
	    		MenuController.getInstance().setPlayerReady(1);
	    		
	    		break;
	    		
	    	case "Second Ready":
	    		
	    		MenuController.getInstance().setPlayerReady(1);
	    		
	    		break;
	    		
	    	case "UnReady":
	    		
	    		MenuController.getInstance().setPlayerReady(0);
	    		
	    		break;
	    		
	    	case "too many players are ready":
	    		
	    		System.out.println("Too many players are ready (only 2 can play). please wait...");
	    		
	    		break;
	    		
	    	case "piece moved sucssefully":
	    		
	    		//System.out.println("piece moved sucssefully");
	    		
	    		break;
	    		
	    	case "GameEndedPlayerWon":
	    		
	    		GameController.getInstance().wonTheGameMessage();
	    		
	    		break;
	    		
			case "lostInGame":
				GameController.getInstance().lostNoTimeMessage();
				
				
				break;
	    		
	    	}
	    	
    	}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
    	}
    	
    }

	/**
	 * Handles the message containing an ArrayList of strings.
	 *
	 * @param arrayList the ArrayList of strings
	 */
    @SuppressWarnings("unchecked")
    private static void handleStringArrayListMessage(ArrayList<?> arrayList) {

            ArrayList<String> arrayListStr = (ArrayList<String>) arrayList;
            String messageType = arrayListStr.get(0);
            try {
	            switch (messageType) {
	            case "connected":
	            	
	            	System.out.println("your ID: " + arrayList.get(1));
	            	
	            	player = new Player((String) arrayList.get(1));
	            	
	            	ClientConnectController.saveIdForPlayer(player.getPlayerId());
	            	
	            	break;
	            	
		    	case "ChangePlayersReady":
		    		// 1 - counter of players that are ready
		    		
		    		MenuController.getInstance().changePlayersReady(arrayListStr.get(1));
		    		
		    		break;
		    		
		    	case "GettingMessageFromPlayer":
		    		//  1 - the message sent
		    		
		    		GameController.getInstance().getMessageFromOponent(arrayListStr.get(1));
		    		break;
		    		
	            case "TimerUpdate":
	            	// 1 - timerName
	            	// 2 - time remaining (seconds)
	            	// 3 - time remaining for the opponent
	            	try {
	            		if(GameController.getInstance().getPlayer().getStatus() == 2 && GameController.getInstance().getOpponent().getStatus() == 2)
	            			GameController.getInstance().updateTimer(arrayListStr.get(2), arrayListStr.get(3));
	            	}
	            	catch (NullPointerException e) {
	            		
	            	}
	            	
	            	break;
		    		
	            }    
	            
	            
            }catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
            }
	}

	

	/**
	 * Handles the message containing a Map of String keys to ArrayList of String values.
	 *
	 * @param map the Map of String keys to ArrayList of String values
	 */
    private static void handleMapStringKeyArrayListStringValueMessage(Map<String, ArrayList<String>> map) {
        // Handle Map<String, ArrayList<String>> messages
    		
    	if(map.containsKey("")) {

    	}

    }

	/**
	 * Handles the message containing a Map of String to String values.
	 *
	 * @param map the Map of String to String values
	 */
    private static void handleMapStringStringValueMessage(Map<String, String> map) {
        // Handle the Map<String, String> here

    	
    }



}
