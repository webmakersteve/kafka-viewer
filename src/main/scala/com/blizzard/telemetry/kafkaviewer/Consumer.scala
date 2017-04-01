package com.blizzard.telemetry.kafkaviewer

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties
import scala.collection.JavaConversions._

class Consumer(topics: List[String]) {

	val config = new Properties();
	config.put("bootstrap.servers", "localhost:9092");
	config.put("group.id", "test");
	config.put("auto.offset.reset", "earliest")
	config.put("enable.auto.commit", "true");
	config.put("auto.commit.interval.ms", "1000");
	config.put("session.timeout.ms", "30000");
	config.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
	config.put("value.deserializer", "org.apache.kafka.common.serialization.ByteArrayDeserializer");

	val consumer = new KafkaConsumer[String, Array[Byte]](config);
	consumer.subscribe(topics);

	def consume(callback: (Array[Byte]) => Unit) : Unit = {
		while (true) {
			val records = consumer.poll(100);
			for (record <- records) {
				callback(record.value())
			}
		}
	}

}
