/*
 * Copyright 1999-2004 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 *
 */

package edu.cmu.sphinx.demo.buildrecsyst10team4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

import edu.cmu.sphinx.frontend.util.Microphone;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;

/**
 * A simple HelloWorld demo showing a simple speech application built using
 * Sphinx-4. This application uses the Sphinx-4 endpointer, which automatically
 * segments incoming audio into utterances and silences.
 */
public class BuildRecSys extends JFrame {

	private static int POINTER = 0;
	private int SCORE = 0;
	private int progressBarCtr = 0;
	private final static Font labelFont = new Font("SanSerif", Font.BOLD, 16);
	private final static Color backgroundColor = new Color(0xFF, 0xFF, 0xFF);
	private final static Color boxColor = new Color(0x00, 0x00, 0x00);

	private JTextField messageTextField;
	private JButton speakButton;
	private JLabel wordToBeShownLabel;
	private JLabel scoreLabel;
	private JProgressBar progressBar;
	private JButton nextButton;
	private JLabel wowLabel;
	
	public ArrayList<String> germanDictionary;

	public BuildRecSys() {
		super("Mini Project One - By Team04");
		setSize(900, 500);
		setDefaultLookAndFeelDecorated(true);
		getContentPane().add(createMainPanel(), BorderLayout.CENTER);

		germanDictionary = fillData();

		nextButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					getWordByBuffer();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// String textInTextbox = messageTextField.getText();
				// if(textInTextbox.toLowerCase().trim().equals(germanDictionary.get(POINTER)))
				// {
				// successFunction();
				// } else {
				// failFunction();
				// }

			}

			private void failFunction() {
				SCORE -= 2;
				POINTER++;
				go(POINTER);

			}

			private void successFunction() {
				SCORE += 10;
				progressBar.setValue(progressBarCtr++);
				POINTER++;
				go(POINTER);

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

	private ArrayList<String> fillData() {
		ArrayList<String> list = new ArrayList<String>();
		list.add("eins");
		list.add("zwei");
		list.add("drei");//
		list.add("vier");//
		list.add("funf");
		list.add("sechs");//h c
		list.add("sieben");//
		list.add("acht");// ch
		list.add("neun");
		list.add("zehn");//h
		list.add("elf");//
		list.add("zwolf");//
		list.add("dreizehn");//
		list.add("vierzehn");//
		list.add("funfzehn");//
		list.add("sechzehn");//
		list.add("siebzehn");//
		list.add("achtzehn");//
		list.add("neunzehn");//
		list.add("zwanzig");//

		return list;
	}

	private JPanel createMainPanel() {
		JPanel mainPanel = getJPanel(new FlowLayout(FlowLayout.LEFT));
		speakButton = new JButton("Speak");
		speakButton.setEnabled(true);
		mainPanel.add(speakButton);
		wordToBeShownLabel = new JLabel();
		mainPanel.add(wordToBeShownLabel);
		progressBar = new JProgressBar(0, 20);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setString("Progress Bar String");
		mainPanel.add(progressBar);
		nextButton = new JButton("Next");
		mainPanel.add(nextButton);
		scoreLabel = new JLabel();
		mainPanel.add(scoreLabel);
		messageTextField = new JTextField(25);
		mainPanel.add(messageTextField);
		wowLabel = new JLabel();
		mainPanel.add(wowLabel);

		return mainPanel;
	}

	/**
	 * Returns a JPanel with the given layout and custom background color.
	 *
	 * @param layout
	 *            the LayoutManager to use for the returned JPanel
	 *
	 * @return a JPanel
	 */
	private JPanel getJPanel(LayoutManager layout) {
		JPanel panel = getJPanel();
		panel.setLayout(layout);
		return panel;
	}

	/**
	 * Returns a JPanel with the custom background color.
	 *
	 * @return a JPanel
	 */
	private JPanel getJPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(backgroundColor);
		return panel;
	}

	public void go(int i) {

		if (i <= 20) {
			String germanWord = germanDictionary.get(i);
			updateUI(germanWord, germanWord.length(), i);
		} else {
			nextButton.setEnabled(false);
		}
	}

	private void updateUI(String germanWord, int length, int i) {
		// TODO Auto-generated method stub
		changeDisplayGermanWord(germanWord);
		updateScore(SCORE);

	}

	private void updateScore(int score) {
		// TODO Auto-generated method stub
		scoreLabel.setText(score + "");

	}

	private void changeDisplayGermanWord(String germanWord) {
		// TODO Auto-generated method stub
		this.wordToBeShownLabel.setText(germanWord);

	}

	private void getWordByBuffer() throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String currentWord = germanDictionary.get(POINTER);
		int currentWordSize = germanDictionary.get(POINTER).length();
		int correctLettersSoFar = 0;
		wowLabel.setText("");
		for (int i = 0; i < currentWordSize; i++){
			for (int j = 0; j < 3; j++) {
				char c = (char) br.readLine().toCharArray()[0];
				if (c == currentWord.charAt(i)) {
					System.out.println("true!!!");
					SCORE += 10;
					correctLettersSoFar++;
					break;
				} else {
					System.out.println("false! we are expecting:" + currentWord.charAt(i));
					SCORE -= 2;

				}
			}
		}
		
		if(correctLettersSoFar == currentWordSize) {
			progressBar.setValue(progressBarCtr++);
			wowLabel.setText("WOW!");
			
		}
		
		POINTER++;
		go(POINTER);
	}

	public static void main(String[] args) {

		BuildRecSys x = new BuildRecSys();
		x.setVisible(true);
		x.go(POINTER);

	}
}