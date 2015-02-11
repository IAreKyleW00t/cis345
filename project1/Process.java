import java.util.ArrayList;
import java.util.List;

public class Process {
	private int pid;
	private char state;
	private List<Integer> run, wait;
	
	/**
	 * Process constructor.
	 */
	public Process(int pid, char state, List<Integer> run, List<Integer> wait) {
		this.pid = pid;
		this.state = state;
		this.run = new ArrayList<Integer>(run);
		this.wait = new ArrayList<Integer>(wait);
	}
	
	/**
	 * Returns the Process ID.
	 */
	public int getPid() {
		return pid;
	}
	
	/**
	 * Returns the initial state of the process.
	 */
	public char getState() {
		return state;
	}
	
	/**
	 * Determines if the process is finished running all of
	 * it's run iterations.
	 */
	public boolean isDoneRunning() {
		return run.isEmpty();
	}
	
	/**
	 * Determines if the process is finished running all of
	 * it's wait iterations. 
	 */
	public boolean isDoneWaiting() {
		return wait.isEmpty();
	}
	
	/**
	 * Performs a Thread.sleep(int) call that causes
	 * the current thread to sleep for 0ms.
	 */
	public boolean sleep() throws Exception {
		return sleep(0);
	}
	
	/**
	 * Performs a Thread.sleep(int) call that causes
	 * the current thread to sleep for Xms.
	 * 
	 * Removes the current iteration from the List once
	 * it is complete.
	 */
	public boolean sleep(int timeout) throws Exception {
		Thread.sleep(timeout);
		wait.set(0, wait.get(0)- 1);
		
		if (wait.get(0) == 0) {
			wait.remove(0);
			return true; //Done waiting
		} else {
			return false; //Not done waiting
		}
	}
	
	/**
	 * Attempts to "run" the process for it current iteration
	 * time. This will only run up to 5 seconds each time, and
	 * finish later if the iteration is not complete.
	 * 
	 * Removes the current iteration from the List once
	 * it is complete.
	 */
	public boolean run() throws Exception {
		int i;
		for (i = 0; i < run.get(0); i++) {
			Thread.sleep(1000);
			
			if (i == 4) { //Stop after 5 iterations
				break;
			}
		}
		if (i == 4) {
			run.set(0, run.get(0) - 5);
			return false; //Run is not complete
		} else {
			run.remove(0);
			return true; //Run completed
		}
	}
	
	public String toString() {
		return pid + " run=" + run.toString() + " wait=" + wait.toString();
	}
}
