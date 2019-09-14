package com.hfad.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * This Activity will be created upon starting the application.
 * It contains all necessary fragments and implements all necessary Interfaces in order
 * to communicate with them.
 */
public class MainActivity extends AppCompatActivity implements TicTacToeFragment.ScoreKeeper, ResultKeeperFragment.Resetter {

    /**
     * Fragment that implements Tic-Tac-Toe game field and logic.
     */
    private TicTacToeFragment playFieldFragment;

    /**
     * Fragment that keeps Tic-Tac-Toe game scores.
     */
    private ResultKeeperFragment resultKeeperFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //keeping fragment references
        playFieldFragment = (TicTacToeFragment)getSupportFragmentManager().findFragmentById(R.id.play_field_fragment);
        resultKeeperFragment = (ResultKeeperFragment)getSupportFragmentManager().findFragmentById(R.id.result_keeper_fragment);
    }

    @Override
    public void resetGame() {
        playFieldFragment.resetGame();
    }

    @Override
    public String getXTeamResult() {
        return resultKeeperFragment.getXTeamResult();
    }

    @Override
    public void setXTeamResult(String result) {
        resultKeeperFragment.setXTeamResult(result);
    }

    @Override
    public String getOTeamResult() {
        return resultKeeperFragment.getOTeamResult();
    }

    @Override
    public void setOTeamResult(String result) {
        resultKeeperFragment.setOTeamResult(result);
    }
}
