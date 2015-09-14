package com.sansuns.jsavr.runner;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.TargetDataLine;


 
public class AudioRecorder implements Runnable {

	private TargetDataLine dataLine;
	private AudioFileFormat.Type audioFileType;
	private AudioInputStream incomingStream;
	private File generatedFile;

	public AudioRecorder(TargetDataLine line, AudioFileFormat.Type requiredFileType, File file)

	{
		dataLine = line;
		incomingStream = new AudioInputStream(line);
		audioFileType = requiredFileType;
		generatedFile = file;
	}

	public void startAudioCapture(Thread audioThread) {
		dataLine.start();
		audioThread.start()	;
	}

	public void stopAudioCapture(Thread audioThread) {
		dataLine.stop();
		dataLine.close();
		audioThread.interrupt();
		
	}

	@Override
	public void run() {
		try {
			
			AudioSystem.write(incomingStream, audioFileType, generatedFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
