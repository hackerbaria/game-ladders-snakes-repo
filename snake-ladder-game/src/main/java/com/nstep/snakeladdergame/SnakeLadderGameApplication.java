package com.nstep.snakeladdergame;

import com.nstep.snakeladdergame.controller.GameController;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class SnakeLadderGameApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(SnakeLadderGameApplication.class, args);
	}

	private GameController gameController;

	public SnakeLadderGameApplication(GameController gameController) {
		this.gameController = gameController;
	}

	@Override
	public void run(String... args) throws IOException {
		gameController.start();

	}
}


