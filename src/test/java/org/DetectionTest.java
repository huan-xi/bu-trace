package org;

import cn.huse.sdk.Config;
import cn.huse.sdk.SdkUtils;
import cn.huse.sdk.SnakeWineSDK;
import cn.huse.sdk.contract.InputRequestBuildFactory;
import cn.huse.sdk.entity.DetectionInfo;
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
public class DetectionTest extends BaseTest{


    //检测机构管理员地址
    private static String adminAddress = "buQXkuPtKwmrxFVTA4V1PAb28rMVtRqd6aE9";
    private static String privateKey = "privbw2iyM4NDcDnV9V11RmBp2LiowNFaDA4VsQutuSR1Uth7ntTvt5p";

    static SnakeWineSDK snakeWineSDK = SnakeWineSDK.getInstance();

    public static void init(){
        address =dbStore.get(DetectionTest.class.getSimpleName());
        if (address ==null) setupDetectionInfo();

    }
    //初始化供应商信息
    public static void setupDetectionInfo(){
        BaseCompanyInfo companyInfo=new BaseCompanyInfo();
        companyInfo.setName("永州市xxx检测机构");
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
        if (null!=contract) System.out.println("检测机构创建成功，地址：" + contract);
        else return;
        address =contract;
        dbStore.put(DetectionTest.class.getSimpleName(),contract);
        addUser();
    }

    public static void addUser() {
        UserInfo info=new UserInfo();
        info.address=adminAddress;
        info.role = "admin";
        BaseUserDetail userDetail=new BaseUserDetail();
        userDetail.setName("菜海");
        userDetail.setRefId("1");
        userDetail.setDesc("检测机构最高管理员");

        info.detail=userDetail;
        snakeWineSDK.addUserInfo(adminAddress,privateKey, address,info);
    }
    public static void setUpAdminInfo() {

        SdkUtils.setMetaData(Config.ORG_ADDRESS_KEY,dbStore.get(DetectionTest.class.getSimpleName()),adminAddress,privateKey);
    }
    public static void addData(){
        ContractInvokeByBUOperation invokeByBUOperation = new ContractInvokeByBUOperation();
        invokeByBUOperation.setBuAmount(0L);
        invokeByBUOperation.setSourceAddress(adminAddress);
        invokeByBUOperation.setContractAddress(getProduct());

        DetectionInfo info=new DetectionInfo();
        info.setIngredient("2018年国家食品安全监督抽检实施细则中对白酒这一品类具体检测项目：酒精度、甲醇、氰化物、铅、糖精钠、甜蜜素、三氯蔗糖清香型白酒GB/T 10781.2-2006:酒精度、甲醇、氰化物、铅、糖精钠、甜蜜素、三氯蔗糖、浓香型白酒GB/T10781.1-2006:总酸（以乙酸计）、总酯（以乙酸乙酯计）、己酸乙酯、固形物米香型白酒cB/riors-3206:酒精度、甲醇、氯化物、铅、糖精钠、甜蜜素、三氯蔗糖、总酸（以乙酸计）、总酯（以乙酸乙酯计）、乳酸乙酯、β苯、乙液态法白酒GB/T 20821-2007、固液法白酒GB/T20822-2007:固形物、酒精度、甲醇、氰化物、铅、糖精钠、甜蜜素、三氯蔗糖、总酸（以乙酸计）、总酯（以乙酸乙酯计蒸馏酒及其配制酒（国抽专项）：酒精度、甲醇、部化物、铅、糖精钠、甜蜜素、三氯蔗糖");
        invokeByBUOperation.setInput(InputRequestBuildFactory.buildInvokeAddData(info, "detection"));
        BaseOperation[] operations = {invokeByBUOperation};
        submitTx(operations,adminAddress,privateKey);
    }

    public static String getAdminAddress() {
        return adminAddress;
    }
}
