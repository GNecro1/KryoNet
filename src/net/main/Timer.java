package net.main;

public class Timer {

	public long miliSeconds, targetTime;
	public long now;

	public boolean done = false;
	private boolean start = false;
	private Player p;

	public Timer(double sec, Player p) {
		this.p = p;
		miliSeconds = (long) (sec * 1000);

	}

	public void tick() {
		if (System.currentTimeMillis() >= targetTime && start) {
			done = true;
		}
	}
	
	public void start(){
		now = System.currentTimeMillis();
		targetTime = now + miliSeconds;
		start  = true;
	}
	
	public void reset() {
		now = System.currentTimeMillis();
		targetTime = now + miliSeconds;
		done = false;
	}

	public boolean Ring() {
		return done;
	}

}
