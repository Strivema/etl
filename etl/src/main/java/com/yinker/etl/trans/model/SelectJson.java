package com.yinker.etl.trans.model;

/**
 * Created by 崔博文 on 2017/11/6.20:49
 */
public class SelectJson {

    private String id;
    private String name;
    private String pId;
    private String open;
    private String select;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getpId () {
        return pId;
    }

    public void setpId (String pId) {
        this.pId = pId;
    }

    public String getOpen () {
        return open;
    }

    public void setOpen (String open) {
        this.open = open;
    }

    public String getSelect () {
        return select;
    }

    public void setSelect (String select) {
        this.select = select;
    }
}
