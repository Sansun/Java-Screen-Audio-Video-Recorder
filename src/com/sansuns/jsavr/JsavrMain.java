package com.sansuns.jsavr;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/* 
 * JSAVR v0.1 - Java Screen Audio Video Recorder
 * Author: Sansun Suraj   Date: Sep 11, 2015.
 * JsavrMain.java - Is the main program that start the GUI frame and the rest of the code.
 * In this version, I coded the UI framework, which is just single frame having  a panel with 
 * two buttons and a status message. Buttons are not wired to any working functions yet. 
 *  
 */

public class JsavrMain extends JFrame {

	public static final Font JSAVR_FONT = new Font("Courier New", Font.BOLD, 8);
	public static final String JSAVR_FRAME_TITLE = "JSAVR v0.1";
	public static final String JSAVR_PANEL_TITLE = "Java Screen A/V Recorder";
	public static final String JSAVR_LABEL_INI = "Initialized...";

	public JsavrMain() {

		JButton buttonStart = new JButton("Start");
		JButton buttonStop = new JButton("Stop");

		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(10, 20, 10, 20);

		mainPanel.add(buttonStart, constraints);
		mainPanel.add(buttonStop, constraints);

		JLabel labelStatus = new JLabel(JSAVR_LABEL_INI);
		constraints.gridx = 0;
		constraints.gridy = 1;
		mainPanel.add(labelStatus, constraints);
		labelStatus.setVisible(true);

		buttonStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		// set border for the panel
		mainPanel.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createEtchedBorder(), JSAVR_PANEL_TITLE));

		// add the panel to this frame
		add(mainPanel);

		pack();
		setLocationRelativeTo(null);
		buttonStop.setEnabled(false);

		buttonStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}

		});

	}

	public static void main(String[] args) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				JFrame frame = new JsavrMain();

				frame.setFont(JSAVR_FONT);
				frame.setTitle(JSAVR_FRAME_TITLE);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocation(0, 0);
				frame.setVisible(true);

			}

		});

	}

}