package edu.wm.cs.cs301.f2024.wordle.model;

public interface AcceptanceRule {
	boolean isAcceptableGuess(Model model);
}