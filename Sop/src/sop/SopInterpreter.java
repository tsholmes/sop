package sop;

import java.io.InputStream;
import java.io.OutputStream;

import sop.inst.SopInstruction;

public class SopInterpreter {
	private SopInstruction[] instructions;
	private InputStream input;
	private OutputStream output;
	private SopEnvironment environment;

	public SopInterpreter(SopInstruction[] instructions, InputStream input,
			OutputStream output) {
		this.instructions = instructions;
		this.input = input;
		this.output = output;
		environment = new SopEnvironment(this.input, this.output);
	}
	
	public boolean isDone() {
		return environment.getExPos() >= instructions.length;
	}
	
	public void executeInstruction() {
		if (!isDone()) {
			int pos = environment.getExPos();
			environment.deltaExPos(1);
			instructions[pos].execute(environment);
		}
	}
	
	public int getExPos() {
		return environment.getExPos();
	}
	
	public int[] getStack() {
		return environment.getStack();
	}
}
