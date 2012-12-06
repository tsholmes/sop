package sop.inst;

import sop.SopEnvironment;

public class BranchInstruction implements SopInstruction {

	@Override
	public void execute(SopEnvironment environment) {
		int n = environment.pop();
		int q = environment.pop();
		if (q != 0) {
			environment.deltaExPos(n);
		}
	}

	@Override
	public String toString() {
		return "?";
	}
}
