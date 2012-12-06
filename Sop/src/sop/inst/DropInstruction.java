package sop.inst;

import sop.SopEnvironment;

public class DropInstruction implements SopInstruction {

	@Override
	public void execute(SopEnvironment environment) {
		int n = environment.pop();
		for (int i = 0; i < n; i++) {
			environment.pop();
		}
	}

	@Override
	public String toString() {
		return "-";
	}
}
