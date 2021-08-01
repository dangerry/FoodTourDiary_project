package ddwu.mobile.finalproject.ma02_20181020;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class FoodXmlParser {

    public enum TagType { NONE, TITLE, ADDRESS, TEL, CONTENTID, FIRSTIMAGE, THUMBNAIL, MAPX, MAPY, AREACODE };

    final static String TAG_ITEM = "item";
    final static String TAG_TITLE = "title";
    final static String TAG_ADDRESS = "addr1";
    final static String TAG_TEL = "tel";
    final static String TAG_CONTENTID = "contentid";
    final static String TAG_FISRTIMAGE = "firstimage";
    final static String TAG_THUMBNAIL = "firstimage2";
    final static String TAG_MAPX = "mapx";
    final static String TAG_MAPY = "mapy";
    final static String TAG_AREACODE = "areacode";

    public FoodXmlParser() { }

    public ArrayList<FoodDto> parse (String xml) {

        ArrayList<FoodDto> resultList = new ArrayList<FoodDto>();
        FoodDto dto = null;

        TagType tagType = TagType.NONE;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(xml));

            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.END_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            dto = new FoodDto();
                        } else if (parser.getName().equals(TAG_TITLE)) {
                            if (dto != null) tagType = TagType.TITLE;
                        } else if (parser.getName().equals(TAG_ADDRESS)) {
                            if (dto != null) tagType = TagType.ADDRESS;
                        } else if (parser.getName().equals(TAG_TEL)) {
                            if (dto != null) tagType = TagType.TEL;
                        } else if (parser.getName().equals(TAG_CONTENTID)) {
                            if (dto != null) tagType = TagType.CONTENTID;
                        } else if (parser.getName().equals(TAG_FISRTIMAGE)) {
                            if (dto != null) tagType = TagType.FIRSTIMAGE;
                        } else if (parser.getName().equals(TAG_MAPX)) {
                            if (dto != null) tagType = TagType.MAPX;
                        } else if (parser.getName().equals(TAG_MAPY)) {
                            if (dto != null) tagType = TagType.MAPY;
                        } else if (parser.getName().equals(TAG_THUMBNAIL)) {
                            if (dto != null) tagType = TagType.THUMBNAIL;
                        } else if (parser.getName().equals(TAG_AREACODE)) {
                            if (dto != null) tagType = TagType.AREACODE;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals(TAG_ITEM)) {
                            resultList.add(dto);
                            dto = null;
                        }
                        break;
                    case XmlPullParser.TEXT:
                        switch (tagType) {
                            case TITLE:
                                dto.setTitle(parser.getText());
                                break;
                            case ADDRESS:
                                dto.setAddress(parser.getText());
                                break;
                            case TEL:
                                dto.setTel(parser.getText());
                                break;
                            case CONTENTID:
                                dto.setContentId(parser.getText());
                                break;
                            case FIRSTIMAGE:
                                dto.setFirstImage(parser.getText());
                                break;
                            case MAPX:
                                dto.setMapX(parser.getText());
                                break;
                            case MAPY:
                                dto.setMapY(parser.getText());
                                break;
                            case THUMBNAIL:
                                dto.setThumbnail(parser.getText());
                                break;
                            case AREACODE:
                                dto.setAreaCode(parser.getText());
                                break;
                        }
                        tagType = TagType.NONE;
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
}
