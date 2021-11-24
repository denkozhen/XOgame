package com.testTask.XOgame.service;


import com.testTask.XOgame.exception.InvalidGameException;
import com.testTask.XOgame.exception.NotFoundException;
import com.testTask.XOgame.model.*;
import com.testTask.XOgame.storage.GameStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
public class GameService {

    public Game createGame(Player player) {
        Game game = new Game();
        game.setBoard(new int[3][3]);
        game.setGameId(UUID.randomUUID().toString());
        game.setFirstPlayer(player);
        game.setStatus(GameStatus.NEW);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game connectToOpponentGame(Player secondPlayer) throws NotFoundException {
        Game game = GameStorage.getInstance().getGames().values().stream()
                .filter(it -> it.getStatus().equals(GameStatus.NEW))
                .findFirst().orElseThrow(() -> new NotFoundException("Игра не найдена"));
        game.setSecondPlayer(secondPlayer);
        game.setStatus(GameStatus.IN_PROGRESS);
        GameStorage.getInstance().setGame(game);
        return game;
    }

    public Game gamePlay(GamePlay gamePlay) throws NotFoundException, InvalidGameException {
        if (!GameStorage.getInstance().getGames().containsKey(gamePlay.getGameId())) {
            throw new NotFoundException("Игра не найдена");
        }

        Game game = GameStorage.getInstance().getGames().get(gamePlay.getGameId());
        if (game.getStatus().equals(GameStatus.FINISHED)) {
            throw new InvalidGameException("Игра окончена");
        }

        int[][] board = game.getBoard();
        board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = gamePlay.getType().getValue();

        Boolean xWinner = checkWinner(game.getBoard(), XO.X);
        Boolean oWinner = checkWinner(game.getBoard(), XO.O);

        if (xWinner) {
            game.setWinner(XO.X);
        }
        if (oWinner) {
            game.setWinner(XO.O);
        }

        Boolean draw = boardIsFull(game.getBoard());
        if (!xWinner && !oWinner && draw){
            game.setWinner(XO.DRAW);
        }

        game.setLastMove(gamePlay.getType());

        GameStorage.getInstance().setGame(game);
        return game;
    }

    private Boolean boardIsFull(int[][] board) {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                if (board[i][j] == 0)
                    return false;
        return true;
    }


    private Boolean checkWinner(int[][] board, XO xo) {
        int[] boardArray = new int[9];
        int counterIndex = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardArray[counterIndex] = board[i][j];
                counterIndex++;
            }
        }

        int[][] winCombinations = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
        for (int i = 0; i < winCombinations.length; i++) {
            int counter = 0;
            for (int j = 0; j < winCombinations[i].length; j++) {
                if (boardArray[winCombinations[i][j]] == xo.getValue()) {
                    counter++;
                    if (counter == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


}
