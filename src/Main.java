import java.util.Enumeration;
import java.util.Hashtable;

import cpu.Process.Status;
import cpu.CPU;
import cpu.Process;

public class Main {

	public static void main(String[] args) {
		CPU stack = new CPU();
		
		System.out.println(stack.getCores() + " CPUs");
		
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
		nextRound(stack);
	}
	
	public static void nextRound(CPU stack) {
		System.out.println("Round: " + stack.getRound());
		stack.run();
		
		Enumeration<Process> processes = stack.getRoundProcesses().keys();
		while (processes.hasMoreElements()) {
		    Process process = processes.nextElement();
		    Process.Status status = stack.getRoundProcesses().get(process);
		    
		    switch (status) {
			case FINISHED:
				System.out.println("PID: " + process.getPid() + "; Status: " + status);
				break;
			
			case BLOCKED:
				System.out.println("PID: " + process.getPid() + "; Status: " + status + " Blocked: " + process.roundsBlocked());
				break;
				
			default:
				System.out.println("PID: " + process.getPid() + "; Status: " + status + " ETA: " + process.remaning());
				break;
			}
		}
	}
}
