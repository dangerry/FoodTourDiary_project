package ddwu.mobile.finalproject.ma02_20181020;

/* */

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    final static int PERMISSIONS_REQ_CODE = 100;

    Spinner spinner;
    String apiAddress, query;

    ArrayList<FoodDto> resultList;
    FoodXmlParser parser;
    FoodNetworkManager networkManager;

    private GoogleMap mGoogleMap;
    private Location lastLocation;
    private LocationManager locationManager;

    ArrayList<Marker> allMarkers = new ArrayList<Marker>();
    double latitude, longitude, lat, lon;
    private Marker marker, markers;

    FloatingActionButton search, review, current, fab;
    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
        search = findViewById(R.id.FABtnSearch);
        review = findViewById(R.id.FABtnReview);
        current = findViewById(R.id.FABtnCurrent);
        fab = findViewById(R.id.FABtn);

        fab.setOnClickListener(new FABClickListener());
        current.setOnClickListener(new FABClickListener());
        review.setOnClickListener(new FABClickListener());
        search.setOnClickListener(new FABClickListener());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);

        resultList = new ArrayList<FoodDto>();
        apiAddress = getResources().getString(R.string.food_api_url);
        parser = new FoodXmlParser();
        networkManager = new FoodNetworkManager(this);

        spinner = findViewById(R.id.spinner_main);
        ArrayAdapter<CharSequence> spinnerAdpater = ArrayAdapter.createFromResource(MainActivity.this,
                R.array.area, android.R.layout.simple_spinner_item);
        spinnerAdpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdpater);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 : query = "1"; lat = 37.540705; lon = 126.956764; break;
                    case 1 : query = "2"; lat = 37.469221; lon = 126.573234; break;
                    case 2 : query = "3"; lat = 36.321655; lon = 127.378953; break;
                    case 3 : query = "4"; lat = 35.798838; lon = 128.583052; break;
                    case 4 : query = "5"; lat = 35.126033; lon = 126.831302; break;
                    case 5 : query = "6"; lat = 35.198362; lon = 129.053922; break;
                    case 6 : query = "7"; lat = 35.519301; lon = 129.239078; break;
                    case 7 : query = "8"; lat = 36.573250; lon = 127.284935; break;
                    case 8 : query = "31"; lat = 37.567167; lon = 127.190292; break;
                    case 9 : query = "32"; lat = 37.555837; lon = 128.209315; break;
                    case 10 : query = "33"; lat = 36.628503; lon = 127.929344; break;
                    case 11 : query = "34"; lat = 36.557229; lon = 126.779757; break;
                    case 12 : query = "35"; lat = 36.248647; lon = 128.664734; break;
                    case 13 : query = "36"; lat = 35.259787; lon = 128.664734; break;
                    case 14 : query = "37"; lat = 35.716705; lon = 127.144185; break;
                    case 15 : query = "38"; lat = 34.819400; lon = 126.893113; break;
                    case 16 : query = "39"; lat = 33.364805; lon = 126.542671; break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    private void locationUpdate() {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 60 , 5, locationListener);
        }
    }

    private void getLastLocation() {
        if (checkPermission()) {
            lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    public void anim() {
        if (isFabOpen) {
            current.startAnimation(fab_close);
            review.startAnimation(fab_close);
            search.startAnimation(fab_close);
            current.setClickable(false);
            review.setClickable(false);
            search.setClickable(false);
            isFabOpen = false;
        } else {
            current.startAnimation(fab_open);
            review.startAnimation(fab_open);
            search.startAnimation(fab_open);
            current.setClickable(true);
            review.setClickable(true);
            search.setClickable(true);
            isFabOpen = true;
        }
    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.search:
                if (marker != null) {
                    for (Marker mark : allMarkers)
                        mark.remove();
                }
                allMarkers.clear();
                try {
                    new NetworkAsyncTask().execute(apiAddress + URLEncoder.encode(query, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    class NetworkAsyncTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            String address = strings[0];
            String result = null;
            result = networkManager.downloadContents(address);
            if (result == null)
                return "Error!";

            Log.d(TAG, result);

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            resultList = parser.parse(result);

            for (int i = 0; i < resultList.size(); i++) {
                LatLng latLng =
                        new LatLng(Double.parseDouble(resultList.get(i).getMapY()),
                                Double.parseDouble(resultList.get(i).getMapX()));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(resultList.get(i).getTitle());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                markers = mGoogleMap.addMarker(markerOptions);
                allMarkers.add(markers);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 10));
            }
        }
    }

    class FABClickListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.FABtn :
                    anim();
                    break;
                case R.id.FABtnCurrent :
                    if (marker != null)
                        marker.remove();

                    getLastLocation();

                    if (lastLocation != null) {
                        latitude = lastLocation.getLatitude();
                        longitude = lastLocation.getLongitude();
                    } else {
                        latitude = 37.606537;
                        longitude = 127.041758;
                    }

                    LatLng currentLoc = new LatLng(latitude, longitude);
                    Log.v(TAG, "결과 : " + latitude + ", " + longitude);
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

                    MarkerOptions centerMarkerOptions = new MarkerOptions();
                    centerMarkerOptions.position(currentLoc);
                    centerMarkerOptions.title("내 위치");
                    centerMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                    marker = mGoogleMap.addMarker(centerMarkerOptions);
                    break;
                case R.id.FABtnReview :
                    Intent rIntent = new Intent(MainActivity.this, ReviewActivity.class);
                    startActivity(rIntent);
                    break;
                case R.id.FABtnSearch :
                    Intent sIntent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(sIntent);
                    break;
            }
        }
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.2, 127.7), 7));

            getLastLocation();
            if (lastLocation != null) {
                latitude = lastLocation.getLatitude();
                longitude = lastLocation.getLongitude();
            } else {
                latitude = 37.606537;
                longitude = 127.041758;
            }
        }
    };

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {     }

        @Override
        public void onProviderEnabled(String provider) {    }

        @Override
        public void onProviderDisabled(String provider) {    }
    };

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED)  {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_REQ_CODE);
                return false;
            } else
                return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQ_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationUpdate();
            } else {
                Toast.makeText(this, "앱 실행을 위해 권한 허용이 필요함", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

}