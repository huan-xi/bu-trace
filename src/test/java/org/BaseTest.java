package org;

import cn.huse.sdk.Config;
import cn.huse.sdk.SnakeWineSDK;
import cn.huse.sdk.db.DbStore;
import cn.huse.sdk.db.LevelDbStoreImpl;

/**
 * @author: huanxi
 * @date: 2019-06-16 17:45
 */
public abstract class BaseTest {
    protected static String address;
    protected static SnakeWineSDK snakeWineSDK = SnakeWineSDK.getInstance();
    protected static DbStore dbStore= LevelDbStoreImpl.getInstance();

    public static String getProduct(){
        return LevelDbStoreImpl.getInstance().get(Config.PRODUCER_KEY);
    }
}
