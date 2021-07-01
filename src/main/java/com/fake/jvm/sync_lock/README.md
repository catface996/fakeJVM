# 生产者和消费者同步问题

队列大小为10

生产者每隔1s投递一条消息,只有在队列未满的情况下投递,如果队列已满,阻塞,身缠这可以有多个

消费者每隔3s消费一条消息,只有在队列不为空的时候可以消费,如果队列已经为空,阻塞,消费者可以有多个

## 方案一 使用synchronized 完成同步处理

publisher和consumer使用同一把锁获得进入临界区的资格

唤醒时,会同时唤醒publisher和consumer

## 使用ReentrantLock + Condition 完成同步处理

publisher和consumer使用同一把锁获得进入临界区的资格

唤醒时,通过不同的Condition,publisher能定向唤醒consumer,consumer能定向唤醒publisher

## 使用ReadWriteLock完成同步处理(不支持队列为空时阻塞consumer)

WriteLock: publisher和consumer会产生竞争,只允许一个线程操作临界区

ReadLock: consumer之间不会产生竞争,consumer会与publisher产生竞争