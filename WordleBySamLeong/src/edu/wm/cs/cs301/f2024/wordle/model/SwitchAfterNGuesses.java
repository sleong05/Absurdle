package edu.wm.cs.cs301.f2024.wordle.model;

public class SwitchAfterNGuesses implements SwitchStrategy{
	
	int n;
	public SwitchAfterNGuesses(int n) {
		this.n = n;
	}
	@Override
	public boolean switchFromAbsurdleToWordle(int currentRow, int lengthCurrentWordList) {
		if (currentRow == n) { // after the users third guess
			return true;
		}
		return false;
	}

}
