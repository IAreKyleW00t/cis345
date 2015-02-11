import java.util.Iterator;

public class Wait implements Runnable {
	private volatile boolean RUNNING = true;
	
	@Override
	public void run() {
		while (RUNNING) {
			try {
				Thread.sleep(1000);
				
				Iterator<Process> iterator = Stack.WAIT_Q.iterator();
				while (iterator.hasNext()) {
					Process p = iterator.next();
					boolean done = p.sleep();
					
					if (done) {
						System.out.println("\tPID " + p.getPid() + " --> READY");
						Stack.READY_Q.add(Stack.READY_Q.size(), p);
						iterator.remove();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				RUNNING = false;
			}
		}
	}
	
	public void terminate() {
		RUNNING = false;
	}
}
