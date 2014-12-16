package com.example.admin.memorygame;

import android.content.Context;
import android.widget.ImageButton;

/**
 * Created by omriGlam on 12/16/2014.
 */
public class card extends ImageButton{
    private int id;
    private ImageButton image ;
    private int side;

    public card(Context c,int id,ImageButton image){
        super(c);
        this.id = id;
        this.image = image;
        this.side = 0;
    }

    public void flipCard(){
        if(this.side == 0) {
            image.setImageResource(R.drawable.level1);
            this.side = 1;
        }else{
            image.setImageResource(R.drawable.card_back);
            this.side = 0;
        }
    }


}
