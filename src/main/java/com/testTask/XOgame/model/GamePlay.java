package com.testTask.XOgame.model;

import lombok.Data;

@Data
public class GamePlay {

    private XO type;
    private Integer coordinateX;
    private Integer coordinateY;
    private String gameId;
    private Player login;


}
