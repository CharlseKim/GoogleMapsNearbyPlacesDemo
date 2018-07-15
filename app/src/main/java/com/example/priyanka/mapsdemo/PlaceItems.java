package com.example.priyanka.mapsdemo;

import android.widget.ImageView;

import java.util.List;

public class PlaceItems {


        private String title;       //제목
        private ImageView image;    //이미지
        private String call;    //전화번호
        private String address;     //장소 주소

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    public String getCall() {
        return call;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
