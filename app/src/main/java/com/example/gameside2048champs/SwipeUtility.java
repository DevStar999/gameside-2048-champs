package com.example.gameside2048champs;

import com.example.gameside2048champs.dataclasses.CellTransitionInfoLane;
import com.example.gameside2048champs.dataclasses.CellTransitionInfoMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

@Getter
public class SwipeUtility {
    private final List<List<Integer>> afterMoveMatrix;
    private final List<List<CellTransitionInfoMatrix>> ctiMatrix;

    public SwipeUtility(int rows, int columns) {
        afterMoveMatrix = new ArrayList<>();
        ctiMatrix = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            List<Integer> afterMoveMatrixRow = new ArrayList<>();
            List<CellTransitionInfoMatrix> ctiMatrixRow = new ArrayList<>();
            for (int column = 0; column < columns; column++) {
                afterMoveMatrixRow.add(0);
                ctiMatrixRow.add(new CellTransitionInfoMatrix());
            }
            afterMoveMatrix.add(afterMoveMatrixRow);
            ctiMatrix.add(ctiMatrixRow);
        }
    }

    private void initialiseMatrices(List<List<Integer>> gameMatrix) {
        // Filling up the above two matrices with initial info
        for (int row = 0; row < gameMatrix.size(); row++) {
            for (int column = 0; column < gameMatrix.get(row).size(); column++) {
                int cellValue = gameMatrix.get(row).get(column);
                afterMoveMatrix.get(row).set(column, cellValue);
                ctiMatrix.get(row).set(column, new CellTransitionInfoMatrix(cellValue, cellValue,
                        row, column, false));
            }
        }
    }

    private void setAfterMoveMatrixValues() {
        for (int row = 0; row < afterMoveMatrix.size(); row++) {
            for (int column = 0; column < afterMoveMatrix.get(row).size(); column++) {
                afterMoveMatrix.get(row).set(column, ctiMatrix.get(row).get(column).getFinalValue());
            }
        }
    }

    private List<CellTransitionInfoLane> getCTILane(List<Integer> cellValues) {
        // Creating part of cellValues
        List<List<Integer>> cellValuesParts = new ArrayList<>();
        List<Integer> cellValuesPart = new ArrayList<>();
        for (int pos = 0; pos < cellValues.size(); pos++) {
            if (cellValues.get(pos) == -1) {
                if (cellValuesPart.size() > 0) cellValuesParts.add(cellValuesPart);

                cellValuesPart = new ArrayList<>();
                cellValuesPart.add(-1);
                cellValuesParts.add(cellValuesPart);

                cellValuesPart = new ArrayList<>();
            } else cellValuesPart.add(cellValues.get(pos));
        }
        if (cellValuesPart.size() > 0) cellValuesParts.add(cellValuesPart);

        // Computing and Merging parts of cellValues
        List<CellTransitionInfoLane> ctiLane = new ArrayList<>();
        for (int part = 0; part < cellValuesParts.size(); part++) {
            if (cellValuesParts.get(part).size() == 1 && cellValuesParts.get(part).get(0) == -1) {
                ctiLane.add(new CellTransitionInfoLane(-1, -1, 0, false));
            } else {
                List<CellTransitionInfoLane> ctiLanePart = new ArrayList<>();
                List<Integer> cellValuesCurrentPart = new ArrayList<>();
                for (int index = 0; index < cellValuesParts.get(part).size(); index++) { // Filling up with some initial value
                    ctiLanePart.add(new CellTransitionInfoLane(cellValuesParts.get(part).get(index),
                            cellValuesParts.get(part).get(index), 0, false));
                    cellValuesCurrentPart.add(cellValuesParts.get(part).get(index));
                }

                // Traversing in the opposite direction of slide
                for (int indexRev = cellValuesCurrentPart.size() - 1; indexRev >= 0; indexRev--) {
                    int currentCellValue = cellValuesCurrentPart.get(indexRev);
                    if (currentCellValue == 0) continue;
                    // Checking till where in direction of swipe cell can be slided
                    for (int index = indexRev + 1; index < cellValuesCurrentPart.size(); index++) {
                        // Means now we have found that position in direction of swipe which is filled
                        if (cellValuesCurrentPart.get(index) != 0) {
                            if (cellValuesCurrentPart.get(index).equals(currentCellValue) && !ctiLanePart.get(index).isDidMerge()) {
                                // Can Merge, as we have two same values and the value we found does not
                                // exist as a part of a previous merge
                                ctiLanePart.get(indexRev).setFinalValue(0);
                                ctiLanePart.get(indexRev).setShiftByCells(Math.abs(index - indexRev));
                                ctiLanePart.get(index).setFinalValue(2 * currentCellValue);
                                ctiLanePart.get(index).setDidMerge(true);

                                // Updating the cellValueCurrentPart ArrayList
                                cellValuesCurrentPart.set(indexRev, 0);
                                cellValuesCurrentPart.set(index, 2 * currentCellValue);
                            } else {
                                // Cannot Merge, as of now as we don't have two same values
                                ctiLanePart.get(indexRev).setFinalValue(0);
                                ctiLanePart.get(indexRev).setShiftByCells(Math.abs(index - indexRev - 1));
                                ctiLanePart.get(index - 1).setFinalValue(currentCellValue);

                                // Updating the cellValueCurrentPart ArrayList
                                cellValuesCurrentPart.set(indexRev, 0);
                                cellValuesCurrentPart.set(index - 1, currentCellValue);
                            }
                            break;
                        } else if (index == cellValuesCurrentPart.size() - 1) {
                            // Means now we have reached the end and the final cell is empty, so we move here
                            ctiLanePart.get(indexRev).setFinalValue(0);
                            ctiLanePart.get(indexRev).setShiftByCells(Math.abs(index - indexRev));
                            ctiLanePart.get(index).setFinalValue(currentCellValue);

                            // Updating the cellValueCurrentPart ArrayList
                            cellValuesCurrentPart.set(indexRev, 0);
                            cellValuesCurrentPart.set(index, currentCellValue);
                        }
                    }
                }

                ctiLane.addAll(ctiLanePart);
            }
        }

        return ctiLane;
    }


    private void setValuesIntoCTIMatrix(int row, int column, int initialValue, int finalValue,
                                        int finalLocationRow, int finalLocationColumn, boolean didMerge) {
        ctiMatrix.get(row).get(column).setInitialValue(initialValue);
        ctiMatrix.get(row).get(column).setFinalValue(finalValue);
        ctiMatrix.get(row).get(column).setFinalLocationRow(finalLocationRow);
        ctiMatrix.get(row).get(column).setFinalLocationColumn(finalLocationColumn);
        ctiMatrix.get(row).get(column).setDidMerge(didMerge);
    }

    public void madeLeftToRightSwipe(List<List<Integer>> gameMatrix) {
        initialiseMatrices(gameMatrix);

        for (int row = 0; row < gameMatrix.size(); row++) {
            Map<Integer, Integer> shiftToLocation = new HashMap<>();
            List<Integer> cellValuesRow = new ArrayList<>();
            for (int column = 0, shift = 0; column < gameMatrix.get(0).size(); column++, shift++) {
                shiftToLocation.put(shift, column);
                cellValuesRow.add(gameMatrix.get(row).get(column));
            }
            List<CellTransitionInfoLane> ctiLane = getCTILane(cellValuesRow);

            for (int column = 0; column < gameMatrix.get(0).size(); column++) {
                setValuesIntoCTIMatrix(row, shiftToLocation.get(column),
                        ctiLane.get(column).getInitialValue(), ctiLane.get(column).getFinalValue(),
                        row, shiftToLocation.get(column + ctiLane.get(column).getShiftByCells()),
                        ctiLane.get(column).isDidMerge());
            }
        }

        setAfterMoveMatrixValues();
    }

    public void madeRightToLeftSwipe(List<List<Integer>> gameMatrix) {
        initialiseMatrices(gameMatrix);

        for (int row = 0; row < gameMatrix.size(); row++) {
            Map<Integer, Integer> shiftToLocation = new HashMap<>();
            List<Integer> cellValuesRow = new ArrayList<>();
            for (int column = gameMatrix.get(0).size() - 1, shift = 0; column >= 0; column--, shift++) {
                shiftToLocation.put(shift, column);
                cellValuesRow.add(gameMatrix.get(row).get(column));
            }
            List<CellTransitionInfoLane> ctiLane = getCTILane(cellValuesRow);

            for (int column = 0; column < gameMatrix.get(0).size(); column++) {
                setValuesIntoCTIMatrix(row, shiftToLocation.get(column),
                        ctiLane.get(column).getInitialValue(), ctiLane.get(column).getFinalValue(),
                        row, shiftToLocation.get(column + ctiLane.get(column).getShiftByCells()),
                        ctiLane.get(column).isDidMerge());
            }
        }

        setAfterMoveMatrixValues();
    }

    public void madeTopToBottomSwipe(List<List<Integer>> gameMatrix) {
        initialiseMatrices(gameMatrix);

        for (int column = 0; column < gameMatrix.get(0).size(); column++) {
            Map<Integer, Integer> shiftToLocation = new HashMap<>();
            List<Integer> cellValuesColumn = new ArrayList<>();
            for (int row = 0, shift = 0; row < gameMatrix.size(); row++, shift++) {
                shiftToLocation.put(shift, row);
                cellValuesColumn.add(gameMatrix.get(row).get(column));
            }
            List<CellTransitionInfoLane> ctiLane = getCTILane(cellValuesColumn);

            for (int row = 0; row < gameMatrix.size(); row++) {
                setValuesIntoCTIMatrix(shiftToLocation.get(row), column,
                        ctiLane.get(row).getInitialValue(), ctiLane.get(row).getFinalValue(),
                        shiftToLocation.get(row + ctiLane.get(row).getShiftByCells()), column,
                        ctiLane.get(row).isDidMerge());
            }
        }

        setAfterMoveMatrixValues();
    }

    public void madeBottomToTopSwipe(List<List<Integer>> gameMatrix) {
        initialiseMatrices(gameMatrix);

        for (int column = 0; column < gameMatrix.get(0).size(); column++) {
            Map<Integer, Integer> shiftToLocation = new HashMap<>();
            List<Integer> cellValuesColumn = new ArrayList<>();
            for (int row = gameMatrix.size() - 1, shift = 0; row >= 0; row--, shift++) {
                shiftToLocation.put(shift, row);
                cellValuesColumn.add(gameMatrix.get(row).get(column));
            }
            List<CellTransitionInfoLane> ctiLane = getCTILane(cellValuesColumn);

            for (int row = 0; row < gameMatrix.size(); row++) {
                setValuesIntoCTIMatrix(shiftToLocation.get(row), column,
                        ctiLane.get(row).getInitialValue(), ctiLane.get(row).getFinalValue(),
                        shiftToLocation.get(row + ctiLane.get(row).getShiftByCells()), column,
                        ctiLane.get(row).isDidMerge());
            }
        }

        setAfterMoveMatrixValues();
    }
}