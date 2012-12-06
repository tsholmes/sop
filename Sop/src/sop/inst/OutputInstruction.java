package sop.inst;

import java.io.IOException;

import sop.SopEnvironment;

public class OutputInstruction implements SopInstruction {

	@Override
	public void execute(SopEnvironment environment) {
		int val = environment.pop();
		try {
			environment.getOut().write(val);
			environment.getOut().flush();
		} catch (IOException ex) {
			throw new RuntimeException("Output Error", ex);
		}
	}

	@Override
	public String toString() {
		return ".";
	}
}
