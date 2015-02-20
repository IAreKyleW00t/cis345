/**
 * Name:			Kyle Colantonio
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
	private static final File DATA_FILE = new File("processes.txt");
	private static Thread WAIT_THREAD, READY_THREAD;

	/**
	 * Main Method
	 * 
	 * Parses data from "processes.txt" and saves each process into the corresponding
	 * List. Creates each thread and then starts them when ready.
	 * 3 Threads total: Running (Main Thread), Waiting (Thread-0), Ready (Thread-1).
	 */
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(DATA_FILE));
		String line; int count = 0;
		
		System.out.println("Loading processes from \"" + DATA_FILE.getName() + "\"...");
		
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
				Queue.READY_Q.add(ps);
			} else if (ps.getState() == 'B') { //Blocked
				Queue.WAIT_Q.add(ps);
			}
			count++; //keep track of the number of processes
			
			System.out.println("\t" + ps); //Debug
		} //end-while

		br.close(); //Close BufferedReader
		System.out.println(count + " processes loaded!\n");
		
		System.out.println("Running processes...");
		long start = System.currentTimeMillis(); //Start time
		
		/*
		 * Creates a WAIT_THREAD which will iterate through the loop
		 * every 1000ms (1 second). It will decrement the wait counter
		 * for each process and then remove move it to READY_Q when the counter
		 * reaches 0.
		 */
		WAIT_THREAD = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(1000); //1000ms iteration
						
						Iterator<Process> iterator = Queue.WAIT_Q.iterator();
						while (iterator.hasNext()) {
							Process p = iterator.next();
							boolean done = p.sleep();
							
							if (done) {
								System.out.println("\t[" + Thread.currentThread().getName() + "] PID " + p.getPid() + " --> READY");
								Queue.READY_Q.add(Queue.READY_Q.size(), p);
								iterator.remove();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}); //Wait Queue Thread
		
		/*
		 * Creates a READY_THREAD which checks every 100ms if there is a process
		 * running. If not, it will move the 0th process in the READY_Q to the RUNNING_Q.
		 */
		READY_THREAD = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(100); //100ms wait time to prevent concurrency issues
						
						Iterator<Process> iterator = Queue.READY_Q.iterator();
						if (iterator.hasNext()) {
							Process	p = iterator.next();
							
							if (Queue.EXEC_Q.isEmpty()) {
								System.out.println("\t[" + Thread.currentThread().getName() + "] PID " + p.getPid() + " --> RUNNING");
								Queue.EXEC_Q.add(p);
								iterator.remove();
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}); //Ready Queue Thread
		
		/* Start Threads */
		WAIT_THREAD.start(); READY_THREAD.start();
		
		/* Running Thread */
		while (Queue.isEmpty() == false) {
			Iterator<Process> iterator = Queue.EXEC_Q.iterator();
			
			if (iterator.hasNext()) {
				Process ps = iterator.next();
				if (ps == null) {
					continue; //Skip any null processes (shouldn't happen?)
				}
				
				/* Run the process */
				System.out.println("\t\t >  PID " + ps.getPid() + " running on " + Thread.currentThread().getName());
				boolean done = ps.run();
				
				if (ps.isDoneRunning()) { //Process is completely done running
					System.out.println("\t[" + Thread.currentThread().getName() + "]     PID " + ps.getPid() + " --> FINISHED");
					Queue.FINISH_Q.add(Queue.FINISH_Q.size(), ps);
					iterator.remove();
				} else if (done) { //Process is done running it's iteration
					System.out.println("\t[" + Thread.currentThread().getName() + "]     PID " + ps.getPid() + " --> BLOCKED");
					Queue.WAIT_Q.add(Queue.WAIT_Q.size(), ps);
					iterator.remove();
				} else { //Process was not completed
					System.out.println("\t[" + Thread.currentThread().getName() + "]     PID " + ps.getPid() + " --> READY");
					Queue.READY_Q.add(Queue.READY_Q.size(), ps);
					iterator.remove();
				}
			}
		} //end-while

		long end = System.currentTimeMillis(); //End time
		System.out.println("Successfully ran " + count + " processes in " + ((end - start) / 1000.0) + "s");
		System.exit(0);
	}
}
