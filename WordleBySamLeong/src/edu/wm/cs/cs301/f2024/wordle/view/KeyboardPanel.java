package edu.wm.cs.cs301.f2024.wordle.view;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import edu.wm.cs.cs301.f2024.wordle.controller.KeyboardButtonAction;
import edu.wm.cs.cs301.f2024.wordle.model.AppColors;
import edu.wm.cs.cs301.f2024.wordle.model.AppStrings;
import edu.wm.cs.cs301.f2024.wordle.model.Model;
import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;

public class KeyboardPanel {

	private int buttonIndex, buttonCount;

	private final JButton[] buttons;

	private final JPanel panel;

	private final KeyboardButtonAction action;

	private final Model model;
	
	JLabel Totallabel;

	public KeyboardPanel(WordleFrame view, Model model2) {
		this.model = model2;
		this.buttonIndex = 0;
		this.buttonCount = firstRow().length + secondRow().length
				+ thirdRow().length + fourthRow().length;
		this.buttons = new JButton[buttonCount];
		this.action = new KeyboardButtonAction(view, model2);
		this.panel = createMainPanel();
	}

	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new GridLayout(0, 1, 0, 0));
		panel.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));

		panel.add(createQPanel());
		panel.add(createAPanel());
		panel.add(createZPanel());
		panel.add(createHelpPanel());
		panel.add(createTotalPanel());

		return panel;
	}

	private JPanel createQPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		Font textfont = AppFonts.getTextFont();

		String[] letters = firstRow();

		for (int index = 0; index < letters.length; index++) {
			JButton button = new JButton(letters[index]);
			setKeyBinding(button, letters[index]);
			button.addActionListener(action);
			button.setFont(textfont);
			buttons[buttonIndex++] = button;
			panel.add(button);
		}

		return panel;
	}

	private String[] firstRow() {
		String[] letters = { AppStrings.Q, AppStrings.W, AppStrings.E, AppStrings.R, AppStrings.T, AppStrings.Y, AppStrings.U, AppStrings.I, AppStrings.O, AppStrings.P,
				AppStrings.BACKSPACE };
		return letters;
	}

	private JPanel createAPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		Font textfont = AppFonts.getTextFont();

		String[] letters = secondRow();

		for (int index = 0; index < letters.length; index++) {
			JButton button = new JButton(letters[index]);
			setKeyBinding(button, letters[index]);
			button.addActionListener(action);
			button.setFont(textfont);
			buttons[buttonIndex++] = button;
			panel.add(button);
		}

		return panel;
	}

	private String[] secondRow() {
		String[] letters = { AppStrings.A, AppStrings.S, AppStrings.D, AppStrings.F, AppStrings.G, AppStrings.H, AppStrings.J, AppStrings.K, AppStrings.L,
				AppStrings.ENTER };
		return letters;
	}

	private JPanel createZPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		Font textfont = AppFonts.getTextFont();

		String[] letters = thirdRow();

		for (int index = 0; index < letters.length; index++) {
			JButton button = new JButton(letters[index]);
			setKeyBinding(button, letters[index]);
			button.addActionListener(action);
			button.setFont(textfont);
			buttons[buttonIndex++] = button;
			panel.add(button);
		}

		return panel;
	}

	private String[] thirdRow() {
		String[] letters = { AppStrings.Z, AppStrings.X, AppStrings.C, AppStrings.V, AppStrings.B, AppStrings.N, AppStrings.M };
		return letters;
	}
	
	private JPanel createHelpPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		Font textfont = AppFonts.getTextFont();

		String[] letters = fourthRow();
		Color[] colorsList = {AppColors.GREEN, AppColors.YELLOW, AppColors.GRAY};
		for (int index = 0; index < letters.length; index++) {
			JButton button = new JButton(letters[index]);
			button.setBackground(colorsList[index]);
			setKeyBinding(button, letters[index]);
			button.addActionListener(action);
			button.setFont(textfont);
			buttons[buttonIndex++] = button;
			panel.add(button);
		}

		return panel;
	}
	
	private String[] fourthRow() {
		String[] letters = { AppStrings.ONCE, AppStrings.TWICE, AppStrings.THRICE };
		return letters;
	}

	private void setKeyBinding(JButton button, String text) {
		InputMap inputMap = button.getInputMap(JButton.WHEN_IN_FOCUSED_WINDOW);
		if (text.equalsIgnoreCase(AppStrings.BACKSPACE)) {
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
					AppStrings.ACTION2);
		} else {
			inputMap.put(KeyStroke.getKeyStroke(text.toUpperCase()), AppStrings.ACTION2);
		}
		ActionMap actionMap = button.getActionMap();
		actionMap.put(AppStrings.ACTION2, action);
	}

	private JPanel createTotalPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
		Font footerFont = AppFonts.getFooterFont();

		String text = String.format("%,d", model.getTotalWordCount());
		text += AppStrings.POSSIBLE + model.getColumnCount() + AppStrings.LETTER_WORDS;
		Totallabel = new JLabel(text);
		Totallabel.setFont(footerFont);
		panel.add(Totallabel);

		return panel;
	}

	public void setColor(String letter, Color backgroundColor,
			Color foregroundColor) {
		for (JButton button : buttons) {
			if (button.getActionCommand().equals(letter)) {
				Color color = button.getBackground();
				if (color.equals(AppColors.GREEN)) {
					// Do nothing
				} else if (color.equals(AppColors.YELLOW)
						&& backgroundColor.equals(AppColors.GREEN)) {
					button.setBackground(backgroundColor);
					button.setForeground(foregroundColor);
				} else {
					button.setBackground(backgroundColor);
					button.setForeground(foregroundColor);
				}
				break;
			}
			button.setOpaque(true);
			String text = String.format("%,d", model.getTotalWordCount());
			text += AppStrings.POSSIBLE + model.getColumnCount() + AppStrings.LETTER_WORDS;
			Totallabel.setText(text);
		}
	}

	public void resetDefaultColors() {
		for (JButton button : buttons) {
			button.setBackground(null);
			button.setForeground(null);
		}
	}

	public JPanel getPanel() {
		return panel;
	}

	public JButton[] getButtons() {
		return this.buttons;
	}
}
