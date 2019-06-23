package cn.huse.sdk.entity;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: huanxi
 * @date: 2019-06-13 20:00
 */
public class LogisticInfo {
    private String image;
    private String source;
    private String waybill;
    private String telephone;
    private boolean status;
    private List<LogisticFlow> info;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getWaybill() {
        return waybill;
    }

    public void setWaybill(String waybill) {
        this.waybill = waybill;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void addFlow(LogisticFlow flow){
        if (null == info) {
            info = new ArrayList<LogisticFlow>();
        }
        info.add(flow);
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<LogisticFlow> getInfo() {
        return info;
    }

    public void setInfo(List<LogisticFlow> info) {
        this.info = info;
    }
}
