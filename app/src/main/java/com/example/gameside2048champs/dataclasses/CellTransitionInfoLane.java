package com.example.gameside2048champs.dataclasses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Transition info for a cell w.r.t. a Lane
public class CellTransitionInfoLane {
    private long initialValue; // Initial Value of current cell
    private long finalValue; // Final Value in current cell
    private int shiftByCells; // Shift in position of a cell in the count of number of cells
    private boolean didMerge; // Did any 2 cells merge in current position
}