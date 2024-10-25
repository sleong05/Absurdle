package edu.wm.cs.cs301.f2024.wordle.controller;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;

import edu.wm.cs.cs301.f2024.wordle.model.AppColors;
import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;
import edu.wm.cs.cs301.f2024.wordle.model.WordleResponse;
import edu.wm.cs.cs301.f2024.wordle.view.StatisticsDialog;
import edu.wm.cs.cs301.f2024.wordle.view.WordleFrame;

public class KeyboardButtonAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/** 
	 * A WordleFrame object used to update view for the GUI
	 */
	private final WordleFrame view;
	/** 
	 * A WordleModel object used to update the game state
	 */
	private final WordleModel model;

	/**
	 * Used to update the view and model of the game
	 * @param view. The new view of the game
	 * @param model. The new model of the game
	 */
	public KeyboardButtonAction(WordleFrame view, WordleModel model) {
		this.view = view;
		this.model = model;
	}
	
	private void enterCase() {
		/**
		 * checks if the current column is the last column
		 */
		if (model.getCurrentColumn() >= (model.getColumnCount() - 1)) {
			/*
			 * sets the background for the letters and stores true in more rows if not on last row
			 */
			boolean moreRows = model.setCurrentRow(); 
			/*
			 * gets the current row and stores it in an array of WordleResponses
			 */
			WordleResponse[] currentRow = model.getCurrentRow(); 
			int greenCount = 0;
			
			for (WordleResponse wordleResponse : currentRow) {
				/*
				 * sets the background and foreground colors
				 */
				view.setColor(Character.toString(wordleResponse.getChar()),
						wordleResponse.getBackgroundColor(), 
						wordleResponse.getForegroundColor());
				/*
				 * stores how many correct characters were inputed
				 */
				if (wordleResponse.getBackgroundColor().equals(AppColors.GREEN)) {
					greenCount++;
				} 
			}
			/*
			 * checks if all the characters are green
			 */
			if (greenCount >= model.getColumnCount()) {
				/*
				 * updates the screen
				 */
				view.repaintWordleGridPanel();
				/*
				 * brings up the post-game stats and adjusts for the win
				 */
				model.getStatistics().incrementTotalGamesPlayed();
				int currentRowNumber = model.getCurrentRowNumber();
				model.getStatistics().addWordsGuessed(currentRowNumber);
				int currentStreak = model.getStatistics().getCurrentStreak();
				model.getStatistics().setCurrentStreak(++currentStreak);
				new StatisticsDialog(view, model);
			} else if (!moreRows) {
				/*
				 * brings up post-game stats and adjusts for loss when there are no more rows
				 */
				view.repaintWordleGridPanel();
				model.getStatistics().incrementTotalGamesPlayed();
				model.getStatistics().setCurrentStreak(0);
				new StatisticsDialog(view, model);
			} else {
				view.repaintWordleGridPanel();
			}
		}
	}
	
	/*
	 * Used whenever and actions is performed to either input a character, delete a character, or enter a five-letter word
	 * After an enter, it adjusts the view and repaints it to the screen
	 * @param event. the inputted event
	 */
	@Override
	public void actionPerformed(ActionEvent event) {
		/**
		* gets the event and stores the event-type as a string in text
		*/
		JButton button = (JButton) event.getSource();
		String text = button.getActionCommand();
		
		/**
		 * looks at the inputed action
		 */
		switch (text) {
		
		case "Enter":
			enterCase();
			break;
		case "Backspace":
			/*
			 * deletes previous character and repaints
			 */
			model.backspace();
			view.repaintWordleGridPanel();
			break;
		default:
			/*
			 * puts in an inputed character and repaints
			 */
			model.setCurrentColumn(text.charAt(0));
			view.repaintWordleGridPanel();
			break;
		}
		
	}

}
