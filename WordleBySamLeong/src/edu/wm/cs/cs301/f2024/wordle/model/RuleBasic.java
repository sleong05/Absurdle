package edu.wm.cs.cs301.f2024.wordle.model;

public class RuleBasic implements AcceptanceRule{

	@Override
	public boolean isAcceptableGuess(Model model) {
		String guess = model.getCurrentGuess();
		if (guess.length() == 5) {
			return true;
		}
		return false;
	}

}
