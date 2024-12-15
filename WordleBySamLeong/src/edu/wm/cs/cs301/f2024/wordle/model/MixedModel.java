package edu.wm.cs.cs301.f2024.wordle.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.swing.JButton;

import edu.wm.cs.cs301.f2024.wordle.controller.ReadWordsRunnable;

public class MixedModel extends Model {
	// model that can be swapped to wordle
	private Model model;
	// strategy for when to swap
	private SwitchStrategy strategy;
	// model to be swapped
	private WordleModel wordle;
	// for grey button case
	private JButton[] buttons;
	// has swapped yet
	boolean hasSwapped = false;
	// logger
	private final static Logger LOGGER = Logger.getLogger(MixedModel.class.getName());

	public MixedModel(SwitchStrategy strategy) {
		super();
		model = new AbsurdleModel();
		this.strategy = strategy;
		wordle = new WordleModel();
		// logger setup
		LOGGER.setLevel(Level.INFO);

		try {
			FileHandler fileTxt = new FileHandler("MixedModelLog");
			fileTxt.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(fileTxt);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean checkSwap() {
		return strategy.switchFromAbsurdleToWordle(model.getCurrentRowNumber() + 1, model.getTotalWordCount());
	}

	private void swapToWordle() {
		System.out.println("Swapping");
		LOGGER.info("Swapping to Wordle");
		AbsurdleModel absurd = (AbsurdleModel) model;
		HashSet<String> possibleWords = absurd.getPossibleWords();

		List<String> listOfPossibleWords = new ArrayList<>(possibleWords);
		wordle.overideWordList(listOfPossibleWords); // sets the wordlist and selects a word

		model = wordle;
		hasSwapped = true;
		logData();
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
		onceLeft -= 1;
		currentColumn++;
		currentColumn = Math.min(currentColumn, (columnCount - 1));
		swapIfThreshold(1);
		return response;
	}

	@Override
	public WordleResponse twiceButton() {
		WordleResponse response = model.twiceButton();
		wordleGrid[currentRow] = model.getNextRow();
		currentColumn++;
		currentColumn = Math.min(currentColumn, (columnCount - 1));
		twiceLeft -= 1;
		swapIfThreshold(2);
		return response;
	}

	@Override
	public char thriceButton(JButton[] buttons) {
		this.buttons = buttons;
		char c = model.thriceButton(buttons);
		thriceLeft -= 1;
		swapIfThreshold(3);
		return c;

	}

	// checks if threshold is reached after a hint button
	private void swapIfThreshold(int button) {
		LOGGER.info("Hint Button has dropped the wordlist to size: " + model.getTotalWordCount());
		if (strategy instanceof SwitchWhenWordListIsBelowThreshold) {
			boolean swap = strategy.switchFromAbsurdleToWordle(model.getCurrentRowNumber() + 1,
					model.getTotalWordCount());
			if (swap && !hasSwapped) {
				WordleResponse[] response = model.getNextRow();
				swapToWordle();
				for (WordleResponse r : response) {
					if (r != null) {
						model.setCurrentColumn(r.getChar());
					}
				}
				switch (button) {
				case 1:
					model.backspace();
					model.onceButton();
					wordleGrid[currentRow] = model.getNextRow();
					break;

				case 2:
					model.backspace();
					model.twiceButton();
					wordleGrid[currentRow] = model.getNextRow();
					break;

				case 3:
					model.thriceButton(buttons);
					wordleGrid[currentRow] = model.getNextRow();
					break;
				}
			}

		}
	}

	@Override
	public boolean setCurrentRow() {
		statistics.recordWord(guess);
		
		model.setCurrentRow();
		WordleResponse[] updatedRow = model.getCurrentRow();
		wordleGrid[currentRow] = updatedRow;
		currentColumn = -1;
		currentRow++;
		guess = new char[columnCount];
		// logging ingo
		logData();
		if (checkSwap() && !hasSwapped) {
			swapToWordle();
			logData();
		}
		return currentRow < maximumRows;
	}

	private void logData() {
		if (model instanceof AbsurdleModel) {
			LOGGER.info("Candidate Set of Words Size = " + model.getTotalWordCount());
		} else {
			char[] answer = wordle.getAnswer();
			StringBuilder answerAsString = new StringBuilder();
			for (char c : answer) {
				answerAsString.append(c);
			}
			LOGGER.info("World word is: " + answerAsString.toString());
		}
	}

	@Override
	public int getTotalWordCount() {
		return model.getTotalWordCount();
	}
	
	public Model getModel() {
		return model;
	}

}
