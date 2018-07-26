package com.shindo.redis.com.shindo.guavaDemo;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;

/**
 * guava cache是一个本地缓存。有以下优点：

 很好的封装了get、put操作，能够集成数据源。
 一般我们在业务中操作缓存，都会操作缓存和数据源两部分。如：put数据时，先插入DB，再删除原来的缓存；ge数据时，先查缓存，命中则返回，没有命中时，需要查询DB，再把查询结果放入缓存中。 guava cache封装了这么多步骤，只需要调用一次get/put方法即可。
 线程安全的缓存，与ConcurrentMap相似，但前者增加了更多的元素失效策略，后者只能显示的移除元素。
 Guava Cache提供了三种基本的缓存回收方式：基于容量回收、定时回收和基于引用回收。定时回收有两种：按照写入时间，最早写入的最先回收；按照访问时间，最早访问的最早回收。
 监控缓存加载/命中情况。

 常用方法：
 V getIfPresent(Object key) 获取缓存中key对应的value，如果缓存没命中，返回null。return value if cached, otherwise return null.
 V get(K key) throws ExecutionException 获取key对应的value，若缓存中没有，则调用LocalCache的load方法，从数据源中加载，并缓存。 return value if cached, otherwise load, cache and return.
 void put(K key, V value) if cached, return; otherwise create, cache , and return.
 void invalidate(Object key); 删除缓存
 void invalidateAll(); 清楚所有的缓存，相当远map的clear操作。
 long size(); 获取缓存中元素的大概个数。为什么是大概呢？元素失效之时，并不会实时的更新size，所以这里的size可能会包含失效元素。
 CacheStats stats(); 缓存的状态数据，包括(未)命中个数，加载成功/失败个数，总共加载时间，删除个数等。
 ConcurrentMap

 批量操作：
 批量操作就是循环调用上面对应的方法，如：
 ImmutableMap
 void putAll(Map<? extends K,? extends V> m);
 void invalidateAll(Iterable<?> keys);

 主要接口:
 CacheBuilder：类，缓存构建器。构建缓存的入口，指定缓存配置参数并初始化本地缓存。
 CacheBuilder在build方法中，会把前面设置的参数，全部传递给LocalCache，它自己实际不参与任何计算。这种初始化参数的方法值得借鉴，代码简洁易读。
 CacheLoader：抽象类。用于从数据源加载数据，定义load、reload、loadAll等操作。
 Cache：接口，定义get、put、invalidate等操作，这里只有缓存增删改的操作，没有数据加载的操作。
 AbstractCache：抽象类，实现Cache接口。其中批量操作都是循环执行单次行为，而单次行为都没有具体定义。
 LoadingCache：接口，继承自Cache。定义get、getUnchecked、getAll等操作，这些操作都会从数据源load数据。
 AbstractLoadingCache：抽象类，继承自AbstractCache，实现LoadingCache接口。
 LocalCache：类。整个guava cache的核心类，包含了guava cache的数据结构以及基本的缓存的操作方法。
 LocalManualCache：LocalCache内部静态类，实现Cache接口。其内部的增删改缓存操作全部调用成员变量localCache（LocalCache类型）的相应方法。
 LocalLoadingCache：LocalCache内部静态类，继承自LocalManualCache类，实现LoadingCache接口。其所有操作也是调用成员变量localCache（LocalCache类型）的相应方法。
 CacheStats：缓存加载/命中统计信息。
 */
public class guavaCacheSample {

    public static void main(String[] args) {

        LoadingCache<String, Integer> cache = CacheBuilder.newBuilder()
                .maximumSize(10)  //最多存放十个数据
                .expireAfterWrite(10, TimeUnit.SECONDS)  //缓存200秒
                .recordStats()   //开启 记录状态数据功能
                .build(new CacheLoader<String, Integer>() {
                    //数据加载，默认返回-1,也可以是查询操作，如从DB查询
                    @Override
                    public Integer load(String key) throws Exception {
                        return -1;
                    }
                });

        //只查询缓存，没有命中，即返回null。 miss++
        System.out.println(cache.getIfPresent("key1")); //null
        //put数据，放在缓存中
        cache.put("key1", 1);
        //再次查询，已存在缓存中, hit++
        System.out.println(cache.getIfPresent("key1")); //1
        //失效缓存
        cache.invalidate("key1");
        //失效之后，查询，已不在缓存中, miss++
        System.out.println(cache.getIfPresent("key1")); //null

        try{
            //查询缓存，未命中，调用load方法，返回-1. miss++
            System.out.println(cache.get("key2"));   //-1
            //put数据，更新缓存
            cache.put("key2", 2);
            //查询得到最新的数据, hit++
            System.out.println(cache.get("key2"));    //2
            System.out.println("size :" + cache.size());  //1

            //插入十个数据
            for(int i=3; i<13; i++){
                cache.put("key"+i, i);
            }
            //超过最大容量的，删除最早插入的数据，size正确
            System.out.println("size :" + cache.size());  //10
            //miss++
            System.out.println(cache.getIfPresent("key2"));  //null

            Thread.sleep(5000); //等待5秒
            cache.put("key1", 1);
            cache.put("key2", 2);
            //key5还没有失效，返回5。缓存中数据为key1，key2，key5-key12. hit++
            System.out.println(cache.getIfPresent("key5")); //5

            Thread.sleep(5000); //等待5秒
            //此时key5-key12已经失效，但是size没有更新
            System.out.println("size :" + cache.size());  //10
            //key1存在, hit++
            System.out.println(cache.getIfPresent("key1")); //1
            System.out.println("size :" + cache.size());  //10
            //获取key5，发现已经失效，然后刷新缓存，遍历数据，去掉失效的所有数据, miss++
            System.out.println(cache.getIfPresent("key5")); //null
            //此时只有key1，key2没有失效
            System.out.println("size :" + cache.size()); //2

            System.out.println("status, hitCount:" + cache.stats().hitCount()
                    + ", missCount:" + cache.stats().missCount()); //4,5
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
