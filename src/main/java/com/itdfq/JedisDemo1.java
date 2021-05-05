package com.itdfq;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * @Author GocChin
 * @Date 2021/5/5 16:00
 * @Blog: itdfq.com
 * @QQ: 909256107
 */


public class JedisDemo1 {
    public static void main(String[] args) {

        /**
         * keys *查看当前库所有key    (匹配：keys *1)
         * exists key判断某个key是否存在
         * type key 查看你的key是什么类型
         * del key       删除指定的key数据
         * unlink key   根据value选择非阻塞删除
         * 仅将keys从keyspace元数据中删除，真正的删除会在后续异步操作。
         * expire key 10   10秒钟：为给定的key设置过期时间
         * ttl key 查看还有多少秒过期，-1表示永不过期，-2表示已过期
         *
         * select命令切换数据库
         * dbsize查看当前数据库的key的数量
         * flushdb清空当前库
         * flushall通杀全部库
          */

        //创建Jedis对象
        Jedis jedis = new Jedis("你的IP",6379);
        //设置密码
        jedis.auth("你的密码");

        //测试
        String ping = jedis.ping();
        System.out.println(ping);
    }
    /**
     *
     set   <key><value>添加键值对
     *NX：当数据库中key不存在时，可以将key-value添加数据库
     *XX：当数据库中key存在时，可以将key-value添加数据库，与NX参数互斥
     *EX：key的超时秒数
     *PX：key的超时毫秒数，与EX互斥
     get   <key>查询对应键值
     append  <key><value>将给定的<value> 追加到原值的末尾
     strlen  <key>获得值的长度
     setnx  <key><value>只有在 key 不存在时    设置 key 的值

     incr  <key>
     将 key 中储存的数字值增1
     只能对数字值操作，如果为空，新增值为1
     decr  <key>
     将 key 中储存的数字值减1
     只能对数字值操作，如果为空，新增值为-1
     incrby / decrby  <key><步长>将 key 中储存的数字值增减。自定义步长。
     mset  <key1><value1><key2><value2>  .....
     同时设置一个或多个 key-value对
     mget  <key1><key2><key3> .....
     同时获取一个或多个 value
     msetnx <key1><value1><key2><value2>  .....
     同时设置一个或多个 key-value 对，当且仅当所有给定 key 都不存在。
     getrange  <key><起始位置><结束位置>
     获得值的范围，类似java中的substring，前包，后包
     setrange  <key><起始位置><value>
     用 <value>  覆写<key>所储存的字符串值，从<起始位置>开始(索引从0开始)。

     setex  <key><过期时间><value>
     设置键值的同时，设置过期时间，单位秒。
     getset <key><value>
     以新换旧，设置了新值同时获得旧值。
     */
    //操作字符串
    @Test
    public void demo1(){
        //创建Jedis对象
        Jedis jedis = new Jedis("你的IP",6379);
        //设置密码
        jedis.auth("你的密码");

        //添加
        jedis.set("name","韩信");
        //获取
        String name = jedis.get("name");
        //查看到期时间  -1永久生效  -2无效
        System.out.println(jedis.ttl("name"));
        System.out.println("获取到的name为"+name);

        //设置多个key-value
        String mset = jedis.mset("k1", "v1", "k2", "v2");
        List<String> mget = jedis.mget("k1", "k2");
        System.out.println(mget);

        Set<String> keys = jedis.keys("*");
        for(String key : keys){
            System.out.println(key);
        }
    }
    //操作list

    /**
     * lpush/rpush  <key><value1><value2><value3> .... 从左边/右边插入一个或多个值。
     * lpop/rpop  <key>从左边/右边吐出一个值。值在键在，值光键亡。
     *
     * rpoplpush  <key1><key2>从<key1>列表右边吐出一个值，插到<key2>列表左边。
     *
     * lrange <key><start><stop>
     * 按照索引下标获得元素(从左到右)
     * lrange mylist 0 -1   0左边第一个，-1右边第一个，（0-1表示获取所有）
     * lindex <key><index>按照索引下标获得元素(从左到右)
     * llen <key>获得列表长度
     *
     * linsert <key>  before <value><newvalue>在<value>的后面插入<newvalue>插入值
     * lrem <key><n><value>从左边删除n个value(从左到右)
     * lset<key><index><value>将列表key下标为index的值替换成value
     */
    @Test
    public void demo2(){
        //创建Jedis对象
        Jedis jedis = new Jedis("你的IP",6379);
        //设置密码
        jedis.auth("你的密码");

        jedis.lpush("klist","韩信","李白","百里玄策");
        List<String> k1 = jedis.lrange("klist", 0, -1);
        System.out.println(k1);
    }
//    操作set对象

    /**
     * sadd <key><value1><value2> .....
     * 将一个或多个 member 元素加入到集合 key 中，已经存在的 member 元素将被忽略
     * smembers <key>取出该集合的所有值。
     * sismember <key><value>判断集合<key>是否为含有该<value>值，有1，没有0
     * scard<key>返回该集合的元素个数。
     * srem <key><value1><value2> .... 删除集合中的某个元素。
     * spop <key>随机从该集合中吐出一个值。
     * srandmember <key><n>随机从该集合中取出n个值。不会从集合中删除 。
     * smove <source><destination>value把集合中一个值从一个集合移动到另一个集合
     * sinter <key1><key2>返回两个集合的交集元素。
     * sunion <key1><key2>返回两个集合的并集元素。
     * sdiff <key1><key2>返回两个集合的差集元素(key1中的，不包含key2中的)
     */
    @Test
    public void demo3(){
        //创建Jedis对象
        Jedis jedis = new Jedis("你的IP",6379);
        //设置密码
        jedis.auth("你的密码");
        jedis.sadd("name1","鲁班","孙悟空");
        Set<String> name1 = jedis.smembers("name1");
        System.out.println(name1);
    }
    //操作Hash

    /**
     * hset <key><field><value>给<key>集合中的  <field>键赋值<value>
     * hget <key1><field>从<key1>集合<field>取出 value
     * hmset <key1><field1><value1><field2><value2>... 批量设置hash的值
     * hexists<key1><field>查看哈希表 key 中，给定域 field 是否存在。
     * hkeys <key>列出该hash集合的所有field
     * hvals <key>列出该hash集合的所有value
     * hincrby <key><field><increment>为哈希表 key 中的域 field 的值加上增量 1   -1
     * hsetnx <key><field><value>将哈希表 key 中的域 field 的值设置为 value ，当且仅当域 field 不存在 .
     */
    @Test
    public void demo4(){
        //创建Jedis对象
        Jedis jedis = new Jedis("你的IP",6379);
        //设置密码
        jedis.auth("你的密码");
        jedis.hset("name2","user","2014");
        jedis.hset("name2","name","成语奥金");
        String hget = jedis.hget("name2", "name");
        System.out.println(hget);
    }

    /**
     * zadd  <key><score1><value1><score2><value2>…
     * 将一个或多个 member 元素及其 score 值加入到有序集 key 当中。
     * zrange <key><start><stop>  [WITHSCORES]
     * 返回有序集 key 中，下标在<start><stop>之间的元素
     * 带WITHSCORES，可以让分数一起和值返回到结果集。
     * zrangebyscore key minmax [withscores] [limit offset count]
     * 返回有序集 key 中，所有 score 值介于 min 和 max 之间(包括等于 min 或 max )的成员。有序集成员按 score 值递增(从小到大)次序排列。
     * zrevrangebyscore key maxmin [withscores] [limit offset count]
     * 同上，改为从大到小排列。
     * zincrby <key><increment><value>      为元素的score加上增量
     * zrem  <key><value>删除该集合下，指定值的元素
     * zcount <key><min><max>统计该集合，分数区间内的元素个数
     * zrank <key><value>返回该值在集合中的排名，从0开始。
     */
    //操作有序集合
    @Test
    public void demo5(){
        //创建Jedis对象
        Jedis jedis = new Jedis("你的IP",6379);
        //设置密码
        jedis.auth("你的密码");
        jedis.zadd("zset",100d,"heina");
        Set<String> zset = jedis.zrange("zset", 0, -1);
        System.out.println(zset);
    }
}
