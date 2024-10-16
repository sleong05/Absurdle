package edu.wm.cs.cs301.f2024.wordle.controller;

import javax.swing.SwingUtilities;

import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;
import edu.wm.cs.cs301.f2024.wordle.view.WordleFrame;

public class Wordle implements Runnable {
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Wordle());
	}
	/*
	 * creates an instance of the GUI WordleFrame and of the model to be used
	 */
	@Override
	public void run() {
		new WordleFrame(new WordleModel());
	}

}
