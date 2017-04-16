package com.hyc;

/**
 * Created by hyc on 2017/4/16 13:12
 */

public class DataStores {
    private static HotMovieBean.SubjectsBean subjectsBean;

    public static HotMovieBean.SubjectsBean getSubjectsBean() {
        return subjectsBean;
    }

    public static void setSubjectsBean(HotMovieBean.SubjectsBean subjectsBean) {
        DataStores.subjectsBean = subjectsBean;
    }
}
