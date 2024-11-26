package edu.wm.cs.cs301.f2024.wordle.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

public class Statistics implements Runnable{
	/*
	 * creates integers to store past game data
	 */
	private int currentStreak, longestStreak, totalGamesPlayed;
	/*
	 * list to store already guessed wrods
	 */
	private List<Integer> wordsGuessed;
	/*
	 * string for the to store the files path and log information
	 */
	private String path, log;
	
	private static final Logger logger = Logger.getLogger(Statistics.class.getName());
	/*
	 * constructer that creates the files for the game
	 */
	public Statistics() {
		logger.setLevel(Level.INFO);

		try {
			FileHandler fileTxt = new FileHandler(AppStrings.STATS_LOGGER_TXT);
			logger.addHandler(fileTxt);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * initializes the list for the words guessed
		 */
		this.wordsGuessed = new ArrayList<>();
		/*
		 * gets the system file seperator for the system (allows compatibility for diffirent systems)
		 */
		String fileSeparator = System.getProperty(AppStrings.FILE_SEPARATOR);
		/*
		 * sets the path for the wordle file and creates a Wordle folder in the home directory
		 */
		this.path = System.getProperty(AppStrings.USER_HOME) + fileSeparator + AppStrings.WORDLE;
		/*
		 * creates log file and stores its location in the Wordle directory
		 */
		this.log = fileSeparator + AppStrings.STATISTICS_LOG;
		/*
		 * reads info from past games from the log file
		 */
	}
	
	@Override
	public void run() {
		readStatistics();
	}

	private void readStatistics() {
		try {
			logger.info(AppStrings.STARTED_LOADING_DATA);
			BufferedReader br = new BufferedReader(new FileReader(path + log));
			
			this.currentStreak = Integer.valueOf(br.readLine().trim());
			
			this.longestStreak = Integer.valueOf(br.readLine().trim());
			
			this.totalGamesPlayed = Integer.valueOf(br.readLine().trim());
			
			int totalWordsGuessed = Integer.valueOf(br.readLine().trim());
			
			
			
			for (int index = 0; index < totalWordsGuessed; index++) {
				int wordsAtSpot = Integer.valueOf(br.readLine().trim());
				wordsGuessed.add(wordsAtSpot);
			}
			br.close();
			logger.info(AppStrings.DATA_SUCCESFULLY_LOADED_WORDS_GUESSED + wordsGuessed + AppStrings.COMMA_SPACE + AppStrings.TOTAL_GAMES_PLAYED_ + this.totalGamesPlayed + AppStrings.COMMA_SPACE + AppStrings.LONGEST_STREAK_ + this.longestStreak + AppStrings.COMMA_SPACE + AppStrings.CURRENT_STREAK_ + this.currentStreak);
		} catch (FileNotFoundException e) {
			this.currentStreak = 0;
			this.longestStreak = 0;
			this.totalGamesPlayed = 0;
		} catch (IOException e) {
			logger.severe(AppStrings.FAILED_TO_LOAD_DATA);
			e.printStackTrace();
		}
	}
	/*
	 * writes info about wins/losses to the statistics.log file
	 */
	public void writeStatistics() {
		try {
			/*
			 * creats a file object and opens it to the Wordle folder making it if it doesnt exist
			 */
			File file = new File(path);
			file.mkdir();
			file = new File(path + log);
			file.createNewFile();
			/*
			 * writes new statistics to the file
			 */
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write(Integer.toString(currentStreak));
			bw.write(System.lineSeparator());
			bw.write(Integer.toString(longestStreak));
			bw.write(System.lineSeparator());
			bw.write(Integer.toString(totalGamesPlayed));
			bw.write(System.lineSeparator());
			bw.write(Integer.toString(wordsGuessed.size()));
			bw.write(System.lineSeparator());
			/*
			 * writes the guessed words into the log file
			 */
			for (Integer value : wordsGuessed) {
				bw.write(Integer.toString(value));
				bw.write(System.lineSeparator());
			}
			
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	 * getter method that access win streak
	 * @return currentStreak
	 */
	public int getCurrentStreak() {
		return currentStreak;
	}
	/*
	 * updates longestStreak
	 */
	public void setCurrentStreak(int currentStreak) {
		this.currentStreak = currentStreak;
		if (currentStreak > longestStreak) {
			this.longestStreak = currentStreak;
		}
	}
	/*
	 * getter method that access the longest streak of wins
	 * @return longestStreak
	 */
	public int getLongestStreak() {
		return longestStreak;
	}
	/*
	 * getter method that obtains the amount of wordle games played
	 * @return totalGamesPlayed
	 */
	public int getTotalGamesPlayed() {
		return totalGamesPlayed;
	}
	/*
	 * adds 1 to totalGamesPlayed
	 */
	public void incrementTotalGamesPlayed() {
		this.totalGamesPlayed++;
	}
	/*
	 * gets a list that stores the number of tries the play has attempted
	 * @return WordsGuessed in current game
	 */
	public List<Integer> getWordsGuessed() {
		return wordsGuessed;
	}
	/*
	 * returns the list of the number of guesses it took to win from previous ganmes
	 */
	public void addWordsGuessed(int wordCount) {
		this.wordsGuessed.add(wordCount);
	}

}
