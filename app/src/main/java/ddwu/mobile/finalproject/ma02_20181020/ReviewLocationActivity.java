package ddwu.mobile.finalproject.ma02_20181020;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import java.util.ArrayList;

public class ReviewLocationActivity extends AppCompatActivity {

    public static final String TAG = "LocationActivity";

    final static int PERMISSIONS_REQ_CODE = 100;

    ArrayList<ReviewDto> list;

    double lat, lon;
    private GoogleMap mGoogleMap;
    private Marker currentMarker;

    private Location lastLocation;
    private LocationManager locationManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_activity);

        list = (ArrayList<ReviewDto>) getIntent().getSerializableExtra("review");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.locationMap);
        mapFragment.getMapAsync(mapReadyCallBack);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReviewLocation:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btnReviewCurrent:
                getLastLocation();
                if (lastLocation != null) {
                    lat = lastLocation.getLatitude();
                    lon = lastLocation.getLongitude();
                } else {
                    lat = 37.606537;
                    lon = 127.041758;
                }
                if (currentMarker != null)
                    currentMarker.remove();

                LatLng currentLoc = new LatLng(lat, lon);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 9));

                MarkerOptions centerMarkerOptions = new MarkerOptions();
                centerMarkerOptions.position(currentLoc);
                centerMarkerOptions.title("??? ??????");
                centerMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                currentMarker = mGoogleMap.addMarker(centerMarkerOptions);
                break;
        }
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

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            getLastLocation();

            for (int i = 0; i < list.size(); i++) {
                LatLng latLng =
                        new LatLng(Double.parseDouble(list.get(i).getGps_y()),
                                Double.parseDouble(list.get(i).getGps_x()));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(list.get(i).getTitle());
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                mGoogleMap.addMarker(markerOptions);
                mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36.2, 127.7), 7));
            }
        }
    };

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
            currentMarker.setPosition(currentLoc);
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
                Toast.makeText(this, "??? ????????? ?????? ?????? ????????? ?????????", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
