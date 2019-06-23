package test;

import cn.huse.sdk.Config;
import cn.huse.sdk.SdkUtils;
import cn.huse.sdk.contract.InputRequest;
import cn.huse.sdk.contract.InputRequestBuildFactory;
import cn.huse.sdk.entity.*;
import cn.huse.sdk.info.RawMaterial;
import cn.huse.sdk.info.base.BaseCompanyInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.bumo.blockchain.BlockService;
import io.bumo.common.ToBaseUnit;
import io.bumo.model.request.*;
import io.bumo.model.request.operation.*;
import io.bumo.model.response.AccountGetBalanceResponse;
import io.bumo.model.response.AccountGetMetadataResponse;
import io.bumo.model.response.BlockGetRewardResponse;
import io.bumo.model.response.ContractCallResponse;

import java.util.*;

import static cn.huse.sdk.SdkUtils.*;

/**
 * @author: huanxi
 * @date: 2019-06-01 22:12
 */
public class Test {


    @org.junit.Test
    //创建合约操作
    public void buildCreatContract() {
        String createContractAddress = "buQm1Ac8vkJ8skHz3BphveQVrosYpdBkz6mt";
        Long initBalance = ToBaseUnit.BU2MO("1000000");
        String payload = "\"use strict\";function init(info){let obj=JSON.parse(info);obj.info.operator=Chain.tx.initiator;Chain.store('initInfo',JSON.stringify(obj.info));Chain.store('alliance',obj.alliance);return'success';}function addData(info,dataType){info.operator=Chain.tx.initiator;let alliance=Chain.load('alliance');let res=Chain.contractQuery(alliance,JSON.stringify({'method':'checkUser','params':{'role':dataType,'address':info.operator}}));Chain.store('res',JSON.stringify(res));if(res.error){throw'check user error!';}if(!res.result){throw'do not have permission';}Chain.store(`${dataType}Info`,JSON.stringify(info));}function main(input_str){let input=JSON.parse(input_str);if(input.method==='addData'){addData(input.params.info,input.params.dataType);}else{throw'have no this method';}}function query(input){return input;}";
        // Build create contract operation
        ContractCreateOperation contractCreateOperation = new ContractCreateOperation();
        contractCreateOperation.setSourceAddress(createContractAddress);
        contractCreateOperation.setInitBalance(initBalance);
        contractCreateOperation.setPayload(payload);

        Map<String, String> info = new HashMap<String, String>();
        info.put("name", "永州异蛇酒");
        String alliance = "buQYiSJdhQhuNJ2VVsE7ZeAAbUhQJcAuiG4w";
        contractCreateOperation.setInitInput(InputRequestBuildFactory.buildInitProduct(info, alliance));
        BaseOperation[] operations = {contractCreateOperation};
        submitTx(operations);

    }

    @org.junit.Test
    //添加供应信息
    public void addUpplier(){
        ContractInvokeByBUOperation invokeByBUOperation = new ContractInvokeByBUOperation();
        invokeByBUOperation.setBuAmount(0L);
        invokeByBUOperation.setSourceAddress("buQYHht5uD1zXLEhYBy4QSYYrvtFkJQ3pmkf");
        invokeByBUOperation.setContractAddress("buQqjjRZSrDZgTKGT22L9HecSbi7nH4YVDYx");

        List<RawMaterial> materials = new ArrayList<RawMaterial>();

        RawMaterial material1=new RawMaterial();
        material1.setName("蒸馏酒，纯净水");
        material1.setImage("https://gss0.bdstatic.com/94o3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=c2cff052ba096b63811959563408e079/c2fdfc039245d6881cb685d9a0c27d1ed21b2412.jpg");
        material1.setPlace("永州市零陵区及江永县");
        material1.setProductionTime("2019年5月份");
        material1.setFunction("异蛇酒的溶液饮品");

        RawMaterial material2=new RawMaterial();
        material2.setName("枸杞,肉桂");
        material2.setImage("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/s%3D220/sign=2917f0cd9d8fa0ec7bc7630f1695594a/b7003af33a87e9507eedc1921c385343faf2b451.jpg");
        material2.setPlace("永州市零陵区及江永县");
        material2.setProductionTime("2019年5月份");
        material2.setFunction("为异蛇酒提供抗疲劳、抗辐射、保肝等功能");

        RawMaterial material3=new RawMaterial();
        material3.setName("乌梢蛇，蝮蛇");
        material3.setImage("https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/w%3D268%3Bg%3D0/sign=e2291041afc379317d68812fd3ffd078/b90e7bec54e736d1b5f75e4f9b504fc2d4626995.jpg");
        material3.setPlace("永州市零陵区及江永县");
        material3.setProductionTime("2019年5月份");
        material3.setFunction("异蛇酒的主材料，能有效的祛风除湿、舒筋活骨");

        materials.add(material1);
        materials.add(material2);
        materials.add(material3);

        Map<String, Object> info = new HashMap<String, Object>();
        info.put("info",materials);
        invokeByBUOperation.setInput(InputRequestBuildFactory.buildInvokeAddData(info, "SupplierAuthority"));
        BaseOperation[] operations = {invokeByBUOperation};
        System.out.println("添加供应数据");
        System.out.println(info);
        submitTx(operations,"buQYHht5uD1zXLEhYBy4QSYYrvtFkJQ3pmkf","privbvaydcwTdWmBPeJnJsaMK7s7k8VPrBiqvUw5QFxGodXmh6TsULoK");
    }

    //添加加工信息
    @org.junit.Test
    public void addProducer(){
        ContractInvokeByBUOperation invokeByBUOperation = new ContractInvokeByBUOperation();
        invokeByBUOperation.setBuAmount(0L);
        invokeByBUOperation.setSourceAddress("buQYHht5uD1zXLEhYBy4QSYYrvtFkJQ3pmkf");
        invokeByBUOperation.setContractAddress("buQqjjRZSrDZgTKGT22L9HecSbi7nH4YVDYx");

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
        submitTx(operations,"buQYHht5uD1zXLEhYBy4QSYYrvtFkJQ3pmkf","privbvaydcwTdWmBPeJnJsaMK7s7k8VPrBiqvUw5QFxGodXmh6TsULoK");
    }

    //添加物流信息
    @org.junit.Test
    public void addogistics(){
        ContractInvokeByBUOperation invokeByBUOperation = new ContractInvokeByBUOperation();
        invokeByBUOperation.setBuAmount(0L);
        invokeByBUOperation.setSourceAddress("buQYHht5uD1zXLEhYBy4QSYYrvtFkJQ3pmkf");
        invokeByBUOperation.setContractAddress("buQqjjRZSrDZgTKGT22L9HecSbi7nH4YVDYx");

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
        submitTx(operations,"buQYHht5uD1zXLEhYBy4QSYYrvtFkJQ3pmkf","privbvaydcwTdWmBPeJnJsaMK7s7k8VPrBiqvUw5QFxGodXmh6TsULoK");
    }

    @org.junit.Test
    public  void addDetection(){
        ContractInvokeByBUOperation invokeByBUOperation = new ContractInvokeByBUOperation();
        invokeByBUOperation.setBuAmount(0L);
        invokeByBUOperation.setSourceAddress("buQYHht5uD1zXLEhYBy4QSYYrvtFkJQ3pmkf");
        invokeByBUOperation.setContractAddress("buQqjjRZSrDZgTKGT22L9HecSbi7nH4YVDYx");

        DetectionInfo info=new DetectionInfo();
        info.setIngredient("2018年国家食品安全监督抽检实施细则中对白酒这一品类具体检测项目：酒精度、甲醇、氰化物、铅、糖精钠、甜蜜素、三氯蔗糖清香型白酒GB/T 10781.2-2006:酒精度、甲醇、氰化物、铅、糖精钠、甜蜜素、三氯蔗糖、浓香型白酒GB/T10781.1-2006:总酸（以乙酸计）、总酯（以乙酸乙酯计）、己酸乙酯、固形物米香型白酒cB/riors-3206:酒精度、甲醇、氯化物、铅、糖精钠、甜蜜素、三氯蔗糖、总酸（以乙酸计）、总酯（以乙酸乙酯计）、乳酸乙酯、β苯、乙液态法白酒GB/T 20821-2007、固液法白酒GB/T20822-2007:固形物、酒精度、甲醇、氰化物、铅、糖精钠、甜蜜素、三氯蔗糖、总酸（以乙酸计）、总酯（以乙酸乙酯计蒸馏酒及其配制酒（国抽专项）：酒精度、甲醇、部化物、铅、糖精钠、甜蜜素、三氯蔗糖");
        invokeByBUOperation.setInput(InputRequestBuildFactory.buildInvokeAddData(info, "detection"));
        BaseOperation[] operations = {invokeByBUOperation};
        submitTx(operations,"buQYHht5uD1zXLEhYBy4QSYYrvtFkJQ3pmkf","privbvaydcwTdWmBPeJnJsaMK7s7k8VPrBiqvUw5QFxGodXmh6TsULoK");
    }

    //鉴权
    @org.junit.Test
    public void query(){
        ContractCallRequest callesponse=new ContractCallRequest();
        callesponse.setSourceAddress(Config.genesisAddress);
        callesponse.setContractAddress("buQYiSJdhQhuNJ2VVsE7ZeAAbUhQJcAuiG4w");
        callesponse.setOptType(2);
        callesponse.setFeeLimit(1000L);
        callesponse.setGasPrice(1000L);

        InputRequest inputRequest=new InputRequest();
        inputRequest.setMethod("checkUser");
        inputRequest.addParam("role","admin");
        inputRequest.addParam("address","buQoVCn6s51gT2zMiz1KcvipYH7sqvtGP5aU");
        callesponse.setInput(inputRequest.toString());

        ContractCallResponse test = sdk.getContractService().call(callesponse);
        JSONObject a = (JSONObject) test.getResult().getQueryRets().get(0);
        System.out.println(a.toJSONString());
    }

    //发行资产
    public BaseOperation[] buildAssetIssue() {
        AssetIssueOperation assetIssueOperation = new AssetIssueOperation();
        assetIssueOperation.setSourceAddress(Config.genesisAddress);
        assetIssueOperation.setCode("milk1");
        assetIssueOperation.setAmount(20000L);
        BaseOperation[] operations = {assetIssueOperation};
        return operations;
    }


    //激活
    @org.junit.Test
    public void createOrg1() {
        AccountActivateOperation activateOperation = new AccountActivateOperation();
        activateOperation.setInitBalance(200 * Math.round(Math.pow(10, 8)));
        activateOperation.setSourceAddress(Config.genesisAddress);
        activateOperation.setDestAddress("buQYHht5uD1zXLEhYBy4QSYYrvtFkJQ3pmkf");
        submitTx(new AccountActivateOperation[]{activateOperation});
    }


    @org.junit.Test
    //设置组织1的基本信息
    public void setInfo2() {
        String accountAddress = "buQYHht5uD1zXLEhYBy4QSYYrvtFkJQ3pmkf";
        String privateKey = "privbvaydcwTdWmBPeJnJsaMK7s7k8VPrBiqvUw5QFxGodXmh6TsULoK";
        AccountSetMetadataOperation operation = new AccountSetMetadataOperation();
        operation.setSourceAddress(accountAddress);
        operation.setKey("info");
        BaseCompanyInfo company=new BaseCompanyInfo();
        company.setName("永州市原材料提供公司");
        company.setBusinessRegistrationNumber("13228499X");
        company.setDateOfApproval("310115000155893");
        company.setRegisteredAddress("中国湖南省永州市东南路4501号102室");
        company.setRegisteredCapital("54888万人民币");
        company.setScope("出口自织的话蛇岁蛇多品、蛇酒、白酒、罗研所需的原辅材料、早械电备、仪整、仪表岁零配节；特器认性动物养殖；永州格蛇酒生织销售；蛇鳖监列织品开发、因术、销售贩运；技入培训、地况岁识红。（以上没目涉岁需收与且可的凭而免且可外认否，法律法万禁止的不得认否）");
        company.setDateOfApproval("1993-11-23");
        company.setTaxpayerRegistration("9131011513228499XW");
        company.setImage("http://www.hongjiunet.cn/gavotte/images/brief_06.jpg");
        operation.setValue(JSON.toJSONString(company));
        System.out.println("设置组织信息...");
        JSON.parseObject(operation.getValue());
        System.out.println(operation.getValue());
        submitTx(new BaseOperation[]{operation}, accountAddress, privateKey);
    }


    @org.junit.Test
    public void getUserInfo(){
        AccountGetMetadataRequest request=new AccountGetMetadataRequest();
        request.setAddress("buQoVCn6s51gT2zMiz1KcvipYH7sqvtGP5aU");
        request.setKey("info");
        AccountGetMetadataResponse res = sdk.getAccountService().getMetadata(request);
        String json=res.getResult().getMetadatas()[0].getValue();
        System.out.println(json);
        JSONObject jsonObj = JSON.parseObject(json);
    }

    //查询余额
    @org.junit.Test
    public void getBalance(){
        AccountGetBalanceRequest request=new AccountGetBalanceRequest();
        request.setAddress(Config.genesisAddress);

        AccountGetBalanceResponse res = sdk.getAccountService().getBalance(request);
        long balance=res.getResult().getBalance();
        System.out.println(balance*Math.pow(10,-8));

    }

    @org.junit.Test
    public void blockServiceTest() {
        BlockService blockService = SdkUtils.sdk.getBlockService();
        System.out.println("区块高度：" + blockService.getNumber().getResult().getHeader().getBlockNumber());
        System.out.println("是否同步：" + blockService.checkStatus().getResult().getSynchronous());
        BlockGetRewardRequest rewardRequest = new BlockGetRewardRequest();
        rewardRequest.setBlockNumber(1L);
        BlockGetRewardResponse respose = blockService.getReward(rewardRequest);
        System.out.println(JSON.toJSONString(respose.getResult(), true));
    }
}
