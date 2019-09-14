package com.hfad.tictactoe;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Custom Dialog that will be shown when one of the teams wins
 */
public class WinnerDialog extends Dialog implements View.OnClickListener {

    /**
     * This variable keeps the necessary Image Resource.
     */
    private Integer resource;

    /**
     * This variable keeps the reference to the Dialog's ImageView
     */
    private ImageView winnerImage;

    /**
     * This variable keeps the reference to the Dialog's TextView
     */
    private TextView winnerLabel;

    /**
     * Constructor that takes one parameter, activity in which Dialog will be located.
     * @param activity activity
     */
    private WinnerDialog(Activity activity) {
        super(activity);
    }

    /**
     * Constructor that takes two parameters, activity in which Dialog will be located
     * and Image Resource that will be displayed in Dialog's ImageView.
     * @param activity activity
     * @param resource image resource
     */
    public WinnerDialog(Activity activity, Integer resource) {
        this(activity);
        this.resource = resource;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.winner_dialog);

        //Keep references to the Dialog's Views
        winnerImage = findViewById(R.id.winner_image);
        winnerLabel = findViewById(R.id.winner_label);

        //Set onClickListener that will dismiss Dialog
        winnerImage.setOnClickListener(this);
        winnerLabel.setOnClickListener(this);

        //Set Image Resource provided in the Constructor
        winnerImage.setImageResource(resource);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Defining the necessary animations and setting their duration
        ObjectAnimator rotateLeft = ObjectAnimator.ofFloat(winnerImage,
                "rotation", 0, 360);
        rotateLeft.setDuration(800);

        ObjectAnimator up = ObjectAnimator.ofFloat(winnerImage,
                "translationY", 200f, 0f);
        up.setDuration(800);

        ObjectAnimator down = ObjectAnimator.ofFloat(winnerImage,
                "translationY", 0f, 200f);
        down.setDuration(800);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(winnerImage, "alpha",
                0f);
        fadeOut.setDuration(400);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(winnerImage, "alpha",
                0f, 1f);
        fadeIn.setDuration(400);

        AnimatorSet animatorSet = new AnimatorSet();

        //Depending on the Image Resource, animate ImageView
        if(resource == R.drawable.x) {
            animatorSet.playTogether(up, rotateLeft);
            animatorSet.playSequentially(up, down);
            animatorSet.start();
        }
        else {
            down.setDuration(200);
            animatorSet.playSequentially(up, fadeOut, down, fadeIn);
            animatorSet.start();
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}