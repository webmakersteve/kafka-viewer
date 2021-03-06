package com.blizzard.telemetry.kafkaviewer

import java.awt.image.BufferedImage
import javax.swing.{ImageIcon, JFrame, JLabel}
import java.awt.{BorderLayout, Dimension}
import java.util.concurrent.{Executors, TimeUnit}

import org.slf4j.LoggerFactory

object Application extends App {

  println(args)

  private lazy val frame = openWindow
  private val executor = Executors.newSingleThreadExecutor()
  private val consumer = new Consumer(List("test"))
  private val logger = LoggerFactory.getLogger(getClass)

  executor.submit(new Runnable {
    override def run(): Unit = {
      consumer.poll((bytes: Array[Byte]) => {
        val side = Math.floor(Math.sqrt(bytes.length)).asInstanceOf[Int]

        //image dimension
        val width = side
        val height = side
        //create buffered image object img
        val img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)

        val totalBytes = bytes.length
        var counter = 0

        def getNextByte: Int = {
          if (counter >= totalBytes) {
            0
          } else {
            var byteAtIndex = bytes(counter).toInt
            if (byteAtIndex < 0) {
              byteAtIndex = 128 + Math.abs(byteAtIndex)
            }
            counter += 1
            byteAtIndex
          }
        }

        for (y <- 0 until height) {
          for (x <- 0 until width) {

            //val a : Int = getNextByte // Alpha
            //val r : Int = getNextByte //red
            //val g : Int = getNextByte //green
            //val b : Int = getNextByte //blue

            //val p : Int = (a<<24) | (r<<16) | (g<<8) | b
            val pixel = 255 << 24 | getNextByte << 16
            img.setRGB(x, y, pixel)
          }
        }

        logger.debug(s"Painting ${bytes.length} bytes...")

        paintWindow(frame, img)
      })
    }
  })

  sys.addShutdownHook {
    logger.info("Got shutdown hook. Shutting down.")
    executor.shutdown()
    consumer.close()
    executor.awaitTermination(10, TimeUnit.SECONDS)
  }

  def paintWindow(frame: JFrame, image: BufferedImage): Unit = {
    //3. Create components and put them in the frame.

    val scaledImage = image.getScaledInstance(100, 100, 0)
    val imageIcon = new ImageIcon(scaledImage)
    val label = new JLabel(imageIcon)

    frame.getContentPane.removeAll()
    frame.getContentPane.add(label, BorderLayout.CENTER)
    frame.pack()
    frame.getContentPane.repaint()
  }

  private def openWindow: JFrame = {
    //1. Create the frame.
    val frame = new JFrame("KafkaViewer")

    //2. Optional: What happens when the frame closes?
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

    //4. Size the frame.
    frame.setMinimumSize(new Dimension(100, 100))
    frame.pack()

    //5. Show it.
    frame.setVisible(true)

    frame
  }
}
