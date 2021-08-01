package ddwu.mobile.finalproject.ma02_20181020;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddReviewActivity extends AppCompatActivity {

    public static final String TAG = "AddReviewActivity";

    EditText etDate, etMemo, etPlace, etTitle;
    RatingBar ratingBar;
    private ImageView imageView;

    FoodReviewDBManager dbManager;
    FoodNetworkManager networkManager;

    double latitude, longitude;

    FoodDto food;
    String imgLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_review);

        food = (FoodDto) getIntent().getSerializableExtra("food");

        etDate = findViewById(R.id.etAddDate);
        etMemo = findViewById(R.id.etAddMemo);
        etPlace = findViewById(R.id.etAddPlace);
        ratingBar = findViewById(R.id.addRating);
        etTitle = findViewById(R.id.etAddTitle);
        imageView = findViewById(R.id.ivAddReview);

        dbManager = new FoodReviewDBManager(this);
        networkManager = new FoodNetworkManager(this);

        etTitle.setText(food.getTitle());

        latitude = Double.parseDouble(food.getMapY());
        longitude = Double.parseDouble(food.getMapX());

        imgLink = food.getFirstImage();
        new ImageAsyncTask().execute(imgLink);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddOk:
                if (etMemo.getText().toString().equals("")) {
                    Toast.makeText(this, "메모는 필수 입력 항목입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    if (etDate.getText().toString().equals("")) {
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat sDate = new SimpleDateFormat("yyyy년 MM월 dd일");
                        etDate.setText(sDate.format(date));
                    }
                    if (etPlace.getText().toString().equals("")) {
                        etPlace.setText(food.getAddress());
                    }
                    if (etTitle.getText().toString().equals("")) {
                        etTitle.setText(food.getTitle());
                    }

                    boolean result = dbManager.addNewReview(new ReviewDto(etTitle.getText().toString(),
                            etDate.getText().toString(), etPlace.getText().toString(), etMemo.getText().toString(),
                            ratingBar.getRating(), food.getMapX(), food.getMapY(), imgLink));

                    if (result) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("review", etTitle.getText().toString());

                        setResult(RESULT_OK, resultIntent);
                        Toast.makeText(this, "리뷰 저장 성공!!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "리뷰 작성 실패!!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.btnAddCancle:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }

    class ImageAsyncTask extends AsyncTask<String, Integer, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Log.d(TAG, "Download Image");
            Bitmap bitmap = networkManager.downloadImage(strings[0]);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }
}