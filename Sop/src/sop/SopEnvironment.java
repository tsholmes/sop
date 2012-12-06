package sop;

import java.io.InputStream;
import java.io.OutputStream;

public class SopEnvironment {
	private InputStream in;
	private OutputStream out;
	
	private int[] stack;
	private boolean[] used;
	private int stackpos = 0;
	private int stackcount = 0;
	
	private int expos;
	
	public SopEnvironment(InputStream in, OutputStream out) {
		this.in = in;
		this.out = out;
		
		stack = new int[100];
		used = new boolean[100];
	}
	
	public InputStream getIn() {
		return in;
	}
	
	public OutputStream getOut() {
		return out;
	}
	
	public int getExPos() {
		return expos;
	}
	
	public int deltaExPos(int delta) {
		if (expos + delta < 0) {
			throw new RuntimeException("Bad Branch");
		}
		expos += delta;
		return expos;
	}
	
	public int pop() {
		if (stackcount > 0) {
			int result = stack[--stackpos];
			used[stackpos] = false;
			while (stackpos > 0 && !used[stackpos-1]) {
				stackpos--;
			}
			stackcount--;
			return result;
		} else {
			throw new RuntimeException("Stack empty");
		}
	}
	
	public void push(int value) {
		if (stackpos == stack.length - 1) {
			int[] newstack = new int[stack.length * 2];
			boolean[] newused = new boolean[stack.length * 2];
			int newpos = 0;
			for (int i = 0; i < stackpos; i++) {
				if (!used[i]) continue;
				 newstack[newpos] = stack[i];
				 newused[newpos++] = true;
			}
			stack = newstack;
			used = newused;
			stackpos = newpos;
		}
		used[stackpos] = true;
		stack[stackpos++] = value;
		stackcount++;
	}
	
	public void pull(int pos) {
		if (pos >= stackcount) {
			throw new RuntimeException("Out of bounds");
		}
		if (stackpos == stack.length - 1) {
			int[] newstack = new int[stack.length * 2];
			boolean[] newused = new boolean[stack.length * 2];
			int newpos = 0;
			for (int i = 0; i <= stackpos; i++) {
				if (!used[i]) continue;
				 newstack[newpos] = stack[i];
				 newused[newpos++] = true;
			}
			stack = newstack;
			used = newused;
			stackpos = newpos;
		}
		
		int down = 0;
		int pullpos = stackpos;
		while (down < pos) {
			if (used[pullpos]) {
				down++;
			}
			pullpos--;
		}
		while (!used[pullpos]) {
			pullpos--;
		}
		used[stackpos] = true;
		stack[stackpos++] = stack[pullpos];
		used[pullpos] = false;
	}
	
	public int[] getStack() {
		int[] result = new int[stackcount];
		int pos = 0;
		for (int i = 0; i < stackpos; i++) {
			if (used[i]) {
				result[pos++] = stack[i];
			}
		}
		return result;
	}
}
