package org;

import cn.huse.sdk.Config;
import cn.huse.sdk.SdkUtils;
import cn.huse.sdk.contract.InputRequestBuildFactory;
import cn.huse.sdk.db.LevelDbStoreImpl;
import cn.huse.sdk.info.RawMaterial;
import cn.huse.sdk.info.base.BaseAuthority;
import cn.huse.sdk.info.base.BaseCompanyInfo;
import cn.huse.sdk.info.base.BaseUserDetail;
import cn.huse.sdk.info.UserInfo;
import cn.huse.sdk.info.base.BaseUserInfo;
import com.alibaba.fastjson.JSON;
import io.bumo.model.request.operation.BaseOperation;
import io.bumo.model.request.operation.ContractInvokeByBUOperation;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.huse.sdk.SdkUtils.invokeContract;
import static cn.huse.sdk.SdkUtils.submitTx;

/**
 * @author: huanxi
 * @date: 2019-06-16 16:47
 */
public class SupplierTest extends BaseTest {

    //供应商管理员地址
    private static String adminAddress = "buQoVCn6s51gT2zMiz1KcvipYH7sqvtGP5aU";
    private static String privateKey = "privbwo4a7oUreNbYJhHqBj64sbnVZ6JBhu3L9KhUyzko3wtJkN1njgb";


    public static void init() {
        address = dbStore.get(SupplierTest.class.getSimpleName());
        if (address == null) setupSupplierInfo();
    }

    //初始化供应商信息
    public static void setupSupplierInfo() {
        BaseCompanyInfo companyInfo = new BaseCompanyInfo();
        companyInfo.setName("永州市原材料提供公司");
        companyInfo.setBusinessRegistrationNumber("13228499X");
        companyInfo.setDateOfApproval("310115000155893");
        companyInfo.setRegisteredAddress("中国湖南省永州市东南路4501号102室");
        companyInfo.setRegisteredCapital("54888万人民币");
        companyInfo.setScope("出口自织的话蛇岁蛇多品、蛇酒、白酒、罗研所需的原辅材料、早械电备、仪整、仪表岁零配节；特器认性动物养殖；永州格蛇酒生织销售；蛇鳖监列织品开发、因术、销售贩运；技入培训、地况岁识红。（以上没目涉岁需收与且可的凭而免且可外认否，法律法万禁止的不得认否）");
        companyInfo.setDateOfApproval("1993-11-23");
        companyInfo.setTaxpayerRegistration("9131011513228499XW");
        companyInfo.setImage("http://www.hongjiunet.cn/gavotte/images/brief_06.jpg");
        String[] adminList = new String[]{adminAddress};
        String contract = snakeWineSDK.setupOrgInfo(adminAddress, companyInfo, adminList, privateKey, new BaseAuthority());
        if (null != contract) System.out.println("原材料供应商创建成功，地址：" + contract);
        else return;
        address = contract;
        dbStore.put(SupplierTest.class.getSimpleName(), contract);
        //地址已添加

        addUser();
    }


    public static void addUser() {
        UserInfo info = new UserInfo();
        info.address = adminAddress;
        info.role = "admin";
        BaseUserDetail userDetail = new BaseUserDetail();
        userDetail.setName("黄花");
        userDetail.setRefId("1");
        userDetail.setDesc("供应商最高管理员");
        info.detail = userDetail;
        snakeWineSDK.addUserInfo(adminAddress, privateKey, address, info);
    }

    //添加测试数据
    public static void addData() {

        List<RawMaterial> materials = new ArrayList<RawMaterial>();

        RawMaterial material1 = new RawMaterial();
        material1.setName("蒸馏酒，纯净水");
        material1.setImage("https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=c2cff052ba096b63811959563408e079/c2fdfc039245d6881cb685d9a0c27d1ed21b2412.jpg");
        material1.setPlace("永州市零陵区及江永县");
        material1.setProductionTime("2019年5月份");
        material1.setFunction("异蛇酒的溶液饮品");

        RawMaterial material2 = new RawMaterial();
        material2.setName("枸杞,肉桂");
        material2.setImage("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/s%3D220/sign=2917f0cd9d8fa0ec7bc7630f1695594a/b7003af33a87e9507eedc1921c385343faf2b451.jpg");
        material2.setPlace("永州市零陵区及江永县");
        material2.setProductionTime("2019年5月份");
        material2.setFunction("为异蛇酒提供抗疲劳、抗辐射、保肝等功能");

        RawMaterial material3 = new RawMaterial();
        material3.setName("乌梢蛇，蝮蛇");
        material3.setImage("https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=e2291041afc379317d68812fd3ffd078/b90e7bec54e736d1b5f75e4f9b504fc2d4626995.jpg");
        material3.setPlace("永州市零陵区及江永县");
        material3.setProductionTime("2019年5月份");
        material3.setFunction("异蛇酒的主材料，能有效的祛风除湿、舒筋活骨");

        materials.add(material1);
        materials.add(material2);
        materials.add(material3);

        Map<String, Object> info = new HashMap<String, Object>();
        info.put("info", materials);
        String input = InputRequestBuildFactory.buildInvokeAddData(info, "supplier");
//        System.out.println("添加供应数据");
//        System.out.println(input);
        invokeContract(adminAddress,privateKey,getProduct(),input);
    }

    public static void setUpAdminInfo() {
        SdkUtils.setMetaData(Config.ORG_ADDRESS_KEY,dbStore.get(SupplierTest.class.getSimpleName()),adminAddress,privateKey);
    }
    public static String getAdminAddress() {
        return adminAddress;
    }
}
