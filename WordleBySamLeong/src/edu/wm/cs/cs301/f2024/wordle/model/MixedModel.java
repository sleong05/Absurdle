package edu.wm.cs.cs301.f2024.wordle.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.swing.JButton;

public class MixedModel extends Model{
	// model that can be swapped to wordle
	Model model;
	// strategy for when to swap
	SwitchStrategy strategy;
	// model to be swapped
	WordleModel wordle;
	boolean hasSwapped = false;
	
	public MixedModel(SwitchStrategy strategy) {
		super();
		model = new AbsurdleModel();
		this.strategy = strategy;
		wordle = new WordleModel();
		
	}
	
	private boolean checkSwap() {
		return strategy.switchFromAbsurdleToWordle(model.getCurrentRowNumber() + 1, model.getTotalWordCount());
	}
	private void swapToWordle() {
		System.out.println("Swapping");
		AbsurdleModel absurd = (AbsurdleModel) model;
		HashSet<String> possibleWords = absurd.getPossibleWords();
		
		 List<String> listOfPossibleWords = new ArrayList<>(possibleWords);
		 wordle.overideWordList(listOfPossibleWords); // sets the wordlist and selects a word
		 
		 model = wordle;
	}
	@Override
	public void initialize() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void setCurrentColumn(char c) {
		super.setCurrentColumn(c);
		model.setCurrentColumn(c);
	}
	@Override
	public void backspace() {
		super.backspace();
		model.backspace();
	}
	@Override
	public WordleResponse onceButton() {
		WordleResponse response = model.onceButton();
		wordleGrid[currentRow] = model.getNextRow();
		onceLeft-=1;
		currentColumn++;
		currentColumn = Math.min(currentColumn, (columnCount - 1)); 
		return response;
	}

	@Override
	public WordleResponse twiceButton() {
		WordleResponse response = model.twiceButton();
		wordleGrid[currentRow] = model.getNextRow();
		currentColumn++;
		currentColumn = Math.min(currentColumn, (columnCount - 1));
		twiceLeft-=1;
		return response;
	}

	@Override
	public char thriceButton(JButton[] buttons) {
		char c = model.thriceButton(buttons);
		wordleGrid[currentRow] = model.getNextRow();
		currentColumn++;
		currentColumn = Math.min(currentColumn, (columnCount - 1));
		thriceLeft-=1;
		return c;

	}
	

	@Override
	public boolean setCurrentRow() {
		model.setCurrentRow();
		WordleResponse[] updatedRow = model.getCurrentRow();
		wordleGrid[currentRow] = updatedRow;
		currentColumn = -1;
		currentRow++;
		guess = new char[columnCount];
		if (checkSwap() && !hasSwapped) {
			swapToWordle();
			hasSwapped = true;
		}
		return currentRow < maximumRows;
	}

	@Override
	public int getTotalWordCount() {
		return model.getTotalWordCount();
	}

}
