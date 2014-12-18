package com.example.admin.memorygame.classes;

import android.content.Context;
import android.widget.ImageButton;

public class card extends ImageButton{
    private int id;
//    private int side;

    public card(Context c,int id){
        super(c);
        this.id = id;
//        this.side = 0;
    }

}
