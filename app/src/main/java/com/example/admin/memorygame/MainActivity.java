package com.example.admin.memorygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

    private int[] viewIds = {R.id.card_0, R.id.card_1, R.id.card_2, R.id.card_3,
            R.id.card_4, R.id.card_5, R.id.card_6, R.id.card_7,
            R.id.card_8, R.id.card_9, R.id.card_10, R.id.card_11,
            R.id.card_12, R.id.card_13, R.id.card_14, R.id.card_15,};

    private int[] drawableIds = {R.drawable.card_0, R.drawable.card_1, R.drawable.card_2, R.drawable.card_3,
            R.drawable.card_4, R.drawable.card_5, R.drawable.card_6, R.drawable.card_7, R.drawable.card_back};

    private int[] assignments;	//Holds the assigned positions of the cards

    private ImageView[] imageviews;

    private static final int NUM_PAIRS = 8;

    private int flippedCards;

    private int currentIndex = -1;

    private int lastIndex = -1;

    private int foundPairs = 0;
    private TextView foundPairsLabel;
    private Bundle bund;
    private int turns_taken=0;
    private TextView turns_taken_label;
    private TextView timerTextField;
    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_screen_layout);
        SoundManager.getInstance(getBaseContext());
        Intent intent = getIntent();
        bund = intent.getExtras();
        int level = (int) bund.get("level");
        handler = new Handler();
        timerTextField = (TextView)MainActivity.this.findViewById(R.id.timer);

        new CountDownTimer(level,1000){

            @Override
            public void onTick(long millisUntilFinished) {
                timerTextField.setText(""+millisUntilFinished/1000);
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this,"Your Time Is Up!",Toast.LENGTH_SHORT).show();
                quit();
            }
        }.start();

        foundPairs = 0;
        foundPairsLabel=(TextView)MainActivity.this.findViewById(R.id.pairs_counter);
        turns_taken_label=(TextView)MainActivity.this.findViewById(R.id.turns);

        //create a new array to hold the card positions
        assignments = new int[16];

        //set the card at each position to -1 (unset)
        for(int i = 0; i < 16; i++){
            assignments[i] = -1;
        }

        imageviews = new ImageView[viewIds.length];
        for(int i = 0; i < viewIds.length; i++){
            imageviews[i] = (ImageView)findViewById(viewIds[i]);
        }

        Random random = new Random();

        //for each card, (we have 8) loop through.
        for(int i = 0; i < 8; i++){
            //each card goes in 2 slots
            for (int j = 0; j < 2; j++){
                //generate a random slot
                int randomSlot = random.nextInt(16);
                //make sure that the slot isn't already populated
                while(assignments[randomSlot] != -1){
                    randomSlot = random.nextInt(16);
                }
                //set this card to that slot
                assignments[randomSlot] = i;
                System.out.println("Putting " + i + " in slot " + randomSlot);
            }

        }

        //set click listeners for each view
        for(int i = 0; i < 16; i++){
            ((ImageView)findViewById(viewIds[i])).setOnClickListener(this);
        }
        //set each image to blank
        for(int i = 0; i < 16; i++){
            ((ImageView)findViewById(viewIds[i])).setImageResource(R.drawable.card_back);
        }


    }

    @Override
    public void onClick(View v) {
        int index = Integer.parseInt((String)v.getTag());
        System.out.println("index is " + index);

        SoundManager.playSound(SoundManager.SOUND_FLIP);

        for(int i =0; i < 16; i++)	{
            //determine which id we're dealing with
            if(v.getId() == viewIds[i]){
                //set the face up image for each
                index = i;
                ((ImageView)findViewById(viewIds[i])).setImageResource(drawableIds[assignments[i]]);
                imageviews[i].setFocusable(false);
                imageviews[i].setClickable(false);
                break;
            }
        }
        flippedCards++;



        if(flippedCards == 2){
            turns_taken++;
            turns_taken_label.setText(String.valueOf(turns_taken));

            currentIndex = index;

            for(ImageView view:imageviews){
                view.setFocusable(false);
                view.setClickable(false);
            }

            flippedCards = 0;

            handler.postDelayed(flipCardsBack, 1000);

        }else{
            lastIndex = index;
        }

    }

    Runnable flipCardsBack = new Runnable() {
        public void run() {
            SoundManager.playSound(SoundManager.SOUND_FLOP);
            if(assignments[currentIndex] == assignments[lastIndex]){
                ((ImageView)findViewById(viewIds[lastIndex])).setVisibility(View.INVISIBLE);
                ((ImageView)findViewById(viewIds[currentIndex])).setVisibility(View.INVISIBLE);

                foundPairs++;
                foundPairsLabel.setText(String.valueOf(foundPairs));	//Update label of matches

                if(foundPairs == NUM_PAIRS){
                    win();
                }

            }else{
                ((ImageView)findViewById(viewIds[currentIndex])).setImageResource(R.drawable.card_back);
                ((ImageView)findViewById(viewIds[lastIndex])).setImageResource(R.drawable.card_back);

            }

            for(ImageView view: imageviews){
                view.setFocusable(true);
                view.setClickable(true);
            }
        }
    };

    private void win(){
        SoundManager.playLoopedSound(SoundManager.SOUND_WINNER);
        ((LinearLayout)this.findViewById(R.id.outcome_layout)).setVisibility(View.VISIBLE);
    }
    @Override
    protected void onResume() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        if(ConfigActivity.getBgMusicEnabled(this)){
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
        quit();
    }

    public void quit(){
        this.finish();
    }
}

