
package com.example.priyanka.mapsdemo;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by JungHyun on 2018-06-03.
 */

class ItemHolder extends RecyclerView.ViewHolder {
    public CardView cardView;
    public ImageView list_img;
    public TextView txt_title;
    public TextView txt_vicinity;
    public LinearLayout linearLayout;


    public ItemHolder(View root) {
        super(root);
        cardView = root.findViewById(R.id.cardView);
        list_img = (ImageView)root.findViewById(R.id.list_img);
        txt_title = (TextView)root.findViewById(R.id.txt_title);
        txt_vicinity = (TextView)root.findViewById(R.id.txt_vicinity);
        linearLayout = (LinearLayout)root.findViewById(R.id.item);

    }
}