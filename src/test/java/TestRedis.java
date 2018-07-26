import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.Jedis;

public class TestRedis {

    private static  Jedis jedis;

    static {
        //连接服务器
        jedis = new Jedis("127.0.0.1",6379);
        //权限认证
//      jedis.auth("");

    }

    //字符串操作
    @Test
    public void testString() {
        jedis.set("name", "沪旦铭");
        System.out.println( jedis.get("name") );
        //追加操作
        jedis.append("name", "是我的名字");
        System.out.println( jedis.get("name") );
        //删除键
        Long result = jedis.del("name2");
        System.out.println( result ); //返回Long型  1表示成功0失败
        //设置多个键值对
        jedis.mset("age","20","addr","湖南");
        //获取多个键  返回一个列表类型  [沪旦铭是我的名字, 20, 湖南]
        System.out.println( jedis.mget("name","age","addr"));

    }

    //哈希操作Hash
    @Test
    public void testHash() {
        Map<String,String> map = new HashMap<String,String>();
        map.put("name", "沪旦铭");
        map.put("age", "20");
        //存储数据
        jedis.hmset("user", map);
        //读取数据  返回一个列表类型 [沪旦铭, 20]
        System.out.println( jedis.hmget("user", "name","age") );
        //HKEYS key 获取所有哈希表中的字段    返回一个列表[name, age]
        System.out.println( jedis.hkeys("user") );
        //HVALS key 获取哈希表中所有值
        System.out.println( jedis.hvals("user") );
        //HLEN key 获取哈希表中字段的数量
        System.out.println( jedis.hlen("user") );
        //获取所有的键  迭代操作
        Iterator<String> iter = jedis.hkeys("user").iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            System.out.println( key+"--"+jedis.hmget("user", key) );
        }

    }

    //List操作
    @Test
    public void testList() {
        //开始前，先移除所有的内容  
        jedis.del("Programming language");

        //LPUSH key value1 [value2] 将一个或多个值插入到列表头部
        jedis.lpush("Programming language", "Java");
        jedis.lpush("Programming language", "Python");
        jedis.lpush("Programming language", "C++");
        //获取数据  返回一个list [Python, Java]
        //第一个是key，第二个是起始位置，第三个是结束位置  
        //其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。 
        //你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。
        System.out.println( jedis.lrange("Programming language",  0, -1) );
        //LPUSHX key value 将一个值插入到已存在的列表头部
        jedis.lpushx("Programming language", "php");
        System.out.println( jedis.lrange("Programming language",  0, -1) );
        //RPUSH key value1 [value2] 在列表中添加一个或多个值
        jedis.rpush("Programming language", "C");
        System.out.println( jedis.lrange("Programming language",  0, -1) );
        //输出情况
//      [C++, Python, Java]
//      [php, C++, Python, Java]
//      [php, C++, Python, Java, C]
    }


    //Set操作
    @Test
    public void testSet() {
        //向集合添加一个或多个成员
        jedis.sadd("webSite", "阿里巴巴","网易");
        jedis.sadd("webSite", "腾讯");
        //SCARD key 获取集合的成员数
        System.out.println( jedis.scard("webSite") );
        //SMEMBERS key 返回集合中的所有成员  返回类型列表[阿里巴巴, 腾讯, 网易]注意顺序不唯一
        System.out.println( jedis.smembers("webSite") );
        //SSCAN key cursor [MATCH pattern] [COUNT count] 迭代集合中的元素
        System.out.println(jedis.sscan("webSite", "0") );


    }

    //sorted set有序Set
    @Test
    public void testSortSet() {
//      Redis 有序集合和集合一样也是string类型元素的集合,且不允许重复的成员。
//      不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。
//      有序集合的成员是唯一的,但分数(score)却可以重复。
        jedis.zadd("city", 0, "北京");
        jedis.zadd("city", 1, "上海");
        jedis.zadd("city", 2, "杭州");

        //ZCARD key 获取有序集合的成员数
        System.out.println( jedis.zcard("city") );
        //ZREVRANK key member 返回有序集合中指定成员的排名，有序集成员按分数值递减(从大到小)排序  java中的方法是zrevrangeByScore
        System.out.println( jedis.zrevrangeByScore("city", 5, 0) );


    }
}