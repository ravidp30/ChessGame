package handlers;

import java.util.ArrayList;
import java.util.Map;

import ClientAndServerLogin.ClientConnectController;


public class MessageHandler_Client {

	private static int playerId = -1;

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
	        default:
	            //System.out.println("Message type does not exist");
	            break;
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
	            	
	            	playerId = Integer.parseInt((String) arrayList.get(1));
	            	
	            	ClientConnectController.saveIdForPlayer(playerId);
	            	
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