package com.example.priyanka.mapsdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


class GetPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    String url;
    public RecyclerView recyclerView;
    public GoogleApiClient client;
    public Context context;

    //필드선언

    @Override
    protected String doInBackground(Object... objects){
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        recyclerView = (RecyclerView)objects[2];
        client = (GoogleApiClient)objects[3];
        context = (Context)objects[4];



        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
            //url 읽어들어옴

        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
        //onPostExecute가 실행됨
    }

    @Override
    protected void onPostExecute(String s){
        ArrayList<PlaceItems> placelist = new ArrayList<>();
        placelist = parseToJson(s);
        //String 을 잘조합하여 ArrayList형태로 반환
        ListAdapter listAdapter = new ListAdapter(placelist,context);
        recyclerView.setAdapter(listAdapter);

    }

    public ArrayList<PlaceItems> parseToJson(String s){
        //String 처리함
        ArrayList<PlaceItems> list = new ArrayList<>();
        JSONObject jsonObject = null;
        String string = null;
        PlaceItems placeItems = null;

        try{
            jsonObject = new JSONObject(s);
            JSONArray items = jsonObject.getJSONArray("results");

            Log.d("length",""+items.length());
            PlacePhotoMetadataBuffer photoMetadataBuffer = null;
            String place_id = null;
            for(int i=0;i<items.length();i++){
                JSONObject c = items.getJSONObject(i);



                if(c.has("photos")){


                    JSONArray temps = (JSONArray) c.get("photos");

                    JSONObject temp = temps.getJSONObject(0);


                    String tempStr = temp.getString("html_attributions");


                    int indexP = tempStr.indexOf("https:");

                    int indexN = tempStr.indexOf("photos")+6;

                    String str[] = tempStr.substring(indexP,indexN).split("\\\\");
                    StringBuffer sbr = new StringBuffer();

                    for(int k = 0 ; k<str.length;k++) {
                        sbr.append(str[k]);

                    }
                    string = sbr.toString();
                    //https:\/\/maps.google.com\/maps\/contrib\/105949256933641745835\/photos
                    //https://maps.google.com/maps/contrib/105949256933641745838/photos
                    //uri 형태에 맞게 변형
                    if(string!=null){
                        placeItems = new PlaceItems();
                        placeItems.setPhotos(string);
                        placeItems.setTitle(c.getString("name"));
                        placeItems.setVicinity(c.getString("vicinity"));
                        placeItems.setImageUrl(c.getString("icon"));

                        list.add(placeItems);
                        //list에 add
                    }




                }






            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }


}
