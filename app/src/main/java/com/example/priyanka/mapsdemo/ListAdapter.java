
package com.example.priyanka.mapsdemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ItemHolder> {
    ArrayList<PlaceItems> Items;


    CardView cardView;
    ImageView list_img;
    TextView txt_title;
    TextView txt_vicinity;
    Context context;

    //필드 선언


    public ListAdapter(ArrayList<PlaceItems> datas,Context context) {
        this.Items = datas;
        this.context = context;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_list_item, parent, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        cardView = holder.cardView;
        list_img = holder.list_img;
        txt_title = holder.txt_title;
        txt_vicinity = holder.txt_vicinity;

        txt_title.setText(Items.get(position).getTitle());
        txt_vicinity.setText(Items.get(position).getVicinity());
        if(!Items.get(position).getImageUrl().isEmpty()){
            Glide.with(context)
                    .load(Items.get(position).getImageUrl())
                    .into(list_img);
            //이미지 uri에 있는 이미지를 holder의 list_img에 담음
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            //아이템 이 눌렸을 경우 상세보기 화면으로 사진uri 를 담아 넘겨줌
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,DetailView.class);
                intent.putExtra("Photos",Items.get(position).getPhotos());
                Log.d("PhotosURL",Items.get(position).getPhotos());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }



    @Override
    public int getItemCount() {
        return Items.size();
    }
}
