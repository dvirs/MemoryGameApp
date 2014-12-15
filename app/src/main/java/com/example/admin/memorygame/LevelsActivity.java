package com.example.admin.memorygame;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class LevelsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_levels);

        Button level1 = (Button)findViewById(R.id.level1Btn_lev);
        Button level2 = (Button)findViewById(R.id.level2Btn_lev);
        Button level3 = (Button)findViewById(R.id.level3Btn_lev);

        level1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gameIntent = new Intent(LevelsActivity.this, CardGameActivity.class);
                gameIntent.putExtra("row",4);
                gameIntent.putExtra("col",3);
                startActivity(gameIntent);


            }
        });

        level2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        level3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_levels, menu);
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
