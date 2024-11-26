package edu.wm.cs.cs301.f2024.wordle.model;

import java.util.List;

public class RuleLegitimateWordsOnly extends RuleBasic implements AcceptanceRule{

	@Override
	public boolean isAcceptableGuess(Model model) {
		
		if (!super.isAcceptableGuess(model)) {
            return false;
		}
		
		List<String> wordList = model.getWordList();
		char[] guess = model.getCurrentGuess();
		String guessAsString = AppStrings.EMPTY;
		for (char letter: guess) {
			guessAsString += letter;
		}
		if (wordList.contains(guessAsString.toLowerCase())) {
			return true;
		}
		return false;
	}

}
