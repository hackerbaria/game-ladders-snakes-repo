package com.nstep.snakeladdergame.service;


import com.nstep.snakeladdergame.model.Board;
import com.nstep.snakeladdergame.model.Ladder;
import com.nstep.snakeladdergame.model.Player;
import com.nstep.snakeladdergame.model.Snake;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SnakeAndLadderService {


    private Board snakeAndLadderBoard;
    private int initialNumberOfPlayers;
    private Queue<Player> players;

    public SnakeAndLadderService() {
    }

    public SnakeAndLadderService(int boardSize) {
        this.snakeAndLadderBoard = new Board(boardSize);
        this.players = new LinkedList<>();
    }

    public void setSnakeAndLadderBoard(int boardSize) {
        this.snakeAndLadderBoard = new Board(boardSize);

    }




    /**
     * ==================Initialize board==================
     */

    public void setPlayers(List<Player> players) {
        this.players = new LinkedList<>();
        this.initialNumberOfPlayers = players.size();
        Map<String, Integer> playerPieces = new HashMap<>();
        for (Player player : players) {
            this.players.add(player);
            playerPieces.put(player.getId(), 0); //Each player has a piece which is initially kept outside the board (i.e., at position 0).
        }
        snakeAndLadderBoard.setPlayerPieces(playerPieces); //  Add pieces to board
    }

    public void setSnakes(List<Snake> snakes) {
        snakeAndLadderBoard.setSnakes(snakes); // Add snakes to board
    }

    public void setLadders(List<Ladder> ladders) {
        snakeAndLadderBoard.setLadders(ladders); // Add ladders to board
    }

    /**
     * ==========Core business logic for the game==========
     */

    private int getNewPositionAfterGoingThroughSnakesAndLadders(int newPosition) {
        int previousPosition;

        do {
            previousPosition = newPosition;
            for (Snake snake : snakeAndLadderBoard.getSnakes()) {
                if (snake.getStart() == newPosition) {
                    System.out.println("oops, you have just touched the head of the snake at position: "+ snake.getStart() + " ,GO DOWN!");
                    newPosition = snake.getEnd(); // Whenever a piece ends up at a position with the head of the snake, the piece should go down to the position of the tail of that snake.
                }
            }

            for (Ladder ladder : snakeAndLadderBoard.getLadders()) {
                if (ladder.getStart() == newPosition) {
                    System.out.println("Lucky, you have just touched the start of the ladder at position: " + ladder.getStart() + ", GO UP ");
                    newPosition = ladder.getEnd(); // Whenever a piece ends up at a position with the start of the ladder, the piece should go up to the position of the end of that ladder.
                }
            }
        } while (newPosition != previousPosition); // There could be another snake/ladder at the tail of the snake or the end position of the ladder and the piece should go up/down accordingly.
        return newPosition;
    }

    private void movePlayer(Player player, int positions) {
        int oldPosition = snakeAndLadderBoard.getPlayerPieces().get(player.getId());
        // Based on the dice value, the player moves their piece forward that number of cells.
        int newPosition = oldPosition + positions;

        int boardSize = snakeAndLadderBoard.getSize();

        if (newPosition > boardSize) {
            newPosition = oldPosition; // After the dice roll, if a piece is supposed to move outside position 100, it does not move.
        } else {
            newPosition = getNewPositionAfterGoingThroughSnakesAndLadders(newPosition);
        }

        snakeAndLadderBoard.getPlayerPieces().put(player.getId(), newPosition);

        System.out.println(player.getName() + " rolled a " + positions + " and moved from " + oldPosition +" to " + newPosition);
    }

    private int getTotalValueAfterDiceRolls() {
        return DiceService.roll();
    }

    private boolean hasPlayerWon(Player player) {
        int playerPosition = snakeAndLadderBoard.getPlayerPieces().get(player.getId());
        int winningPosition = snakeAndLadderBoard.getSize();
        return playerPosition == winningPosition; // A player wins if it exactly reaches the position 100 and the game ends there.
    }

    private boolean isGameCompleted() {
        int currentNumberOfPlayers = players.size();
        return currentNumberOfPlayers < initialNumberOfPlayers;
    }

    public void play() {
        while (!isGameCompleted()) {
            int totalDiceValue = getTotalValueAfterDiceRolls(); // Each player rolls the dice when their turn comes.
            Player currentPlayer = players.poll();
            movePlayer(currentPlayer, totalDiceValue);
            if (hasPlayerWon(currentPlayer)) {
                System.out.println(currentPlayer.getName() + " wins the game");
                snakeAndLadderBoard.getPlayerPieces().remove(currentPlayer.getId());
            } else {
                players.add(currentPlayer);
            }
        }
    }

}
