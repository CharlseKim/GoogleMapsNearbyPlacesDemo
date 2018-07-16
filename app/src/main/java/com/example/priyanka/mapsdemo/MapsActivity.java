package com.example.priyanka.mapsdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
GoogleApiClient.OnConnectionFailedListener,
LocationListener{


    private GoogleMap mMap;
    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private Location lastlocation;
    private Marker currentLocationmMarker;  //지도에 표시되는 마커
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 5000;   //검색범위

    double latitude,longitude;

    public RecyclerView recyclerView;

    //필드선언


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //펄미션 체크
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();

        }

        //SupportMapFragment를 가져와 지도를 사용할 수 있음
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapFragment.getView().setVisibility(View.INVISIBLE);


        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager layoutManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(layoutManager);
        //recyclerView 설정


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //퍼미션권한 요청
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) !=  PackageManager.PERMISSION_GRANTED)
                    {
                        if(client == null)
                        {
                            //Api 빌드
                            bulidGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else
                {

                }
        }
    }


    //지도가 준비되면 호출된다.
    //장소에 마커를 추가하거나 카메라를 움질일 수 있다
   @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //퍼미션체크
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            //API 빌드
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


    }


    protected synchronized void bulidGoogleApiClient() {
        //API 빌드
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();


    }

    //위치 이동이 이뤄지면 호출된다.
    @Override
    public void onLocationChanged(Location location) {

        latitude = location.getLatitude();      //현재의 위도정보 가져오기
        longitude = location.getLongitude();    //현재의 경도정보 가져오기
        lastlocation = location;                //위치 저장
        if(currentLocationmMarker != null)
        {
            currentLocationmMarker.remove();    //마커를 지움

        }


        //카메라 이동 처리에 필요한 클래스
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());

        //Marker를 만든다
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        //마커의 위치설정
        markerOptions.title("Current Location");
        //마커의 제목설정
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        //마커 아이콘 설정
        currentLocationmMarker = mMap.addMarker(markerOptions);
        //마커추가

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //이동처리
        mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
        //줌

        if(client != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
        }
    }

    public void onClick(View v)
    {
        Object dataTransfer[] = new Object[5];
        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
        GetPlacesData getPlacesData = new GetPlacesData();

        //필드선언

        switch(v.getId())
        {
            case R.id.B_search:
                mMap.clear();
                String restaurant = "restaurant";
                String url = getUrl(latitude, longitude, restaurant);
                //요청할 api 주소를 반환된다 요청할 곳의 위치정보(위도,경도)와 종류(음식)를 파라미터로 받음
                Log.d("resclick",url);
                Log.d("ObjectClint",""+client);
                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                dataTransfer[2] = recyclerView;
                dataTransfer[3] = client;
                dataTransfer[4] = getApplicationContext();

                //Object 배열에 0번 째에 GoogleMap 을 1번째에는 요청 api 주소

                //장소데이터를 처리하고 화면에 뿌려주는 AsyncTask 호출
                getPlacesData.execute(dataTransfer);
                //Object 를 인자로 받음
                Toast.makeText(MapsActivity.this, "Showing Nearby Restaurants", Toast.LENGTH_SHORT).show();
                Button btn = findViewById(R.id.B_search);
                btn.setVisibility(View.INVISIBLE);
                break;


        }
    }

    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        //입력된 사용자의 위도,경도,장소타입 을 받아서
        //조립한 후 String 으로 리턴

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&types="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyDW58Amxy2klMcesFN2OT6XzvZZAj3Zyp0");

        Log.d("MapsActivit", "url = "+googlePlaceUrl.toString());

        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
            //위치업데이트
        }
    }


    public boolean checkLocationPermission()
            //퍼미션체크
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)  != PackageManager.PERMISSION_GRANTED )
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else
            return true;
    }


    @Override
    public void onConnectionSuspended(int i) {
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        //연결실패
    }
}
