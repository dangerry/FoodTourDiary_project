package ddwu.mobile.finalproject.ma02_20181020;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateReviewActivity extends AppCompatActivity {

    public static final String TAG = "UpdateReviewActivity";

    ReviewDto review;

    EditText etDate, etMemo, etPlace, etTitle;
    RatingBar ratingBar;
    ImageView imageView;

    FoodReviewDBManager dbManager;
    FoodNetworkManager networkManager;

    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_review);

        review = (ReviewDto) getIntent().getSerializableExtra("review");

        etDate = findViewById(R.id.etUpdateDate);
        etMemo = findViewById(R.id.etUpdateMemo);
        etPlace = findViewById(R.id.etUpdatePlace);
        ratingBar = findViewById(R.id.updateRating);
        etTitle = findViewById(R.id.etUpdateTitle);
        imageView = findViewById(R.id.ivUpdateReview);

        dbManager = new FoodReviewDBManager(this);
        networkManager = new FoodNetworkManager(this);

        etDate.setText(review.getDate());
        etMemo.setText(review.getMemo());
        etPlace.setText(review.getPlace());
        ratingBar.setRating(review.getRating());
        etTitle.setText(review.getTitle());
        new ImageAsyncTask().execute(review.getImgLink());

        latitude = Double.parseDouble(review.getGps_y());
        longitude = Double.parseDouble(review.getGps_x());
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnUpdateOk :
                if (etMemo.getText().toString().equals("")) {
                    Toast.makeText(this, "메모는 필수 입력 항목입니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (etDate.getText().toString().equals("")) {
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat sDate = new SimpleDateFormat("yyyy년 MM월 dd일");
                        etDate.setText(sDate.format(date));
                    }
                    if (etTitle.getText().toString().equals("")) {
                        etTitle.setText(review.getTitle());
                    }
                    if (etPlace.getText().toString().equals("")) {
                        etPlace.setText(review.getPlace());
                    }

                    review.setTitle(etTitle.getText().toString());
                    review.setDate(etDate.getText().toString());
                    review.setMemo(etMemo.getText().toString());
                    review.setPlace(etPlace.getText().toString());
                    review.setRating(ratingBar.getRating());
                    review.setImgLink(review.getImgLink());

                    if (dbManager.modifyReview(review)) {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("review", review);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(this, "리뷰 수정 실패!!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.btnUpdateCancle :
                setResult(RESULT_CANCELED);
                finish();
                break;
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
