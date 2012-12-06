package sop.inst;

import sop.SopEnvironment;

public class NumericInstruction implements SopInstruction {
	private int value;
	
	public NumericInstruction(int value) {
		this.value = value;
	}

	@Override
	public void execute(SopEnvironment environment) {
		environment.push(value);
	}

	@Override
	public String toString() {
		return value + "";
	}
}
