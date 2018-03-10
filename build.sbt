name := "kafkaviewer"
organization := "com.bizzard.telemetry.kafkaviewer"
scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
	"org.apache.kafka" % "kafka-clients" % "0.10.2.0",
	"org.slf4j" % "slf4j-api" % "1.7.5",
	"org.slf4j" % "slf4j-log4j12" % "1.7.5"
)
