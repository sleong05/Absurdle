package edu.wm.cs.cs301.f2024.wordle.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserInputInterpreter {
	private String userInputLog, path, wordleAnswerslog, wordleWinlog;

	private int totalWordsGuessed = 0;

	private List<String> wordsGuessed = new ArrayList<>();

	private List<String> previousAnswers = new ArrayList<>();
	
	private List<String> wordsThatWon = new ArrayList<>();

	public UserInputInterpreter() {
		String fileSeparator = System.getProperty(AppStrings.FILE_SEPARATOR);

		this.path = System.getProperty(AppStrings.USER_HOME) + fileSeparator + AppStrings.WORDLE;

		this.userInputLog = fileSeparator + "userInputs.log"; // log for userInputData

		this.wordleAnswerslog = fileSeparator + "wordleAnswers.log";
		
		this.wordleWinlog = fileSeparator + "wordleWin.log";

		readUserInputs();
		readWordleAnswers();
		readWordleWinWords();
	}

	

	// calculates f(w): how often word w was selected over the history of games that
	// we have on file
	// when the random strategy is played.
	public double f(String word) {
		word = word.toLowerCase();
		double timesWordWasAnswer = 0;
		for (String answer : previousAnswers) {
			if (answer.equals(word)) {
				timesWordWasAnswer++;
			}
		}
		double totalAnswers = (double) previousAnswers.size();
		return timesWordWasAnswer/totalAnswers;
	}
	// how often the user tried the word w over the history of games that we have on
	// file and regardless of the game strategy (absurdle or random).
	public double g(String w) {
		w = w.toUpperCase();
		double frequencyOfInput = 0.0;
		for (String word : wordsGuessed) {
			if (word.equals(w)) {
				frequencyOfInput += 1.0;
			}
		}
		double totalWords = (double) wordsGuessed.size();
		return frequencyOfInput/totalWords;
	}
	//  how often the user wins if w is the selected word.
	public double h(String w) {
		w = w.toUpperCase();
		double winsWithWord = 0;
		for (String word: wordsThatWon) {
			if (word.equals(w)) {
				winsWithWord+= 1.0;
			}
		}
		
		double totalTimesWordHasBeenSeen = 0;
		for (String answer:previousAnswers) {
			if (answer.toUpperCase().equals(w)) {
				totalTimesWordHasBeenSeen++;
			}
		}
		if (totalTimesWordHasBeenSeen == 0) {
			return 0;
		}
		return winsWithWord/totalTimesWordHasBeenSeen;
	}
	// how often the user loses if w is the selected word
	public double m(String w) {
		double winRate = h(w);
		return 1.0 - winRate;
		
	}
	
	private void readWordleWinWords() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + wordleWinlog));
			String word = br.readLine();
			while (word != null) {
				wordsThatWon.add(word);
				word = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readWordleAnswers() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + wordleAnswerslog));
			String word = br.readLine();
			while (word != null) {
				previousAnswers.add(word);
				word = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// reads all the words the user has inputted
	private void readUserInputs() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path + userInputLog));
			String word = br.readLine();
			while (word != null) {
				totalWordsGuessed++;
				wordsGuessed.add(word);
				word = br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void printWordsGuesses() {
		System.out.println(wordsGuessed);
	}
}
