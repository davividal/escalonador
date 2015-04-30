package cpu;

import java.util.Collections;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Random;

import queue.GreatestPid;
import queue.Queue;
import queue.RoundRobin;
import queue.SRT;

public class Stack {
	private static final Integer MAX_PROCESSES = 20;
	private static final Integer MAX_PROCESSES_ROUND = 5;

	protected LinkedList<Process> queue = new LinkedList<Process>();
	protected LinkedList<Process> ready = new LinkedList<Process>();
	protected LinkedList<Process> ran = new LinkedList<Process>();
	protected LinkedList<Process> blocked = new LinkedList<Process>();
	protected LinkedList<Process> finished = new LinkedList<Process>();
	protected Hashtable<Process, Process.Status> roundProcesses;
	protected Integer cpus;
	protected Integer processes = 0;
	protected Integer round = 1;

	protected Queue[] sorters = {new RoundRobin(), new SRT(), new GreatestPid()};

	public Stack() {
		this.cpus = (new Integer[]{1,2,4,8})[(new Random()).nextInt(3)];
	}

	public Integer getCpus() {
		return this.cpus;
	}

	protected void createProcesses() {
		Integer newProcesses = (new Random()).nextInt(Stack.MAX_PROCESSES_ROUND) + 1;
		for (int i = 0; i < newProcesses && this.processes < Stack.MAX_PROCESSES; i++) {
			if (this.processes < Stack.MAX_PROCESSES) {
				Process process = new Process(++this.processes, this.round);
				this.queue.add(process);
			}
		}
	}

	// http://www.coderanch.com/t/601882/java/java/Multiple-comparators
	protected void sortQueue() {
		for (Queue sorter : this.sorters) {
			Collections.sort(this.ready, sorter);
		}

		this.queue.addAll(this.ready);
	}

	protected LinkedList<Process> readyProcesses() {
		LinkedList<Process> readyProcesses = new LinkedList<Process>();

		try {
			for (int i = 0; i < this.cpus; i++) {
				readyProcesses.add(this.queue.remove());
			}
		} catch (NoSuchElementException ne) {
			// Queue empty
		}

		return readyProcesses;
	}

	public void run() {
		this.roundProcesses = new Hashtable<Process, Process.Status>();

		this.sortQueue();

		for (Process process: this.readyProcesses()) {
			process.run();
			this.ready.remove(process);
			this.queue.add(process);
			this.ran.add(process);
			roundProcesses.put(process, Process.Status.RUNNING);
		}

		this.createProcesses();

		for (Process process: this.queue) {
			roundProcesses.putIfAbsent(process, Process.Status.READY);
		}

		for (Process process: this.finished) {
			roundProcesses.putIfAbsent(process, Process.Status.FINISHED);
		}

		for (Process process: this.ran) {
			roundProcesses.putIfAbsent(process, Process.Status.RUNNING);
		}

		for (Process process: this.blocked) {
			roundProcesses.putIfAbsent(process, Process.Status.BLOCKED);
		}

		Iterator<Process> qi = this.queue.iterator();
		while (qi.hasNext()) {
			Process process = qi.next();

			if (process.isBlocked()) {
				process.setBlocked();

				qi.remove();
				this.blocked.add(process);
			} else if (process.isFinished()) {
				process.setFinished();

				qi.remove();
				this.finished.add(process);
			} else {
				process.setReady();
			}
		}

		Iterator<Process> bi = this.blocked.iterator();
		while (bi.hasNext()) {
			Process process = bi.next();

			if (!process.isBlocked()) {
				process.setReady();

				bi.remove();
				this.queue.add(process);
			}
		}
	}

	public LinkedList<Process> getBlocked() {
		return blocked;
	}

	public LinkedList<Process> getQueue() {
		return queue;
	}

	public LinkedList<Process> getReady() {
		return ready;
	}

	public Integer getProcesses() {
		return processes;
	}

	public Integer getRound() {
		return round;
	}

	public LinkedList<Process> getFinished() {
		return this.finished;
	}

	public Hashtable<Process, Process.Status> getRoundProcesses() {
		return this.roundProcesses;
	}
}
