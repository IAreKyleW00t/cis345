/**
 * Name:			Kyle Colantonio, Nick Vladimiroff
 * Project:			1
 * Description:		Process Scheduler/Management simulation.
 * Date:			2-23-2015
 **/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
	/* Global variables */
	private static final File PROCESSES_FILE = new File("processes.txt");
	private static Queue WAIT_Q, READY_Q;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(PROCESSES_FILE));
		String line; int count = 0;
		
		System.out.println("Loading processes from \"" + PROCESSES_FILE.getName() + "\"...");
		
		/* Read each line in PROCESSES_FILE */
		while ((line = br.readLine()) != null) {
			int pid; char state; List<Integer> run, wait;
			String[] str = line.split(" "); //split line by space

			/* Parse input from array */
			pid = Integer.parseInt(str[0]);
			state = str[1].charAt(0);
			run = new ArrayList<Integer>();
			wait = new ArrayList<Integer>();
			
			/* Parse times for READY and BLOCKED processes */
			if (state == 'R') {
				for (int i = 2; i < str.length; i++) {
					if (i % 2 == 0)
						run.add(Integer.parseInt(str[i]));
					else
						wait.add(Integer.parseInt(str[i]));
				}
			} else if (state == 'B') {
				for (int i = 2; i < str.length; i++) {
					if (i % 2 == 0)
						wait.add(Integer.parseInt(str[i]));
					else
						run.add(Integer.parseInt(str[i]));
				}
			}
 			
			/* Create new Process object */
			Process ps = new Process(pid, state, run, wait);
			
			/* Move the process into it's initial queue */
			if (ps.getState() == 'R') { //Ready-to-run
				Stack.READY_Q.add(ps);
			} else if (ps.getState() == 'B') { //Blocked
				Stack.WAIT_Q.add(ps);
			}
			count++; //keep track of the number of processes
			
			System.out.println("\t" + ps); //Debug
		} //end-while

		br.close(); //Close BufferedReader
		System.out.println(count + " processes loaded!\n");
		
		System.out.println("Running processes...");
		long start = System.currentTimeMillis();
		
		/* Create and start the Queue's */
		WAIT_Q = new Queue(new Wait()); //Wait Queue Thread
		READY_Q = new Queue(new Ready()); //Ready Queue Thread
		WAIT_Q.start();
		READY_Q.start();
		
		/* Running Thread */
		while (Stack.isEmpty() == false) {
			Iterator<Process> iterator = Stack.EXEC_Q.iterator();
			
			if (iterator.hasNext()) {
				Process ps = iterator.next();
				if (ps == null) {
					continue; //Skip any null processes (shouldn't happen?)
				}
				
				System.out.println("\tPID " + ps.getPid() + " --> RUNNING");
				boolean done = ps.run();
				
				if (ps.isDoneRunning()) { //Process is completely done running
					System.out.println("\tPID " + ps.getPid() + " --> FINISHED");
					Stack.FINISH_Q.add(Stack.FINISH_Q.size(), ps);
					iterator.remove();
				} else if (done) { //Process is done running it's iteration
					System.out.println("\tPID " + ps.getPid() + " --> BLOCKED");
					Stack.WAIT_Q.add(Stack.WAIT_Q.size(), ps);
					iterator.remove();
				} else { //Process was not completed
					System.out.println("\tPID " + ps.getPid() + " --> READY");
					Stack.READY_Q.add(Stack.READY_Q.size(), ps);
					iterator.remove();
				}
			}
		} //end-while

		long end = System.currentTimeMillis();
		System.out.println("Successfully ran " + count + " processes in " + ((end - start) / 1000.0) + "s");
		System.exit(0);
	}
}
