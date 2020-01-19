package com.xuaiyuan.smp.common.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.springframework.util.ClassUtils;


/**
 * @Description 缓存工具类
 * @Author lj
 * @Date 2019/12/19 17:22
 */
@Slf4j
public class EhcacheUtil {

    private static final String PATH = ClassUtils.getDefaultClassLoader().getResource("").getPath()+"cache/ehcache.xml";
    private CacheManager manager;
    private static EhcacheUtil ehCache;

    /**
     * 获得缓存配置管理
     * @param path
     */
    private EhcacheUtil(String path) {
        try {
            manager = CacheManager.create(path);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取配置文件错误：{}",e.getMessage());
        }
    }

    /**
     * 初始化缓存管理类
     * @return
     */
    public static EhcacheUtil getInstance() {
        try {
            if (ehCache== null) {
                ehCache= new EhcacheUtil(PATH);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("初始化错误：{}",e.getMessage());
        }
        return ehCache;
    }

    /**
     * 获取Cache类
     * @param cacheName
     * @return
     */
    public Cache get(String cacheName) {
        return manager.getCache(cacheName);
    }

    /**
     * 添加缓存数据
     * @param cacheName
     * @param key
     * @param value
     */
    public void put(String cacheName, String key, Object value) {
        try {
            Cache cache = manager.getCache(cacheName);
            Element element = new Element(key, value);
            cache.put(element);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加缓存失败：{}",e.getMessage());
        }
    }

    /**
     * 获取缓存数据
     * @param cacheName
     * @param key
     * @return
     */
    public Object get(String cacheName, String key) {
        try {
            Cache cache = manager.getCache(cacheName);
            Element element = cache.get(key);
            return element == null ? null : element.getObjectValue();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取缓存数据失败：{}",e.getMessage());
            return null;
        }
    }
    /**
     * 删除缓存数据
     * @param cacheName
     * @param key
     */
    public void delete(String cacheName, String key) {
        try {
            Cache cache = manager.getCache(cacheName);
            cache.remove(key);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("删除缓存数据失败：{}",e.getMessage());
        }
    }
}
