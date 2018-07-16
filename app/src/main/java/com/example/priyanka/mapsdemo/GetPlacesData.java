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
/**
 * @author Priyanka
 */

class GetPlacesData extends AsyncTask<Object, String, String> {

    private String googlePlacesData;
    private GoogleMap mMap;
    String url;
    public RecyclerView recyclerView;
    public GoogleApiClient client;
    public Context context;

    @Override
    protected String doInBackground(Object... objects){
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        recyclerView = (RecyclerView)objects[2];
        client = (GoogleApiClient)objects[3];
        context = (Context)objects[4];

        Log.d("recylerViewL",""+recyclerView);

        DownloadURL downloadURL = new DownloadURL();
        try {
            googlePlacesData = downloadURL.readUrl(url);
            Log.d("googlePlacesData ",googlePlacesData);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String s){
        ArrayList<PlaceItems> placelist = new ArrayList<>();
        placelist = parseToJson(s);
        ListAdapter listAdapter = new ListAdapter(placelist,context);
        recyclerView.setAdapter(listAdapter);


    }

    public ArrayList<PlaceItems> parseToJson(String s){
        ArrayList<PlaceItems> booklist = new ArrayList<>();
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
                Log.d("JSONOB",""+c);


                if(c.has("photos")){
                    Log.d("HaveKey",c.getString("name"));

                    JSONArray temps = (JSONArray) c.get("photos");
                    Log.d("JSONAR",""+temps);
                    JSONObject temp = temps.getJSONObject(0);
                    Log.d("tempL",""+temp);
                    Log.d(("tempLL"),""+temp.get("html_attributions"));

                    String tempStr = temp.getString("html_attributions");
                    Log.d("tempStr",tempStr);

                    int indexP = tempStr.indexOf("https:");

                    int indexN = tempStr.indexOf("photos")+6;

                    String str[] = tempStr.substring(indexP,indexN).split("\\\\");
                    StringBuffer sbr = new StringBuffer();

                    for(int k = 0 ; k<str.length;k++) {
                        sbr.append(str[k]);

                    }
                    string = sbr.toString();
                    if(string!=null){
                        placeItems = new PlaceItems();
                        placeItems.setPhotos(string);
                        placeItems.setTitle(c.getString("name"));
                        placeItems.setVicinity(c.getString("vicinity"));
                        placeItems.setImageUrl(c.getString("icon"));
                        Log.d("indexOf",string);
                        booklist.add(placeItems);
                    }




                }






            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return booklist;
    }



    private void showNearbyPlaces(List<HashMap<String, String>> nearbyPlaceList)
    {
        for(int i = 0; i < nearbyPlaceList.size(); i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = nearbyPlaceList.get(i);

            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            double lat = Double.parseDouble( googlePlace.get("lat"));
            double lng = Double.parseDouble( googlePlace.get("lng"));

            LatLng latLng = new LatLng( lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : "+ vicinity);
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

            mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }
}
