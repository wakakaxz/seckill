# Java 高并发秒杀系统设计，实现与优化
## 0. 使用技术
- Java 1.8
- SpringBoot 2.7.4
- RabbitMQ
- Redis 6.2.7
- Mybatis-plus 3.5.2
- MySQL 5.7
## 1. 流程
- 功能开发
- 系统压测
- 分布式会话
- 优化
    - 页面优化
    - 服务优化
    - 安全优化

## 2. 高并发带来的一些问题
可能会遇到黄牛，大量并发写和并发读。

因此设计系统要保证**高可用**，**保证数据一致性**，**高性能**。

## 3. 参考配置
[Druid 数据源配置属性说明](https://github.com/alibaba/druid/wiki/DruidDataSource%E9%85%8D%E7%BD%AE%E5%B1%9E%E6%80%A7%E5%88%97%E8%A1%A8)

[MybatisX逆向工程](https://baomidou.com/pages/ba5b24/#%E5%8A%9F%E8%83%BD)

[Mybatis 执行器](https://blog.csdn.net/zongf0504/article/details/100104029)