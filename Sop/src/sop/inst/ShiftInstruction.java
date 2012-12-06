package sop.inst;

import sop.SopEnvironment;

public class ShiftInstruction implements SopInstruction {

	@Override
	public void execute(SopEnvironment environment) {
		int s = environment.pop();
		int n = environment.pop();
		s = s & 31;
		/*n = ((n << s) & (((1 << (32 - s)) - 1) << s))
				| ((n >> (32 - s)) & ((1 << s) - 1));*/
		for (int i = 0; i < s; i++) {
			n = ((n >> 1) & Integer.MAX_VALUE) | ((n << 31) & Integer.MIN_VALUE);
		}
		environment.push(n);
	}

	@Override
	public String toString() {
		return ">";
	}
}
