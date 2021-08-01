package ddwu.mobile.finalproject.ma02_20181020;

import java.io.Serializable;

public class ReviewDto implements Serializable {

    int _id;
    String title;
    String date;
    String place;
    String memo;
    float rating;
    String gps_x;
    String gps_y;
    String imgLink;

    public ReviewDto(String title, String date, String place, String memo, float rating, String gps_x, String gps_y, String imgLink) {
        this.title = title;
        this.date = date;
        this.place = place;
        this.memo = memo;
        this.rating = rating;
        this.gps_x = gps_x;
        this.gps_y = gps_y;
        this.imgLink = imgLink;
    }

    public ReviewDto(int _id, String title, String date, String place, String memo, float rating, String gps_x, String gps_y, String imgLink) {
        this._id = _id;
        this.title = title;
        this.date = date;
        this.place = place;
        this.memo = memo;
        this.rating = rating;
        this.gps_x = gps_x;
        this.gps_y = gps_y;
        this.imgLink = imgLink;
    }

    public int get_id() { return _id; }

    public void set_id(int _id) { this._id = _id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDate() { return date; }

    public void setDate(String date) { this.date = date; }

    public String getPlace() { return place; }

    public void setPlace(String place) { this.place = place; }

    public String getMemo() { return memo; }

    public void setMemo(String memo) { this.memo = memo; }

    public float getRating() { return rating; }

    public void setRating(float rating) { this.rating = rating; }

    public String getGps_x() { return gps_x; }

    public void setGps_x(String gps_x) { this.gps_x = gps_x; }

    public String getGps_y() { return gps_y; }

    public void setGps_y(String gps_y) { this.gps_y = gps_y; }

    public String getImgLink() { return imgLink; }

    public void setImgLink(String imgLink) { this.imgLink = imgLink; }
}

