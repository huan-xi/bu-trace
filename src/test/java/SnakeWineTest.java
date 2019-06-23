import cn.huse.sdk.Config;
import cn.huse.sdk.SnakeWineSDK;
import cn.huse.sdk.contract.InputRequestBuildFactory;
import cn.huse.sdk.db.DbStore;
import cn.huse.sdk.db.LevelDbStoreImpl;
import io.bumo.model.request.AccountGetBalanceRequest;
import io.bumo.model.response.AccountGetBalanceResponse;
import org.DetectionTest;
import org.LogisticsTest;
import org.SupplierTest;
import org.junit.Test;
import org.ProducerTest;


import java.util.HashMap;
import java.util.Map;

import static cn.huse.sdk.SdkUtils.createContract;
import static cn.huse.sdk.SdkUtils.sdk;

/**
 * @author: huanxi
 * @date: 2019-06-12 17:43
 */
public class SnakeWineTest {

    SnakeWineSDK snakeWineSDK = SnakeWineSDK.getInstance();
    DbStore dbStore = LevelDbStoreImpl.getInstance();

    @Test
    public void initInfo() {
        clear();
        SupplierTest.init();
    }



    private static SnakeWineTest SnakeWineTest = new SnakeWineTest();

    @Test
    public void active() {
        snakeWineSDK.activateOrg("buQXkuPtKwmrxFVTA4V1PAb28rMVtRqd6aE9");
    }
    private static void run(Runnable runnable) {
        new Thread(runnable).start();
    }

    private void setUpOrgInfo(){
        run(new Runnable() {
            @Override
            public void run() {
                SupplierTest.init();
            }
        });
        run(new Runnable() {
            @Override
            public void run() {
                ProducerTest.init();
            }
        });
        run(new Runnable() {
            @Override
            public void run() {
                DetectionTest.init();
            }
        });
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                LogisticsTest.init();
            }
        });
        t.start();
        try {
            t.join();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public  void setUpAdminInfo(){
        run(new Runnable() {
            @Override
            public void run() {
                SupplierTest.setUpAdminInfo();
            }
        });
        run(new Runnable() {
            @Override
            public void run() {
                ProducerTest.setUpAdminInfo();
            }
        });
        run(new Runnable() {
            @Override
            public void run() {
                DetectionTest.setUpAdminInfo();
            }
        });
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                LogisticsTest.setUpAdminInfo();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    final static String ALLIANCE_KEY = "alliance";

    /**
     * 创建异蛇酒生产联盟
     */
    @Test
    public void createAlliance() {
        if (dbStore.get(ALLIANCE_KEY) != null) {
            return;
        }
        String input = InputRequestBuildFactory.buildInitAlliance(
                new String[]{dbStore.get(SupplierTest.class.getSimpleName())},
                new String[]{dbStore.get(ProducerTest.class.getSimpleName())},
                new String[]{dbStore.get(DetectionTest.class.getSimpleName())},
                new String[]{dbStore.get(LogisticsTest.class.getSimpleName())},
                new String[]{Config.genesisAddress});
        String res = createContract(Config.genesisAddress, Config.senderPrivateKey, SnakeWineSDK.getAllianceContract(), input);
        dbStore.put(ALLIANCE_KEY, res);
    }

    /**
     * 生产产品
     */
    @Test
    public void createProduct() {
        if (dbStore.get(Config.PRODUCER_KEY) != null) {
//            return;
        }
        Map<String, String> info = new HashMap<String, String>();
        info.put("name", "永州异蛇酒");
        String alliance = dbStore.get(ALLIANCE_KEY);
        String input = InputRequestBuildFactory.buildInitProduct(info, alliance);
        String res = createContract(Config.genesisAddress, Config.senderPrivateKey, SnakeWineSDK.getProductContract(), input);
        dbStore.put(Config.PRODUCER_KEY, res);
        System.out.println("产品地址:"+res);
    }

    @Test
    public  void addData(){
        run(new Runnable() {
            @Override
            public void run() {
                SupplierTest.addData();
            }
        });
        run(new Runnable() {
            @Override
            public void run() {
                ProducerTest.addData();
            }
        });
        run(new Runnable() {
            @Override
            public void run() {
                LogisticsTest.addData();
            }
        });
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                DetectionTest.addData();
            }
        });
        t.start();
        try {
            t.join();
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void clear() {
        dbStore.remove(ProducerTest.class.getSimpleName());
        dbStore.remove(SupplierTest.class.getSimpleName());
        dbStore.remove(DetectionTest.class.getSimpleName());
        dbStore.remove(LogisticsTest.class.getSimpleName());
        dbStore.remove(ALLIANCE_KEY);
        dbStore.remove(Config.PRODUCER_KEY);
    }

    @Test
    public void list() {
        System.out.println("供应商地址：" + dbStore.get(SupplierTest.class.getSimpleName()));
        System.out.println("加工组织地址：" + dbStore.get(ProducerTest.class.getSimpleName()));
        System.out.println("检测机构地址：" + dbStore.get(DetectionTest.class.getSimpleName()));
        System.out.println("物流公司地址：" + dbStore.get(LogisticsTest.class.getSimpleName()));
        System.out.println("联盟地址：" + dbStore.get(ALLIANCE_KEY));
        System.out.println("产品地址：" + dbStore.get(Config.PRODUCER_KEY));
    }

    /**
     * 查询各个组织管理员余额
     */
    @Test
    public void listBalance(){
        double u=  Math.pow(10,-8);
        AccountGetBalanceRequest request=new AccountGetBalanceRequest();
        request.setAddress(Config.genesisAddress);
        AccountGetBalanceResponse res = sdk.getAccountService().getBalance(request);
        long balance=res.getResult().getBalance();
        System.out.println("联盟管理员余额："+balance*u);

        request.setAddress(SupplierTest.getAdminAddress());
        res = sdk.getAccountService().getBalance(request);
        balance=res.getResult().getBalance();
        System.out.println("供应商管理员余额："+balance*u);

        request.setAddress(ProducerTest.getAdminAddress());
        res = sdk.getAccountService().getBalance(request);
        balance=res.getResult().getBalance();
        System.out.println("加工厂管理员余额："+balance*u);

        request.setAddress(DetectionTest.getAdminAddress());
        res = sdk.getAccountService().getBalance(request);
        balance=res.getResult().getBalance();
        System.out.println("检测机构管理员余额："+balance*u);

        request.setAddress(LogisticsTest.getAdminAddress());
        res = sdk.getAccountService().getBalance(request);
        balance=res.getResult().getBalance();
        System.out.println("物流公司管理员余额："+balance*u);

    }

    final static SnakeWineTest test=new SnakeWineTest();

    public static void main(String[] args) {
        System.out.println("清除上次记录...");
        test.clear();           //清除本地数据
        System.out.println("正在初始化组织信息...");
        test.setUpOrgInfo();   //初始化组织信息
        System.out.println("正在初始化组织管理员信息...");
        test.setUpAdminInfo(); //初始化组织管理员信息
        System.out.println("正在创建联盟合约...");
        test.createAlliance();
        System.out.println("正在创建产品合约...");
        test.createProduct();
        System.out.println("正在添加溯源信息...");
        test.addData();
        System.out.println("测试完成。各合约地址：");
        test.list(); //列出合约地址
    }
}
