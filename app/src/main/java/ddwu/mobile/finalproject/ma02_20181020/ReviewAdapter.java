package ddwu.mobile.finalproject.ma02_20181020;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ReviewAdapter extends BaseAdapter {

    final int UPDATE_CODE = 200;

    private Context context;
    private int layout;
    private ArrayList<ReviewDto> list;
    private LayoutInflater layoutInflater;
    FoodNetworkManager networkManager;

    public ReviewAdapter(Context context, int layout, ArrayList<ReviewDto> list) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        networkManager = new FoodNetworkManager(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.tvTitle = convertView.findViewById(R.id.tvReviewTitle);
            viewHolder.tvDate = convertView.findViewById(R.id.tvReviewDate);
            viewHolder.ratingBar = convertView.findViewById(R.id.reviewRating);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvTitle.setText(list.get(pos).getTitle());
        viewHolder.tvDate.setText(list.get(pos).getDate());
        viewHolder.ratingBar.setRating(list.get(pos).getRating());

        viewHolder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setRating(rating);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvDate;
        RatingBar ratingBar;
    }

}
