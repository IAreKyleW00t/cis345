Project 1
---------
#### Description
  Simulate a process scheduler/manager by moving a `READY` process into `RUNNING` if nothing is already running, and
  some sort of "waiting" by moving the process to `BLOCKED`.
  
  Read each process from `processes.txt` line by line using the format `pid R|B time time ... time`. R indicates the process
  will start in the `READY` queue and B indicates it will start in the `WAIT` (Blocked) queue.
  
  Each process will run for up to 5 seconds. If any time is remaining (ie: the process did not run it's full iteration)
  then the process will be moved to the bottom of the `READY` queue, otherwise it will be moved to the `WAIT` queue and
  wait for the given wait period.
  
  Each queue will run on a separate thread. `WAIT_Q` will iterate through each waiting process every second and move them to
  the `READY_Q` when they are done. `READY_Q` will iterate every 100ms (to prevent concurrency issues) to check if it can add
  move a process into the `RUNNING_Q`. `RUNNING_Q` will execute the process on the `main` thread and then move it into the
  corresponding queue afterwards.
  
  Once all processes have been moved into the `FINISHED_Q`, the program will exit.
  
#### Example `processes.txt`
  ```
  1234 R 3 1 3 7 5
  4321 B 8 1 7 4
  ```
  PID 1234 will start in `READY` and run for 3 seconds, then wait for 1 second, then run for 3 seconds again, and wait for 7,
  then finally run for 5 seconds. PID 4321 will start in `WAIT` (Blocked) for 8 seconds, then run for 1 second, wait for 7
  more seconds, and finally run for 4 seconds.

#### Example Output
  ```
  Loading processes from "processes.txt"...
  	1234 run=[3, 3, 5] wait=[1, 7]
  	4321 run=[1, 4] wait=[8, 7]
  2 processes loaded!
  
  Running processes...
  	[Thread-1] PID 1234 --> RUNNING
  		 >  PID 1234 running on main
  	[main]     PID 1234 --> BLOCKED
  	[Thread-0] PID 1234 --> READY
  	[Thread-1] PID 1234 --> RUNNING
  		 >  PID 1234 running on main
  	[main]     PID 1234 --> BLOCKED
  	[Thread-0] PID 4321 --> READY
  	[Thread-1] PID 4321 --> RUNNING
  		 >  PID 4321 running on main
  	[main]     PID 4321 --> BLOCKED
  	[Thread-0] PID 1234 --> READY
  	[Thread-1] PID 1234 --> RUNNING
  		 >  PID 1234 running on main
  	[Thread-0] PID 4321 --> READY
  	[main]     PID 1234 --> READY
  	[Thread-1] PID 4321 --> RUNNING
  		 >  PID 4321 running on main
  	[main]     PID 4321 --> READY
  	[Thread-1] PID 1234 --> RUNNING
  		 >  PID 1234 running on main
  	[main]     PID 1234 --> FINISHED
  	[Thread-1] PID 4321 --> RUNNING
  		 >  PID 4321 running on main
  	[main]     PID 4321 --> FINISHED
  Successfully ran 2 processes in 23.32s
  ```
