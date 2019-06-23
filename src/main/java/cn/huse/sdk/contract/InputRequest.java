package cn.huse.sdk.contract;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: huanxi
 * @date: 2019-06-05 19:51
 */
public class InputRequest extends JSONObject {
    private String method;
    private Map<String, Object> params = new HashMap<String, Object>();
    private Map<String, Object> infos = new HashMap<String, Object>();

    public String getJsonString() {
        this.put("method", method);
        if (infos.size() > 0) params.put("info", infos);
        if (params.size() > 0) this.put("params", params);
        return this.toJSONString();
    }

    public void addParam(String paramName, String paramValue) {
        params.put(paramName, paramValue);
    }

    public void setInfo(String paramName, Object paramValue) {
        infos.put(paramName, paramValue);
    }

    public void setInfos(Map<String, Object> infos) {
        this.infos = infos;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }


    @Override
    public String toString() {
        return getJsonString();
    }
}
