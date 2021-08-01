package ddwu.mobile.finalproject.ma02_20181020;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class FoodAdapter extends BaseAdapter {

    public static final String TAG = "CnEAdapter";

    private LayoutInflater inflater;
    private Context context;
    private int layout;
    private ArrayList<FoodDto> list;
    private FoodNetworkManager networkManager = null;
    private ImageFileManager imageFileManager = null;

    final int ADD_CODE = 100;

    public FoodAdapter(Context context, int resource, ArrayList<FoodDto> list) {
        this.context = context;
        this.layout = resource;
        this.list = list;
        imageFileManager = new ImageFileManager(context);
        networkManager = new FoodNetworkManager(context);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        return list.get(position).get_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder = null;

        if (view == null) {
            view = inflater.inflate(layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = view.findViewById(R.id.tvTitle);
            viewHolder.tvAddress = view.findViewById(R.id.tvAddr);
            viewHolder.tvTel = view.findViewById(R.id.tvTel);
            viewHolder.ivImage = view.findViewById(R.id.ivReview);
            viewHolder.btnLocation = view.findViewById(R.id.btnLocation);
            viewHolder.btnReview = view.findViewById(R.id.btnReview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        FoodDto dto = list.get(position);

        viewHolder.tvTitle.setText(dto.getTitle());
        viewHolder.tvAddress.setText(dto.getAddress());
        viewHolder.tvTel.setText(dto.getTel());
        viewHolder.btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LocationActivity.class);
                intent.putExtra("food", dto);
                context.startActivity(intent);
            }
        });
        viewHolder.btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, AddReviewActivity.class);
                intent.putExtra("food", dto);
                context.startActivity(intent);
            }
        });

        if (dto.getThumbnail() == null) {
            viewHolder.ivImage.setImageResource(R.mipmap.ic_launcher);
            return view;
        }

        Bitmap savedBitmap = imageFileManager.getBitmapFromTemporary(dto.getThumbnail());    // 파일 이름

        if (savedBitmap != null) {
            viewHolder.ivImage.setImageBitmap(savedBitmap);
            Log.d(TAG, "Image loading from file");
        }
        else {
            viewHolder.ivImage.setImageResource(R.mipmap.ic_launcher);
            new GetImageAsyncTask(viewHolder).execute(dto.getThumbnail());
            Log.d(TAG, "Image loading from network");
        }

        return view;
    }

    public void setList(ArrayList<FoodDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        public TextView tvTitle = null;
        public TextView tvAddress = null;
        public TextView tvTel = null;
        public ImageView ivImage = null;
        public ImageButton btnLocation = null;
        public ImageButton btnReview = null;
    }

    class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {

        ViewHolder viewHolder;
        String imageAddress;

        public GetImageAsyncTask(ViewHolder holder) {
            viewHolder = holder;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            imageAddress = params[0];
            Bitmap result = null;
            result = networkManager.downloadImage(imageAddress);
            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                viewHolder.ivImage.setImageBitmap(bitmap);
                imageFileManager.saveBitmapToTemporary(bitmap, imageAddress);
            }
        }
    }

}
