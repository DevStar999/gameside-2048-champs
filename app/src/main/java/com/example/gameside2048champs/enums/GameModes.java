package com.example.gameside2048champs.enums;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

/* TODO -> ## Bug ## : The sizes are all traversed using 'dimensions' string for a question mark
           string like '? X ?' so when call to getGameModeEnum() method is made this mode better be
           covered in the last else block, or else the app will crash
*/
@Getter
public enum GameModes {
    SQUARE_4X4(4, 4, "4 X 4", "SQUARE", 2048, true, new ArrayList<>() {{
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
    }}, "square_4x4.png"), // Total 16 cells.

    SQUARE_5X5(5,5,"5 X 5","SQUARE",4096, true, new ArrayList<>() {{
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
    }}, "square_5x5.png"), // Total 25 cells.

    SQUARE_6X6(6,6,"6 X 6","SQUARE",8192, true, new ArrayList<>() {{
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
    }}, "square_6x6.png"), // Total 36 cells.

    RECTANGLE_4X5(4,5,"4 X 5","RECTANGLE",2048, true, new ArrayList<>() {{
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
    }}, "rectangle_4x5.png"), // Total 20 cells.

    RECTANGLE_4X6(4,6,"4 X 6","RECTANGLE",4096, true, new ArrayList<>() {{
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
    }}, "rectangle_4x6.png"), // Total 24 cells.

    RECTANGLE_5X6(5,6,"5 X 6","RECTANGLE",8192, true, new ArrayList<>() {{
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
    }}, "rectangle_5x6.png"), // Total 30 cells.

    BLOCK_MIDDLE_SQUARE_5X5(5, 5, "5 X 5", "BLOCK MIDDLE SQ.", 2048, true, new ArrayList<>() {{
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(-1); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
    }}, "block_middle_square_5x5.png"), // Total 25 cells.

    BLOCK_MIDDLE_SQUARE_6X6(6,6,"? X ?","BLOCK MIDDLE SQ.",4096, false, new ArrayList<>() {{
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(-1); add(-1); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(-1); add(-1); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
    }}, "arriving_game_mode.png"), // Total 36 cells.

    BLOCK_MIDDLE_RECTANGLE_4X6(4, 6, "4 X 6", "BLOCK MIDDLE REC.", 2048, true, new ArrayList<>() {{
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(-1); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(-1); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
    }}, "block_middle_rectangle_4x6.png"), // Total 24 cells.

    BLOCK_MIDDLE_RECTANGLE_5X6(5,6,"? X ?","BLOCK MIDDLE REC.",4096, false, new ArrayList<>() {{
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(-1); add(-1); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(-1); add(-1); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
    }}, "arriving_game_mode.png"), // Total 30 cells.

    BLOCK_2_CORNERS_SQUARE_5X5(5, 5, "5 X 5", "BLOCK 2 CORNERS SQ.", 2048, true, new ArrayList<>() {{
        add(new ArrayList<>() {{add(-1); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(-1);}});
    }}, "block_2_corners_square_5x5.png"), // Total 25 cells.

    BLOCK_2_CORNERS_SQUARE_6X6(6, 6, "? X ?", "BLOCK 2 CORNERS SQ.", 4096, false, new ArrayList<>() {{
        add(new ArrayList<>() {{add(-1); add(-1); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(-1); add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0); add(-1);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(-1); add(-1);}});
    }}, "arriving_game_mode.png"), // Total 36 cells.

    BLOCK_2_CORNERS_RECTANGLE_4X6(4, 6, "4 X 6", "BLOCK 2 CORNERS REC.", 2048, true, new ArrayList<>() {{
        add(new ArrayList<>() {{add(-1); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(-1);}});
    }}, "block_2_corners_rectangle_4x6.png"), // Total 24 cells.

    BLOCK_2_CORNERS_RECTANGLE_5X6(5,6,"? X ?","BLOCK 2 CORNERS REC.",4096, false, new ArrayList<>() {{
        add(new ArrayList<>() {{add(-1); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(-1); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(0);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(-1);}});
        add(new ArrayList<>() {{add(0); add(0); add(0); add(0); add(-1);}});
    }}, "arriving_game_mode.png"); // Total 30 cells.

    private final int columns;
    private final int rows;
    private final String dimensions;
    private final String mode;
    private final int goal;
    private final boolean canAccess;
    private final ArrayList<ArrayList<Integer>> blockCells; // '-1' is for a block cell
    private final String gamePreviewAssetFileName;
    private final GameLayoutProperties gameLayoutProperties;

    GameModes(int columns, int rows, String dimensions, String mode, int goal, boolean canAccess,
              ArrayList<ArrayList<Integer>> blockCells, String gamePreviewAssetFileName) {
        this.columns = columns;
        this.rows = rows;
        this.dimensions = dimensions;
        this.mode = mode;
        this.goal = goal;
        this.canAccess = canAccess;
        this.blockCells = blockCells;
        this.gamePreviewAssetFileName = gamePreviewAssetFileName;
        this.gameLayoutProperties = GameLayoutProperties.valueOf(name() + "_LAYOUT_PROPERTIES");
    }

    public static List<String> getAllGameVariantsOfMode(String mode) {
        List<String> gameModeVariants = new ArrayList<>();
        for (GameModes gameMode : GameModes.values()) {
            if (gameMode.getMode().equals(mode)) {
                gameModeVariants.add(gameMode.getDimensions());
            }
        }
        return gameModeVariants;
    }

    public static List<String> getAllGameModes() {
        List<String> allGameModes = new ArrayList<>();
        for (GameModes gameMode : GameModes.values()) {
            if (!allGameModes.contains(gameMode.getMode())) {
                allGameModes.add(gameMode.getMode());
            }
        }
        return allGameModes;
    }

    public static GameModes getGameModeEnum(int columns, int rows, String mode) {
        switch (mode) {
            // For RECTANGLE board
            case "RECTANGLE": {
                if (columns == 4) {
                    return ((rows == 5) ? valueOf("RECTANGLE_4X5") : valueOf("RECTANGLE_4X6"));
                } else if (columns == 5) {
                    return valueOf("RECTANGLE_5X6");
                }
            }
            // For BLOCK MIDDLE SQUARE board
            case "BLOCK MIDDLE SQ.": {
                if (columns == 5) {
                    return valueOf("BLOCK_MIDDLE_SQUARE_5X5");
                } else {
                    return valueOf("BLOCK_MIDDLE_SQUARE_6X6");
                }
            }
            // For BLOCK MIDDLE RECTANGLE board
            case "BLOCK MIDDLE REC.": {
                if (columns == 4) {
                    return valueOf("BLOCK_MIDDLE_RECTANGLE_4X6");
                } else {
                    return valueOf("BLOCK_MIDDLE_RECTANGLE_5X6");
                }
            }
            // For BLOCK 2 CORNERS SQUARE board
            case "BLOCK 2 CORNERS SQ.": {
                if (columns == 5) {
                    return valueOf("BLOCK_2_CORNERS_SQUARE_5X5");
                } else {
                    return valueOf("BLOCK_2_CORNERS_SQUARE_6X6");
                }
            }
            // For BLOCK 2 CORNERS RECTANGLE board
            case "BLOCK 2 CORNERS REC.": {
                if (columns == 4) {
                    return valueOf("BLOCK_2_CORNERS_RECTANGLE_4X6");
                } else {
                    return valueOf("BLOCK_2_CORNERS_RECTANGLE_5X6");
                }
            }
            // For SQUARE board
            default: {
                if (columns == 4) {
                    return valueOf("SQUARE_4X4");
                } else if (columns == 5) {
                    return valueOf("SQUARE_5X5");
                } else { // Purposely, not mentioning the no. of columns check like above to avoid compilation error
                    return valueOf("SQUARE_6X6");
                }
            }
        }
    }
}