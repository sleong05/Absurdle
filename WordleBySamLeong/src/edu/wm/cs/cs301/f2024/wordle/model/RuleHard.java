package edu.wm.cs.cs301.f2024.wordle.model;

public class RuleHard extends RuleBasic implements AcceptanceRule{

	@Override
	public boolean isAcceptableGuess(Model model) {
		if (!super.isAcceptableGuess(model)) {
            return false;
		}
		
		char[] guess = model.getCurrentGuess(); 
		
		// looks through answers
		WordleResponse[][] wordleGrid = model.getWordleGrid();
		for (int row = 0; row < wordleGrid.length; row++) {
			for (int column = 0; column < wordleGrid[row].length; column++) {
				// stops checks once it hits empty area
				if (wordleGrid[row][column] == null) {
					break;
				}
				WordleResponse response = wordleGrid[row][column];
				// if green make sure the guess has the char in that spot
				if (response.getBackgroundColor() == AppColors.GREEN) {
					if (guess[column] != response.getChar()) {
						return false;
					} 
				// if spot is yellow, ensure that the current spot is not the letter and that the word contains the letter
				} else if (response.getBackgroundColor() == AppColors.YELLOW) {
					if (guess[column] == response.getChar()) {
						return false;
					}
					boolean isIn = false;
					for (char letter: guess) {
						if (letter == response.getChar()) {
							isIn = true;
						}
					}
					if (!isIn) {
						return false;
					}
				// if spot is gray, ensure letter is not in word
				} else if (response.getBackgroundColor() == AppColors.GRAY) {
					for (char letter: guess) {
						if (letter == response.getChar()) {
							return false;
						}
					}
				}
			}
		}
		return true;
	}

}
