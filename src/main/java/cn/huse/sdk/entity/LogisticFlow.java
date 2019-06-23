package cn.huse.sdk.entity;

import org.spongycastle.asn1.dvcs.Data;

import java.util.Date;

/**
 * @author: huanxi
 * @date: 2019-06-12 13:00
 */
public class LogisticFlow {
    private long time;
    private String context;

    public LogisticFlow( String context) {
        this.time = new Date().getTime();
        this.context = context;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
