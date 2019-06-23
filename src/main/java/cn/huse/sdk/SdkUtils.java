package cn.huse.sdk;

import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xalan.internal.xsltc.dom.AdaptiveResultTreeImpl;
import io.bumo.SDK;
import io.bumo.common.ToBaseUnit;
import io.bumo.model.request.*;
import io.bumo.model.request.operation.*;
import io.bumo.model.response.*;
import io.bumo.model.response.result.data.Signature;
import io.bumo.model.response.result.data.TransactionHistory;
import static cn.huse.sdk.Config.*;
/**
 * @author: huanxi
 * @date: 2019-06-01 22:35
 */
public class SdkUtils {

    public final static SDK sdk = SDK.getInstance(url);


    public static String submitTx(BaseOperation[] operations) {
        return submitTx(operations, genesisAddress, senderPrivateKey);
    }

    public static void setMetaData(String key,String value,String address,String privateKey){
        AccountSetMetadataOperation operation=new AccountSetMetadataOperation();
        operation.setKey(key);
        operation.setValue(value);
        operation.setSourceAddress(address);
        BaseOperation[] operations = {operation};
        submitTx(operations, address, privateKey);
    }
    /**
     * 创建智能合约
     * @param source
     * @param privateKey
     * @param contract
     * @param input
     * @return
     */
    public static String createContract(String source,String privateKey,String contract,String input){
        Long initBalance = ToBaseUnit.BU2MO("2");
        ContractCreateOperation contractCreateOperation = new ContractCreateOperation();
        contractCreateOperation.setSourceAddress(source);
        contractCreateOperation.setInitBalance(initBalance);
        contractCreateOperation.setPayload(contract);
        contractCreateOperation.setInitInput(input);
        BaseOperation[] operations = {contractCreateOperation};
        String hash = submitTx(operations, source, privateKey);
        if (null != hash) return getAddress(hash);
        return null;
    }

    /**
     * 调用智能合约
     *
     * @param address
     * @param privateKey
     * @param contractAddress
     * @param input
     */
    public static void invokeContract(String address, String privateKey, String contractAddress, String input) {
        ContractInvokeByBUOperation invokeByBUOperation = new ContractInvokeByBUOperation();
        invokeByBUOperation.setBuAmount(0L);
        invokeByBUOperation.setSourceAddress(address);
        invokeByBUOperation.setContractAddress(contractAddress);
        invokeByBUOperation.setInput(input);
        submitTx(new ContractInvokeByBUOperation[]{invokeByBUOperation}, address, privateKey);
    }
    public static String submitTx(BaseOperation[] operations, String accountAddress, String privateKey) {

        if (Config.isTest()) {
            BaseOperation operation = operations[0];
            if (operation instanceof ContractInvokeByBUOperation) {
                System.out.println(((ContractInvokeByBUOperation) operation).getInput());
            }if (operation instanceof ContractCreateOperation){
                System.out.println(((ContractCreateOperation) operation).getInitInput());
            }
            return null;
        }

        long nonce = getAccountNonce(accountAddress);
        //序列化交易
        String transactionBlob = serializeTransaction(nonce, operations, accountAddress);
        //签名交易
        Signature[] signatures = signTransaction(transactionBlob, privateKey);
        //提交交易
        String hash = submitTransaction(transactionBlob, signatures);
        if (null != hash)
            System.out.println("交易提交成功，hash:" + hash);

        //查询交易结果
        checkTransactionStatus(hash, 0);
        return hash;
    }

    /**
     * 获取账号Nonce
     *
     * @param accountAddress 账号地址
     * @return Nonce 值
     */
    public static long getAccountNonce(String accountAddress) {
        long nonce = 0;
        // Init request
        AccountGetNonceRequest request = new AccountGetNonceRequest();
        request.setAddress(accountAddress);
        // Call getNonce
        AccountGetNonceResponse response = sdk.getAccountService().getNonce(request);
        if (0 == response.getErrorCode()) {
            nonce = response.getResult().getNonce();
        } else {
            System.out.println("error: " + response.getErrorDesc());
        }
        return nonce;
    }


    /**
     * 序列化交易
     *
     * @param nonce
     * @param operations
     * @return
     */
    public static String serializeTransaction(Long nonce, BaseOperation[] operations, String accountAddress) {
        String transactionBlob = null;
        nonce += 1;
        // Build transaction  Blob
        TransactionBuildBlobRequest transactionBuildBlobRequest = new TransactionBuildBlobRequest();
        transactionBuildBlobRequest.setSourceAddress(accountAddress);
        transactionBuildBlobRequest.setNonce(nonce);
        transactionBuildBlobRequest.setFeeLimit(feeLimit);
        transactionBuildBlobRequest.setGasPrice(gasPrice);
        for (int i = 0; i < operations.length; i++) {
            transactionBuildBlobRequest.addOperation(operations[i]);
        }
        TransactionBuildBlobResponse transactionBuildBlobResponse = sdk.getTransactionService().buildBlob(transactionBuildBlobRequest);
        if (transactionBuildBlobResponse.getErrorCode() == 0) {
            transactionBlob = transactionBuildBlobResponse.getResult().getTransactionBlob();
        } else {
            System.out.println("error: " + transactionBuildBlobResponse.getErrorDesc());
        }
        return transactionBlob;
    }


    public static void evaluateFee(Long nonce, BaseOperation[] operations) {
        TransactionEvaluateFeeRequest request = new TransactionEvaluateFeeRequest();
        nonce += 1;
        request.setNonce(nonce);
        request.setSourceAddress(genesisAddress);
        long number = sdk.getBlockService().getNumber().getResult().getHeader().getBlockNumber();
        request.setSignatureNumber((int) number);
        for (int i = 0; i < operations.length; i++) {
            request.addOperation(operations[i]);
        }
        TransactionEvaluateFeeResponse result = sdk.getTransactionService().evaluateFee(request);
        if (0 == result.getErrorCode()) {
            System.out.println(result.getResult().getTxs()[0].getTransactionEnv().getTransactionFees().getFeeLimit());
        } else {
            System.out.println("error: " + result.getErrorDesc());
        }

    }

    /**
     * 签名交易
     *
     * @param transactionBlob  交易Blob
     * @param senderPrivateKey 签名私钥
     * @return
     */
    public static Signature[] signTransaction(String transactionBlob, String senderPrivateKey) {
        Signature[] signatures = null;
        TransactionSignRequest transactionSignRequest = new TransactionSignRequest();
        transactionSignRequest.setBlob(transactionBlob);
        transactionSignRequest.addPrivateKey(senderPrivateKey);
        TransactionSignResponse transactionSignResponse = sdk.getTransactionService().sign(transactionSignRequest);
        if (transactionSignResponse.getErrorCode() == 0) {
            signatures = transactionSignResponse.getResult().getSignatures();
        } else {
            System.out.println("error: " + transactionSignResponse.getErrorDesc());
        }
        return signatures;
    }


    /**
     * 提交交易
     *
     * @param transactionBlob 交易Blob
     * @param signatures      数值签名
     * @return 交易hash
     */
    public static String submitTransaction(String transactionBlob, Signature[] signatures) {
        String hash = null;
        // Submit transaction
        TransactionSubmitRequest transactionSubmitRequest = new TransactionSubmitRequest();
        transactionSubmitRequest.setTransactionBlob(transactionBlob);
        transactionSubmitRequest.setSignatures(signatures);
        TransactionSubmitResponse transactionSubmitResponse = sdk.getTransactionService().submit(transactionSubmitRequest);
        if (0 == transactionSubmitResponse.getErrorCode()) {
            hash = transactionSubmitResponse.getResult().getHash();
        } else {
            System.out.println("错误信息: " + transactionSubmitResponse.getErrorDesc());
        }
        return hash;
    }

    /**
     * 验证交易
     *
     * @param txHash 交易hash
     * @return 交易状态码
     */
    public static int checkTransactionStatus(String txHash, int time) {
        time++;
        if (time == 20) return 0;
        //延时查询
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Init request
        TransactionGetInfoRequest request = new TransactionGetInfoRequest();
        request.setHash(txHash);
        // Call getInfo
        TransactionGetInfoResponse response = sdk.getTransactionService().getInfo(request);
        int errorCode = response.getErrorCode();
        String errorMsg = null;
        if (errorCode == 0) {
            TransactionHistory transactionHistory = response.getResult().getTransactions()[0];
            errorMsg = transactionHistory.getErrorDesc();

            errorCode = transactionHistory.getErrorCode();
        }
        if (errorCode == 4){
//            System.out.println("查询交易结果...");
        }
        else if (errorCode == 0)
            System.out.println("交易执行成功");
        else if (errorCode == 151) {
            String exception = (String) JSON.parseObject(errorMsg).get("exception");
            System.out.println("合约执行失败：" + exception);
        } else {
            System.out.println("交易执行出错，错误代码: " + errorCode);
            if (null != errorMsg && !errorMsg.isEmpty())
                System.out.println("错误信息:" + errorMsg);
        }
        ;
        if (errorCode == 4) checkTransactionStatus(txHash, time);
        return errorCode;
    }

    /**
     * 从创世账户转账到制定账户
     */
    public static void bUSend(String descAddress, int bu) {
        Long amount = Math.round(bu * Math.pow(10, 8));
        BUSendOperation operation = new BUSendOperation();
        operation.setAmount(amount);
        operation.setDestAddress(descAddress);
        operation.setSourceAddress(genesisAddress);
        submitTx(new BaseOperation[]{operation});
    }

    /**
     * 查询合约地址
     */
    public static String getAddress(String hash) {
        ContractGetAddressRequest request = new ContractGetAddressRequest();
        request.setHash(hash);
        ContractGetAddressResponse response = sdk.getContractService().getAddress(request);
        if (response.getErrorCode() == 0)
            return response.getResult().getContractAddressInfos().get(0).getContractAddress();
        return null;
    }

}
