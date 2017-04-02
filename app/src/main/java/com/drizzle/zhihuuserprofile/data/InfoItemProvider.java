package com.drizzle.zhihuuserprofile.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by drizzle on 2017/4/2.
 */
public class InfoItemProvider {

    public static List<InfoItem> getHomepageInfo() {
        List<InfoItem> homepageInfoList = new ArrayList<>();
        homepageInfoList.add(new InfoItem(InfoItem.TYPE_SIMPLE_INFO));
        for (int i = 0; i < 6; i++) {
            homepageInfoList.add(new InfoItem(InfoItem.TYPE_SIMPE_ITEM));
        }
        homepageInfoList.add(new InfoItem(InfoItem.TYPE_CUSTOM_TITLE));
        for (int i = 0; i < 20; i++) {
            homepageInfoList.add(new InfoItem(InfoItem.TYPE_QUESTION));
        }
        return homepageInfoList;
    }

    public static List<InfoItem> getDetailInfo() {
        List<InfoItem> detailInfoList = new ArrayList<>();
        detailInfoList.add(new InfoItem(InfoItem.TYPE_PERSONAL_ACHIEVEMENT));
        for (int i = 0; i < 8; i++) {
            detailInfoList.add(new InfoItem(InfoItem.TYPE_DETAIL_INFO));
        }
        return detailInfoList;
    }

    public static int getInfoTypeCount() {
        return 8;
    }
}
