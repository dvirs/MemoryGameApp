package com.example.admin.memorygame;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity implements OnClickListener {

    private final int MAX_CARDS = 16;

    private int[] viewIds = {R.id.card_0, R.id.card_1, R.id.card_2, R.id.card_3,
            R.id.card_4, R.id.card_5, R.id.card_6, R.id.card_7,
            R.id.card_8, R.id.card_9, R.id.card_10, R.id.card_11,
            R.id.card_12, R.id.card_13, R.id.card_14, R.id.card_15,};

    private int[] drawableIds = {R.drawable.card_0, R.drawable.card_1, R.drawable.card_2, R.drawable.card_3,
            R.drawable.card_4, R.drawable.card_5, R.drawable.card_6, R.drawable.card_7, R.drawable.card_back};

    private int[] cardsPositions;    //Holds the assigned positions of the cards

    private ImageView[] imageviews;

    private TextView foundPairsLabel;
    private TextView turns_taken_label;
    private TextView timerTextField;
    private TextView timelable;
    private Bundle bund;
    private Handler handler;
    private CountDownTimer timerThread;
    private boolean back;
    private int delay;
    private int flippedCards;
    private int currentIndex = -1;
    private int lastIndex = -1;
    private int foundPairs = 0;
    private int turns_taken = 0;
    private int numCrads;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen_layout);
        SoundManager.getInstance(getBaseContext());
        Intent intent = getIntent();
        bund = intent.getExtras();
        int level = (int) bund.get("level");
        int time;

        handler = new Handler();
        timerTextField = (TextView) this.findViewById(R.id.timer);
        timelable = (TextView) this.findViewById(R.id.timelb);

        time = settingsByLevel(level);

        timerThread = new CountDownTimer(time, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                timerTextField.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {

                Toast.makeText(MainActivity.this, "Your Time Is Up!", Toast.LENGTH_LONG).show();
                timelable.setText("You ");
                timerThread.cancel();
                timerTextField.setText("Lose!");
                SoundManager.playLoopedSound(SoundManager.SOUND_LOSER);
                quit();
            }
        }.start();

        foundPairs = 0;
        foundPairsLabel = (TextView) MainActivity.this.findViewById(R.id.pairs_counter);
        turns_taken_label = (TextView) MainActivity.this.findViewById(R.id.turns);

        //create a new cards array by the selected level
        cardsPositions = new int[numCrads];

        //set the card at each position to -1 (unset)
        for (int i = 0; i < numCrads; i++) {
            cardsPositions[i] = -1;
        }

        imageviews = new ImageView[MAX_CARDS];
        for (int i = 0; i < MAX_CARDS; i++) {
            if (i < numCrads) {
                imageviews[i] = (ImageView) findViewById(viewIds[i]);
            } else {
                ((ImageView) findViewById(viewIds[i])).setVisibility(View.INVISIBLE);
            }
        }


        //for each card loop through.
        setImageRandomPosition();

        //set click listeners for each view & set image to back card
        for (int i = 0; i < numCrads; i++) {
            ((ImageView) findViewById(viewIds[i])).setOnClickListener(this);
            ((ImageView) findViewById(viewIds[i])).setImageResource(R.drawable.card_back);
        }


    }

    private void setImageRandomPosition() {
        Random random = new Random();
        for (int i = 0; i < numCrads / 2; i++) {
            //each card goes in 2 slots
            for (int j = 0; j < 2; j++) {
                //generate a random slot
                int randomSlot = random.nextInt(numCrads);
                //make sure that the slot isn't already populated
                while (cardsPositions[randomSlot] != -1) {
                    randomSlot = random.nextInt(numCrads);
                }
                //set this card to that slot
                cardsPositions[randomSlot] = i;
            }
        }
    }

    @Override
    public void onClick(View v) {
        back = false;
        delay = 0;
        int index = Integer.parseInt((String) v.getTag());
        SoundManager.playSound(SoundManager.SOUND_FLIP);

        for (int i = 0; i < numCrads; i++) {
            //determine which id we're dealing with
            if (v.getId() == viewIds[i]) {
                //set the face up image for each
                index = i;
                ((ImageView) findViewById(viewIds[i])).setImageResource(drawableIds[cardsPositions[i]]);
                imageviews[i].setFocusable(false);
                imageviews[i].setClickable(false);
                break;
            }
        }
        flippedCards++;

        if (flippedCards == 2) {
            turns_taken++;
            turns_taken_label.setText(String.valueOf(turns_taken));

            currentIndex = index;

            for (int i = 0; i < MAX_CARDS; i++) {
                if (i < numCrads) {
                    imageviews[i].setFocusable(false);
                    imageviews[i].setClickable(false);
                }
            }
            flippedCards = 0;
            handler.postDelayed(flipCardsBack, 1000);
        } else {
            lastIndex = index;
        }
    }

    Runnable flipCardsBack = new Runnable() {
        public void run() {
            SoundManager.playSound(SoundManager.SOUND_FLOP);
            //Check if the two cards with the same image
            if (cardsPositions[currentIndex] == cardsPositions[lastIndex]) {
                ((ImageView) findViewById(viewIds[lastIndex])).setVisibility(View.INVISIBLE);
                ((ImageView) findViewById(viewIds[currentIndex])).setVisibility(View.INVISIBLE);

                foundPairs++;
                foundPairsLabel.setText(String.valueOf(foundPairs));    //Update label of matches

                if (foundPairs == numCrads / 2) {
                    win();
                }

            }
            //Cards with different image
            else {
                ((ImageView) findViewById(viewIds[currentIndex])).setImageResource(R.drawable.card_back);
                ((ImageView) findViewById(viewIds[lastIndex])).setImageResource(R.drawable.card_back);
            }

            for (int i = 0; i < MAX_CARDS; i++) {
                if (i < numCrads) {
                    imageviews[i].setFocusable(true);
                    imageviews[i].setClickable(true);
                }
            }
        }
    };

    @Override
    protected void onResume() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if (ConfigActivity.getBgMusicEnabled(this)) {
            //SoundManager.playLoopedSound(SOUND_BACKGROUND);
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        SoundManager.pauseLoopedSound(SoundManager.SOUND_BACKGROUND);
        SoundManager.pauseLoopedSound(SoundManager.SOUND_WINNER);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        back = true;
        quit();
    }

    public void quit() {
        if (back) {
            delay = 0;
            back = false;
        } else {
            delay = 4000;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SoundManager.pauseLoopedSound(SoundManager.SOUND_WINNER);
                SoundManager.pauseLoopedSound(SoundManager.SOUND_LOSER);
                timerThread.cancel();
                MainActivity.this.finish();
            }
        }, delay);

    }

    private void win() {

        ((LinearLayout) this.findViewById(R.id.outcome_layout)).setVisibility(View.VISIBLE);
        timelable.setText("Good ");
        timerThread.cancel();
        timerTextField.setText("Job!");
        SoundManager.playLoopedSound(SoundManager.SOUND_WINNER);
        quit();

    }

    private int settingsByLevel(int level) {
        int time;
        if(level == 1) {
            time = 100000;
            numCrads = 8;
        }
        else if(level == 2) {
            time = 85000;
            numCrads = 12;
        }
        else {
            time = 60000;
            numCrads = 16;
        }
        return time;
    }

}

