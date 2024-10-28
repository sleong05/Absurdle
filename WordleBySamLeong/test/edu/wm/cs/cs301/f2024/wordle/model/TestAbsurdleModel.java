package edu.wm.cs.cs301.f2024.wordle.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class TestAbsurdleModel {
	int rowSize = 5;
	int rowCount = 6;
	int colCount = 5;
	int depth = 0; //for recursive test case for threads.

	/*
	 * initilizes a absurdle model with the inputted list of words
	 */
	private AbsurdleModel createModel(List<String> possibleWords) {
		// return new AbsurdleModel();
		AbsurdleModel testModel = new AbsurdleModel();
		testModel.overideWordList(possibleWords);
		return testModel;
	}

	/*
	 * @param the game model
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
	private void insertWord(AbsurdleModel model, String word) {
		char[] wordArray = word.toUpperCase().toCharArray();
		for (char letter:wordArray) {
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
		List<String> input = new ArrayList();
		input.add("Flash");
		input.add("Brisk");
		input.add("Knack");
		AbsurdleModel testModel = new AbsurdleModel();
		testModel.setCurrentColumn('B');
		testModel.setCurrentColumn('R');
		testModel.setCurrentColumn('A');
		testModel.setCurrentColumn('V');
		testModel.setCurrentColumn('O');
		testModel.setCurrentRow();
		/*
		 * checks if all gray
		 */
		int[] answer = {0, 0, 2, 0, 0};
		assertIntArrayEqual(testModel, answer);
	}
	/*
	 * Tests if repeats of a correct character in the wrong place are highlighted the correct number of times
	 */
	@Test
	void testBug2Coloring() {
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
		insertWord(model, "allow");
		
		int[] correctAnswer = {0, 1, 2, 0, 0};
		assertIntArrayEqual(model, correctAnswer);
	}
	/*
	 * Tests if only real words are allowed by checking it the model moves current row to the second row after entering a non word
	 */
	@Test
	void testBug3MustGuessRealWords() {
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
		insertWord(model, "abcde");
		
		int row = model.getCurrentRowNumber(); // looks at row above where inputs are allowed to see the row just entered so should be -1 for first row
		assertEquals(row, -1);
	}
	/*
	 * tests if backspace correctly returns to the first row which should be -1. (Insertion moves the col forward first, then inputs)
	 */
	@Test
	void testBug4Backspace() {
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
		model.setCurrentColumn('P'); //currentCol = 0 now
		model.backspace(); //currentCol - 1 = -1
		assertEquals(model.getCurrentColumn(), -1);
	}
	/*
	 * Correct Guess
	 */
	@Test
	void CorrectGuess() {
		List<String> input = new ArrayList();
		input.add("Bells");
		AbsurdleModel model = createModel(input);
		insertWord(model, "Bells");
		
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
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
		assertEquals(this.colCount, model.getColumnCount());
	}
	
	/*
	 * checks getMaximumRows to see if it gets the rows
	 */
	@Test
	void testgetMaximumRows() { 
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
		assertEquals(this.colCount, model.getColumnCount());
	}
	
	/*
	 * checks getCurrentColumn when row is full
	 */
	@Test
	void testgetCurrentColumnFifthSpot() { 
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
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
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
		assertEquals(-1, model.getCurrentColumn());
	}
	
	/*
	 * checks getCurrentColumn when row is empty after something was added and delted
	 */
	@Test
	void testgetCurrentColumnFirstSpotAfterDeletion() { 
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
		model.setCurrentColumn('p');
		model.backspace();
		assertEquals(-1, model.getCurrentColumn());
	}
	/*
	 * TESTS THAT HAVE TO DO WITH ENTERED LINES OF WORDS 
	 */
	
	/*
	 * Tests if the word is inputted correctly on the last try it works and checks if behavior for inputs 1-5 are as expected
	 */
	@Test
	void getCorrectWordAfterMultipleTries() {
		/*
		 * sets up game
		 */
		List<String> input = new ArrayList();
		input.add("PLANE"); //
		input.add("PLANT"); //
		input.add("CRANE"); //
		input.add("BLAME");
		input.add("FLAKE");
		input.add("PLATE"); //
		AbsurdleModel model = createModel(input);
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "CRANE");
		int[] correctAnswer = {0, 0, 2, 0, 2};
		assertIntArrayEqual(model, correctAnswer);
		
		insertWord(model, "PLATE");
		int[] correctAnswer2 = {0, 2, 2, 0, 2};
		assertIntArrayEqual(model, correctAnswer2);
		
		insertWord(model, "FLAKE");
		int[] correctAnswer3 = {0, 2, 2, 0, 2};
		assertIntArrayEqual(model, correctAnswer3);
		
		insertWord(model, "PLATE");
		int[] correctAnswer4 = {2, 2, 2, 2, 2};
		assertIntArrayEqual(model, correctAnswer4);
		
	}
	/*
	 * tests if a yellow letter works after their correct spot
	 */
	@Test
	void testYellowAfterItsSpot() {
		/*
		 * sets up game
		 */
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "GRILL");
		int[] correctAnswer = {0, 0, 0, 2, 1};
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
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
		
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "ALLOY");
		int[] correctAnswer = {0, 1, 2, 0, 0};
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
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "BULLS");
		int[] correctAnswer = {2, 0, 2, 2, 2};
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
		List<String> input = new ArrayList();
		input.add("Bells");
		input.add("Balls");
		input.add("Bills");
		AbsurdleModel model = createModel(input);
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "CLOWN");
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
		List<String> input = new ArrayList();
		input.add("SLOOP");
		AbsurdleModel model = createModel(input);
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "LOOPS");
		int[] correctAnswer = {1, 1, 1, 1, 1};
		assertIntArrayEqual(model, correctAnswer);
	}
	
	/*
	 * TEST FOR BACKSPACE
	 */
	

	/*
	 * test if removing all the character using backspace and then readding it still works
	 */
	@Test
	void testBackspaceAllCharacter() {
		/*
		 * sets up game
		 */
		List<String> input = new ArrayList();
		input.add("PUNKS");
		AbsurdleModel model = createModel(input);
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
		List<String> input = new ArrayList();
		input.add("PUNKS");
		AbsurdleModel model = createModel(input);
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
		List<String> input = new ArrayList();
		input.add("PUNKS");
		AbsurdleModel model = createModel(input);
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "PUNKS");
		int[] correctAnswer = {2, 2, 2, 2, 2};
		assertIntArrayEqual(model, correctAnswer);
		
		model.initialize();
		
		List<String> input2 = new ArrayList();
		input.add("STING");
		model.overideWordList(input2);
		
		insertWord(model, "STING");
		assertIntArrayEqual(model, correctAnswer);
	}
	
}
