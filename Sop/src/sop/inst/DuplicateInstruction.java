package sop.inst;

import sop.SopEnvironment;

public class DuplicateInstruction implements SopInstruction {

	@Override
	public void execute(SopEnvironment environment) {
		int n = environment.pop();
		environment.push(n);
		environment.push(n);
	}

	@Override
	public String toString() {
		return "+";
	}
}
