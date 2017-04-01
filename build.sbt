name := "kafkaviewer"

organization := "com.bizzard.telemetry.kafkaviewer"

resolvers ++= Seq(
)

libraryDependencies ++= Seq(
	"org.apache.kafka" % "kafka-clients" % "0.9.0.1",
	"org.slf4j" % "slf4j-api" % "1.7.5",
	"org.slf4j" % "slf4j-log4j12" % "1.7.5"
)
