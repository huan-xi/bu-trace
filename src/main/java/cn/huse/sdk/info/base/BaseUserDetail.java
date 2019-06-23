package cn.huse.sdk.info.base;

/**
 * @author: huanxi
 * @date: 2019-06-13 21:43
 */
public class BaseUserDetail {
    protected String name; //姓名
    protected String refId; //外部数据id
    protected String desc; //描述

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
