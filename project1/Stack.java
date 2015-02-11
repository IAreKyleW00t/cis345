import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum Stack {
	INSTANCE; //Single instance
	
	/* Global Synchronized Lists */
	public static List<Process> READY_Q = Collections.synchronizedList(new ArrayList<Process>());
	public static List<Process> WAIT_Q = Collections.synchronizedList(new ArrayList<Process>());
	public static List<Process> EXEC_Q = Collections.synchronizedList(new ArrayList<Process>());
	public static List<Process> FINISH_Q = Collections.synchronizedList(new ArrayList<Process>());
	
	/**
	 * Determines if all the lists (excluding the FINISHED_Q) are
	 * empty.
	 */
	public static final boolean isEmpty() {
		return READY_Q.isEmpty() && WAIT_Q.isEmpty() && EXEC_Q.isEmpty();
	}
}
