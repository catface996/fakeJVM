# synchronized

# Lock

## ReentrantLock

### synchronized 和 ReentrantLock的差异

| 差异点             | synchronized | Lock            |
| ------------------ | ------------ | --------------- |
| 是否支持锁重入     | 支持         | 支持            |
| 是否支持尝试获得锁 | 不支持       | 支持(tryLock)   |
| 是否支持公平锁     | 不支持       | 支持            |
| 是否支持锁中断     | 不支持       | 支持            |
| 是否支持定向唤醒   | 不支持       | 支持(Condition) |

## ReadWriteLock

# 常用于的业务场景

## [发布订阅](./发布订阅/README.md)

