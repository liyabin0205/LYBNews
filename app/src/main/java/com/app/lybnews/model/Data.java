package com.app.lybnews.model;

/**
 * @author: liyabin
 * @description:
 * @projectName: LYBNews
 * @date: 2016-08-28
 * @time: 17:04
 */
public class Data {
    private String title;

    private String date;

    private String author_name;

    private String thumbnail_pic_s;

    private String thumbnail_pic_s02;

    private String thumbnail_pic_s03;

    private String url;

    private String uniquekey;

    private String type;

    private String realtype;

    public void setTitle(String title){
        this.title = title;
    }
    public String getTitle(){
        return this.title;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getDate(){
        return this.date;
    }
    public void setAuthor_name(String author_name){
        this.author_name = author_name;
    }
    public String getAuthor_name(){
        return this.author_name;
    }
    public void setThumbnail_pic_s(String thumbnail_pic_s){
        this.thumbnail_pic_s = thumbnail_pic_s;
    }
    public String getThumbnail_pic_s(){
        return this.thumbnail_pic_s;
    }
    public void setThumbnail_pic_s02(String thumbnail_pic_s02){
        this.thumbnail_pic_s02 = thumbnail_pic_s02;
    }
    public String getThumbnail_pic_s02(){
        return this.thumbnail_pic_s02;
    }
    public void setThumbnail_pic_s03(String thumbnail_pic_s03){
        this.thumbnail_pic_s03 = thumbnail_pic_s03;
    }
    public String getThumbnail_pic_s03(){
        return this.thumbnail_pic_s03;
    }
    public void setUrl(String url){
        this.url = url;
    }
    public String getUrl(){
        return this.url;
    }
    public void setUniquekey(String uniquekey){
        this.uniquekey = uniquekey;
    }
    public String getUniquekey(){
        return this.uniquekey;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getType(){
        return this.type;
    }
    public void setRealtype(String realtype){
        this.realtype = realtype;
    }
    public String getRealtype(){
        return this.realtype;
    }

}
