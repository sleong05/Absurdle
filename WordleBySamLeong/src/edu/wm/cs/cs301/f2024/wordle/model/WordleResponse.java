package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;

public class WordleResponse {
	/*
	 * the character for an input
	 */
	private final char c;
	/*
	 * colorResponse object to store the correct colors for the background and foreground
	 */
	private final ColorResponse colorResponse;
	/*
	 * constructer that stores the colors needed for the character's background and foreground
	 */
	public WordleResponse(char c, Color backgroundColor, Color foregroundColor) {
		/*
		 * stores the character
		 */
		this.c = c;
		/*
		 * passes the inputed colors to a ColorResponse object and stores it
		 */
		this.colorResponse = new ColorResponse(backgroundColor, foregroundColor);
	}
	/*
	 * get method to obtain the character
	 */
	public char getChar() {
		return c;
	}
	/*
	 * get method to obtain the background color
	 * @return returns background color
	 */
	public Color getBackgroundColor() {
		return colorResponse.getBackgroundColor();
	}
	/*
	 * get method to obtain the foreground color
	 * @return returns foregroundcolor
	 */
	public Color getForegroundColor() {
		return colorResponse.getForegroundColor();
	}
	
}
