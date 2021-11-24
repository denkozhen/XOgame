package com.testTask.XOgame.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum XO {
    X(1), O(2), DRAW(3);

    private Integer value;
}
