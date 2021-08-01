package ddwu.mobile.finalproject.ma02_20181020;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class FoodReviewDBManager {

    FoodReviewDBHelper dbHelper = null;
    Cursor cursor = null;

    public FoodReviewDBManager(Context context) { dbHelper = new FoodReviewDBHelper(context); }

    public ArrayList<ReviewDto> getAllReview() {
        ArrayList reviewList = new ArrayList();
        SQLiteDatabase foodDB = dbHelper.getReadableDatabase();
        cursor = foodDB.rawQuery("SELECT * FROM " + FoodReviewDBHelper.TABLE_NAME, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(FoodReviewDBHelper.COL_ID));
            String title = cursor.getString(cursor.getColumnIndex(FoodReviewDBHelper.COL_TITLE));
            String date = cursor.getString(cursor.getColumnIndex(FoodReviewDBHelper.COL_DATE));
            String place = cursor.getString(cursor.getColumnIndex(FoodReviewDBHelper.COL_PLACE));
            float rating = cursor.getFloat(cursor.getColumnIndex(FoodReviewDBHelper.COL_RATING));
            String gpsX = cursor.getString(cursor.getColumnIndex(FoodReviewDBHelper.COL_GPSX));
            String gpsY = cursor.getString(cursor.getColumnIndex(FoodReviewDBHelper.COL_GPSY));
            String memo = cursor.getString(cursor.getColumnIndex(FoodReviewDBHelper.COL_MEMO));
            String imgLink = cursor.getString(cursor.getColumnIndex(FoodReviewDBHelper.COL_IMAGE));

            reviewList.add (new ReviewDto(id, title, date, place, memo, rating, gpsX, gpsY, imgLink));
        }

        cursor.close();
        dbHelper.close();
        return reviewList;
    }

    public boolean addNewReview(ReviewDto newReview) {
        SQLiteDatabase foodDB = dbHelper.getWritableDatabase();
        ContentValues value = new ContentValues();

        value.put(dbHelper.COL_TITLE, newReview.getTitle());
        value.put(dbHelper.COL_DATE, newReview.getDate());
        value.put(dbHelper.COL_PLACE, newReview.getPlace());
        value.put(dbHelper.COL_RATING, newReview.getRating());
        value.put(dbHelper.COL_GPSX, newReview.getGps_x());
        value.put(dbHelper.COL_GPSY, newReview.getGps_y());
        value.put(dbHelper.COL_MEMO, newReview.getMemo());
        value.put(dbHelper.COL_IMAGE, newReview.getImgLink());

        long count = foodDB.insert(dbHelper.TABLE_NAME, null, value);
        dbHelper.close();

        if (count > 0) {
            return true;
        }
        return false;
    }

    public boolean modifyReview(ReviewDto review) {
        SQLiteDatabase foodDB = dbHelper.getWritableDatabase();
        ContentValues row = new ContentValues();

        row.put(dbHelper.COL_TITLE, review.getTitle());
        row.put(dbHelper.COL_DATE, review.getDate());
        row.put(dbHelper.COL_PLACE, review.getPlace());
        row.put(dbHelper.COL_RATING, review.getRating());
        row.put(dbHelper.COL_GPSX, review.getGps_x());
        row.put(dbHelper.COL_GPSY, review.getGps_y());
        row.put(dbHelper.COL_MEMO, review.getMemo());
        row.put(dbHelper.COL_IMAGE, review.getImgLink());

        String whereClause = dbHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(review.get_id()) };
        int result = foodDB.update(dbHelper.TABLE_NAME, row, whereClause, whereArgs);
        dbHelper.close();

        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean removeReview(long id) {
        SQLiteDatabase foodDB = dbHelper.getWritableDatabase();
        String whereClause = dbHelper.COL_ID + "=?";
        String[] whereArgs = new String[] { String.valueOf(id) };
        int result = foodDB.delete(dbHelper.TABLE_NAME, whereClause,whereArgs);
        dbHelper.close();

        if (result > 0) {
            return true;
        }
        return false;
    }
}

