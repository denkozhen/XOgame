package com.testTask.XOgame.controller;


import com.testTask.XOgame.model.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private String gameId;
}
