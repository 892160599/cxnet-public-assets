package com.cxnet.project.assets.data.cache;

import com.cxnet.project.assets.data.DataMgr;
import com.cxnet.project.assets.data.PreAuthorizeProcessor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @author Chanyin
 * @description ehcache缓存管理 系统启动开启缓存
 * @date 2020-03-20 15:51:23
 */
@Component
public class CacheStart implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        // 字典数据
        new CacheMgr().setCache(CacheKey.CacheDictType.name(), DataMgr.getDictType());
        new CacheMgr().setCache(CacheKey.CacheDictData.name(), DataMgr.getDictData());
        // 权限数据
        new CacheMgr().setCache(CacheKey.CachePreAuthorizeProcessor.name(), PreAuthorizeProcessor.preAuthorizeMap);
    }
}
