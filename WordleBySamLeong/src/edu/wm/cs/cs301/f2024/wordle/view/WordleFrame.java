package edu.wm.cs.cs301.f2024.wordle.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import edu.wm.cs.cs301.f2024.wordle.model.AbsurdleModel;
import edu.wm.cs.cs301.f2024.wordle.model.AppStrings;
import edu.wm.cs.cs301.f2024.wordle.model.MixedModel;
import edu.wm.cs.cs301.f2024.wordle.model.Model;
import edu.wm.cs.cs301.f2024.wordle.model.WordleModel;

public class WordleFrame {
	
	private final JFrame frame;
	
	private final KeyboardPanel keyboardPanel;
	
	private final Model model;
	
	private final WordleGridPanel wordleGridPanel;

	public WordleFrame(Model model) {
		this.model = model;
		this.keyboardPanel = new KeyboardPanel(this, model);
		int width = keyboardPanel.getPanel().getPreferredSize().width;
		this.wordleGridPanel = new WordleGridPanel(this, model, width);
		this.frame = createAndShowGUI();
	}
	
	private JFrame createAndShowGUI() {
		JFrame frame =  new JFrame(AppStrings.WORDLE); 
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setJMenuBar(createMenuBar());
		frame.setResizable(false);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			 public void windowClosing(WindowEvent event) {
				shutdown();
			}
		});
		
		frame.add(createTitlePanel(), BorderLayout.NORTH);
		frame.add(wordleGridPanel, BorderLayout.CENTER);
		frame.add(keyboardPanel.getPanel(), BorderLayout.SOUTH);
		
		frame.pack();
		frame.setLocationByPlatform(true);
		frame.setVisible(true);
		
		System.out.println(AppStrings.FRAME_SIZE + frame.getSize());
		
		return frame;
	}
	
	private JMenuBar createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu helpMenu = new JMenu(AppStrings.HELP);
		menuBar.add(helpMenu);
		
		JMenuItem instructionsItem = new JMenuItem(AppStrings.INSTRUCTIONS___);
		instructionsItem.addActionListener(event -> new InstructionsDialog(this));
		helpMenu.add(instructionsItem);
		
		JMenuItem aboutItem = new JMenuItem(AppStrings.ABOUT___);
		aboutItem.addActionListener(event -> new AboutDialog(this));
		helpMenu.add(aboutItem);
		
		JMenuItem settingsItem = new JMenuItem(AppStrings.SETTINGS___);
		settingsItem.addActionListener(event -> new SettingsDialog(this));
		helpMenu.add(settingsItem);
		
		return menuBar;
	}
	
	private JPanel createTitlePanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
		
		InputMap inputMap = panel.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), AppStrings.CANCEL_ACTION);
		ActionMap actionMap = panel.getActionMap();
		actionMap.put(AppStrings.CANCEL_ACTION, new CancelAction());
		
		JLabel label = new JLabel(AppStrings.WORDLE);
		if (model instanceof AbsurdleModel) {
			label.setText(AppStrings.ABSURDLE); 
			}
		else if (model instanceof MixedModel) {
			label.setText("Mixed Model"); 
			}
		label.setFont(AppFonts.getTitleFont());
		panel.add(label);
		
		return panel;
	}
	
	public void shutdown() {
		model.saveDataToFile();
		frame.dispose();
		System.exit(0);
	}
	
	public void resetDefaultColors() {
		keyboardPanel.resetDefaultColors();
	}
	
	public void setColor(String letter, Color backgroundColor, Color foregroundColor) {
		keyboardPanel.setColor(letter, backgroundColor, foregroundColor);
	}
	
	public void repaintWordleGridPanel() {
		wordleGridPanel.repaint();
	}

	public JFrame getFrame() {
		return frame;
	}
	
	private class CancelAction extends AbstractAction {

		private static final long serialVersionUID = 1L;

		@Override
		public void actionPerformed(ActionEvent event) {
			shutdown();
		}
		
	}
	
	public KeyboardPanel getKeyboardPanel() {
		return this.keyboardPanel;
	}
	
	public JButton[] getButtons() {
		return this.keyboardPanel.getButtons();
	}

}
