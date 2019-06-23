package cn.huse.sdk;

import cn.huse.sdk.contract.InputRequestBuildFactory;
import cn.huse.sdk.info.base.BaseAuthority;
import cn.huse.sdk.info.base.BaseCompanyInfo;
import cn.huse.sdk.info.UserInfo;
import io.bumo.model.request.operation.*;

import static cn.huse.sdk.SdkUtils.*;

/**
 * @author: huanxi
 * 异蛇酒单元测试
 * @date: 2019-06-11 21:14
 */
public class SnakeWineSDK {

    final static String orgContract = "\"use strict\";const key={user:\"user_\",admin:\"admin\",orgInfo:\"orgInfo\",authority:\"authority\"};const role={admin:'admin',user:'user'};function init(info){let obj=JSON.parse(info);Chain.store(key.admin,JSON.stringify(obj.adminList));Chain.store(key.orgInfo,JSON.stringify(obj.orgInfo));Chain.store(key.authority,JSON.stringify(obj.authority));return'success';}function checkUser(address,role){let roles=JSON.parse(Chain.load(`${role}List`));let i;if(roles){for(i=0;i<roles.length;i+=1){if(address===roles[i]){return true;}}}return false;}function arrayDel(arr,e){let index=arr.indexOf(e);if(index!==-1){arr.splice(index,1);}}function addUser(info,role,address){let adminList;let i;if(role===role.admin){adminList=JSON.parse(Chain.load(key.admin));for(i=0;i<adminList.length-1;i+=1){if(adminList[i]===address){break;}}if(i===adminList&&address!==adminList[adminList.length-1]){adminList.push(address);}Chain.store(key.admin,JSON.stringify(adminList));}info.address=address;info.role=role;Chain.store(`${key.user}${address}`,JSON.stringify(info));}function delUser(info,role,address){let adminList;if(role===key.admin){adminList=JSON.parse(Chain.load(key.admin));arrayDel(adminList,address);Chain.store('adminList',JSON.stringify(adminList));}Chain.del(`${key.user}${address}`);}function main(input_str){let input=JSON.parse(input_str);if(input.method==='addUser'){return addUser(input.params.info,input.params.role,input.params.address);}else if(input.method==='delUser'){return delUser(input.params.info,input.params.role,input.params.address);}else{throw'no this method';}}function checkPermission(operType,address){let roles=Chain.load(key.admin);let i;if(roles){for(i=0;i<roles.length;i+=1){if(address===roles[i]){return true;}}}return false;}function query(input_str){let input=JSON.parse(input_str);if(input.method==='checkPermission'){return checkPermission(input.params.role,input.params.address);}else if(input.method==='queryUser'){return Chain.store(`${key.user}${input.param.address}`);}else{throw'no this method';}}";
    final static String allianceContract="\"use strict\";const key={supplier:\"supplier\",producer:\"producer\",detection:\"detection\",logistics:\"logistics\",admin:\"admin\"};function init(input){let obj=JSON.parse(input);Chain.store(key.supplier,JSON.stringify(obj.supplier));Chain.store(key.producer,JSON.stringify(obj.producer));Chain.store(key.detection,JSON.stringify(obj.detection));Chain.store(key.logistics,JSON.stringify(obj.logistics));Chain.store(key.admin,JSON.stringify(obj.admin));return'success';}function getUsers(role){let roles_src=Chain.load(role);return JSON.parse(roles_src);}function checkPermission(role,address){let roles=getUsers(role);let i;if(roles){for(i=0;i<roles.length;i+=1){if(address===roles[i]){return true;}}}return false;}function addUser(role,address){if(Utils.addressCheck(address)){throw'address error!';}if(!checkPermission('admin',Chain.tx.initiator)){return\"do not have permission\";}let roles=getUsers(role);let i;if(roles){for(i=0;i<roles.length;i+=1){if(address===roles[i]){throw\"usr has in this role list\";}}}roles.push(address);Chain.store(role,JSON.stringify(roles));}function arrayDel(arr,e){let index=arr.indexOf(e);if(index!==-1){arr.splice(index,1);}}function delUser(address,role){if(Utils.addressCheck(address)){throw'address error!';}if(!checkPermission('admin',Chain.tx.initiator)){throw\"do not have permission\";}let roles=Chain.load(`${role}List`);arrayDel(roles,address);Chain.store(`${role}List`,roles);}function main(input_str){let input=JSON.parse(input_str);if(input.method==='addUser'){return addUser(input.params.role,input.params.address);}else if(input.method==='addUser'){return delUser();}else{throw'no this method';}}function query(input_str){let input=JSON.parse(input_str);if(input.method==='checkPermission'){return checkPermission(input.params.role,input.params.address);}else{throw'no this method';}}";
    final static String productContract="\"use strict\";const key={supplier:\"supplier\",producer:\"producer\",detection:\"detection\",logistics:\"logistics\",admin:\"admin\",initInfo:\"initInfo\",alliance:\"alliance\",orgAddress:\"orgAddress\",operType:\"operType\"};function init(info){let obj=JSON.parse(info);obj.info.operator=Chain.tx.initiator;Chain.store(key.initInfo,JSON.stringify(obj.info));Chain.store(key.alliance,obj.alliance);return'success';}function _errPermission(res,o){if(res.error){throw'check user error!';}if(!res.result){throw`do not have permission${o}`;}}function addData(info,dataType,operType=\"\"){info.operator=Chain.tx.initiator;let orgAddress=Chain.getAccountMetadata(info.operator,key.orgAddress);if(!orgAddress){throw`not has org address!${orgAddress},operator:${info.operator}`;}let res=Chain.contractQuery(orgAddress,JSON.stringify({'method':'checkPermission','params':{'role':operType,'address':orgAddress}}));let alliance=Chain.load('alliance');res=Chain.contractQuery(alliance,JSON.stringify({'method':'checkPermission','params':{'role':dataType,'address':orgAddress}}));_errPermission(res,orgAddress);info.timestamp=Chain.block.timestamp;if(!operType){Chain.store(`${dataType}Info`,JSON.stringify(info));}else{let infoObj=JSON.parse(Chain.load(`${dataType}Info`));infoObj[dataType]=info;Chain.store(`${dataType}Info`,JSON.stringify(infoObj));}}function delData(dataType,operType){if(!operType){Chain.del(`${dataType}Info`);}else{let infoObj=JSON.parse(Chain.load(`${dataType}Info`));delete infoObj[dataType];Chain.store(`${dataType}Info`,JSON.stringify(infoObj));}}function main(input_str){let input=JSON.parse(input_str);if(input.method==='addData'){return addData(input.params.info,input.params.dataType,input.params.operType);}else if(input.method==='delData'){return delData(input.params.info,input.params.dataType,input.params.operType);}else{throw'have no this method';}}function query(input){return input;}";
    private static SnakeWineSDK snakeWineSDK;

    private SnakeWineSDK() {
    }

    public static SnakeWineSDK getInstance() {
        if (snakeWineSDK == null) snakeWineSDK = new SnakeWineSDK();
        return snakeWineSDK;
    }


    /**
     * 转账激活
     */
    public void activateOrg(String address) {
        AccountActivateOperation activateOperation = new AccountActivateOperation();
        activateOperation.setInitBalance(200 * Math.round(Math.pow(10, 8)));
        activateOperation.setSourceAddress(Config.genesisAddress);
        activateOperation.setDestAddress(address);
        submitTx(new AccountActivateOperation[]{activateOperation});
    }

    public static int flag = 0;
    /**
     * 创建组织智能合约
     */
    public String setupOrgInfo(String address, BaseCompanyInfo companyInfo, String[] adminList, String privateKey, BaseAuthority authority) {
         String res= createContract(address,privateKey,orgContract,InputRequestBuildFactory.buildInitOrg(adminList, companyInfo,authority));
        synchronized (SnakeWineSDK.class) {
            flag++;
        }
        return res;
    }



    public void addUserInfo(String adminAddress, String privateKey, String orgAddress, UserInfo info) {
        String input = InputRequestBuildFactory.buildInvokeAddUser(info.detail, info.address, info.role);
        invokeContract(adminAddress, privateKey, orgAddress, input);
    }

    public static String getAllianceContract() {
        return allianceContract;
    }

    public static String getProductContract() {
        return productContract;
    }
}
