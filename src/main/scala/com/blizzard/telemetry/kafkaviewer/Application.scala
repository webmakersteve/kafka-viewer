package com.blizzard.telemetry.kafkaviewer;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.{JFrame,JLabel,ImageIcon}
import java.awt.BorderLayout

object Application {
	def main(args: Array[String]) : Unit = {
		val frame = openWindow

		val consumer = new Consumer(List("test"))

		consumer.consume((bytes: Array[Byte]) => {
			//image dimension
			val width = 640;
			val height = 320;
			//create buffered image object img
			val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			val totalBytes = bytes.size
			var counter = 0

			for(y <- 0 to height - 1) {
			  for(x <- 0 to width - 1) {

					def getNextByte : Int = {
						if (counter >= totalBytes) {
							0
						} else {
							val byteAtIndex = bytes(counter)
							counter+=1
							return byteAtIndex.toInt
						}
					}

			  	// some code goes here...
					val a : Int = getNextByte // Alpha
					val r : Int = getNextByte //red
					val g : Int = getNextByte //green
					val b : Int = getNextByte //blue

					val p : Int = (a<<24) | (r<<16) | (g<<8) | b;
					img.setRGB(x, y, p);
			  }
			}

			paintWindow(frame, img)
		})

	}

	def paintWindow(frame : JFrame, image: BufferedImage) : Unit = {
		//3. Create components and put them in the frame.
		//...create emptyLabel...
		frame.getContentPane().add(new JLabel(new ImageIcon(image)), BorderLayout.CENTER);

		frame.pack();
	}

	def openWindow : JFrame = {
		//1. Create the frame.
		val frame = new JFrame("FrameDemo");

		//2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//4. Size the frame.
		frame.pack();

		//5. Show it.
		frame.setVisible(true);

		return frame
	}
}
