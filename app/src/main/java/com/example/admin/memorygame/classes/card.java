package com.example.admin.memorygame.classes;

import android.content.Context;
import android.widget.ImageButton;

public class Card extends ImageButton{
    private int id;
    private int side;

    public Card(Context c, int id){
        super(c);
        this.id = id;
        this.side = 0;
    }

    public int cardGetId(){
        return this.id;
    }

    public int getSide() {
        return this.side;
    }

    public void setSide(int side) {
        this.side = side;
    }
}
