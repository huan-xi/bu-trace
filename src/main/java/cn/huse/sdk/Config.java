package cn.huse.sdk;

import io.bumo.common.ToBaseUnit;

/**
 * @author: huanxi
 * @date: 2019-06-13 22:06
 */
public class Config {
//    public final static String url = "http://47.107.102.147:16002";
    public final static String url = "http://seed1.bumotest.io:26002";
    //    public final static String url = "http://111.230.251.119:16002";
    public static String senderPrivateKey = "privbw4VkwKTU28DcbXPdySwE7JWbw5simJPy5dTA8ibb9KA2Q8kDw8s";
    public static String genesisAddress = "buQm1Ac8vkJ8skHz3BphveQVrosYpdBkz6mt";
    private static final boolean test=false;
    // The gasPrice is fixed at 1000L, the unit is MO
    public static Long gasPrice = 1000L;
    //设置最大花费
    public static Long feeLimit = ToBaseUnit.BU2MO("10.1");
    public static String dbFile="./data";
    public static final String PRODUCER_KEY = "product";
    public static final String ORG_ADDRESS_KEY = "orgAddress";

    public static boolean isTest() {
        return test;
    }
}
