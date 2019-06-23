package cn.huse.sdk.db;

import org.iq80.leveldb.DB;

import java.io.IOException;

import static org.iq80.leveldb.impl.Iq80DBFactory.asString;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

/**
 * levelDB 存储键值对
 */
public class LevelDbStoreImpl implements DbStore {
    private DB db;

    private static DbStore dbStore;

    public static DbStore getInstance() {
        if (null == dbStore) dbStore = new LevelDbStoreImpl();
        return dbStore;
    }

    private LevelDbStoreImpl() {
        try {
            db = DBConfig.levelDB();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void put(String key, String value) {
        db.put(bytes(key), bytes(value));
    }

    @Override
    public String get(String key) {
        return asString(db.get(bytes(key)));
    }

    @Override
    public void remove(String key) {
        db.delete(bytes(key));
    }
}
