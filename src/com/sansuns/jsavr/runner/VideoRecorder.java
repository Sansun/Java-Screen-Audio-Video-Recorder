package com.sansuns.jsavr.runner;

import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.IRational;

public class VideoRecorder implements Runnable {

	private static IRational FRAME_RATE = IRational.make(3, 1);

	//private static final String OUT_FILE = "./res/outputVideo.mp4";
	private static IMediaWriter writer = null;
	private String outputVideo;
	

	//static Logger logger = Logger.getLogger(VideoRecorder.class);
	
	public VideoRecorder(String file){
		this.outputVideo = file;
	}
	

	@Override
	public void run() {

		try {
			//final String outFile = OUT_FILE;
			// This is the robot for taking a snapshot of the
			// screen. It's part of Java AWT
			final Robot robot = new Robot();
			final Toolkit toolkit = Toolkit.getDefaultToolkit();
			final Rectangle screenBounds = new Rectangle(
					toolkit.getScreenSize());

			// First, let's make a IMediaWriter to write the file.
			//writer = ToolFactory.makeWriter(outFile);
			writer = ToolFactory.makeWriter(outputVideo);
			// We tell it we're going to add one video stream, with id 0,
			// at position 0, and that it will have a fixed frame rate of
			// FRAME_RATE.
			writer.addVideoStream(0, 0, FRAME_RATE, screenBounds.width,
					screenBounds.height);

			// Now, we're going to loop
			long startTime = System.nanoTime();
			int index = 0;
			while (!Thread.currentThread().isInterrupted()) {

				BufferedImage screen = robot.createScreenCapture(screenBounds);

				// convert to the right image type
				BufferedImage bgrScreen = convertToType(screen,
						BufferedImage.TYPE_3BYTE_BGR);

				// encode the image
				writer.encodeVideo(0, bgrScreen, System.nanoTime() - startTime,
						TimeUnit.NANOSECONDS);

				System.out.println("Encoded image: " + index);

				// sleep for framerate milliseconds
				Thread.sleep((long) (1000 / FRAME_RATE.getDouble()));
				++index;
			}
			// Finally we tell the writer to close and write the trailer if
			// needed
			writer.close();

		} catch (InterruptedException e) {
			// good practice
			Thread.currentThread().interrupt();
			writer.close();

		} catch (Throwable e) {
			Thread.currentThread().interrupt();
			writer.close();
			System.err.println("Recording interrupted... " + e.getMessage());
		}

	}

	public static BufferedImage convertToType(BufferedImage sourceImage,
			int targetType) {
		BufferedImage image;

		// if the source image is already the target type, return the source
		// image

		if (sourceImage.getType() == targetType)
			image = sourceImage;

		// otherwise create a new image of the target type and draw the new
		// image

		else {
			image = new BufferedImage(sourceImage.getWidth(),
					sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}

		return image;
	}

}
