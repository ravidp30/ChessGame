package handlers;

import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {
    private int secondsRemaining;
    private Timer timer;
    private boolean isPaused;
    private String timerName;

    public GameTimer(String timerName, int initialSeconds) {
        this.secondsRemaining = initialSeconds;
        this.timer = new Timer();
        this.isPaused = false;
        this.timerName = timerName;
    }

    public void start() {
        timer.scheduleAtFixedRate(createTimerTask(), 0, 1000); // Schedule the task to run every 1000 milliseconds (1 second)
    }

    public void stop() {
        timer.cancel();
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }
    
    public int getTime() {
    	return secondsRemaining;
    }
    
    public String toString() {
    	return "" + secondsRemaining + " : " + secondsRemaining/60;
    }
    
    private TimerTask createTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                if (!isPaused && secondsRemaining > 0) {
                    // Notify the server about the remaining time
                    MessageHandler_Server.notifyTimerUpdate(timerName, secondsRemaining);
                    secondsRemaining--;
                } else if (secondsRemaining == 0) {
                    stop();
                    // Trigger game over or any other relevant action
                    System.out.println("Time's up!");
                }
            }
        };
    }

	public void setTime(int i) {
		secondsRemaining = i;
		
	}

    
}

