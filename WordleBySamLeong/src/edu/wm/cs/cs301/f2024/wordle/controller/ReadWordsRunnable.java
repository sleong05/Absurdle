package edu.wm.cs.cs301.f2024.wordle.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.wm.cs.cs301.f2024.wordle.model.Model;
import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;

public class ReadWordsRunnable implements Runnable {

	/*
	 * creates a logger for storing events
	 */
	private final static Logger LOGGER =
			Logger.getLogger(ReadWordsRunnable.class.getName());
	
	/*
	 * the model that will be updated
	 */
	private final Model model;

	/*
	 * constructer innitilizeds the WordleModel to be used and sets up the logger for files
	 * @param the game model
	 */
	public ReadWordsRunnable(Model model) {
		LOGGER.setLevel(Level.INFO);

		try {
			FileHandler fileTxt = new FileHandler("./logging.txt");
			LOGGER.addHandler(fileTxt);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * innitilizes model instance
		 */
		this.model = model;
	}

	/*
	 * accesses usa.txt and scans for suitable possible words
	 * sets the possible words in model and asks model to generate the current word for the game
	 */
	@Override
	public void run() {
		/*
		 * declares wordlist for storing possible words
		 */
		List<String> wordlist;

		try {
			/*
			 * recieves words that fit the size of the game from  usa.txt and logs it
			 */
			wordlist = createWordList();
			LOGGER.info("Created word list of " + wordlist.size() + " words.");
		} catch (IOException e) {
			LOGGER.info(e.getMessage());
			e.printStackTrace();
			wordlist = new ArrayList<>();
		}
		/*
		 * updates the word list in model and generates the a word
		 */
		model.setWordList(wordlist);
		if (model instanceof WordleModel) {
		((WordleModel)model).generateCurrentWord();
		}
	}

	/*
	 * creates an input stream from the usa.txt file
	 * @return returns a stream version of usa.txt
	 */
	private InputStream deliverInputStream() {
		String text = "/resources/usa.txt";
		// Original code
		/*
		ClassLoader loader = this.getClass().getClassLoader();
		InputStream stream = loader.getResourceAsStream(text);
		*/
		// https://stackoverflow.com/questions/68314700/why-java-cannot-read-same-resource-file-when-module-info-is-added
		
		InputStream stream = Wordle.class.getResourceAsStream(text);
		
		if (null == stream) {
			System.out.println("Failed to open stream with " + text);
			System.exit(0);
		}
		else 
			System.out.println("Successfully opened inputstream for " + text);
		
		return stream;
	}
	
	/*
	 * reads words from a file
	 * @return returns a list of potential wordle words
	 */
	private List<String> createWordList() throws IOException {
		/*
		 * sees how long a word can be
		 */
		int minimum = model.getColumnCount();
		/*
		 * creates a list to return words from
		 */
		List<String> wordlist = new ArrayList<>();

		/*
		 * gets the input and feeds it into a buffered reader
		 */
		InputStream stream = deliverInputStream();
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(stream));
		/*
		 * reads through lines from the input stream and adds them to wordlist if they are the correct length
		 */
		String line = reader.readLine();
		while (line != null) {
			line = line.trim();
			if (line.length() == minimum) {
				wordlist.add(line);
			}
			line = reader.readLine();
		}
		reader.close();

		return wordlist;
	}
	
}
