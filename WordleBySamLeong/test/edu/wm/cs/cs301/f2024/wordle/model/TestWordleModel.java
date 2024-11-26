package edu.wm.cs.cs301.f2024.wordle.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import javax.swing.JButton;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.wm.cs.cs301.f2024.wordle.view.KeyboardPanel;
import edu.wm.cs.cs301.f2024.wordle.view.WordleFrame;

class TestWordleModel {
	int rowSize = 5;
	int rowCount = 6;
	int colCount = 5;
	int depth = 0; //for recursive test case for threads.

	private WordleModel createModel(String answer) {
		// return new WordleModel();
		WordleModel testModel = new WordleModel();
		testModel.setCurrentWordForTesting(answer);
		return testModel;
	}

	/*
	 * @param the game model
	 * 
	 * @return returns a list of ints representing the inputted characters values. 0
	 * for grey, 1 for yellow, 2 for green
	 */
	private int[] getCharacterColorsasInts(WordleModel model) {
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
	private void insertWord(WordleModel model, String word) {
		char[] wordArray = word.toUpperCase().toCharArray();
		for (char letter:wordArray) {
			model.setCurrentColumn(letter);
		}
		model.setCurrentRow();
	}
	
	private void assertIntArrayEqual(WordleModel model, int[] correctAnswer) {
		int[] answer = getCharacterColorsasInts(model);
		for (int index = 0; index < 5; index++) {
			assertEquals(correctAnswer[index], answer[index]);
		}
	}
	
	@Test
	void testHardModeInvalidInput() {
		WordleModel model = createModel("blurb");
		model.addRule(new RuleHard());
		insertWord(model, "blues");
		
		int[] correctAnswer = {2, 2, 2, 0, 0}; // makes sure behavior is normal before actual test
		assertIntArrayEqual(model, correctAnswer);
		
		insertWord(model, "blond"); // needs u for a proper answer
		assertEquals(-1, model.getCurrentColumn()); // checks if the answer has been deleted
		
	}
	
	@Test
	void testHardModeValidInput() {
		WordleModel model = createModel("blurb");
		model.addRule(new RuleHard());
		insertWord(model, "blues");
		
		int[] correctAnswer = {2, 2, 2, 0, 0}; // makes sure behavior is normal before actual test
		assertIntArrayEqual(model, correctAnswer);
		
		insertWord(model, "blurb"); 
		int[] correctAnswer2 = {2, 2, 2, 2, 2};
		assertIntArrayEqual(model, correctAnswer2);
		
	}
	/*
	 * checks thread synch by trying to input something quicky before the thread would have time to proccess if it wasnt synchronized. If it isn't, they should all remain there default colors
	 * so they should all be gray even if they are right answers
	 * First check if all gray. If any not gray there should not be a problem with the threading
	 * Second check is they really should all be gray and if they are call the test again up to 200 times. This should guarentee a working solution due to the input STAIR being extremely common
	 * for wordle to flag words in. 
	 * Very difficult to test since you don't know what to expect as the word so you don't know which colors should be highlighted
	 * If you were to add a get method for the answer it wouldn't work because the getMethod would also have to be synched as so we wouldn't be able to isolate sycnhing the enter button.
	 */
	@Test
	void testBug1ThreadSynchronization() {
		WordleModel testModel = new WordleModel();
		testModel.setCurrentColumn('S');
		testModel.setCurrentColumn('T');
		testModel.setCurrentColumn('A');
		testModel.setCurrentColumn('I');
		testModel.setCurrentColumn('R');
		
		testModel.setCurrentRow();
		/*
		 * checks if all gray
		 */
		int[] response = getCharacterColorsasInts(testModel);
		boolean allGray = true;
		for (int answer: response) {
			if (answer!=0) {
				allGray = false;
			}
		}
		/*
		 * checks if should be all gray
		 */
		boolean shouldBeAllGray = true;
		char [] testWord = {'S', 'T', 'A', 'I', 'R'};		
		for (char b: testWord) {
			if (testModel.isCharacterInCurrent(b)) {
				shouldBeAllGray = false;
			}
		}
		
		if (!shouldBeAllGray) {
			assertEquals(allGray, false);
		} else {
			depth += 1;
			if (depth < 201) {
				testBug1ThreadSynchronization();
			}
			else {
				assertEquals(true, false); // max depth reached, failing test
			}
		}
	}
	/*
	 * Tests if repeats of a correct character in the wrong place are highlighted the correct number of times
	 */
	@Test
	void testBug2Coloring() {
		WordleModel model = createModel("blurb");
		insertWord(model, "hello");
		
		int[] correctAnswer = {0, 0, 1, 0, 0};
		assertIntArrayEqual(model, correctAnswer);
	}
	/*
	 * Tests if only real words are allowed by checking it the model moves current row to the second row after entering a non word
	 */
	@Test
	void testBug3MustGuessRealWords() {
		WordleModel model = createModel("blurb");
		model.addRule(new RuleLegitimateWordsOnly());
		insertWord(model, "abcde");
		
		int row = model.getCurrentRowNumber(); // looks at row above where inputs are allowed to see the row just entered so should be -1 for first row
		assertEquals(row, -1);
	}
	/*
	 * tests if backspace correctly returns to the first row which should be -1. (Insertion moves the col forward first, then inputs)
	 */
	@Test
	void testBug4Backspace() {
		WordleModel model = createModel("spawn");
		model.setCurrentColumn('P'); //currentCol = 0 now
		model.backspace(); //currentCol - 1 = -1
		assertEquals(model.getCurrentColumn(), -1);
	}
	
	/*
	 * tests if keyboard accuraterly represents colors. Bug only an issue on mac. For mac, the butts must have there Opaque to be set to true to show. (i dont have a mac a
	 * but this is what google told me) (hard to test if it works) 
	 */
	@Test
	void testBug5KeyboardColors() {
		WordleModel model = createModel("spawn");
		WordleFrame Frame = new WordleFrame(model);
		insertWord(model, "spray");
		
		Frame.repaintWordleGridPanel();
		KeyboardPanel keyboard = Frame.getKeyboardPanel();
		JButton[] keyboardButtons = keyboard.getButtons();
		for (JButton spot: keyboardButtons) {
			assertEquals(spot.isOpaque(), true);
		}
	}
	
	/*
	 * first try guess
	 */
	@Test
	void testFirstTryGuess() {
		WordleModel model = createModel("spawn");
		insertWord(model, "spawn");
		
		int[] correctAnswer = {2, 2, 2, 2, 2};
		
		assertIntArrayEqual(model, correctAnswer);
	}
	
	 
	/*
	 * TESTS TO SEE IF GET METHOD WORKS
	 */
	/*
	 * checkss col count get method
	 */
	@Test
	void testGetMaximumRows() { 
		WordleModel model = createModel("punks");
		assertEquals(this.colCount, model.getColumnCount());
	}
	
	/*
	 * checks getMaximumRows to see if it gets the rows
	 */
	@Test
	void testgetMaximumRows() { 
		WordleModel model = createModel("punks");
		assertEquals(this.colCount, model.getColumnCount());
	}
	
	/*
	 * checks getCurrentColumn when row is full
	 */
	@Test
	void testgetCurrentColumnFifthSpot() { 
		WordleModel model = createModel("punks");
		model.setCurrentColumn('p');
		model.setCurrentColumn('p');
		model.setCurrentColumn('p');
		model.setCurrentColumn('p');
		model.setCurrentColumn('p');
		assertEquals(4, model.getCurrentColumn());
	}
	
	/*
	 * checks getCurrentColumn when row is empty
	 */
	@Test
	void testgetCurrentColumnFirstSpot() { 
		WordleModel model = createModel("punks");
		assertEquals(-1, model.getCurrentColumn());
	}
	
	/*
	 * checks getCurrentColumn when row is empty after something was added and delted
	 */
	@Test
	void testgetCurrentColumnFirstSpotAfterDeletion() { 
		WordleModel model = createModel("punks");
		model.setCurrentColumn('p');
		model.backspace();
		assertEquals(-1, model.getCurrentColumn());
	}
	/*
	 * TESTS THAT HAVE TO DO WITH ENTERED LINES OF WORDS 
	 */
	
	/*
	 * Tests if the word is inputted correctly on the first try that it works
	 */
	@Test
	void testCorrectWordFirstTry() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("pawns");
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "pawns");
		int[] correctAnswer = {2, 2, 2, 2, 2};
		assertIntArrayEqual(model, correctAnswer);
	}
	/*
	 * Tests if the word is inputted correctly on the last try it works and checks if behavior for inputs 1-5 are as expected
	 */
	@Test
	void testCorrectWordSixTries() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("flops");
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "fable");
		int[] correctAnswer = {2, 0, 0, 1, 0};
		assertIntArrayEqual(model, correctAnswer);
		
		insertWord(model, "flags");
		int[] correctAnswer2 = {2, 2, 0, 0, 2};
		assertIntArrayEqual(model, correctAnswer2);
		
		insertWord(model, "float");
		int[] correctAnswer3 = {2, 2, 2, 0, 0};
		assertIntArrayEqual(model, correctAnswer3);
		
		insertWord(model, "plops");
		int[] correctAnswer4 = {0, 2, 2, 2, 2};
		assertIntArrayEqual(model, correctAnswer4);
		
		insertWord(model, "heart");
		int[] correctAnswer5 = {0, 0, 0, 0, 0};
		assertIntArrayEqual(model, correctAnswer5);
		
		insertWord(model, "flops");
		int[] correctAnswer6 = {2, 2, 2, 2, 2};
		assertIntArrayEqual(model, correctAnswer6);
	}
	/*
	 * tests if a yellow letter works after their correct spot
	 */
	@Test
	void testYellowAfterItsSpot() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("pawns");
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "plaza");
		int[] correctAnswer = {2, 0, 1, 0, 0};
		assertIntArrayEqual(model, correctAnswer);
	}
	
	/*
	 * tests if a yellow letter works before their correct spot
	 */
	@Test
	void testYellowBeforeItsSpot() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("pawns");
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "agony");
		int[] correctAnswer = {1, 0, 0, 2, 0};
		assertIntArrayEqual(model, correctAnswer);
	}
	
	@Test
	/*
	 * checks if there can only be as many yellow letters of a specific character as there are in the actual word
	 */
	void testMultipleofYellowCharacter() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("slope");
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "oozed");
		int[] correctAnswer = {1, 0, 0, 1, 0};
		assertIntArrayEqual(model, correctAnswer);
	}
	@Test
	/*
	 * checks if there are duplicates of a green letter after its spot that they are grey
	 */
	void testMultipleofGreenCharacterAfterGreen() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("flyer");
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "slyly");
		int[] correctAnswer = {0, 2, 2, 0, 0};
		assertIntArrayEqual(model, correctAnswer);
	}
	/*
	 * checks if there are duplicates of a green letter before its spot that they are grey
	 */
	@Test
	void testMultipleofGreenCharacterBeforeGreen() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("adept");
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "teeth");
		int[] correctAnswer = {1, 0, 2, 0, 0};
		assertIntArrayEqual(model, correctAnswer);
	}
	/*
	 * Test if all inputs are wrong
	 */
	@Test
	void testAllInputsWrong() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("flyer");
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "punks");
		int[] correctAnswer = {0, 0, 0, 0, 0};
		assertIntArrayEqual(model, correctAnswer);
	}
	/*
	 * test all inputs yellow
	 */
	@Test
	void testAllInputsYellow() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("punks");
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "spunk");
		int[] correctAnswer = {1, 1, 1, 1, 1};
		assertIntArrayEqual(model, correctAnswer);
	}
	
	/*
	 * TEST FOR BACKSPACE
	 */
	
	/*
	 * test if removing a character using backspace and then readding it still works
	 */
	@Test
	void testBackspaceOneCharacter() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("punks");
		model.setCurrentColumn('P');
		model.setCurrentColumn('U');
		model.setCurrentColumn('N');
		model.setCurrentColumn('K');
		model.setCurrentColumn('S');
		model.backspace();
		model.setCurrentColumn('S');
		model.setCurrentRow();
		/*
		 * checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		int[] correctAnswer = {2, 2, 2, 2, 2};
		assertIntArrayEqual(model, correctAnswer);
	}
	/*
	 * test if removing all the character using backspace and then readding it still works
	 */
	@Test
	void testBackspaceAllCharacter() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("punks");
		model.setCurrentColumn('P');
		model.setCurrentColumn('U');
		model.setCurrentColumn('N');
		model.setCurrentColumn('K');
		model.setCurrentColumn('S');
		model.backspace();
		model.backspace();
		model.backspace();
		model.backspace();
		model.backspace();
		model.setCurrentColumn('P');
		model.setCurrentColumn('U');
		model.setCurrentColumn('N');
		model.setCurrentColumn('K');
		model.setCurrentColumn('S');
		model.setCurrentRow();
		/*
		 * checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		int[] correctAnswer = {2, 2, 2, 2, 2};
		assertIntArrayEqual(model, correctAnswer);
	}
	
	/*
	 * test if removing all the character but the last one using backspace and then readding it still works
	 */
	@Test
	void testBackspaceAllCharactersButOne() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("punks");
		model.setCurrentColumn('P');
		model.setCurrentColumn('U');
		model.setCurrentColumn('N');
		model.setCurrentColumn('K');
		model.setCurrentColumn('S');
		model.backspace();
		model.backspace();
		model.backspace();
		model.backspace();
		model.setCurrentColumn('U');
		model.setCurrentColumn('N');
		model.setCurrentColumn('K');
		model.setCurrentColumn('S');
		model.setCurrentRow();
		/*
		 * checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		int[] correctAnswer = {2, 2, 2, 2, 2};
		assertIntArrayEqual(model, correctAnswer);
	}
	/*
	 * test if innitilizing again actually resets the word
	 */
	@Test
	void testWordleReset() {
		/*
		 * sets up game
		 */
		WordleModel model = createModel("pawns");
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "pawns");
		int[] correctAnswer = {2, 2, 2, 2, 2};
		assertIntArrayEqual(model, correctAnswer);
		
		model.initialize();
		
		insertWord(model, "pawns");
		WordleResponse[] row = model.getCurrentRow();
		boolean allright = true;
		for (WordleResponse response:row) {
			if (response.getBackgroundColor() != AppColors.GREEN) {
				allright = false;
			}
		}
		assertEquals(allright, false);
	}
	
}
