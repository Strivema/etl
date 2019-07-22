package com.yinker.etl.trans.model;

/**
 * EChart's modo
 * if need more ,you can add on this.
 *
 * @author 崔博文
 * @date 2018/3/19
 */
public class EchartData {

    private String value;
    private String name;

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }
}
