package sop;

import java.io.File;
import java.io.FileInputStream;

import sop.gui.SopFrame;
import sop.inst.SopInstruction;

public class Main {
	public static void main(String[] args) throws Exception {
		boolean headless = false;
		String filename = null;
		for (String arg : args) {
			if (arg.equals("--headless")) {
				headless = true;
			} else {
				File file = new File(arg);
				if (file.exists()) {
					filename = arg;
				}
			}
		}
		if (headless) {
			if (filename == null) {
				filename = "helloworld.sop";
			}
			SopParser parser = new SopParser(new FileInputStream(new File(filename)));
			SopInstruction[] instructions = parser.parse();
			
			SopInterpreter interpreter = new SopInterpreter(instructions, System.in, System.out);
			while (!interpreter.isDone()) {
				interpreter.executeInstruction();
			}
		} else {
			new SopFrame();
		}
	}
}
