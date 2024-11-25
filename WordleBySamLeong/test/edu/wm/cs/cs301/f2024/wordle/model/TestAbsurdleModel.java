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
	
	@Test
	void testHardModeGray() {
		List<String> input = new ArrayList();
		input.add("Belts");
		input.add("Balms");
		input.add("Palms");
		AbsurdleModel model = createModel(input);
		model.addRule(new RuleHard());
		insertWord(model, "xxlxx");
		
		int[] correctAnswer = {0, 0, 2, 0, 0};
		assertIntArrayEqual(model, correctAnswer);
		
		insertWord(model, "xxxxx"); // needs l for a proper answer
		assertEquals(-1, model.getCurrentColumn()); // checks if the answer has been deleted
	}
	
	@Test
	//checks for yellow in the same spot it was before
	void testHardModeYellowInWrongSpot() {
		List<String> input = new ArrayList();
		input.add("Belts");
		input.add("Balms");
		input.add("Palms");
		AbsurdleModel model = createModel(input);
		model.addRule(new RuleHard());
		insertWord(model, "xlxxx");
		
		int[] correctAnswer = {0, 1, 0, 0, 0};
		assertIntArrayEqual(model, correctAnswer);
		
		insertWord(model, "xlxxx"); // needs l for a proper answer
		assertEquals(-1, model.getCurrentColumn()); // checks if the answer has been deleted
	}
	//cehcks for yellow in a diffirent spot then before
	@Test
	void testHardModeYellowInACorrectSpot() {
		List<String> input = new ArrayList();
		input.add("Belts");
		input.add("Balms");
		input.add("Palms");
		AbsurdleModel model = createModel(input);
		model.addRule(new RuleHard());
		insertWord(model, "xlxxx");
		
		int[] correctAnswer = {0, 1, 0, 0, 0};
		assertIntArrayEqual(model, correctAnswer);
		
		insertWord(model, "lpppp"); 
		int[] correctAnswer1 = {1, 0, 0, 0, 0};
		assertIntArrayEqual(model, correctAnswer1);
	}
	/*
	 * checks thread synch by entering a word that we know will be gray. in our case the word stair and seeing if we get our results of all grey. We cant use a set word list because my create wordlist method waits for the thread
	 * but we know the stair will return all false so we jsut put it in and see if we get all grey
	 */
	@Test
	void testBug1ThreadSynchronization() {
		AbsurdleModel testModel = new AbsurdleModel();
		testModel.setCurrentColumn('S');
		testModel.setCurrentColumn('T');
		testModel.setCurrentColumn('A');
		testModel.setCurrentColumn('I');
		testModel.setCurrentColumn('R');
		testModel.setCurrentRow();
		/*
		 * checks if all gray
		 */
		int[] answer = {0, 0, 0, 0, 0};
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
		input.add("Bolts");
		AbsurdleModel model = createModel(input);
		insertWord(model, "Bolts");
		
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
		
		insertWord(model, "BLAME");
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
		input.add("Belts");
		input.add("Balms");
		input.add("Flips");
		AbsurdleModel model = createModel(input);
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "CROWN");
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
		input.add("TRAPS");
		AbsurdleModel model = createModel(input);
		/*
		 * inserts a word and checks if the correct colors are present 2:green 1:yellow 0:grey
		 */
		insertWord(model, "STRAP");
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
