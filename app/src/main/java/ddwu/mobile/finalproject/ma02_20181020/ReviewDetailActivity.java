package ddwu.mobile.finalproject.ma02_20181020;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ActivityChooserView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class ReviewDetailActivity extends AppCompatActivity {

    public static final String TAG = "ReviewDetailActivity";

    final int UPDATE_CODE = 200;

    ReviewDto review;

    TextView tvDate, tvMemo, tvPlace, tvTitle;
    RatingBar ratingBar;
    ImageView imageView;

    FoodReviewDBManager dbManager;
    FoodNetworkManager networkManager;

    double latitude, longitude;
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_detail);

        review = (ReviewDto) getIntent().getSerializableExtra("review");

        tvDate = findViewById(R.id.tvDetailDate);
        tvMemo = findViewById(R.id.tvDetailMemo);
        tvPlace = findViewById(R.id.tvDetailPlace);
        ratingBar = findViewById(R.id.detailRating);
        tvTitle = findViewById(R.id.tvDetailTitle);
        imageView = findViewById(R.id.ivDetailReview);

        dbManager = new FoodReviewDBManager(this);
        networkManager = new FoodNetworkManager(this);

        tvDate.setText(review.getDate());
        tvMemo.setText(review.getMemo());
        tvPlace.setText(review.getPlace());
        ratingBar.setRating(review.getRating());
        tvTitle.setText(review.getTitle());
        new ImageAsyncTask().execute(review.getImgLink());

        ratingBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // ?????? ????????? ??????
                return true;
            }
        });

        latitude = Double.parseDouble(review.getGps_y());
        longitude = Double.parseDouble(review.getGps_x());

        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.detailMap);
        mapFragment.getMapAsync(mapReadyCallBack);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateDetail:
                Intent intent = new Intent(ReviewDetailActivity.this, UpdateReviewActivity.class);
                intent.putExtra("review", review);
                startActivityForResult(intent, UPDATE_CODE);
                break;

            case R.id.btnDetailOk:
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.btnShare :
                Intent msg = new Intent(Intent.ACTION_SEND);
                msg.addCategory(Intent.CATEGORY_DEFAULT);
                msg.putExtra(Intent.EXTRA_SUBJECT, "?? ??? ??? ??? ??? ??\n");
                msg.putExtra(Intent.EXTRA_TEXT, review.getTitle() + "\n?????? : " + review.getPlace() + "\n?????? ???????????????!! ?????? ??????!!");
                msg.putExtra(Intent.EXTRA_TITLE, review.getTitle());
                msg.setType("text/plain");
                startActivity(Intent.createChooser(msg, "????????? ?????? ??????????????????."));
                break;

        }
    }

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;

            LatLng location = new LatLng(latitude, longitude);
            Log.v(TAG, "?????? : " + latitude + ", ?????? : " + longitude);

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(location);
            markerOptions.title(review.getTitle());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            mGoogleMap.addMarker(markerOptions);

            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_CODE) {
            switch (resultCode) {
                case RESULT_OK :
                    Toast.makeText(this, "????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, ReviewActivity.class);
                    startActivity(intent);
                    break;
                case RESULT_CANCELED:
                    Toast.makeText(this, "?????? ????????? ?????????????????????.", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    class ImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... inputParams) {
            Bitmap bitmap = networkManager.downloadImage(inputParams[0]);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}
