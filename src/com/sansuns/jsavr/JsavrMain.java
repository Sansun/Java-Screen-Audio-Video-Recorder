package com.sansuns.jsavr;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.sansuns.jsavr.merge.MergeAudioVideo;
import com.sansuns.jsavr.runner.AudioRecorder;
import com.sansuns.jsavr.runner.VideoRecorder;

/* 
 * JSAVR v0.3 - Java Screen Audio Video Recorder
 * Author: Sansun Suraj   Date: Sep 11, 2015.
 * JsavrMain.java - Here I added the two new classes 'AudioRecorder.java' and 'VideoRecorder.jar' for 
 * the audio recording and video recording.
 *  VideoRecorder.java is using Xuggler API which basically written on FFMpeg library. This code implements Runnable and it overwrides the run method.
 *  Used AWT Robot to capture the screen frames and using xuggler the captured frames are encoded into video.
 *  The thread is put into sleep for the time determine by the preset frame rate and the whole process is looped
 *  until the thread is interrupted.   
 */

@SuppressWarnings("serial")
public class JsavrMain extends JFrame {

	public static final Font JSAVR_FONT = new Font("Courier New", Font.BOLD, 8);
	public static final String JSAVR_FRAME_TITLE = "JSAVR v0.3";
	public static final String JSAVR_PANEL_TITLE = "Java Screen A/V Recorder";
	
	public static final String JSAVR_BUTTON_START = "Start";
	public static final String JSAVR_BUTTON_STOP  = "Stop";
	
	public static final String JSAVR_LABEL_INI     = "Initialized...";
	public static final String JSAVR_LABEL_STARTED = "Started...";
	public static final String JSAVR_LABEL_STOPPED = "Stopped...";
	
	public static final String JSAVR_OUT_FILE_DIR   = "./res";
	public static final String JSAVR_OUT_FILE_AUDIO =  "./res/outputAudio.wav";
	public static final String JSAVR_OUT_FILE_VIDEO =   "./res/outputVideo.mp4";
	public static final String JSAVR_OUT_FILE_MERGED =  "./res/outputAuVuMerged.flv";

	static JFrame frame;
	static JPanel mainPanel;
	static JLabel labelStatus;
	static JButton buttonStart;
	static JButton buttonStop;
	
	static VideoRecorder vr;
	static Thread videoThread;
	
	static AudioRecorder ar;
	static Thread audioThread;
	
	static Logger logger = Logger.getLogger(JsavrMain.class);

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
				
				//wait 6 seconds before merge
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//merge();
				
			}
		});
	}

	public static void startAction(){
		
		
	    labelStatus.setVisible(true);
		labelStatus.setText(JSAVR_LABEL_STARTED);
		buttonStop.setEnabled(true); 
		buttonStart.setEnabled(false); 
		
		frame.pack();
		
		vr = new VideoRecorder(JSAVR_OUT_FILE_VIDEO);
		videoThread = new Thread(vr, "Video recording process started");
		videoThread.start();
		
		File outputAudioFile = new File(JSAVR_OUT_FILE_AUDIO);

		AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 22050.0F, 16, 2, 4, 22050.0F, false);

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
		TargetDataLine targetDataLine = null;
		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
			targetDataLine.open(audioFormat);
		} catch (LineUnavailableException e) {
			logger.info("Unable to acquire an audio recording line");
			e.printStackTrace();
			System.exit(1);
		}

		logger.info("AudioFormat settings: " + audioFormat.toString());

		AudioFileFormat.Type targetType = AudioFileFormat.Type.WAVE;

		ar = new AudioRecorder(targetDataLine, targetType, outputAudioFile);
		
		audioThread = new Thread(ar, "Audio recording process started");
		ar.startAudioCapture(audioThread);
		
		logger.info("Starting the audio recording now...");
		
		
		
	}
	
	public static void stopAction(){
		
		labelStatus.setVisible(true);
		labelStatus.setText(JSAVR_LABEL_STOPPED);
		buttonStop.setEnabled(false); 
		buttonStart.setEnabled(true); 
		
		frame.pack();
		videoThread.interrupt();
		logger.info("Video recording stopped.");
		ar.stopAudioCapture(audioThread);
		logger.info("Audio recording stopped.");
		merge();
		logger.info("Audio Video files are merged.");
	}
	
	public static void merge() {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MergeAudioVideo mav = new MergeAudioVideo();
		File audioFile = new File(JSAVR_OUT_FILE_AUDIO);
		File videoFile = new File(JSAVR_OUT_FILE_VIDEO);
		if(audioFile.exists() && videoFile.exists()){
			//mav.mergeAudioVideo(JSAVR_OUT_FILE_AUDIO, JSAVR_OUT_FILE_VIDEO, JSAVR_OUT_FILE_MERGED);
			mav.mergeAudioVideo(audioFile.getAbsolutePath(), videoFile.getAbsolutePath(), JSAVR_OUT_FILE_MERGED);
		}else{
			logger.info("AudioFile " + JSAVR_OUT_FILE_AUDIO + "; not found.");
			 
		}
	}
	
	 
	public static void main(String[] args) {
		
		//create the res dir in the current folder if not exist
				
		File f = new File(JSAVR_OUT_FILE_DIR);
		if(!f.exists()){
			f.mkdir();
			logger.info(JSAVR_OUT_FILE_DIR + " subdir created...");
		}
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
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
