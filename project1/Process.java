import java.util.ArrayList;
import java.util.List;

public class Process {
	private int pid;
	private char state;
	private List<Integer> run, wait;
	
	/**
	 * Process constructor
	 */
	public Process(int pid, char state, List<Integer> run, List<Integer> wait) {
		this.pid = pid;
		this.state = state;
		this.run = new ArrayList<Integer>(run);
		this.wait = new ArrayList<Integer>(wait);
	}
	
	public int getPid() {
		return pid;
	}
	
	public char getState() {
		return state;
	}
	
	public boolean isDoneRunning() {
		return run.isEmpty();
	}
	
	public boolean isDoneWaiting() {
		return wait.isEmpty();
	}
	
	public boolean sleep() throws Exception {
		return sleep(0);
	}
	
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
