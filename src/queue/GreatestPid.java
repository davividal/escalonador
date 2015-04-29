package queue;

public class GreatestPid extends Queue {
	@Override
	public int compare(cpu.Process o1, cpu.Process o2) {
		return o1.getPid() - o2.getPid();
	}
}
