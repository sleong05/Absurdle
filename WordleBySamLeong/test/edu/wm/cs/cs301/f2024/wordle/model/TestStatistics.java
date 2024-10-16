package edu.wm.cs.cs301.f2024.wordle.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class TestStatistics {

	private Statistics createStats() {
		Statistics StatsInstance = new Statistics();
		return StatsInstance;
	}

	/*
	 * tests that longeststreak updates when currentstreak is greater than longeststreak
	 */
	@Test
	void testLongestStreakUpdate() {
		Statistics statsInstance = createStats();
		int oldLongestStreak = statsInstance.getLongestStreak();
		statsInstance.setCurrentStreak(statsInstance.getLongestStreak() + 4);
		statsInstance.writeStatistics();
		assertEquals(statsInstance.getLongestStreak(), oldLongestStreak + 4);
		
	}
	
	/*
	 * checks if a new instance of stats reads the update to longestStreak info after its written
	 */
	@Test
	void testLongestStreakUpdateWorksForNewInstance() {
		Statistics statsInstance = createStats();
		statsInstance.setCurrentStreak(statsInstance.getLongestStreak() + 4);
		int firstInstanceLongestStreak = statsInstance.getLongestStreak();
		statsInstance.writeStatistics();
		
		Statistics statsInstance2 = createStats();
		assertEquals(firstInstanceLongestStreak, statsInstance2.getLongestStreak());
	}
	
	/*
	 * checks if currentstreak can be properly changed by checking the reset case after a loss
	 */
	@Test
	void testCurrentStreakAfterALoss() {
		Statistics statsInstance = createStats();
		/*
		 * ensures that the streak is not 0
		 */
		statsInstance.setCurrentStreak(statsInstance.getLongestStreak() + 1);
		
		statsInstance.writeStatistics();
		statsInstance.setCurrentStreak(0);
		statsInstance.writeStatistics();
		
		assertEquals(statsInstance.getCurrentStreak(), 0);
		
	}
	/*
	 * checks if a new instance of stats reads the update to CurrentStreak info after its written
	 */
	@Test
	
	void testCurrentStreakUpdateWorksForNewInstance() {
		Statistics statsInstance = createStats();
		statsInstance.setCurrentStreak(statsInstance.getCurrentStreak() + 4);
		int firstInstanceCurrentStreak = statsInstance.getCurrentStreak();
		statsInstance.writeStatistics();
		
		Statistics statsInstance2 = createStats();
		assertEquals(firstInstanceCurrentStreak, statsInstance2.getCurrentStreak());
	}
	
	/*
	 * checks if a when the current streak is less than longest, that it works,if its over, and if its equal
	 */
	@Test
	
	void testLongestStreakChanging() {
		Statistics statsInstance = createStats();
		/*
		 * for current<longest
		 */
		statsInstance.setCurrentStreak(statsInstance.getLongestStreak() -1);
		assertNotEquals(statsInstance.getLongestStreak(), statsInstance.getCurrentStreak());
		/*
		 * for current = longest
		 */
		statsInstance.setCurrentStreak(statsInstance.getLongestStreak());
		assertEquals(statsInstance.getLongestStreak(), statsInstance.getCurrentStreak());
		/*
		 * for current > longest
		 */
		statsInstance.setCurrentStreak(statsInstance.getLongestStreak() + 1);
		assertEquals(statsInstance.getLongestStreak(), statsInstance.getCurrentStreak());
	}
	
	/*
	 * checks if the increment method adds 1 each time its called
	 */
	@Test
	void testincrementTotalGamesPlayed() {
		Statistics statsInstance = createStats();
		int oldGamesPlayed = statsInstance.getTotalGamesPlayed();
		
		statsInstance.incrementTotalGamesPlayed();
		statsInstance.incrementTotalGamesPlayed();
		statsInstance.incrementTotalGamesPlayed();
		statsInstance.incrementTotalGamesPlayed();
		statsInstance.incrementTotalGamesPlayed();
		
		assertEquals(oldGamesPlayed + 5, statsInstance.getTotalGamesPlayed());
		
	}
	
	/*
	 * checks if a new instance of stats reads the update to total game played info after its written
	 */
	@Test
	
	void testTotalGamesPlayedUpdateWorksForNewInstance() {
		Statistics statsInstance = createStats();
		statsInstance.incrementTotalGamesPlayed();
		
		int firstInstanceTotalGamesPlayed = statsInstance.getTotalGamesPlayed();
		statsInstance.writeStatistics();
		
		Statistics statsInstance2 = createStats();
		assertEquals(firstInstanceTotalGamesPlayed, statsInstance2.getTotalGamesPlayed());
	}
	
	/*
	 * tests if something is added to wordsGuessed
	 */
	@Test
	void testaddWordsGuessed() {
		Statistics statsInstance = createStats();
		int oldwordsGuessed = statsInstance.getWordsGuessed().size();
		statsInstance.addWordsGuessed(5);
		assertEquals(oldwordsGuessed, statsInstance.getWordsGuessed().size()-1);
	}
	/*
	 * tests if the correct value is added to wordsGuessed
	 */
	@Test
	void testaddWordsGuessedValue() {
		Statistics statsInstance = createStats();
		int inserted = 5;
		statsInstance.addWordsGuessed(inserted);
		

		int insertedValue = statsInstance.getWordsGuessed().getLast();
		
		assertEquals(inserted, insertedValue);
	}
	/*
	 * checks if a new statsInstance can see an updated guessed value
	 */
	@Test
	void testaddWordsGuessedValueByAnotherInstance() {
		Statistics statsInstance = createStats();
		int inserted = 5;
		statsInstance.addWordsGuessed(inserted);
		

		int insertedValue = statsInstance.getWordsGuessed().getLast();
		statsInstance.writeStatistics();
		
		Statistics statsInstance2 = createStats();
		int lastValue = statsInstance2.getWordsGuessed().getLast();
		assertEquals(insertedValue, lastValue);
	}

}
