package ddwu.mobile.finalproject.ma02_20181020;

import java.io.Serializable;

public class FoodDto implements Serializable {

    private int _id;
    /* 기본 정보 */
    private String title;       // 제목
    private String address;     // 주소
    private String tel;         // 전화번호
    private String contentId;   // 콘텐츠 id
    private String areaCode;    // 지역코드
    private String firstImage;  // 대표 이미지
    private String thumbnail;   // 썸네일
    private String mapX;        // GPS X좌표
    private String mapY;        // GPS Y좌표
    private String toggle;

    public int get_id() { return _id; }

    public void set_id(int _id) { this._id = _id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getTel() { return tel; }

    public void setTel(String tel) { this.tel = tel; }

    public String getMapX() { return mapX; }

    public void setMapX(String mapX) { this.mapX = mapX; }

    public String getMapY() { return mapY; }

    public void setMapY(String mapY) { this.mapY = mapY; }

    public String getContentId() { return contentId; }

    public void setContentId(String contentId) { this.contentId = contentId; }

    public String getFirstImage() { return firstImage; }

    public void setFirstImage(String firstImage) { this.firstImage = firstImage; }

    public String getAreaCode() { return areaCode; }

    public void setAreaCode(String areaCode) { this.areaCode = areaCode; }

    public String getToggle() { return toggle; }

    public void setToggle(String toggle) { this.toggle = toggle; }
}
