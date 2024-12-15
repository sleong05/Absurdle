package edu.wm.cs.cs301.f2024.wordle.model;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TestHappyModel {

	int rowSize = 5;
	int rowCount = 6;
	int colCount = 5;
	int depth = 0; // for recursive test case for threads.

	private HappyModel createModel(boolean isHard) {
		HappyModel testModel;
		if (isHard) {
			testModel = new HappyModel(0);
		} else {
			testModel = new HappyModel(100);
		}

		return testModel;
	}
	
	
	 
	// race conditons apply for this class so by making tests start at diffirent times these can be fixed. Not a issue for the real game as the user wont be making many new games at the same time
	@Test
	void testMakeGameEasier() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HappyModel model = createModel(false);
		assertEquals(0, model.getHardOrEasy()); // 0 = easy list
	}
	
	@Test
	void testMakeGameHarder() {
		HappyModel model = createModel(true);
		int answer = model.getHardOrEasy();
		assertEquals(1, answer); // 1 = hard list 
		
	}
	 
}
