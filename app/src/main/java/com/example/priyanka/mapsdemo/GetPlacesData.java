package com.example.priyanka.mapsdemo;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    @Override
    protected String doInBackground(Object... objects){
        mMap = (GoogleMap)objects[0];
        url = (String)objects[1];
        recyclerView = (RecyclerView)objects[2];
        client = (GoogleApiClient)objects[3];

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
        ListAdapter listAdapter = new ListAdapter(placelist);
        recyclerView.setAdapter(listAdapter);


    }





    public ArrayList<PlaceItems> parseToJson(String s){
        ArrayList<PlaceItems> booklist = new ArrayList<>();
        JSONObject jsonObject = null;

        try{
            jsonObject = new JSONObject(s);
            JSONArray items = jsonObject.getJSONArray("results");
            PlacePhotoMetadataBuffer photoMetadataBuffer = null;
            String place_id = null;
            for(int i=0;i<items.length();i++){
                JSONObject c = items.getJSONObject(i);
                Log.d("JSONOB",""+c);
                PlaceItems placeItems = new PlaceItems();
                placeItems.setTitle(c.getString("name"));
                place_id = c.getString("id");
                Log.d("place_id",place_id);
                GoogleApiClient d = null;
                Log.d("clientLog",""+client);



                booklist.add(placeItems);
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
