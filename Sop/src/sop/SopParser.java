package sop;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

import sop.inst.BranchInstruction;
import sop.inst.DropInstruction;
import sop.inst.DuplicateInstruction;
import sop.inst.InputInstruction;
import sop.inst.NandInstruction;
import sop.inst.NumericInstruction;
import sop.inst.OutputInstruction;
import sop.inst.PullInstruction;
import sop.inst.ShiftInstruction;
import sop.inst.SopInstruction;

public class SopParser {
	private InputStream stream;
	
	public SopParser(InputStream stream) {
		this.stream = stream;
	}
	
	public SopParser(String source) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(output);
		out.print(source);
		this.stream = new ByteArrayInputStream(output.toByteArray());
	}
	
	public SopInstruction[] parse() {
		Scanner in = new Scanner(stream);
		ArrayList<SopInstruction> instructions = new ArrayList<SopInstruction>();
		while (in.hasNext()) {
			String inst = in.next();
			if (inst.equals("^")) {
				instructions.add(new PullInstruction());
			} else if (inst.equals("+")) {
				instructions.add(new DuplicateInstruction());
			} else if (inst.equals(".")) {
				instructions.add(new OutputInstruction());
			} else if (inst.equals(",")) {
				instructions.add(new InputInstruction());
			} else if (inst.equals("?")) {
				instructions.add(new BranchInstruction());
			} else if(inst.equals("-")) {
				instructions.add(new DropInstruction());
			} else if (inst.equals(">")) {
				instructions.add(new ShiftInstruction());
			} else if(inst.equals("~")) {
				instructions.add(new NandInstruction());
			} else if (inst.equals("#")) {
				if (in.hasNextLine()) {
					in.nextLine();
				}
			} else {
				try {
					int val = Integer.parseInt(inst);
					instructions.add(new NumericInstruction(val));
				} catch (NumberFormatException ex) {
					in.close();
					throw new RuntimeException("Invalid Instruction: " + inst);
				}
			}
		}
		in.close();
		return instructions.toArray(new SopInstruction[0]);
	}
}
