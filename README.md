# kill
基于微服务SpringBoot的商城高并发抢单系统  实战

使用IDEA 插件：Mybatis Generator Plus 对数据库做逆向工程，可参考：https://www.cnblogs.com/personsiglewine/p/12848595.html

### 死信队列处理失效订单的缺陷：

有许多订单在某个TTL时间点集中失效,但是恰好RabbitMQ服务挂了。

#### 解决方案：
1. rabbitMQ高可用,搭建rabbitMQ集群。
2. 对rabbitMQ的集群或者rabbitMQ服务要做监控，一旦发现超过XX分钟未提供服务,可初步判断rabbitMQ服务挂掉了,告警及处理策略。

#### 补充解决方案：

基于@Scheduled注解的定时任务实现-批量获取status=0的订单并判断时间超过了TTL
但是@Scheduled只适用与单体架构，在分布式应用场景，应该采用分布式任务调度平台，如：xxl-job, elastic-job-lite
 

#### 超卖问题：
没有控制好多个线程对于共享的数据、共享的代码进行控制

原因：真正用于判重的代码逻辑晚于它的判断

核心方案：分布式锁解决共享资源在高并发访问时出现的"并发安全"问题

协助方案：对于瞬时流量、并发请求进行限流（目前是接口的限流，有条件时还能进行网关层面的限流 nginx）

辅助方案一：应用（秒杀系统）、中间件（RabbitMQ、Redis....）服务做集群部署，提高高可用与稳定性

辅助方案二：数据库Mysql做主备部署，如可以搭建一个Master写库，多个Slave读库实例的服务！！
       
#### 核心SQL优化逻辑：
"查询以及更减库存"时需要判断当前"可被更减的数量"是否仍然还大于0

参考单例模式双重检查机制增强数据同步性

> 并发问题的本质就是共享数据在多线程间数据变更的同步问题

#### redis原子操作优化：
核心方法：SETNX + EXPIRE  联合使用
原理：Redis本身就是一个基于内存的、单线程的key-value存储数据库

#### redission 分布式锁
> 参考：https://www.cnblogs.com/cjsblog/p/11273205.html