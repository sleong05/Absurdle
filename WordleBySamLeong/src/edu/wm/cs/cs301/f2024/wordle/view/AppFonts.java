package edu.wm.cs.cs301.f2024.wordle.view;

import java.awt.Font;

import edu.wm.cs.cs301.f2024.wordle.model.AppStrings;

public class AppFonts {
	
	public static Font getTitleFont() {
		return new Font(AppStrings.DIALOG, Font.BOLD, 36);
	}
	
	public static Font getTextFont() {
		return new Font(AppStrings.DIALOG, Font.PLAIN, 16);
	}
	
	public static Font getFooterFont() {
		return new Font(AppStrings.DIALOG, Font.PLAIN, 12);
	}

}
