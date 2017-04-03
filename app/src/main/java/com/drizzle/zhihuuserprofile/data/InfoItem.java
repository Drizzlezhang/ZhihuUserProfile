package com.drizzle.zhihuuserprofile.data;

/**
 * Created by drizzle on 2017/4/2.
 */
public class InfoItem {

    public static final int TYPE_CUSTOM_TITLE = 1;//小标题
    public static final int TYPE_SIMPLE_INFO = 2;//基本信息
    public static final int TYPE_SIMPLE_ITEM = 3;//我的回答、提问等等
    public static final int TYPE_QUESTION = 4;//关注了问题、赞同了回答（无图）
    public static final int TYPE_PERSONAL_ACHIEVEMENT = 5;//个人成就
    public static final int TYPE_DETAIL_INFO = 6;//详细信息

    private int infoType;

    public InfoItem(int infoType) {
        this.infoType = infoType;
    }

    public int getInfoType() {
        return infoType;
    }

    public void setInfoType(int infoType) {
        this.infoType = infoType;
    }
}
