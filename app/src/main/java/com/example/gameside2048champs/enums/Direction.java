package com.example.gameside2048champs.enums;

import lombok.Getter;

@Getter
public enum Direction {
    UP(0, -1, -1),
    DOWN(0, +1, +1),
    LEFT(-1, 0, -1),
    RIGHT(0, +1, +1);

    private final int dx;
    private final int dy;
    private final int shift;

    Direction(int dx, int dy, int shift) {
        this.dx = dx;
        this.dy = dy;
        this.shift = shift;
    }
}
