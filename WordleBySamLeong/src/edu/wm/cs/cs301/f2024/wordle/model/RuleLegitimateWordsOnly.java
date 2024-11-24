package edu.wm.cs.cs301.f2024.wordle.model;

import java.util.List;

public class RuleLegitimateWordsOnly extends RuleBasic implements AcceptanceRule{

	@Override
	public boolean isAcceptableGuess(Model model) {
		
		if (!super.isAcceptableGuess(model)) {
            return false;
		}
		
		List<String> wordList = model.getWordList();
		String guess = model.getCurrentGuess().toLowerCase();
		if (wordList.contains(guess)) {
			return true;
		}
		return false;
	}

}
