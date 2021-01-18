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


