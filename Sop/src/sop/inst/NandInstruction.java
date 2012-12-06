package sop.inst;

import sop.SopEnvironment;

public class NandInstruction implements SopInstruction {

	@Override
	public void execute(SopEnvironment environment) {
		int a = environment.pop();
		int b = environment.pop();
		environment.push(~(a & b));
	}

	@Override
	public String toString() {
		return "~";
	}
}
