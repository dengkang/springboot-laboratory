####################################
springboot 集成 activeMQ的配置
spring boot 自动配置（非java config配置）
org.springframework.jms.connection.CachingConnectionFactory@5fd62371\sun.misc.Launcher$AppClassLoader@18b4aac2
处理消息hello
org.springframework.jms.connection.CachingConnectionFactory@5fd62371


java config配置
org.apache.activemq.ActiveMQConnectionFactory@66b7550d\sun.misc.Launcher$AppClassLoader@18b4aac2
2019-04-18 17:33:08.509  INFO 2268 --- [ActiveMQ Task-1] o.a.a.t.failover.FailoverTransport       : Successfully connected to tcp://127.0.0.1:61616
2019-04-18 17:33:08.542  INFO 2268 --- [           main] c.n.activemq.ActivemqApplicationTests    : Started ActivemqApplicationTests in 1.363 seconds (JVM running for 2.077)
org.apache.activemq.ActiveMQConnectionFactory@66b7550d\sun.misc.Launcher$AppClassLoader@18b4aac2
2019-04-18 17:33:08.682  INFO 2268 --- [ActiveMQ Task-1] o.a.a.t.failover.FailoverTransport       : Successfully connected to tcp:// 127.0.0.1:61616
处理消息hello
org.apache.activemq.ActiveMQConnectionFactory@66b7550d

两种配置方式二选一
如果没有特殊要求可以使用自动配置
如果对配置有特殊设置使用java config ，在Application中取消相应的自动配置。
#######################
消息持久化机制
AMQ，KahaDB（默认方式），JDBC，LevelDB，将数据保存下来确保重启消息不丢失

消息确认机制
AUTO_ACKNOWLEDGE = 1    自动确认
CLIENT_ACKNOWLEDGE = 2    客户端手动确认
DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
INDIVIDUAL_ACKNOWLEDGE = 4 单条确认 (仅activeMQ支持)

实验queue中不对消息确认 但是最终消息还是出队了，原来是 消息按照设置的重发规则，发完规定的重发次数后进入死信队列ActiveMQ.DLQ。
那么这样一个场景，队列中每条消息都很重要，必须执行，如果再执行过程中出现异常，如何保证消息不会进入死信队列(如果希望对消息执行可以监控死信队列)

消息重发机制

事务：本地事务(单broker)，与外部事物（这里让接收消息和数据库访问处于同一事务中）



rollback和recover，作用


#######################
应用场景
系统间解耦
