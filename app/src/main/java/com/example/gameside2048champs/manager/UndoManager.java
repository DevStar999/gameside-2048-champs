package com.example.gameside2048champs.manager;

import android.util.Pair;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UndoManager {
    private final int undoLimit;
    private int movesSinceUndoLimit;
    private boolean undoUsed;
    // We will show the option to user to use combo undo only if the user loses game in ads version or to premium user
    private boolean comboUndoUsed;
    /*  Using Deque instead of a Stack, as Stack is legacy.
        The first element in the pair Integer is to save and the previous score and
        the second element is to save the game matrix itself */
    private Deque<Pair<Integer, List<List<Integer>>>> gameMatrixPreviousStates;


    public UndoManager() {
        undoLimit = 5;
        movesSinceUndoLimit = 0;
        undoUsed = true;
        comboUndoUsed = false;
        gameMatrixPreviousStates = new ArrayDeque<>();
    }

    public Pair<Integer, List<List<Integer>>> undoToPreviousState() {
        movesSinceUndoLimit--;
        undoUsed = true;
        return gameMatrixPreviousStates.pollFirst();
    }

    public Pair<Integer, List<List<Integer>>> undoTillUndoLimit() {
        movesSinceUndoLimit = 0;
        undoUsed = true;
        comboUndoUsed = true;
        for (int i = 1; i < undoLimit; i++) {
            gameMatrixPreviousStates.pollFirst();
        }
        return gameMatrixPreviousStates.pollFirst();
    }

    public void saveStatePostMove(int score, List<List<Integer>> gameMatrix) {
        undoUsed = false;
        if (movesSinceUndoLimit == 5) {
            gameMatrixPreviousStates.pollLast();
        } else {
            movesSinceUndoLimit++;
        }
        gameMatrixPreviousStates.offerFirst(new Pair<>(score, gameMatrix));
    }
}
