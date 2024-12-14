package edu.wm.cs.cs301.f2024.wordle.model;

import java.util.Random;

public class SwitchRandomly implements SwitchStrategy{

	int n;
	
	public SwitchRandomly() {
		Random random = new Random();
		n = random.nextInt(1, 6); // a number 1-5
	}
	@Override
	public boolean switchFromAbsurdleToWordle(int currentRow, int lengthCurrentWordList) {
		if (currentRow == n) { // after the users third guess
			return true;
		}
		return false;
	}
	
	public int getN() {
		return n;
	}
}
