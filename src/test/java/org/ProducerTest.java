package org;

import cn.huse.sdk.Config;
import cn.huse.sdk.SdkUtils;
import cn.huse.sdk.SnakeWineSDK;
import cn.huse.sdk.contract.InputRequestBuildFactory;
import cn.huse.sdk.entity.ProcessFlow;
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
import java.util.Date;
import java.util.List;

import static cn.huse.sdk.SdkUtils.submitTx;

/**
 * @author: huanxi
 * @date: 2019-06-16 16:47
 */
public class ProducerTest extends BaseTest {


    //加工商管理员地址
    private static String adminAddress = "buQYHht5uD1zXLEhYBy4QSYYrvtFkJQ3pmkf";
    private static String privateKey = "privbvaydcwTdWmBPeJnJsaMK7s7k8VPrBiqvUw5QFxGodXmh6TsULoK";


    static SnakeWineSDK snakeWineSDK = SnakeWineSDK.getInstance();

    public static void init() {
        address = dbStore.get(ProducerTest.class.getSimpleName());
        if (address == null) setupProducerInfo();

    }

    //初始化供应商信息
    public static void setupProducerInfo() {
        BaseCompanyInfo companyInfo = new BaseCompanyInfo();
        companyInfo.setName("永州市xxx加工公司");
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
        if (null != contract) System.out.println("加工组织创建成功，地址：" + contract);
        else return;
        address = contract;
        dbStore.put(ProducerTest.class.getSimpleName(), contract);
        addProducerUser();
    }

    public static void addProducerUser() {
        UserInfo info = new UserInfo();
        info.address = adminAddress;
        info.role = "admin";
        BaseUserDetail userDetail = new BaseUserDetail();
        userDetail.setName("李付");
        userDetail.setRefId("1");
        userDetail.setDesc("加工商最高管理员");
        info.detail = userDetail;
        snakeWineSDK.addUserInfo(adminAddress, privateKey, address, info);
    }


    public static void addData() {
        ContractInvokeByBUOperation invokeByBUOperation = new ContractInvokeByBUOperation();
        invokeByBUOperation.setBuAmount(0L);
        invokeByBUOperation.setSourceAddress(adminAddress);
        invokeByBUOperation.setContractAddress(getProduct());

        List<ProcessFlow> processFlows=new ArrayList<ProcessFlow>();
        ProcessFlow processFlow=new ProcessFlow();
        processFlow.setTimestamp(new Date().getTime());
        processFlow.setTitle("对枸杞，肉桂进行加工提取");
        processFlow.setPrincipal("黄花");
        processFlow.setWorkshop("2区A栋车间C");

        processFlows.add(processFlow);
        ProcessFlow processFlow1=new ProcessFlow();
        processFlow1.setTimestamp(new Date().getTime());
        processFlow1.setTitle("蒸馏酒，纯净水加入");
        processFlow1.setPrincipal("黄花");
        processFlow1.setWorkshop("2区A栋车间C");

        processFlows.add(processFlow1);
        ProcessFlow processFlow2=new ProcessFlow();
        processFlow2.setTimestamp(new Date().getTime());
        processFlow2.setTitle("将异蛇处理后放入酒缸中进行发酵");
        processFlow2.setPrincipal("黄花");
        processFlow2.setWorkshop("2区A栋车间C");

        processFlows.add(processFlow2);
        ProcessFlow processFlow3=new ProcessFlow();
        processFlow3.setTimestamp(new Date().getTime());
        processFlow3.setTitle("生产完毕");
        processFlow3.setPrincipal("黄花");
        processFlow3.setWorkshop("2区A栋车间C");

        processFlows.add(processFlow3);

        invokeByBUOperation.setInput(InputRequestBuildFactory.buildInvokeAddData(processFlows, "producer"));

        BaseOperation[] operations = {invokeByBUOperation};
        submitTx(operations,adminAddress,privateKey);
    }

    public static void setUpAdminInfo() {
        SdkUtils.setMetaData(Config.ORG_ADDRESS_KEY,dbStore.get(ProducerTest.class.getSimpleName()),adminAddress,privateKey);
    }

    public static String getAdminAddress() {
        return adminAddress;
    }
}
