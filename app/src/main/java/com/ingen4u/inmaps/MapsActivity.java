package com.ingen4u.inmaps;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private final int REQUEST_LOCATION_PERMISSION = 101;
    private final int REQUEST_WRITE_SDCARD = 102;
    private final int REQUEST_READ_SDCARD = 103;

    private GoogleMap mMap;

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(5);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {
                    mMap.clear();
                    mMap.setMinZoomPreference(12);

                    CircleOptions circleOptions = new CircleOptions();
                    circleOptions.center(new LatLng(location.getLatitude(),
                            location.getLongitude()));
                    circleOptions.radius(200);
                    circleOptions.fillColor(Color.RED);
                    circleOptions.strokeWidth(6);

                    mMap.addCircle(circleOptions);
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Tampilkan Notifikasi Permintaan Ijin
        this.reqPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    // Event Saat Map Siap Tampil
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        mMap.setOnMyLocationClickListener(onMyLocationClickListener);
        enableMyLocationIfPermitted();

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMinZoomPreference(11);

        // Ambil Data CCTV, Tempat Saya, dan Iklan
        this.fetchMapsData();
    }

    // Fungsi Untuk Mengambil Data CCTV, Tempat Saya, dan Iklan
    private void fetchMapsData(){
        // ............
    }

    // Tampilkan Tombol Lokasi Saya jika ijin Melacak Lokasi Saya diberikan
    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    // Fungsi Mendapatkan Ijin Akses Aplikasi
    private void reqPermission(){
        // Request Fine Location Permission
        int permissionFineLoc = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionFineLoc != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }

        // Tampilkan Notifikasi Membutuhkan Ijin Akses Menulis Memori Internal
        int permissionWriteExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_SDCARD);
        }

        // Tampilkan Notifikasi Membutuhkan Ijin Akses Membaca Memori Internal
        int permissionReadExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_READ_SDCARD);
        }
    }

    // Event Saat Aplikasi Ditutup
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Event Saat Tmbol Back Di Tekan
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        // Keluar Dari Aplikasi
        finishAffinity();
    }

}
