package cn.huse.sdk.contract;

import cn.huse.sdk.info.base.BaseAuthority;
import cn.huse.sdk.info.base.BaseCompanyInfo;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @author: huanxi
 * 请求参数构建
 * @date: 2019-06-05 20:06
 */
public class InputRequestBuildFactory {
    public static String buildInvokeAddData(Map<String,Object> info,String dataType){
        InputRequest input =new InputRequest();
        input.setMethod("addData");
        input.setInfos(info);
        input.addParam("dataType",dataType);
        return input.toString();
    }
    public static String buildInvokeAddData(Object info,String dataType){
        InputRequest input =new InputRequest();
        input.setMethod("addData");
        input.setInfo("info",info);
        input.addParam("dataType",dataType);
        return input.toString();
    }
    public static String buildInitProduct(Object info,String alliance) {
        JSONObject input=new JSONObject();
        input.put("info",info);
        input.put("alliance", alliance);
        return input.toJSONString();
    }

    public static String buildInitOrg(String[] adminList, BaseCompanyInfo orgInfo, BaseAuthority authority) {
        JSONObject input = new JSONObject();
        input.put("adminList",adminList);
        input.put("orgInfo", orgInfo);
        input.put("authority",authority);
        return input.toJSONString();
    }

    /**
     *
     * @param info 用户信息
     * @param address 用户地址
     * @param role 用户角色 admin
     * @return
     */
    public static String buildInvokeAddUser(Object info,String address,String role) {
        InputRequest input =new InputRequest();
        input.setMethod("addUser");
        input.setInfo("info",info);
        input.addParam("address",address);
        input.addParam("role",role);
        return input.toString();
    }

    public static String buildInitAlliance(String[] supplier,String[] producer,String[] detection,String[] logistics,String[] admin) {
        JSONObject input=new JSONObject();
        input.put("supplier", supplier);
        input.put("producer", producer);
        input.put("detection", detection);
        input.put("logistics",logistics);
        input.put("admin", admin);
        return input.toJSONString();
    }
}
