public class Queue extends Thread {
	
	public Queue(Runnable r) {
		super(r);
	}

	public final void start() {
		super.start();
	}
	
	public final void kill() {
		super.interrupt();
	}
}
