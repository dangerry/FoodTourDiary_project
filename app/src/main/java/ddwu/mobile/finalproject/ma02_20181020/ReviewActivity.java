package ddwu.mobile.finalproject.ma02_20181020;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity {

    final int ADD_CODE = 100;
    final int DETAIL_CODE = 200;

    ListView listView;
    ReviewAdapter adapter;
    ArrayList<ReviewDto> list = null;
    FoodReviewDBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_activity);

        listView = findViewById(R.id.lv_review);
        list = new ArrayList();
        adapter = new ReviewAdapter(this, R.layout.listview_review, list);
        listView.setAdapter(adapter);
        dbManager = new FoodReviewDBManager(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ReviewDto review = list.get(position);
                Intent intent = new Intent(ReviewActivity.this, ReviewDetailActivity.class);
                intent.putExtra("review", review);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
                builder.setTitle("리뷰 삭제")
                        .setIcon(R.drawable.ic_pencil)
                        .setMessage(list.get(pos).getTitle() + " 리뷰를 삭제하시겠습니까?")
                        .setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (dbManager.removeReview(list.get(pos).get_id())) {
                                    Toast.makeText(ReviewActivity.this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                                    list.clear();
                                    list.addAll(dbManager.getAllReview());
                                    adapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(ReviewActivity.this, "삭제 실패!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("취소", null)
                        .setCancelable(false)
                        .show();

                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        list.addAll(dbManager.getAllReview());
        adapter.notifyDataSetChanged();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnReviewSearch:
                Intent intent = new Intent(ReviewActivity.this, SearchActivity.class);
                startActivity(intent);
                break;
            case R.id.btnReviewMap :
                Intent lIntent = new Intent(ReviewActivity.this, ReviewLocationActivity.class);
                lIntent.putExtra("review", list);
                startActivity(lIntent);
        }
    }

}


