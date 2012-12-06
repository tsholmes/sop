package sop.inst;

import sop.SopEnvironment;

public class PullInstruction implements SopInstruction {

	@Override
	public void execute(SopEnvironment environment) {
		int n = environment.pop();
		environment.pull(n);
	}

	@Override
	public String toString() {
		return "^";
	}
}
