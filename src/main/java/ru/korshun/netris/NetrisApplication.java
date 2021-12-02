package ru.korshun.netris;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class NetrisApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetrisApplication.class, args);

	}

}
