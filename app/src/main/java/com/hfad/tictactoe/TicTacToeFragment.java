package com.hfad.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * This fragment represents playing field of the Tic-Tac-Toe game.
 */
public class TicTacToeFragment extends Fragment implements View.OnClickListener {

    /**
     * Reference to the GridLayout which is used
     * to visually represent Tic-Tac-Toe game field.
     */
    private GridLayout playField;

    /**
     * This variable represents defaultPlayFieldSize.
     */
    private static final int defaultPlayFieldSize = 3;        //CHANGE THIS VARIABLE IN ORDER TO MAKE THIS GAME A LITTLE HARDER :)

    /**
     * Reference to the TicTacToe class that manages all logic of this game.
     */
    private TicTacToe game;

    /**
     * Boolean for checking which team played first. It is used to keep this game
     * fair, X and O will keep switching in position of being the first player to play because
     * first player has an advantage.
     */
    private boolean xPlayedFirst;

    /**
     * Interface that every Activity that wants to include this Fragment needs to implement.
     * It contains four methods that properly handle getting and setting team results.
     */
    interface ScoreKeeper {
        /**
         * Getter for X-team's result.
         * @return X-team's result.
         */
        String getXTeamResult();

        /**
         * Getter for O-team's result.
         * @return O-team's result.
         */
        String getOTeamResult();

        /**
         * Setter for X-team result.
         * @param result X-team's result.
         */
        void setXTeamResult(String result);

        /**
         * Setter for O-team's result
         * @param result O-team's result.
         */
        void setOTeamResult(String result);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tic_tac_toe, container, false);
        //Find view that this fragment contains.
        playField = view.findViewById(R.id.play_field);
        //Set play field size to the default value.
        setPlayFieldSize();
        //Make new TicTacToe object with default value parameters.
        game = new TicTacToe(playField.getRowCount(), playField.getColumnCount());

        //Fill the GridLayout with set number of fields
        for(int i = 0; i < playField.getColumnCount(); i++) {
            for(int j = 0; j < playField.getRowCount(); j++) {
                //Set weights in rows and columns so that each element takes equal space.
                GridLayout.Spec rowSpec = GridLayout.spec(i, GridLayout.FILL, 1);
                GridLayout.Spec columnSpec = GridLayout.spec(j, GridLayout.FILL, 1);
                GridLayout.LayoutParams param = new GridLayout.LayoutParams(rowSpec, columnSpec);
                param.height = 0;
                param.width = 0;

                //Creating new ImageView and setting necessary parameters
                ImageView imageView = new ImageView(getActivity());
                imageView.setBackgroundResource(R.color.colorPrimary);
                imageView.setOnClickListener(this);

                //Padding is calculated depending on number of fields in the play field.
                imageView.setPadding(180/defaultPlayFieldSize,180/defaultPlayFieldSize, 180/defaultPlayFieldSize, 180/defaultPlayFieldSize);
                createGrid(i, j, param);

                //Setting parameters and adding image to the GridLayout
                imageView.setLayoutParams(param);
                playField.addView(imageView);
            }
        }

        //Retrieve the saved data if exists.
        if(savedInstanceState != null) {
            xPlayedFirst = savedInstanceState.getBoolean("X_FIRST");
            game.setXTurn(savedInstanceState.getBoolean("IS_X_TURN"));
            game.setxFields(savedInstanceState.getIntegerArrayList("X_FIELDS"));
            game.setoFields(savedInstanceState.getIntegerArrayList("O_FIELDS"));
            setPlayingField(game.getoFields(), R.drawable.o);
            setPlayingField(game.getxFields(), R.drawable.x);
        }
        else {
            SharedPreferences sp = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
            xPlayedFirst = sp.getBoolean("X_FIRST", false);
            game.setXTurn(sp.getBoolean("IS_X_TURN", false));

            ArrayList<Integer> xFields = getArrayList("X_FIELDS");
            ArrayList<Integer> oFields = getArrayList("O_FIELDS");

            if(xFields != null) {
                game.setxFields(xFields);
                setPlayingField(game.getxFields(), R.drawable.x);
            }

            if(oFields != null) {
                game.setoFields(oFields);
                setPlayingField(game.getoFields(), R.drawable.o);
            }
        }

        return view;
    }

    public void onClick(View view) {

        int index = ((GridLayout)view.getParent()).indexOfChild(view);
        ArrayList<Integer> oFields = game.getoFields();
        ArrayList<Integer> xFields = game.getxFields();

        //If game has already been won, reset the play field (but do not reset the game score)
        if(game.checkWin(oFields) || game.checkWin(xFields)) {
            resetGame();
            return;
        }

        //If number of fields is even number
        if(playField.getChildCount()%2 == 0) {
            //if all fields are filled, reset the play field
            if(oFields.size() == playField.getChildCount()/2 && xFields.size() == playField.getChildCount()/2) {
                resetGame();
                return;
            }
        }
        //If number of fields is odd number
        else {
            //if all fields are filled, reset the play field
            if(oFields.size() == playField.getChildCount()/2 && xFields.size() == (playField.getChildCount()/2+1) ||
                    xFields.size() == playField.getChildCount()/2 && oFields.size() == (playField.getChildCount()/2+1)) {
                resetGame();
                return;
            }
        }

        //If user presses field which is already filled
        if(xFields.contains(index) || oFields.contains(index)) {
            return;
        }

        boolean isXTurn = game.isXTurn();

        //If the new game is started, xPlayed first will keep which team played first
        if(oFields.size() == 0 && xFields.size() == 0) {
            xPlayedFirst = isXTurn;
        }

        //Fill the field with proper ImageResource
        if(isXTurn) {
            ((ImageView) view).setImageResource(R.drawable.x);
            xFields.add(index);
        }
        else {
            ((ImageView) view).setImageResource(R.drawable.o);
            oFields.add(index);
        }

        //Ensure that on the next move the opposite player will play
        game.setXTurn(!(isXTurn));

        System.out.println("ODMAH NAKON:" + game.isXTurn());

        //If X-team has won the game increment result and show the winning Dialog
        if(game.checkWin(xFields)) {
            String text = ((ScoreKeeper)getActivity()).getXTeamResult();
            WinnerDialog wd = new WinnerDialog(getActivity(), R.drawable.x);
            ((ScoreKeeper)getActivity()).setXTeamResult(TicTacToe.incrementResult(text));
            wd.show();
        }

        //If O-team has won the game increment the result and show the winning Dialog
        if(game.checkWin(oFields)) {
            String text = ((ScoreKeeper)getActivity()).getOTeamResult();
            WinnerDialog wd = new WinnerDialog(getActivity(), R.drawable.o);
            ((ScoreKeeper)getActivity()).setOTeamResult(TicTacToe.incrementResult(text));
            wd.show();
        }
    }

    /**
     * This method is used to set play field size.
     * This method will take defaultPlayFieldSize as argument
     * to produce square-like fields, example: 3x3, 5x5, 9x9
     */
    private void setPlayFieldSize() {
        playField.setRowCount(TicTacToeFragment.defaultPlayFieldSize);
        playField.setColumnCount(TicTacToeFragment.defaultPlayFieldSize);
    }

    /**
     * This method is used to reset the game.
     * This method will do 3 things:
     * 1. Clear the play field
     * 2. Clear the xFields that are keeping fields where X is located
     * 3. Clear the xFields that are keeping fields where O is located
     * 4. If X played first, it will ensure that 0 plays first next round (and opposite)
     */
    public void resetGame() {
        for(int i = 0; i < playField.getChildCount(); i++) {
            ((ImageView)playField.getChildAt(i)).setImageDrawable(null);
        }
        game.setxFields(new ArrayList<Integer>());
        game.setoFields(new ArrayList<Integer>());
        game.setXTurn(!(xPlayedFirst));
    }

    /**
     * This method will create Tic-Tac-Toe grid by putting necessary margins to each field.
     * @param row row in which field is located
     * @param column column in which field is located
     * @param params params of the View in field
     */
    private void createGrid(int row, int column, GridLayout.LayoutParams params) {
        if(row != 0) {
            params.topMargin = 7;
        }

        if(column != 0) {
            params.setMarginStart(7);
        }

        if(column != playField.getColumnCount()-1) {
            params.setMarginEnd(7);
        }

        if(row != playField.getRowCount()-1) {
            params.bottomMargin = 7;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //Save necessary data
        outState.putIntegerArrayList("X_FIELDS", game.getxFields());
        outState.putIntegerArrayList("O_FIELDS", game.getoFields());
        outState.putBoolean("X_FIRST", xPlayedFirst);
        outState.putBoolean("IS_X_TURN", game.isXTurn());
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
        editor.putBoolean("X_FIRST", xPlayedFirst);
        editor.putBoolean("IS_X_TURN", game.isXTurn());
        editor.commit();

        saveArrayList(game.getxFields(), "X_FIELDS");
        saveArrayList(game.getoFields(), "O_FIELDS");
    }


    public void saveArrayList(ArrayList<Integer> list, String key){
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.commit();

    }

    public ArrayList<Integer> getArrayList(String key){
        SharedPreferences prefs = getActivity().getSharedPreferences("settings", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
        return gson.fromJson(json, type);
    }

    /**
     * This method fills the playing field based on the fields saved in List.
     * @param fields fields
     * @param resource image resource
     */
    private void setPlayingField(ArrayList<Integer> fields, Integer resource) {
        for(Integer o : fields) {
            ((ImageView)playField.getChildAt(o)).setImageResource(resource);
        }
    }
}
