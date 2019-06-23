package cn.huse.sdk.db;

import cn.huse.sdk.Config;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.impl.Iq80DBFactory;
import java.io.File;
import java.io.IOException;

/**
 * @author: huanxi
 * @date: 2019/1/4 17:53
 * 自动配置levelDB
 */
public class DBConfig {
    public static DB levelDB() throws IOException {
        org.iq80.leveldb.Options options = new org.iq80.leveldb.Options();
        options.createIfMissing(true);
        return Iq80DBFactory.factory.open(new File(Config.dbFile), options);
    }
}
