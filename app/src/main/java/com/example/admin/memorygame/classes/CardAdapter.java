package com.example.admin.memorygame.classes;

import android.content.Context;
import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.memorygame.R;

import java.util.ArrayList;
import java.util.Collections;

public class CardAdapter extends BaseAdapter{


    private Integer[] imageRoot = {
            R.drawable.c,
            R.drawable.c,
            R.drawable.c,
            R.drawable.c,
            R.drawable.c,
            R.drawable.c,
            R.drawable.c,
            R.drawable.c,
            R.drawable.c,
            R.drawable.c,
    };
    private int counter;
    private ArrayList<Card> cards;
    Context context;
    private Card beforeCard;
    private Card imageButton;

    public CardAdapter(Context context) {
        this.context = context;
        this.counter = 0;

        cards = new ArrayList<>();
        for(int i=0; i<10; i++) {
            cards.add(new Card(context,i));
            cards.add(new Card(context,i));
        }
        Collections.shuffle(cards);
        setLayout();
    }

    private void setLayout() {

        for (int i = 0; i < getCount(); i++) {
            ImageButton imageView = cards.get(i);

            imageView.setLayoutParams(new GridView.LayoutParams(60, 80));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(2, 2, 2, 2);

            imageView.setImageResource(R.drawable.card_back);

            createOnClickListener(i);
        }
    }

    private void createOnClickListener(int i) {

        imageButton = cards.get(i);
        imageButton.setOnClickListener(
                new View.OnClickListener() {
            public void onClick(View v) {
                counter++;
                flip(imageButton);
                if (counter < 2) { //first click
                    beforeCard = imageButton;
                }else { //second click
                    counter = 0;
                    if(beforeCard.cardGetId() == imageButton.cardGetId()) {
                        Toast.makeText(context, "Great Memory", Toast.LENGTH_LONG).show();
                        removeOnClickListener(beforeCard);
                        removeOnClickListener(imageButton);
                    }else {
                        SystemClock.sleep(1000);
                        flip(beforeCard);
                        flip(imageButton);
                    }
                }
            }
        });
    }

    private void removeOnClickListener(Card imageButton) {
        imageButton.setOnClickListener(null);
    }

    private void flip(Card imageButton) {
        if(imageButton.getSide() == 0){
            imageButton.setImageResource(R.drawable.card_back);
            imageButton.setSide(1);
        }else{
            imageButton.setImageResource(imageRoot[imageButton.cardGetId()]);
            imageButton.setSide(0);
        }
    }


    @Override
    public int getCount() {
        if (cards != null)
            return cards.size();
        return 0;

    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return cards.get(position);
    }

}
