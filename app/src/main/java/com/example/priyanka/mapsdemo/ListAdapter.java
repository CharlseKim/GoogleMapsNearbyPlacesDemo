
package com.example.priyanka.mapsdemo;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by JungHyun on 2018-06-03.
 */

public class ListAdapter extends RecyclerView.Adapter<ItemHolder> {
    ArrayList<PlaceItems> Items;


    CardView cardView;
    ImageView list_img;
    TextView txt_title;


    public ListAdapter(ArrayList<PlaceItems> datas) {
        this.Items = datas;

    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        cardView = holder.cardView;
        list_img = holder.list_img;
        txt_title = holder.txt_title;


        txt_title.setText(Items.get(position).getTitle());


    }



    @Override
    public int getItemCount() {
        return Items.size();
    }
}
