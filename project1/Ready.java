import java.util.Iterator;

public class Ready implements Runnable {
	private volatile boolean RUNNING = true;
	
	@Override
	public void run() {
		while (RUNNING) {
			try {
				Thread.sleep(100); //1ms offset to prevent concurrency issues
				
				Iterator<Process> iterator = Stack.READY_Q.iterator();
				if (iterator.hasNext()) {
					Process	p = iterator.next();
					
					if (Stack.EXEC_Q.isEmpty()) {
						Stack.EXEC_Q.add(p);
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
