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
 * JSAVR v0.2 - Java Screen Audio Video Recorder
 * Author: Sansun Suraj   Date: Sep 11, 2015.
 * JsavrMain.java - In this version, I add function to the two actionListeners buttonStart and buttonStop.
 * To do that, two new methods are created 'startAction' and 'stopAction' and the logic been added to it. 
 * And then these two methods are called from their respective actionListeners.
 * Also the UI components are moved out to the main constructor method and declared static variables.
 * Finally all the static text messages are move to the top and declared as static String variables. 
 */

@SuppressWarnings("serial")
public class JsavrMain extends JFrame {

	public static final Font JSAVR_FONT = new Font("Courier New", Font.BOLD, 8);
	public static final String JSAVR_FRAME_TITLE = "JSAVR v0.2";
	public static final String JSAVR_PANEL_TITLE = "Java Screen A/V Recorder";
	
	public static final String JSAVR_BUTTON_START = "Start";
	public static final String JSAVR_BUTTON_STOP  = "Stop";
	
	public static final String JSAVR_LABEL_INI     = "Initialized...";
	public static final String JSAVR_LABEL_STARTED = "Started...";
	public static final String JSAVR_LABEL_STOPPED = "Stopped...";
	

	static JFrame frame;
	static JPanel mainPanel;
	static JLabel labelStatus;
	static JButton buttonStart;
	static JButton buttonStop;

	public JsavrMain() {

		buttonStart = new JButton(JSAVR_BUTTON_START);
		buttonStop  = new JButton(JSAVR_BUTTON_STOP);

		mainPanel = new JPanel();
		mainPanel.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.insets = new Insets(10, 20, 10, 20);

		mainPanel.add(buttonStart, constraints);
		mainPanel.add(buttonStop, constraints);

		labelStatus = new JLabel(JSAVR_LABEL_INI);
		constraints.gridx = 0;
		constraints.gridy = 1;
		mainPanel.add(labelStatus, constraints);
		labelStatus.setVisible(true);

		buttonStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				startAction();
				
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
				stopAction();
				
			}
			
		});
			


	}

	public static void startAction(){
	    labelStatus.setVisible(true);
		labelStatus.setText(JSAVR_LABEL_STARTED);
		buttonStop.setEnabled(true); 
		buttonStart.setEnabled(false); 
		
		frame.pack();
	}
	
	public static void stopAction(){
		
		labelStatus.setVisible(true);
		labelStatus.setText(JSAVR_LABEL_STOPPED);
		buttonStop.setEnabled(false); 
		buttonStart.setEnabled(true); 
		
		frame.pack();
		
	}
	
	public static void main(String[] args) {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {

			public void run() {
				frame = new JsavrMain();
				
				frame.setFont(JSAVR_FONT);
				frame.setTitle(JSAVR_FRAME_TITLE);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setLocation(0, 0);
				frame.setVisible(true);

			}

		});
		

	}

}
