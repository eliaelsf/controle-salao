package com.fabiorHair.controlesalao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

import com.fabioHair.controlesalao.properties.FileStorageProperties;

@SpringBootApplication
@ComponentScan
@EnableAsync
@EnableConfigurationProperties({
	FileStorageProperties.class
})
public class ControleSalaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ControleSalaoApplication.class, args);
	}
}
