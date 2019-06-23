package org;

import cn.huse.sdk.Config;
import cn.huse.sdk.SdkUtils;
import cn.huse.sdk.SnakeWineSDK;
import cn.huse.sdk.contract.InputRequestBuildFactory;
import cn.huse.sdk.entity.LogisticFlow;
import cn.huse.sdk.entity.LogisticInfo;
import cn.huse.sdk.info.UserInfo;
import cn.huse.sdk.info.base.BaseAuthority;
import cn.huse.sdk.info.base.BaseCompanyInfo;
import cn.huse.sdk.info.base.BaseUserDetail;
import io.bumo.model.request.operation.BaseOperation;
import io.bumo.model.request.operation.ContractInvokeByBUOperation;
import org.junit.Test;

import static cn.huse.sdk.SdkUtils.submitTx;

/**
 * @author: huanxi
 * @date: 2019-06-16 16:47
 */
public class LogisticsTest extends BaseTest{


    //出仓物流公司管理员地址
    private static String adminAddress = "buQqQzm4wZcPYJg9hct3cmSRVUijMCXLMinz";
    private static String privateKey = "privbxVgXDznH9jHF349FNPyAPmXnM2CE3QpPaAFkoYSPxC956BR64DY";

    static SnakeWineSDK snakeWineSDK = SnakeWineSDK.getInstance();

    public static void init(){
        address =dbStore.get(LogisticsTest.class.getSimpleName());
        if (address ==null) setupDetectionInfo();

    }
    //初始化供应商信息
    @Test
    public static void setupDetectionInfo(){
        BaseCompanyInfo companyInfo=new BaseCompanyInfo();
        companyInfo.setName("永州市xxx物流公司");
        companyInfo.setBusinessRegistrationNumber("13228499X");
        companyInfo.setDateOfApproval("310115000155893");
        companyInfo.setRegisteredAddress("中国湖南省永州市东南路4501号102室");
        companyInfo.setRegisteredCapital("54888万人民币");
        companyInfo.setScope("出口自织的话蛇岁蛇多品、蛇酒、白酒、罗研所需的原辅材料、早械电备、仪整、仪表岁零配节；特器认性动物养殖；永州格蛇酒生织销售；蛇鳖监列织品开发、因术、销售贩运；技入培训、地况岁识红。（以上没目涉岁需收与且可的凭而免且可外认否，法律法万禁止的不得认否）");
        companyInfo.setDateOfApproval("1993-11-23");
        companyInfo.setTaxpayerRegistration("9131011513228499XW");
        companyInfo.setImage("http://www.hongjiunet.cn/gavotte/images/brief_06.jpg");
        String[] adminList= new String[]{adminAddress};
        String contract=snakeWineSDK.setupOrgInfo(adminAddress,companyInfo,adminList,privateKey, new BaseAuthority());
        if (null!=contract) System.out.println("物流公司创建成功，地址：" + contract);
        else return;
        address =contract;
        dbStore.put(LogisticsTest.class.getSimpleName(),contract);
        addUser();
    }

    @Test
    public static void addUser() {
        UserInfo info=new UserInfo();
        info.address=adminAddress;
        info.role = "admin";
        BaseUserDetail userDetail=new BaseUserDetail();
        userDetail.setName("唐建");
        userDetail.setRefId("1");
        userDetail.setDesc("xx物流公司最高管理员");
        info.detail=userDetail;
        snakeWineSDK.addUserInfo(adminAddress,privateKey, address,info);
    }
    public static void setUpAdminInfo() {
        SdkUtils.setMetaData(Config.ORG_ADDRESS_KEY,dbStore.get(LogisticsTest.class.getSimpleName()),adminAddress,privateKey);
    }
    public static void addData(){
        ContractInvokeByBUOperation invokeByBUOperation = new ContractInvokeByBUOperation();
        invokeByBUOperation.setBuAmount(0L);
        invokeByBUOperation.setSourceAddress(adminAddress);
        invokeByBUOperation.setContractAddress(getProduct());

        LogisticInfo info = new LogisticInfo();
        info.setImage("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1560427609&di=61ca0527c31ada22bcf4f05a6f30d00e&src=http://img.jdzj.com/UserDocument/2015b/ZW5656896/Picture/2015910235544.jpg");
        info.setStatus(false);
        info.setSource("永州异蛇酒生成公司");
        info.setWaybill("05224327542523");
        info.setTelephone("17680506845");
        LogisticFlow flow=new LogisticFlow("包裹正在揽收");
        info.addFlow(flow);
        info.addFlow(new LogisticFlow("永州零陵区已收入"));
        info.addFlow(new LogisticFlow("衡阳装运公司收入"));
        info.addFlow(new LogisticFlow("上海装运公司已收入"));
        info.addFlow(new LogisticFlow("包裹已到达上海"));

        invokeByBUOperation.setInput(InputRequestBuildFactory.buildInvokeAddData(info, "logistics"));
        BaseOperation[] operations = {invokeByBUOperation};
        submitTx(operations,adminAddress,privateKey);
    }

    public static String getAdminAddress() {
        return adminAddress;
    }
}
