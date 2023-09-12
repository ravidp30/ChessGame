import java.util.Timer;
import java.util.TimerTask;

public class GameTimer {
    private int secondsRemaining;
    private Timer timer;
    private boolean isPaused;

    public GameTimer(int initialSeconds) {
        this.secondsRemaining = initialSeconds;
        this.timer = new Timer();
        this.isPaused = false;
    }

    public void start() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!isPaused) {
                    if (secondsRemaining <= 0) {
                        stop();
                        // Trigger game over or any other relevant action
                        System.out.println("Time's up!");
                    } else {
                        // Update game logic here as time progresses
                        System.out.println("Seconds remaining: " + secondsRemaining);
                        secondsRemaining--;
                    }
                }
            }
        }, 0, 1000); // Schedule the task to run every 1000 milliseconds (1 second)
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
    
    public String toString() {
    	return "" + secondsRemaining + " : " + secondsRemaining/60;
    }

    
}
