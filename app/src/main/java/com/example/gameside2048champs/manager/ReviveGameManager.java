package com.example.gameside2048champs.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviveGameManager {
    private List<List<Long>> gameMatrixPostToolUse;

    public ReviveGameManager(List<List<Long>> givenGameMatrix) {
        gameMatrixPostToolUse = new ArrayList<>();

        for (int currentRow = 0; currentRow < givenGameMatrix.size(); currentRow++) {
            List<Long> gameMatrixRow = new ArrayList<>();
            for (int currentColumn = 0; currentColumn < givenGameMatrix.get(currentRow).size(); currentColumn++) {
                gameMatrixRow.add(givenGameMatrix.get(currentRow).get(currentColumn));
            }
            gameMatrixPostToolUse.add(gameMatrixRow);
        }
    }

    public List<List<Long>> gameMatrixPostReviveGame() {
        int totalRows = gameMatrixPostToolUse.size(), totalColumns = gameMatrixPostToolUse.get(0).size();

        // Collecting all the values
        List<Long> allGameTiles = new ArrayList<>();
        List<Long> allGameTileValues = new ArrayList<>();
        List<Long> topThreeGameTileValues = new ArrayList<>();
        for (int currentRow = 0; currentRow < gameMatrixPostToolUse.size(); currentRow++) {
            for (int currentColumn = 0; currentColumn < gameMatrixPostToolUse.get(0).size(); currentColumn++) {
                if (gameMatrixPostToolUse.get(currentRow).get(currentColumn) != 0
                        && gameMatrixPostToolUse.get(currentRow).get(currentColumn) != -1) {
                    long currentElement = gameMatrixPostToolUse.get(currentRow).get(currentColumn);
                    allGameTiles.add(currentElement);
                    if (!allGameTileValues.contains(currentElement)) {
                        allGameTileValues.add(currentElement);
                        topThreeGameTileValues.add(currentElement);
                    }
                }
            }
        }
        Collections.sort(topThreeGameTileValues);

        // Keeping only the top 3 values
        while (topThreeGameTileValues.size() > 3) {
            int removalIndex = 0;
            topThreeGameTileValues.remove(removalIndex);
        }
        allGameTileValues.removeAll(topThreeGameTileValues); // Now allGameTileValues has all values except the top 3
        allGameTiles.removeAll(allGameTileValues);

        // Resetting the board to empty before placing the values
        for (int currentRow = 0; currentRow < gameMatrixPostToolUse.size(); currentRow++) {
            for (int currentColumn = 0; currentColumn < gameMatrixPostToolUse.get(0).size(); currentColumn++) {
                if (gameMatrixPostToolUse.get(currentRow).get(currentColumn) != -1L) {
                    gameMatrixPostToolUse.get(currentRow).set(currentColumn, 0L);
                }
            }
        }

        // Placing all the values
        allGameTiles.sort(Collections.reverseOrder());
        int currentRow = totalRows-1, currentColumn = 0;
        while (allGameTiles.size() > 0) {
            if (gameMatrixPostToolUse.get(currentRow).get(currentColumn) != -1L) {
                gameMatrixPostToolUse.get(currentRow).set(currentColumn, allGameTiles.get(0));
                allGameTiles.remove(allGameTiles.get(0));
            }

            // Moving on to the next possible tile
            if (currentColumn + 1 == totalColumns) {
                currentRow--; currentColumn = 0;
            } else {
                currentColumn++;
            }
        }

        return gameMatrixPostToolUse;
    }
}
