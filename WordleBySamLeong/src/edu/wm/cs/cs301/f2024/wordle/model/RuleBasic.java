package edu.wm.cs.cs301.f2024.wordle.model;

public class RuleBasic implements AcceptanceRule{

	@Override
	public boolean isAcceptableGuess(Model model) {
		//System.out.println(model.getCurrentColumn() + " >= " + (model.getColumnCount() -1));
		if ((model.getCurrentColumn() == (model.getColumnCount() -1))) {
			return true;
		}
		return false;
	}

}
