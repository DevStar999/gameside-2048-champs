package com.example.gameside2048champs.enums;

import com.example.gameside2048champs.R;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum BlockDesigns {
    BLOCK_CELL_X(R.drawable.block_cell_x, R.drawable.block_preview_cell_x, 1f, 1f,
            0f, 0f), // Preview Image = BLOCK MIDDLE SQ., 5x5
    BLOCK_DRAGON(R.drawable.block_dragon, R.drawable.block_preview_dragon, 1.1f, 1f,
            0f, 0f), // Preview Image = BLOCK MIDDLE REC., 4x6
    BLOCK_PIRATE(R.drawable.block_pirate, R.drawable.block_preview_pirate, 1.15f, 1f,
            0f, 0f), // Preview Image = BLOCK 2 CORNERS REC., 4x6
    BLOCK_VIKING(R.drawable.block_viking, R.drawable.block_preview_viking, 1f, 1f,
            0f, 0f), // Preview Image = BLOCK MIDDLE REC., 4x6
    BLOCK_NINJA(R.drawable.block_ninja, R.drawable.block_preview_ninja, 1f, 1f,
            0f, 0f), // Preview Image = BLOCK 2 CORNERS REC., 4x6
    BLOCK_WIZARD(R.drawable.block_wizard, R.drawable.block_preview_wizard, 1.2f, 1f,
            0f, 0f), // Preview Image = BLOCK MIDDLE REC., 4x6
    BLOCK_KNIGHT(R.drawable.block_knight, R.drawable.block_preview_knight, 1.25f, 1f,
            0f, 0f), // Preview Image = BLOCK MIDDLE REC., 4x6
    BLOCK_UNICORN(R.drawable.block_unicorn, R.drawable.block_preview_unicorn,1f, 1f,
            0f, -180f), // Preview Image = BLOCK MIDDLE REC., 4x6
    BLOCK_STORMTROOPER(R.drawable.block_stormtrooper, R.drawable.block_preview_stormtrooper, 1.25f, 1.2f,
            0f, 0f), // Preview Image = BLOCK 2 CORNERS SQ., 5x5
    BLOCK_SOLDIER(R.drawable.block_soldier, R.drawable.block_preview_soldier, 1f, 1f,
            0f, 0f), // Preview Image = BLOCK 2 CORNERS SQ., 5x5
    BLOCK_SPACESHIP(R.drawable.block_spaceship, R.drawable.block_preview_spaceship, 1f, 1f,
            0f, 0f), // Preview Image = BLOCK 2 CORNERS REC., 4x6
    BLOCK_HEART(R.drawable.block_heart, R.drawable.block_preview_heart, 0.9f, 0.9f,
            0f, 0f), // Preview Image = BLOCK 2 CORNERS REC., 4x6
    BLOCK_MAGIC_BROOM(R.drawable.block_magic_broom, R.drawable.block_preview_magic_broom, 1f, 0.9f,
            0f, 0f), // Preview Image = BLOCK MIDDLE SQ., 5x5
    BLOCK_SNOW_MOUNTAIN(R.drawable.block_snow_mountain, R.drawable.block_preview_snow_mountain, 1f, 1.2f,
            0f, 0f), // Preview Image = BLOCK 2 CORNERS SQ., 5x5
    BLOCK_ALIEN(R.drawable.block_alien, R.drawable.block_preview_alien, 0.95f, 0.95f,
            0f, 0f), // Preview Image = BLOCK MIDDLE SQ., 5x5
    BLOCK_ROCK(R.drawable.block_rock, R.drawable.block_preview_rock, 1f, 1f,
            0f, 0f), // Preview Image = BLOCK 2 CORNERS SQ., 5x5
    BLOCK_POOP(R.drawable.block_poop, R.drawable.block_preview_poop, 0.9f, 0.9f,
            0f, 0f), // Preview Image = BLOCK 2 CORNERS REC., 4x6
    BLOCK_CACTUS(R.drawable.block_cactus, R.drawable.block_preview_cactus, 1.15f, 1f,
            0f, 0f), // Preview Image = BLOCK MIDDLE SQ., 5x5
    BLOCK_VAULT(R.drawable.block_vault, R.drawable.block_preview_vault, 0.85f, 0.9f,
            0f, 0f), // Preview Image = BLOCK 2 CORNERS SQ., 5x5
    BLOCK_HURDLE(R.drawable.block_hurdle, R.drawable.block_preview_hurdle, 1f, 1.2f,
            0f, 0f); // Preview Image = BLOCK MIDDLE SQ., 5x5

    private int blockDrawableResourceId;
    private int blockPreviewResourceId;
    private float blockDrawableScaleX;
    private float blockDrawableScaleY;
    private float blockDrawableRotationX;
    private float blockDrawableRotationY;

    BlockDesigns(int blockDrawableResourceId, int blockPreviewResourceId, float blockDrawableScaleX,
                 float blockDrawableScaleY, float blockDrawableRotationX, float blockDrawableRotationY) {
        this.blockDrawableResourceId = blockDrawableResourceId;
        this.blockPreviewResourceId = blockPreviewResourceId;
        this.blockDrawableScaleX = blockDrawableScaleX;
        this.blockDrawableScaleY = blockDrawableScaleY;
        this.blockDrawableRotationX = blockDrawableRotationX;
        this.blockDrawableRotationY = blockDrawableRotationY;
    }

    public static List<BlockDesigns> getAllBlockDesigns() {
        List<BlockDesigns> allBlockDesigns = new ArrayList<>();
        for (BlockDesigns blockDesign : BlockDesigns.values()) {
            if (!allBlockDesigns.contains(blockDesign)) {
                allBlockDesigns.add(blockDesign);
            }
        }
        return allBlockDesigns;
    }
}
