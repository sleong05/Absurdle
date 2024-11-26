package edu.wm.cs.cs301.f2024.wordle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import edu.wm.cs.cs301.f2024.wordle.model.AppStrings;

public class AboutDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	
	private final CancelAction cancelAction;

	public AboutDialog(WordleFrame view) {
		super(view.getFrame(), AppStrings.ABOUT___, true);
		this.cancelAction = new CancelAction();
		
		add(createMainPanel(), BorderLayout.CENTER);
		add(createButtonPanel(), BorderLayout.SOUTH);
		
		pack();
		setLocationRelativeTo(view.getFrame());
		setVisible(true);
	}
	
	private JPanel createMainPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		Font titleFont = AppFonts.getTitleFont();
		Font textFont = AppFonts.getTextFont();
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 5, 5, 30);
		
		gbc.gridwidth = 2;
		gbc.gridx = 0;
		gbc.gridy = 0;
		JLabel label = new JLabel(AppStrings.ABOUT_WORDLE);
		label.setFont(titleFont);
		label.setHorizontalAlignment(JLabel.CENTER);
		Color backgroundColor = label.getBackground();
		panel.add(label, gbc);
		
		gbc.gridy++;
		String text = AppStrings.WORDLE_WAS_CREATED_BY_SOFTWARE_ENGINEER
				+ AppStrings.AND_FORMER_REDDIT_EMPLOYEE_JOSH_WARDLE_AND
				+ AppStrings.WAS_LAUNCHED_IN_OCTOBER_2021;
		JTextArea textArea = new JTextArea(4, 25);
		textArea.setBackground(backgroundColor);
		textArea.setEditable(false);
		textArea.setFont(textFont);
		textArea.setLineWrap(true);
		textArea.setText(text);
		textArea.setWrapStyleWord(true);
		panel.add(textArea, gbc);
		
		gbc.gridwidth = 1;
		gbc.gridy++;
		label = new JLabel(AppStrings.AUTHOR_);
		label.setFont(textFont);
		panel.add(label, gbc);
		
		gbc.gridx++;
		label = new JLabel(AppStrings.GILBERT_G_LE_BLANC);
		label.setFont(textFont);
		panel.add(label, gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		label = new JLabel(AppStrings.DATE_CREATED_);
		label.setFont(textFont);
		panel.add(label, gbc);
		
		gbc.gridx++;
		label = new JLabel(AppStrings._31_MARCH_2022);
		label.setFont(textFont);
		panel.add(label, gbc);
		
		gbc.gridx = 0;
		gbc.gridy++;
		label = new JLabel(AppStrings.VERSION_);
		label.setFont(textFont);
		panel.add(label, gbc);
		
		gbc.gridx++;
		label = new JLabel(AppStrings._1_0);
		label.setFont(textFont);
		panel.add(label, gbc);
		
		return panel;
	}
	
	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), AppStrings.CANCEL_ACTION);
		ActionMap actionMap = panel.getActionMap();
		actionMap.put(AppStrings.CANCEL_ACTION, cancelAction);
		
		JButton button = new JButton(AppStrings.CANCEL);
		button.addActionListener(cancelAction);
		panel.add(button);
		
		return panel;
	}
	
	private class CancelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent event) {
			dispose();
		}
		
	}

}
