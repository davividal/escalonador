package cpu;

import java.util.Random;

public class Process {
	private static final Integer MAX_ETA = 20;
	private static final Integer MIN_ETA = 10;

	public enum Status {
		RUNNING, BLOCKED, READY, FINISHED
	}

	protected Integer pid;
	protected Integer ran;
	protected Integer eta;
	protected Status status;
	protected Integer created_at;
	protected Integer blocked = 0;

	public Process(Integer pid, Integer created_at) {
		this.pid = pid;
		this.ran = 0;
		this.created_at = created_at;
		this.status = Status.READY;

		this.eta = (new Random()).nextInt((MAX_ETA - MIN_ETA) + 1) + MIN_ETA;
	}

	public Status run() {
		if (this.isFinished()) {
			this.setFinished();
			return Status.FINISHED;
		}

		this.ran++;
		this.status = Status.RUNNING;

		return this.status;
	}

	public Boolean isBlocked() {
		if (this.status == Status.BLOCKED && this.blocked > 0) {
			this.blocked--;

			return true;
		} else if (this.status == Status.RUNNING) {
			if ((new Boolean[]{true, false})[(new Random()).nextInt(2)]) {
				this.status = Status.BLOCKED;

				this.blocked = (new Random()).nextInt(4) + 1;

				return true;
			}
		}

		return false;
	}

	public Boolean isFinished() {
		return (this.remaning() == 0);
	}

	public void setFinished() {
		this.status = Status.FINISHED;
	}

	public void setBlocked() {
		this.status = Status.BLOCKED;
	}

	public void setReady() {
		this.status = Status.READY;
		this.blocked = 0;
	}

	public Integer getPid() {
		return pid;
	}

	public Integer remaning() {
		return this.eta - this.ran;
	}

	public Status getStatus() {
		return status;
	}

	public String getStatus(Status status) {
		switch (status) {
		case RUNNING:
			return "E";

		case BLOCKED:
			return "B";

		case READY:
			return "P";

		case FINISHED:
			return "F";

		default:
			return "-";
		}
	}

	public Integer roundsBlocked() {
		return this.blocked;
	}

	public Integer getEta() {
		return this.eta;
	}
}
