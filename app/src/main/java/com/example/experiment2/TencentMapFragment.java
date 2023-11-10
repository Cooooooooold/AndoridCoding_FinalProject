package com.example.experiment2;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.experiment2.data.DataDownload;
import com.example.experiment2.data.ShopLocation;
import com.tencent.tencentmap.mapsdk.maps.CameraUpdateFactory;
import com.tencent.tencentmap.mapsdk.maps.TencentMap;
import com.tencent.tencentmap.mapsdk.maps.model.LatLng;
import com.tencent.tencentmap.mapsdk.maps.model.Marker;
import com.tencent.tencentmap.mapsdk.maps.model.MarkerOptions;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TencentMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TencentMapFragment extends Fragment {
//    private MapView mMapView = null;
    private com.tencent.tencentmap.mapsdk.maps.MapView mapView = null;
    private TencentMap mTencentMap;
    public TencentMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment BaiduMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TencentMapFragment newInstance() {
        TencentMapFragment fragment = new TencentMapFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }
    public class DataDownloadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String ... urls) {
            return new DataDownload().download(urls[0]);
        }
        @Override
        protected void onPostExecute(String responseData) {
            super.onPostExecute(responseData);
            if (responseData != null) {
                ArrayList<ShopLocation> shopLocations= new DataDownload().parseJsonObjects(responseData);
                TencentMap tencentMap = mapView.getMap();
                for (ShopLocation shopLocation : shopLocations) {
                    LatLng point1 = new LatLng(shopLocation.getLatitude(), shopLocation.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions(point1)
                            .title(shopLocation.getName());
                    Marker marker = tencentMap.addMarker(markerOptions);
                }
            }
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tencent_map, container, false);
        mapView = rootView.findViewById(R.id.mapView);
//        DataDownload dataDownload = new DataDownload();

        new DataDownloadTask().execute("http://file.nidama.net/class/mobile_develop/data/bookstore2023.json");


        mTencentMap = mapView.getMap();
//
        // 添加图标型Marker
        LatLng position1 = new LatLng(22.252731, 113.535649);//北门坐标
        mTencentMap.moveCamera(CameraUpdateFactory.newLatLng(position1));

        // 创建一个Marker对象
        MarkerOptions markerOptions = new MarkerOptions(position1);
        //点击Marker是否弹信息框
        markerOptions.infoWindowEnable(true);//默认为true
        //信息框编辑
        markerOptions.title("暨南大学珠海校区-北门")
                .snippet("坐标:(22.252702,113.53562)");
        // 添加标记到地图上
        Marker marker = mTencentMap.addMarker(markerOptions);
        //显示信息窗口
        marker.showInfoWindow();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String responseData=new DataDownload().download("http://file.nidama.net/class/mobile_develop/data/bookstore.json");
                ArrayList<ShopLocation> shopLocations= new DataDownload().parseJsonObjects(responseData);
                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TencentMap tencentMap = mapView.getMap();
                        for (ShopLocation shopLocation : shopLocations) {
                            LatLng point = new LatLng(shopLocation.getLatitude(), shopLocation.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions(point)
                                    .title(shopLocation.getName());
                            Marker marker = tencentMap.addMarker(markerOptions);


                        }
                    }
                });
            }
        }).start();
        return rootView;
    }
//    MarkerOptions markerOptions = new MarkerOptions()
//            .position(new LatLng(latitude, longitude))
//            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
//            .title("Marker Title")
//            .snippet("Marker Snippet");
//    Marker marker = tencentMap.addMarker(markerOptions);

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}