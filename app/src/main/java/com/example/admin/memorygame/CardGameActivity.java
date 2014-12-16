package com.example.admin.memorygame;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


public class CardGameActivity extends Activity{
    public int side = 0;
    public ImageButton c0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.level3);
        Bundle bund = getIntent().getExtras();

        c0 = (ImageButton) findViewById(R.id.c0);
        c0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(side == 0) {
                    c0.setImageResource(R.drawable.level1);
                    side = 1;
                }else{
                    c0.setImageResource(R.drawable.card_back);
                    side = 0;
                }
            }
        });
    }
    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_card_game, menu);
        int n;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
