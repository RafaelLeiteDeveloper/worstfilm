package com.rafael.leite.io.tecnico;

import com.rafael.leite.io.tecnico.port.application.file.FileLoad;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class ApiApplication implements CommandLineRunner {

	private final FileLoad fileLoad;

	@Override
	public void run(String... args){
		this.fileLoad.runDatabaseLoad();
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
