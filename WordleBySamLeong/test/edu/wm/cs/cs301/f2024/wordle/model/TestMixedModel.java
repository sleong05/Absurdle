package edu.wm.cs.cs301.f2024.wordle.model;

import java.util.List;

import javax.swing.JButton;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import edu.wm.cs.cs301.f2024.wordle.view.WordleFrame;

class TestMixedModel {
	int rowSize = 5;
	int rowCount = 6;
	int colCount = 5;

	/*
	 * initilizes a absurdle model with the inputted list of words
	 */
	private MixedModel createModel(SwitchStrategy strategy) {
		// return new AbsurdleModel();
		MixedModel testModel = new MixedModel(strategy);
		return testModel;
	}

	/*
	 * @param the game model
	 * 
	 * @return returns a list of ints representing the inputted characters values. 0
	 * for grey, 1 for yellow, 2 for green
	 */
	private int[] getCharacterColorsasInts(AbsurdleModel model) {
		WordleResponse[] wordleRow = model.getCurrentRow();
		int[] answers = new int[rowSize];
		int index = 0;

		for (WordleResponse response : wordleRow) {
			if (response.getBackgroundColor() == AppColors.GREEN) {
				answers[index] = 2;
			} else if (response.getBackgroundColor() == AppColors.YELLOW) {
				answers[index] = 1;
			} else {
				answers[index] = 0;
			}
			index += 1;
		}
		return answers;
	}

	/*
	 * inserts a word into the row and locks in the row
	 */
	private void insertWord(MixedModel model, String word) {
		char[] wordArray = word.toUpperCase().toCharArray();
		for (char letter : wordArray) {
			model.setCurrentColumn(letter);
		}
		model.setCurrentRow();
	}

	private void assertIntArrayEqual(AbsurdleModel model, int[] correctAnswer) {
		int[] answer = getCharacterColorsasInts(model);
		for (int index = 0; index < 5; index++) {
			assertEquals(correctAnswer[index], answer[index]);
		}
	}

	@Test
	void testSwitchAfter3Guesses() {
		MixedModel model = createModel(new SwitchAfterNGuesses(3));
		insertWord(model, "stair");
		Model currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step

		insertWord(model, "blomp");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step

		insertWord(model, "stair");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof WordleModel)); // check if its the right model after each step

	}

	@Test
	void testSwitchAfter1Guesses() {
		MixedModel model = createModel(new SwitchAfterNGuesses(1));
		insertWord(model, "stair");
		Model currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof WordleModel)); // check if its the right model after each step

	}

	@Test
	void testSwitchAfter6Guesses() {
		MixedModel model = createModel(new SwitchAfterNGuesses(5));
		insertWord(model, "stair");
		Model currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step

		insertWord(model, "blomp");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step

		insertWord(model, "blomp");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step

		insertWord(model, "blomp");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step

		insertWord(model, "stair");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof WordleModel)); // check if its the right model after each step

	}
	// does this test 30 times to increase the probability that hits all random cases 1-5. Odds of not hitting a case are (4/5)^30 = .1%
	@Test
	void testSwitchRandomly() {
		for (int k = 0; k < 30; k++) {
			SwitchRandomly randomStrat = new SwitchRandomly();
			MixedModel model = createModel(randomStrat);
			int n = randomStrat.getN();

			for (int i = 1; i < n; i++) { // puts in words up until its about to hit n
				insertWord(model, "stair");
				Model currentModel = model.getModel();
				assertEquals(true, (currentModel instanceof AbsurdleModel));
			}
			insertWord(model, "stair");
			Model currentModel = model.getModel();
			assertEquals(true, (currentModel instanceof WordleModel));
		}
	}
	
	@Test
	void testSwitchWhenWordListIsBelowThreshhold() {
			MixedModel model = createModel(new SwitchWhenWordListIsBelowThreshold(10));
			
			insertWord(model, "audio");
			Model currentModel = model.getModel();
			assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
			
			insertWord(model, "stair");
			currentModel = model.getModel();
			assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
			
			insertWord(model, "pasme");
			currentModel = model.getModel();
			assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
			
			insertWord(model, "canes");
			currentModel = model.getModel();
			assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
			
			insertWord(model, "gfblh");
			currentModel = model.getModel();
			assertEquals(true, (currentModel instanceof WordleModel)); // check if its the right model after each step
		
	}
	@Test
	void testSwitchWhenWordListIsBelowThreshholdUsingOnceButton() {
		MixedModel model = createModel(new SwitchWhenWordListIsBelowThreshold(10));
		
		insertWord(model, "audio");
		Model currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		insertWord(model, "stair");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		model.onceButton();
		model.setCurrentColumn('E');
		model.setCurrentColumn('S');
		model.setCurrentColumn('A');
		model.setCurrentColumn('L');
		model.setCurrentRow();
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof WordleModel)); // check if its swapped
	
}
	@Test
	void testSwitchWhenWordListIsBelowThreshholdAfterUsingOnceButton() {
		MixedModel model = createModel(new SwitchWhenWordListIsBelowThreshold(10));
		
		insertWord(model, "audio");
		Model currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		insertWord(model, "stair");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		insertWord(model, "besal");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		insertWord(model, "fghjk");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		model.onceButton();
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof WordleModel)); // check if its swapped
	
	}
	@Test
	void testSwitchWhenWordListIsBelowThreshholdAfterUsingTwiceButton() {
		MixedModel model = createModel(new SwitchWhenWordListIsBelowThreshold(10));
		
		insertWord(model, "audio");
		Model currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		insertWord(model, "stair");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		insertWord(model, "besal");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		insertWord(model, "mnfgy");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		model.twiceButton();
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof WordleModel)); // check if its swapped
	
	}
	
	@Test
	void testSwitchWhenWordListIsBelowThreshholdAfterUsingThriceButton() {
		MixedModel model = createModel(new SwitchWhenWordListIsBelowThreshold(10));
		WordleFrame frame = new WordleFrame(model);
		
		insertWord(model, "audio");
		Model currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		insertWord(model, "strep");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		insertWord(model, "flqwz");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		insertWord(model, "xgvjo");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof AbsurdleModel)); // check if its the right model after each step
		
		model.thriceButton(frame.getKeyboardPanel().getButtons());
		model.thriceButton(frame.getKeyboardPanel().getButtons());
		
		insertWord(model, "chask");
		currentModel = model.getModel();
		assertEquals(true, (currentModel instanceof WordleModel)); // check if its swapped
	}

	@Test
	void testBackspace() {
		MixedModel model = createModel(new SwitchWhenWordListIsBelowThreshold(10));
		model.setCurrentColumn('A');
		model.backspace();
		
		assertEquals(-1, model.getCurrentColumn());
	}
}
