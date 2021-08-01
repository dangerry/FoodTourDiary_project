package ddwu.mobile.finalproject.ma02_20181020;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageFileManager {

    final static String TAG = "ImageFileManager";

    private Context context;

    public ImageFileManager(Context context) {
        this.context = context;
    }

    public String saveBitmapToTemporary(Bitmap bitmap, String url) {
        String fileName = null;
        try {
            fileName = getFileNameFromUrl(url);
            File saveFile = new File(context.getFilesDir(), fileName);
            FileOutputStream fos = new FileOutputStream(saveFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            fileName = null;
        } catch (IOException e) {
            e.printStackTrace();
            fileName = null;
        }
        return fileName;
    }

    public Bitmap getBitmapFromTemporary(String url) {
        String fileName = getFileNameFromUrl(url);
        String path = context.getFilesDir().getPath() + "/" + fileName;

        Bitmap bitmap = null;
        bitmap = BitmapFactory.decodeFile(path);
        Log.i(TAG, path);

        return bitmap;
    }

    public String getFileNameFromUrl(String url) {
        String fileName = Uri.parse(url).getLastPathSegment();
        return fileName.replace("\n", "");
    }
}
