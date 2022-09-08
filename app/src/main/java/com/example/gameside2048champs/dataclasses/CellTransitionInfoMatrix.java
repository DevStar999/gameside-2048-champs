package com.example.gameside2048champs.dataclasses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// Transition info for a cell w.r.t. a matrix
public class CellTransitionInfoMatrix {
    private int initialValue; // Initial Value of current cell
    private int finalValue; // Final Value in current cell
    private int finalLocationRow; // After move, Final Row Location
    private int finalLocationColumn; // After move, Final Column Location
    private boolean didMerge; // Did any 2 cells merge in current position
}