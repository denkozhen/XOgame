package com.testTask.XOgame.controller;


import com.testTask.XOgame.exception.InvalidGameException;
import com.testTask.XOgame.exception.NotFoundException;
import com.testTask.XOgame.model.Game;
import com.testTask.XOgame.model.GamePlay;
import com.testTask.XOgame.model.Player;
import com.testTask.XOgame.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/start")
    public ResponseEntity<Game> start(@RequestBody Player player) {
        log.info("Запрос на старт игры: {}", player);
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @PostMapping("/connect/ToOpponent")
    public ResponseEntity<Game> connectToOpponent(@RequestBody Player player) throws NotFoundException {
        log.info("Подключение к оппоненту: {}", player);
        return ResponseEntity.ok(gameService.connectToOpponentGame(player));
    }

    @PostMapping("/gameplay")
    public ResponseEntity<Game> gamePlay(@RequestBody GamePlay request) throws NotFoundException, InvalidGameException {
        log.info("Игровой процесс: {}", request);
        Game game = gameService.gamePlay(request);
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + game.getGameId(), game);
        return ResponseEntity.ok(game);
    }
}
