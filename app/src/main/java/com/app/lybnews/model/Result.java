package com.app.lybnews.model;

/**
 * @author: liyabin
 * @description:
 * @projectName: LYBNews
 * @date: 2016-08-28
 * @time: 17:04
 */
import java.util.List;
public class Result {
    private String stat;

    private List<Data> data ;

    public void setStat(String stat){
        this.stat = stat;
    }
    public String getStat(){
        return this.stat;
    }
    public void setData(List<Data> data){
        this.data = data;
    }
    public List<Data> getData(){
        return this.data;
    }

}
