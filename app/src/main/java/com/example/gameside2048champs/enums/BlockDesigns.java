package com.example.gameside2048champs.enums;

import com.example.gameside2048champs.R;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public enum BlockDesigns {
    BLOCK_CELL_X(R.drawable.block_cell_x, R.drawable.block_preview_cell_x, 1f, 1f),
    BLOCK_ALIEN(R.drawable.block_alien, R.drawable.block_preview_alien, 0.95f, 0.95f),
    BLOCK_HEART(R.drawable.block_heart, R.drawable.block_preview_heart, 0.9f, 0.9f),
    BLOCK_POOP(R.drawable.block_poop, R.drawable.block_preview_poop, 0.9f, 0.9f),
    BLOCK_ROCK(R.drawable.block_rock, R.drawable.block_preview_rock, 1f, 1f),
    BLOCK_CACTUS(R.drawable.block_cactus, R.drawable.block_preview_cactus, 1.15f, 1f),
    BLOCK_HURDLE(R.drawable.block_hurdle, R.drawable.block_preview_hurdle, 1f, 1.2f),
    BLOCK_NINJA(R.drawable.block_ninja, R.drawable.block_preview_ninja, 1f, 1f),
    BLOCK_STORMTROOPER(R.drawable.block_stormtrooper, R.drawable.block_preview_stormtrooper, 1.25f, 1.2f),
    BLOCK_VAULT(R.drawable.block_vault, R.drawable.block_preview_vault, 0.85f, 0.9f);

    private int blockDrawableResourceId;
    private int blockPreviewResourceId;
    private float blockDrawableScaleX;
    private float blockDrawableScaleY;

    BlockDesigns(int blockDrawableResourceId, int blockPreviewResourceId,
                 float blockDrawableScaleX, float blockDrawableScaleY) {
        this.blockDrawableResourceId = blockDrawableResourceId;
        this.blockPreviewResourceId = blockPreviewResourceId;
        this.blockDrawableScaleX = blockDrawableScaleX;
        this.blockDrawableScaleY = blockDrawableScaleY;
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
