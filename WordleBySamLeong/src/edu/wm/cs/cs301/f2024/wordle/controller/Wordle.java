package edu.wm.cs.cs301.f2024.wordle.controller;

import javax.swing.SwingUtilities;

import edu.wm.cs.cs301.f2024.wordle.model.AbsurdleModel;
import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;
import edu.wm.cs.cs301.f2024.wordle.view.WordleFrame;

public class Wordle implements Runnable {
	private static int gameMode = 0;
	public static void main(String[] args) {
		if (args.length > 0 && (args[0].equals("absurdle") || args[0].equals("random"))) {
			gameMode = 1;
		}
			
		SwingUtilities.invokeLater(new Wordle());
	}
	/*
	 * creates an instance of the GUI WordleFrame and of the model to be used
	 */
	@Override
	public void run() {
		if (gameMode == 1) {
			new WordleFrame(new AbsurdleModel());
		}
		else {
			new WordleFrame(new WordleModel());
		}
	}

}
