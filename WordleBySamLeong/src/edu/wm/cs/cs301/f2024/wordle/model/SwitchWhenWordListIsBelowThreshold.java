package edu.wm.cs.cs301.f2024.wordle.model;

public class SwitchWhenWordListIsBelowThreshold implements SwitchStrategy{
	int threshold;
	// the threshhold
	public SwitchWhenWordListIsBelowThreshold(int n) {
		this.threshold = n;
	}
	@Override
	public boolean switchFromAbsurdleToWordle(int currentRow, int lengthCurrentWordList) {
		if (lengthCurrentWordList < threshold) {
			return true;
		}
		return false;
	}

}
