package com.blizzard.telemetry.kafkaviewer

import java.awt.image.BufferedImage
import javax.swing.{ImageIcon, JFrame, JLabel}
import java.awt.{BorderLayout, Dimension}

object Application {
	def main(args: Array[String]) : Unit = {
		val frame = openWindow

		val consumer = new Consumer(List("test"))

		consumer.consume((bytes: Array[Byte]) => {
			val side = Math.floor(Math.sqrt(bytes.length)).asInstanceOf[Int]

			//image dimension
			val width = side
			val height = side
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
							var byteAtIndex = bytes(counter).toInt
              if (byteAtIndex < 0) {
                byteAtIndex = 128 + Math.abs(byteAtIndex)
              }
							counter+=1
							return byteAtIndex
						}
					}

					//val a : Int = getNextByte // Alpha
					//val r : Int = getNextByte //red
					//val g : Int = getNextByte //green
					//val b : Int = getNextByte //blue

					//val p : Int = (a<<24) | (r<<16) | (g<<8) | b;
          val pixel = 255 << 24 | getNextByte << 16
					img.setRGB(x, y, pixel)
			  }
			}

      System.out.println(s"Painting ${bytes.length} bytes...")

			paintWindow(frame, img)
		})

	}

	def paintWindow(frame : JFrame, image: BufferedImage) : Unit = {
		//3. Create components and put them in the frame.

    val scaledImage = image.getScaledInstance(100, 100, 0)
    val imageIcon = new ImageIcon(scaledImage)
    val label = new JLabel(imageIcon)

		frame.getContentPane().removeAll()
		frame.getContentPane().add(label, BorderLayout.CENTER)
		frame.pack()
		frame.getContentPane().repaint()
	}

	def openWindow : JFrame = {
		//1. Create the frame.
		val frame = new JFrame("FrameDemo");

		//2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

		//4. Size the frame.
    frame.setMinimumSize(new Dimension(100, 100))
    frame.pack()

		//5. Show it.
		frame.setVisible(true)

		return frame
	}
}
