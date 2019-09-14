package com.hfad.tictactoe;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * This fragment is intended to keep results from the Tic-Tac-Toe game.
 */
public class ResultKeeperFragment extends Fragment implements View.OnClickListener {

    /**
     * Reference to the TextView that is keeping O-team scores.
     */
    private TextView resultO;

    /**
     * Reference to the TextView that is keeping X-team scores.
     */
    private TextView resultX;

    /**
     * Reference to the Button that is used for restarting game
     * and setting all scores to 0.
     */
    private Button restartButton;

    /**
     * Interface that every Activity that wants to include this Fragment needs to implement.
     * It contains one method that properly handles resetting the Tic-Tac-Toe game.
     */
    interface Resetter{
        /**
         * Resets the Tic-Tac-Toe game.
         */
        void resetGame();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_keeper, container, false);

        //Getting references to this fragment's view
        resultO = view.findViewById(R.id.result_o);
        resultX = view.findViewById(R.id.result_x);
        restartButton = view.findViewById(R.id.restart_button);

        restartButton.setOnClickListener(this);
        //Retrieving the data when activity gets recreated.
        if(savedInstanceState != null) {
            resultX.setText(savedInstanceState.getString("X_RESULT"));
            resultO.setText(savedInstanceState.getString("O_RESULT"));
        }
        else {
            SharedPreferences prefs = getActivity().getSharedPreferences("settings", MODE_PRIVATE);
            resultX.setText(prefs.getString("X_RESULT", String.valueOf(0)));
            resultO.setText(prefs.getString("O_RESULT", String.valueOf(0)));
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        //Resetting the game field by calling interface's method
        ((Resetter)getActivity()).resetGame();
        //Setting all results to 0
        resultO.setText(String.valueOf(0));
        resultX.setText(String.valueOf(0));
    }

    /**
     * Getter for X-team score.
     * @return X-team score
     */
    public String getXTeamResult() {
        return  resultX.getText().toString();
    }

    /**
     * Setter for X-team score.
     * @param result X-team score
     */
    public void setXTeamResult(String result) {
        resultX.setText(result);
    }

    /**
     * Getter for O-team score.
     * @return O-team score
     */
    public String getOTeamResult() {
        return resultO.getText().toString();
    }

    /**
     * Setter for O-team score
     * @param result O-team score
     */
    public void setOTeamResult(String result) {
        resultO.setText(result);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        String scoreX = resultX.getText().toString();
        String scoreO = resultO.getText().toString();
        //Saving the data before Fragment gets destroyed
        outState.putString("X_RESULT", scoreX);
        outState.putString("O_RESULT", scoreO);
    }

    @Override
    public void onStop() {
        super.onStop();
        String scoreX = resultX.getText().toString();
        String scoreO = resultO.getText().toString();

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("X_RESULT", scoreX);
        editor.putString("O_RESULT", scoreO);
        editor.commit();
    }
}
