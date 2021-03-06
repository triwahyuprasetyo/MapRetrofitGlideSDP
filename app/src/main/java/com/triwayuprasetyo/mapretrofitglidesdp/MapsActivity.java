package com.triwayuprasetyo.mapretrofitglidesdp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.triwayuprasetyo.mapretrofitglidesdp.gmap.PermissionUtils;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLongClickListener, GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback, LocationListener {
    /**
     * Request code for location permission request.
     *
     * @see #onRequestPermissionsResult(int, String[], int[])
     */
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    protected LocationManager locationManager;
    private GoogleMap mMap;
    /**
     * Flag indicating whether a requested permission has been denied after returning in
     * {@link #onRequestPermissionsResult(int, String[], int[])}.
     */
    private boolean mPermissionDenied = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);

        int sdkVersion = Build.VERSION.SDK_INT;
        if (sdkVersion < 23) {
            Log.i("SDK VERSION", "" + sdkVersion);
//            LatLng uns = new LatLng(-7.561577, 110.856582);
//            mMap.addMarker(new MarkerOptions().position(uns).title("Univ 11 Maret"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(uns));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        } else {
            Log.i("SDK VERSION", "Marshmallow : " + sdkVersion);
//            LatLng istanaNegara = new LatLng(-6.168731, 106.824055);
//            mMap.addMarker(new MarkerOptions().position(istanaNegara).title("Jalan Merdeka"));
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(istanaNegara));
//            mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
        }

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Call your Alert message
            Log.i("GPS_PROVIDER", "ENABLE");

            mMap.setOnMyLocationButtonClickListener(this);
            enableMyLocation();
            findLocationSingleRequestGPS();
        }else {
            Log.i("GPS_PROVIDER", "DISABLE");
            String locationProviders = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (locationProviders == null || locationProviders.equals("")) {
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                buildAlertMessageNoGps();
            }
        }

/*
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.i("NETWORK_PROVIDER", "ENABLE");
            mMap.setOnMyLocationButtonClickListener(this);
            enableMyLocation();
            findLocationSingleRequestNetwork();
        }else{
            Log.i("NETWORK_PROVIDER", "DISABLE");
            buildAlertMessageNoNetwork();
        }
 */
    }

    private void buildAlertMessageNoNetwork() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Network seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void findLocationSingleRequestGPS() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
            Log.i("LOCATION MANAGER GPS", "Permission to access the location is missing");
        } else if (locationManager != null) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, this, null);
            Log.i("LOCATION  MANAGER GPS", "ENABLE");
        }
    }

    private void findLocationSingleRequestNetwork() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
            Log.i("LOCATION MANAGER NET", "Permission to access the location is missing");
        } else if (locationManager != null) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, this, null);
            Log.i("LOCATION  MANAGER NET", "ENABLE");
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        double latitude = latLng.latitude;
        double longitude = latLng.longitude;
        Log.i("Latitude", latitude + "");
        Log.i("Longitude", longitude + "");
        Toast.makeText(getApplicationContext(), latitude + " - " + longitude, Toast.LENGTH_SHORT).show();
        pinMarker(latLng);
    }

    private void pinMarker(final LatLng latLng) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Type Location Name");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(input);
        builder.setPositiveButton("Add Marker", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameLocation = input.getText().toString();
                if (!nameLocation.trim().equals("")) {
                    mMap.addMarker(new MarkerOptions().title(nameLocation).position(latLng));
                    Log.i("MAP", "Add Marker Success");
                } else {
                    Log.i("MAP", "Marker Not Added");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i("MAP", "Cancel Add Marker");
            }
        });

        builder.show();
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    android.Manifest.permission.ACCESS_FINE_LOCATION, true);
            Log.i("MAP LOCATION", "Permission to access the location is missing");
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);

            Log.i("MAP LOCATION", "ENABLE");
        }
    }


    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            Log.i("MAP Permission", "Granted");
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            Log.i("MAP Permission", "Not Granted");
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.i("LOCATION", "FOUND");
        Log.i("Latitude", location.getLatitude() + "");
        Log.i("Longitude", location.getLongitude() + "");
        LatLng istanaNegara = new LatLng(location.getLatitude(), location.getLongitude());
        //mMap.addMarker(new MarkerOptions().position(istanaNegara).title("Jalan Merdeka"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(istanaNegara));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i("StatusChanged", "" + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i("Provider", "Enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i("Provider", "Disabled");
    }
}
