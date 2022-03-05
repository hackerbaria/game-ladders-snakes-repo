package com.nstep.snakeladdergame.controller;

import com.nstep.snakeladdergame.model.Ladder;
import com.nstep.snakeladdergame.model.Player;
import com.nstep.snakeladdergame.model.Snake;
import com.nstep.snakeladdergame.service.SnakeAndLadderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

@Controller
public class GameController {

    @Value("${game.boardSize}")
    private int boardSize;

    private List<Snake> snakes = new ArrayList<>();

    private List<Ladder> ladders = new ArrayList<>();

    private SnakeAndLadderService snakeAndLadderService;

    public GameController(SnakeAndLadderService snakeAndLadderService) {

        this.snakeAndLadderService = snakeAndLadderService;
    }

    public void start() throws IOException {
        System.out.println("Board size: " + boardSize);
        ClassPathResource resource = new ClassPathResource("SnakesMatrix.txt");

        BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        List<String> snakesArray = reader.lines().collect(Collectors.toList());
        int noOfSnakes = Integer.valueOf(snakesArray.get(0));
        System.out.println("There are total " + noOfSnakes + " snakes in this game!" );

        for (int i = 0; i < noOfSnakes; i++) {
            int start = Integer.valueOf(snakesArray.get(i + 1).split(" ")[0]);
            int end = Integer.valueOf(snakesArray.get(i + 1).split(" ")[1]);
            System.out.println("The position snake " + (i+1)  + " -- head: " + start + " tail: " + end);
            snakes.add(new Snake(start, end));
        }

        resource = new ClassPathResource("LaddersMatrix.txt");

        reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
        List<String> laddersArray = reader.lines().collect(Collectors.toList());
        int noOfLadders = Integer.valueOf(laddersArray.get(0));
        System.out.println("There are total " + noOfLadders + " ladders in this game!" );
        for (int i = 0; i < noOfLadders; i++) {
            int start = Integer.valueOf(laddersArray.get(i + 1).split(" ")[0]);
            int end = Integer.valueOf(laddersArray.get(i + 1).split(" ")[1]);
            System.out.println("The position ladder " + (i+1)  + " -- start: " + start + " end: " + end);
            ladders.add(new Ladder(start, end));
        }


        // finished reading from files: snakes and ladders
        // now it's the time for user to input user players information
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the number of Players (1 mean you play with Computer): ");
        int noOfPlayers = scanner.nextInt();
        List<Player> players = new ArrayList<>();

        if(noOfPlayers == 1) {
            players.add(new Player("Computer"));
            System.out.println("Enter your name: ");
            players.add(new Player(scanner.next()));
        } else {
            for (int i = 0; i < noOfPlayers; i++) {
                System.out.println("Enter the name of player " + (i+1) + ": ");
                players.add(new Player(scanner.next()));
            }

        }

        snakeAndLadderService.setSnakeAndLadderBoard(boardSize);
        snakeAndLadderService.setPlayers(players);
        snakeAndLadderService.setSnakes(snakes);
        snakeAndLadderService.setLadders(ladders);

        System.out.println("Yeah, start game!!! ");
        snakeAndLadderService.play();

    }
}
