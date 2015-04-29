package queue;

import cpu.Process;

public class RoundRobin extends Queue {
	@Override
	public int compare(Process o1, Process o2) {
		return 0;
	}
}
