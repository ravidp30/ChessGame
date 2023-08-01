package config;

public class Player {//dd
	
	private int playerId;
	private int status; // 0 - in lobby, 1 - ready, 2 - in game
	
	public Player(int playerId) {
		this.playerId = playerId;
		status = 0;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
