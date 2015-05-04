package queue;

import cpu.Process;

public class SPF extends Queue {
	@Override
	public int compare(Process o1, Process o2) {
		// P2 ETA 12 - P1 ETA 10 = -2 = P2
		return o2.getEta() - o1.getEta();
	}
}
