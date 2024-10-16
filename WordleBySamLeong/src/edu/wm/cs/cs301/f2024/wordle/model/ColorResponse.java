package edu.wm.cs.cs301.f2024.wordle.model;

import java.awt.Color;

public class ColorResponse {
	/*
	 * the immutable colors for the background and foreground
	 */
	private final Color backgroundColor, foregroundColor;
	/*
	 * connstructor that sets the colors for ColorResponse
	 */
	public ColorResponse(Color backgroundColor, Color foregroundColor) {
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
	}
	/*
	 * @return returns immutable backgroundColor
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	/*
	 * @return returns immutable foregroundColor
	 */
	public Color getForegroundColor() {
		return foregroundColor;
	}

}
