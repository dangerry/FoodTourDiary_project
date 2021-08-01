package ddwu.mobile.finalproject.ma02_20181020;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FoodReviewDBHelper extends SQLiteOpenHelper {

    final static String DB_NAME = "food.db";
    public final static String TABLE_NAME = "food_table";
    public final static String COL_ID = "_id";
    public final static String COL_TITLE = "title";     // 식당 이름
    public final static String COL_DATE = "date";       // 날짜
    public final static String COL_PLACE = "place";     // 장소
    public final static String COL_GPSX = "gps_x";      // GPS X좌표
    public final static String COL_GPSY = "gps_y";      // GPS Y좌표
    public final static String COL_MEMO = "memo";       // 메모
    public final static String COL_RATING = "rating";   // 별점
    public final static String COL_IMAGE = "imgLink";   // 이미지


    public FoodReviewDBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" + COL_ID + " integer primary key autoincrement, " +
                COL_TITLE + " TEXT, " + COL_DATE + " TEXT, " + COL_RATING + " FLOAT, " +
                COL_PLACE + " TEXT, " + COL_GPSX + " TEXT, " + COL_GPSY + " TEXT, " + COL_MEMO + " TEXT, " + COL_IMAGE + " TEXT)";

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
