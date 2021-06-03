package com.cxnet.project.assets.data.cache;

import com.cxnet.common.utils.StringUtils;
import com.cxnet.common.utils.spring.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.SearchAttribute;
import net.sf.ehcache.search.Direction;
import net.sf.ehcache.search.Query;
import net.sf.ehcache.search.Result;
import net.sf.ehcache.search.Results;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Chanyin尹强
 * @description ehcache缓存管理
 * @date 2019/12/4 19:04
 */
@Slf4j
public class CacheMgr {

    private CacheManager cacheManager = SpringUtils.getBean(CacheManager.class);

    private ReadWriteLock rwLock = new ReentrantReadWriteLock();

    /**
     * 添加缓存
     *
     * @param key  缓存主键
     * @param cMap 缓存至 Map集合
     */
    public void setCache(String key, Map cMap) {
        try {
            rwLock.writeLock().lock();
            Cache cache = cacheManager.getCache(key);
            cache.removeAll();
            cMap.forEach((k, v) -> {
                cache.put(new Element(k, v));
            });
        } catch (Exception e) {
            log.error("Catch Exception setCache error:" + e.toString());
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 获取缓存
     *
     * @param key 缓存主键
     */
    public List<Result> getCache(String key) {
        try {
            rwLock.readLock().lock();
            Cache cache = cacheManager.getCache(key);
            Query query = cache.createQuery().includeValues().end();
            return query.execute().all();
        } catch (Exception e) {
            log.error("Catch Exception getCache error:" + e.toString());
            return null;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 获取缓存 第二key
     *
     * @param key       第一主键
     * @param secondKey 第二主键
     */
    public Object getCache(String key, Object secondKey) {
        try {
            rwLock.readLock().lock();
            Cache cache = cacheManager.getCache(key);
            Element element = cache.get(secondKey);
            if (element != null) {
                return element.getObjectValue();
            }
            return null;
        } catch (Exception e) {
            log.error("Catch Exception getCache error:" + e.toString());
            return null;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 获取缓存 by ID集合
     *
     * @param key 缓存主键
     * @param ids ID集合
     */
    public List<Object> getCache(String key, String[] ids) {
        try {
            rwLock.readLock().lock();
            List<Object> res = new ArrayList<>();
            Cache cache = cacheManager.getCache(key);
            if (!StringUtils.isEmpty(ids)) {
                for (String id : ids) {
                    Element element = cache.get(id);
                    if (element != null) {
                        res.add(element.getObjectValue());
                    }
                }
            }
            return res;
        } catch (Exception e) {
            log.error("Catch Exception getCache error:" + e.toString());
            return null;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 删除缓存
     *
     * @param key      缓存主键key
     * @param cacheKey 需要删除的key
     */
    public boolean removeCache(String key, String cacheKey) {
        try {
            rwLock.writeLock().lock();
            Cache cache = cacheManager.getCache(key);
            return cache.remove(cacheKey);
        } catch (Exception e) {
            log.error("Catch Exception removeCache error:" + e.toString());
            return false;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    /**
     * 获取缓存 分页
     *
     * @param key       缓存主键
     * @param pageStart 开始位置
     * @param pageSize  每页数量
     */
    public Map getCache(String key, int pageStart, int pageSize) {
        try {
            rwLock.readLock().lock();
            Cache cache = cacheManager.getCache(key);
            Query query = cache.createQuery().includeValues().end();
            Results results = query.execute();
            return new HashMap() {{
                put("total", results.size());
                put("results", results.range(pageStart, pageSize));
            }};
        } catch (Exception e) {
            log.error("Catch Exception getCache error:" + e.toString());
            return null;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 获取缓存 查询条件 分页
     *
     * @param key        缓存主键
     * @param queryMap   查询条件
     * @param orderByMap 排序条件
     * @param pageStart  开始位置
     * @param pageSize   每页数量
     */
    public Map getCache(String key, Map queryMap, Map orderByMap, int pageStart, int pageSize) {
        try {
            rwLock.readLock().lock();
            Cache cache = cacheManager.getCache(key);
            Query query = cache.createQuery();
            query.includeValues();
            Map<String, SearchAttribute> attributes = cache.getCacheConfiguration().getSearchAttributes();
            if (!StringUtils.isEmpty(queryMap)) {
                queryMap.forEach((k, v) -> {
                    if (attributes.containsKey(k)) {
                        if ("id".equals(k) || k.toString().endsWith("Id")) {
                            query.addCriteria(cache.getSearchAttribute((String) k).eq(v));
                        } else {
                            query.addCriteria(cache.getSearchAttribute((String) k).ilike("*" + v + "*"));
                        }
                    }
                });
            }
            if (attributes.containsKey("createTime")) {
                query.addOrderBy(cache.getSearchAttribute("createTime"), Direction.DESCENDING);
            }
            if (!StringUtils.isEmpty(orderByMap)) {
                orderByMap.forEach((k, v) -> {
                    if (attributes.containsKey(k)) {
                        query.addOrderBy(cache.getSearchAttribute((String) k), "desc".equals(v) ? Direction.DESCENDING : Direction.ASCENDING);
                    }
                });
            }
            query.end();
            Results results = query.execute();
            return new HashMap() {{
                put("total", results.size());
                put("results", results.range(pageStart, pageSize));
            }};
        } catch (Exception e) {
            log.error("Catch Exception getCache error:" + e.toString());
            return null;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    /**
     * 获取缓存 查询条件
     *
     * @param key        缓存主键
     * @param queryMap   查询条件
     * @param orderByMap 排序条件
     */
    public List getCache(String key, Map queryMap, Map orderByMap) {
        try {
            rwLock.readLock().lock();
            Cache cache = cacheManager.getCache(key);
            Query query = cache.createQuery();
            query.includeValues();
            Map<String, SearchAttribute> attributes = cache.getCacheConfiguration().getSearchAttributes();
            if (!StringUtils.isEmpty(queryMap)) {
                queryMap.forEach((k, v) -> {
                    if (attributes.containsKey(k)) {
                        query.addCriteria(cache.getSearchAttribute((String) k).eq(v));
                    }
                });
            }
            if (!StringUtils.isEmpty(orderByMap)) {
                orderByMap.forEach((k, v) -> {
                    if (attributes.containsKey(k)) {
                        query.addOrderBy(cache.getSearchAttribute((String) k), "desc".equals(v) ? Direction.DESCENDING : Direction.ASCENDING);
                    }
                });
            }
            query.end();
            List<Result> all = query.execute().all();
            return new ArrayList<Object>() {{
                for (Result result : all) {
                    add(result.getValue());
                }
            }};
        } catch (Exception e) {
            log.error("Catch Exception getCache error:" + e.toString());
            return null;
        } finally {
            rwLock.readLock().unlock();
        }
    }

}
