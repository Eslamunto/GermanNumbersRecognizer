package edu.cmu.sphinx.demo.buildrecsyst10team4;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class BuildRecSys extends JFrame {

	private int wordColor = 0;
	private int charPosition = 0;
	private int noOfTrials = 0;
	private String moveOn;
	private static int POINTER = 0;
	private int SCORE = 0;
	private int progressBarCtr = 0;
	private final static Color backgroundColor = new Color(0xFF, 0xFF, 0xFF);

	private JTextField messageTextField;
	private JButton speakButton;
	private JLabel wordToBeShownLabel;
	private JLabel scoreLabel;
	private JLabel messageToUser;
	private JProgressBar progressBar;
	private JButton nextButton;
	private JLabel wowLabel;
	private BuildRecSysRecognizer recognizer;

	public ArrayList<String> germanDictionary;

	public BuildRecSys() throws IOException {
		super("Mini Project One - By Team04");
		setSize(400, 400);
		setDefaultLookAndFeelDecorated(true);
		getContentPane().add(createMainPanel(), BorderLayout.CENTER);

		germanDictionary = fillData();
		recognizer = new BuildRecSysRecognizer();

		recognizer.startup();

		speakButton.addMouseListener(new MouseListener() {
			@Override
			public void mousePressed(MouseEvent e) {

				System.out.println("MousePressed+++");
				System.out.println("Start speaking. Press Ctrl-C to quit.\n");

				recognizer.run();

				System.out.println(recognizer.getTheCharacter());

				micHelper();
			}

			private void micHelper() {

				String currentWord = germanDictionary.get(POINTER);
				int currentWordSize = germanDictionary.get(POINTER).length();
				int correctLettersSoFar = 0;

				char c = recognizer.getTheCharacter();
				if (c == currentWord.charAt(charPosition) && noOfTrials < 3) {
					// System.out.println("true!!!");
					updateMessageToUser("<html><font color='green'>True!</font> Move on to the next letter.</html>");
					SCORE += 10;
					correctLettersSoFar++;
					charPosition++;
					noOfTrials = 0;
				} else {
					// System.out.println("false! we are expecting:"
					// + currentWord.charAt(charPosition));
					if ((2 - noOfTrials) == 0) {
						moveOn = "Move on to the next letter";
					} else {
						moveOn = "";
					}
					updateMessageToUser("<html><font color='red'>False!</font> we are expecting: "
							+ currentWord.charAt(charPosition)
							+ ". You have "
							+ (2 - noOfTrials)
							+ " trials!<br>"
							+ moveOn
							+ "</html>");

					SCORE -= 2;
					noOfTrials++;
					if (noOfTrials > 2) {
						noOfTrials = 0; // reset the number of trials and we are
										// ready to move on to the next WHATEVER
						charPosition++;
						moveOn = "";
					}
				}

				if (charPosition == currentWordSize) {
					charPosition = 0;
					POINTER++;
					if (POINTER < 20) {
						updateMessageToUser(" ");
						go(POINTER);

						if (correctLettersSoFar == currentWordSize) {
							progressBarCtr++;

							if (progressBarCtr <= 5) {
								progressBar.setForeground(Color.red);
							} else if (progressBarCtr > 5
									&& progressBarCtr <= 10) {
								progressBar.setForeground(Color.orange);
							} else if (progressBarCtr > 10
									&& progressBarCtr <= 15) {
								progressBar.setForeground(Color.yellow);
							} else if (progressBarCtr > 15
									&& progressBarCtr <= 20) {
								progressBar.setForeground(Color.green);
							}

							progressBar.setValue(progressBarCtr);
						}
					} else {
						wowLabel.setText("WOW!!");
					}

				}

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("MouseReleased---");
			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
			}

		});

		nextButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					getWordByBuffer();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
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
		list.add("drei");
		list.add("vier");
		list.add("funf");
		list.add("sechs");
		list.add("sieben");
		list.add("acht");
		list.add("neun");
		list.add("zehn");
		list.add("elf");
		list.add("zwolf");
		list.add("dreizehn");
		list.add("vierzehn");
		list.add("funfzehn");
		list.add("sechzehn");
		list.add("siebzehn");
		list.add("achtzehn");
		list.add("neunzehn");
		list.add("zwanzig");

		return list;
	}

	private JPanel createMainPanel() {
		JPanel mainPanel = new JPanel();
		mainPanel.setBackground(backgroundColor);
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		wordToBeShownLabel = new JLabel();
		mainPanel.add(wordToBeShownLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));

		speakButton = new JButton("Press To Speak");
		speakButton.setEnabled(true);
		mainPanel.add(speakButton);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));

		messageToUser = new JLabel();
		messageToUser.setText("");
		mainPanel.add(messageToUser);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 20)));

		scoreLabel = new JLabel();
		mainPanel.add(scoreLabel);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));

		progressBar = new JProgressBar(0, 20);
		progressBar.setValue(0);
		progressBar.setStringPainted(true);
		progressBar.setString("Progress");
		mainPanel.add(progressBar);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 20)));
		mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

		mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));
		nextButton = new JButton("Test the backend");
		mainPanel.add(nextButton);
		mainPanel.add(Box.createRigidArea(new Dimension(10, 10)));

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
		changeDisplayGermanWord(germanWord, i + 1);
		updateScore(SCORE);

	}

	private void updateScore(int score) {
		// TODO Auto-generated method stub
		scoreLabel.setText("Score: " + score + "");
	}

	private void updateMessageToUser(String message) {
		messageToUser.setText(message);
	}

	private void changeDisplayGermanWord(String germanWord, int wordNumber) {
		// TODO Auto-generated method stub
		if (wordColor == 0) {
			this.wordToBeShownLabel.setText(String.format(
					"<html>Word number %d: <font color='red'>%s</font></html>",
					wordNumber, germanWord));
			wordColor = 1;
		} else {
			this.wordToBeShownLabel
					.setText(String
							.format("<html>Word number %d: <font color='green'>%s</font></html>",
									wordNumber, germanWord));
			wordColor = 0;
		}

	}

	private void getWordByBuffer() throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String currentWord = germanDictionary.get(POINTER);
		int currentWordSize = germanDictionary.get(POINTER).length();
		int correctLettersSoFar = 0;
		for (int i = 0; i < currentWordSize; i++) {
			for (int j = 0; j < 3; j++) {
				char c = (char) br.readLine().toCharArray()[0];
				if (c == currentWord.charAt(i)) {
					System.out.println("true!!!");
					SCORE += 10;
					correctLettersSoFar++;
					break;
				} else {
					System.out.println("false! we are expecting:"
							+ currentWord.charAt(i));
					SCORE -= 2;
				}
			}
		}

		if (correctLettersSoFar == currentWordSize) {
			progressBarCtr++;
			if (progressBarCtr <= 5) {
				progressBar.setForeground(Color.red);
			} else if (progressBarCtr > 5 && progressBarCtr <= 10) {
				progressBar.setForeground(Color.orange);
			} else if (progressBarCtr > 10 && progressBarCtr <= 15) {
				progressBar.setForeground(Color.yellow);
			} else if (progressBarCtr > 15 && progressBarCtr <= 20) {
				progressBar.setForeground(Color.green);
			}
			progressBar.setValue(progressBarCtr);
		}

		POINTER++;
		if (POINTER > 20) {
			wowLabel.setText("WOW!");
		} else {
			go(POINTER);
		}

	}

	public static void main(String[] args) throws IOException {
		BuildRecSys x = new BuildRecSys();
		x.setVisible(true);
		x.go(POINTER);

	}
}

