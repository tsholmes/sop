package sop.inst;

import java.io.IOException;

import sop.SopEnvironment;

public class InputInstruction implements SopInstruction {

	@Override
	public void execute(SopEnvironment environment) {
		int val = 0;
		try {
			val = environment.getIn().read();
		} catch (IOException ex) {
			throw new RuntimeException("Input Error", ex);
		}
		environment.push(val);
	}

	@Override
	public String toString() {
		return ",";
	}
}
