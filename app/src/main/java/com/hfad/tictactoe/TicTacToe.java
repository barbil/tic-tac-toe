package com.hfad.tictactoe;

import java.util.ArrayList;
import java.util.List;

/**
 * This Fragment implements Tic-Tac-Toe game.
 */
public class TicTacToe {

    /**
     * Boolean that tells us is X-team currently
     * on move or not. Used for switching between players.
     */
    private boolean isXTurn;

    /**
     * ArrayList that is keeping Integers that represent
     * what fields have been filled with X-values.
     * Example: In 3x3 Tic-Tac-Toe field:
     * 0 - top left field
     * 1- top center field
     * 2 - top right field
     * 4 - center field
     * 8 - bottom right field
     */
    private ArrayList<Integer> xFields = new ArrayList<>();

    /**
     * ArrayList that is keeping Integers that represent
     * what fields have been filled with O-values.
     * Example: In 3x3 Tic-Tac-Toe field:
     * 0 - top left field
     * 1- top center field
     * 2 - top right field
     * 4 - center field
     * 8 - bottom right field
     */
    private ArrayList<Integer> oFields = new ArrayList<>();

    /**
     * This variable represents number of rows in Tic-Tac-Toe field.
     */
    private int rowCount;

    /**
     * This variable represents number of columns in Tic-Tac-Toe field.
     */
    private int columnCount;

    /**
     * Constructor that takes two arguments: row count and column count.
     * @param rowCount number of rows
     * @param columnCount number of columns
     */
    public TicTacToe(int rowCount, int columnCount) {
        this.rowCount = rowCount;
        this.columnCount = columnCount;
    }

    /**
     * This method returns true if one of the teams has
     * managed to fill at least one column with their symbol (X or O).
     * @param team list of fields that the team has filled.
     * @return true if column filled, false otherwise.
     */
    public boolean checkForColumnWin(List<Integer> team) {
        for(int j = 0; j < rowCount; j++) {
            boolean match = true;

            for(int i = 0; i < columnCount; i++) {
                if(!(team.contains(getFieldIndex(i, j)))) {
                    match = false;
                }
            }
            if(match) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns true if one of the teams has
     * managed to fill at least one row with their symbol (X or O).
     * @param team list of fields that the team has filled.
     * @return true if row filled, false otherwise.
     */
    private boolean checkForRowWin(List<Integer> team) {
        for(int i = 0; i < columnCount; i++) {
            boolean match = true;

            for(int j = 0; j < rowCount; j++) {
                if(!(team.contains(getFieldIndex(i, j)))) {
                    match = false;
                }
            }
            if(match) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method returns true if one of the teams has
     * managed to fill the first diagonal with their symbol (X or O).
     * @param team list of fields that the team has filled.
     * @return true if the first diagonal is filled, false otherwise.
     */
    private boolean checkForFirstDiagonalWin(List<Integer> team) {
        for(int i = 0; i < columnCount; i++) {
            for(int j = 0; j < rowCount; j++) {
                if(i == j) {
                    if(!(team.contains(getFieldIndex(i, j)))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * This method returns true if one of the teams has
     * managed to fill the second diagonal with their symbol (X or O).
     * @param team list of fields that the team has filled.
     * @return true if the second diagonal is filled, false otherwise.
     */
    private boolean checkForSecondDiagonalWin(List<Integer> team) {
        for(int i = 0; i < columnCount; i++) {
            for(int j = 0; j < rowCount; j++) {
                if(i == (columnCount-1-j)) {
                    if(!(team.contains(getFieldIndex(i, j)))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * This method checks if one of the team has managed to win the game.
     * The game can be won by filling row, column, first diagonal or second diagonal.
     * @param fields list of fields that the team has filled.
     * @return true if the team has won, false otherwise.
     */
    public boolean checkWin(List<Integer> fields) {
        return checkForSecondDiagonalWin(fields) || checkForFirstDiagonalWin(fields) ||
                checkForRowWin(fields) || checkForColumnWin(fields);
    }

    /**
     * This method is used for calculating the index of the field by taking the
     * row and column parameter.
     * @param row row of the field.
     * @param column column of the field
     * @return index of the field.
     */
    public int getFieldIndex(int row, int column) {
        return row*columnCount+column;
    }

    /**
     * Getter for oFields.
     * @return oFields.
     */
    public ArrayList<Integer> getoFields() {
        return oFields;
    }

    /**
     * Getter for xFields.
     * @return xFields.
     */
    public ArrayList<Integer> getxFields() {
        return xFields;
    }

    /**
     * Setter for oFields.
     * @param oFields oFields.
     */
    public void setoFields(ArrayList<Integer> oFields) {
        this.oFields = oFields;
    }

    /**
     * Setter for xFields
     * @param xFields xFields
     */
    public void setxFields(ArrayList<Integer> xFields) {
        this.xFields = xFields;
    }

    /**
     * Setter for xTurn.
     * @param XTurn xTurn.
     */
    public void setXTurn(boolean XTurn) {
        isXTurn = XTurn;
    }

    /**
     * Getter for xTurn.
     * @return isXTurn.
     */
    public boolean isXTurn() {
        return isXTurn;
    }

    /**
     * This method takes one String parameter that is
     * parseable to Integer. Example "1", "2", "3".
     * After that it will increment the given number and
     * return String representation of it.
     * @param number number
     * @return incremented number
     */
    public static String incrementResult(String number) {
        return String.valueOf(Integer.parseInt(number)+1);

    }
}