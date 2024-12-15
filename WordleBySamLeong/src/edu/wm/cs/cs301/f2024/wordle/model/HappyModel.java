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
	
	private int hardOrEasy; // for testing easy = 0, hard = 1
	
	List <String> adjustedList = new ArrayList<>();
	public HappyModel(int threshold) {
		super();
		
		
			try {
				statsThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		this.threshold = (double) threshold;
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
		System.out.println(winRate + " > " + threshold);
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
			System.out.println("makeharder true");
			makeHarder = true;
		}
		if (makeHarder) {
			adjustListHard();
			System.out.println(hardOrEasy + " dpasdlasdasd");
			hardOrEasy =1;
			System.out.println(hardOrEasy + " dpasdlasdasd");
		} else {
			adjustListEasy();
			hardOrEasy = 0;
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
				System.out.println(winRate + " > " + threshold);
				LOGGER.info("possible word selected: " + word + " | hasCommonChars = " + hasCommonChars(mostCommonWord, word) + " | g(" + word + ") = " + interpreter.g(word)*100 + "% | f(" + word + ") = " + interpreter.f(word)*100 + "% | h(" + word + ") = " + interpreter.h(word) *100+ "% | m(" + word + ") = " + interpreter.m(word)*100 + "%");
			}
		}
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
				LOGGER.info("possible word selected: " + word + " | hasCommonChars = " + hasCommonChars(mostCommonWord, word) + " | g(" + word + ") = " + interpreter.g(word)*100 + "% | f(" + word + ") = " + interpreter.f(word)*100 + "% | h(" + word + ") = " + interpreter.h(word) *100+ "% | m(" + word + ") = " + interpreter.m(word)*100 + "%");
			} 
		}
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
	public int getHardOrEasy() {
		try {
			statsThread.join();
			Thread.sleep(400);  
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hardOrEasy;
	}
}
