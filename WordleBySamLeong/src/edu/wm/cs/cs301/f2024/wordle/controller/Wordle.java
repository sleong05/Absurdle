package edu.wm.cs.cs301.f2024.wordle.controller;

import javax.swing.SwingUtilities;

import edu.wm.cs.cs301.f2024.wordle.model.AbsurdleModel;
import edu.wm.cs.cs301.f2024.wordle.model.AppStrings;
import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;
import edu.wm.cs.cs301.f2024.wordle.view.WordleFrame;
import edu.wm.cs.cs301.f2024.wordle.model.Model;
import edu.wm.cs.cs301.f2024.wordle.model.RuleHard;
import edu.wm.cs.cs301.f2024.wordle.model.RuleLegitimateWordsOnly;

public class Wordle implements Runnable {
	private static int gameMode = 0;
	private static boolean hardMode = false;
	private static boolean wordsOnly = false;
	
	public static void main(String[] args) {
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case AppStrings.GAMEMODE:
				if (i + 1 < args.length) {
					String strategy = args[i + 1];
					if (strategy.equals(AppStrings.WORDLE_GAME_MODE)) {
						gameMode = 0;
					} else if (strategy.equals(AppStrings.ABSURDLE_GAME_MODE)) {
						gameMode = 1;
					}
					i++; // skip next arg
				} else {
                    System.err.println(AppStrings.ERROR_S_REQUIRES_A_STRATEGY_RANDOM_OR_ABSURDLE);
                }
				break;
				
			case AppStrings.HARD_MODE:
                hardMode = true;
                break;
            case AppStrings.REAL_WORDS_ONLY_MODE:
                wordsOnly = true;
                break;
            default:
                System.err.println(AppStrings.WARNING_UNRECOGNIZED_OPTION + args[i]);
                break;
			}
		}
		if (args.length > 0 && args[0].equals(AppStrings.ABSURDLE_GAME_MODE)) {
			gameMode = 1;
		}

		SwingUtilities.invokeLater(new Wordle());
	}

	/*
	 * creates an instance of the GUI WordleFrame and of the model to be used
	 */
	@Override
	public void run() {
		Model model = new WordleModel(); //default
		if (gameMode == 1) {
			model = new AbsurdleModel();
		} 
		if (hardMode) {
			model.addRule(new RuleHard());
		}
		if (wordsOnly) {
			model.addRule(new RuleLegitimateWordsOnly());
		}
		new WordleFrame(model);
	}

}
