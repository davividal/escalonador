package queue;

public class LeatestPid extends Queue {
	@Override
	public int compare(cpu.Process o1, cpu.Process o2) {
		return o2.getPid() - o1.getPid();
	}
}
