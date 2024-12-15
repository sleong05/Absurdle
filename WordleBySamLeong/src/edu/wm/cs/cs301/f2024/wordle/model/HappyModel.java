package edu.wm.cs.cs301.f2024.wordle.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class HappyModel extends WordleModel{
	double threshold;
	
	boolean makeHarder = false;
	
	int winRate;
	
	private final static Logger LOGGER = Logger.getLogger(HappyModel.class.getName());
	
	UserInputInterpreter interpreter = new UserInputInterpreter();
	
	List <String> adjustedList = new ArrayList<>();
	public HappyModel(int threshold) {
		super();
		this.threshold = (double) threshold;
		
			try {
				statsThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		winRate = (statistics.getTotalGamesWon() * 1000 + 5) / (statistics.getTotalGamesPlayed() * 10);
		// logger setup
		LOGGER.setLevel(Level.INFO);
		try {
			FileHandler fileTxt = new FileHandler("HappyModelLog");
			fileTxt.setFormatter(new SimpleFormatter());
			LOGGER.addHandler(fileTxt);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void generateCurrentWord() {
		adjustWordListForDifficulty();
		
		String word = this.getCurrentWord();
		statistics.writeAnswer(word);
		/*
		 * updates currentWord with the randomly picked word
		 */
		this.currentWord = word.toUpperCase().toCharArray();
		LOGGER.info("currentWord randomly selected to: " + word.toUpperCase());
	}
	
	private String getCurrentWord() {
		return adjustedList.get(getRandomIndex());
	}
	
	private int getRandomIndex() {
		int size = adjustedList.size();
		random = new Random();
		System.out.println(size);
		return random.nextInt(size);
	}
	
	private void adjustWordListForDifficulty() {
		
		try {
			statsThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(winRate + " > " + threshold);
		if (winRate > threshold) {
			makeHarder = true;
		}
		if (makeHarder) {
			adjustListHard();
		} else {
			adjustListEasy();
		}
	}

	private void adjustListEasy() {
		LOGGER.info("Make game easier selected");
		char[] mostCommonWord = findMostCommonWord();
		// for logging
		StringBuilder commonWord = new StringBuilder();
		for (char l: mostCommonWord) {
			commonWord.append(l);
		}
		LOGGER.info("Most Common word: " + commonWord.toString());
		
		for (String word: wordList) {
			if (hasCommonChars(mostCommonWord, word) || interpreter.g(word)>0) {
				adjustedList.add(word);
				LOGGER.info("possible word selected: " + word + " | hasCommonChars = " + hasCommonChars(mostCommonWord, word) + " | g(" + word + ") = " + interpreter.g(word));
			}
		}
		System.out.println(adjustedList);
	}

	private void adjustListHard() {
		LOGGER.info("Make game harder selected");
		char[] mostCommonWord = findMostCommonWord();
		// for logging
				StringBuilder commonWord = new StringBuilder();
				for (char l: mostCommonWord) {
					commonWord.append(l);
				}
				LOGGER.info("Most Common word: " + commonWord.toString());
				
		for (String word: wordList) {
			if (!hasCommonChars(mostCommonWord, word) && interpreter.g(word) == 0) {
				adjustedList.add(word);
				LOGGER.info("possible word selected: " + word + " | hasCommonChars = " + hasCommonChars(mostCommonWord, word) + " | g(" + word + ") = " + interpreter.g(word));
			} 
		}
		System.out.println(adjustedList);
	}
	
	private char[] findMostCommonWord() {
		String currentMostCommon = "STAIR";  // default most common start word
		double frequencyOfMostCommonWord = 0;
		for (String word: wordList) { // finds the most used word by the user
			double frequencyOfWord = interpreter.g(word);
			if (frequencyOfWord > frequencyOfMostCommonWord) {
				frequencyOfMostCommonWord = frequencyOfWord;
				currentMostCommon = word;
			}
		}
		return currentMostCommon.toCharArray();
	}
	
	private boolean hasCommonChars(char[] mostCommonWord, String word) {
		for (char c: mostCommonWord) {
			for (char Char: word.toCharArray()) {
				if (c == Char) {
					return true;
				}
			}
		}
		return false;
	}
}
