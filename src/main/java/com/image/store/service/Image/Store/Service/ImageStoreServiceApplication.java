package com.image.store.service.Image.Store.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootApplication
@EnableCaching
@ComponentScan({"controller","service"})
public class ImageStoreServiceApplication  implements ApplicationRunner {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

	public static void main(String[] args) {

	    SpringApplication.run(ImageStoreServiceApplication.class, args);
	}
    @KafkaListener(topics = "first_topic", groupId = "my-first-application")
    public void listen(String message) {
        System.out.println("Received Messasge in group - group-id: " + message);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        sendMessage("Hi Welcome to Spring For Apache Kafka");
    }


    public void sendMessage(String msg) {
        kafkaTemplate.send("first_topic", msg);
    }
}
