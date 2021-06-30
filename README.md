# fakeJVM
极简版JVM

## JVM 8个原子指令



| 命令   | 作用内存区域 | 描述                              |
| ------ | ------------ | --------------------------------- |
| lock   | 主内存       | 标识变量为线程独占                |
| unlock | 主内存       | 解锁线程独占变量                  |
| read   | 主内存       | 读取内容到工作内存                |
| load   | 工作内存     | read后的值,放到线程的本地变量副本 |
| use    | 工作内存     | 传值给执行引擎                    |
| assign | 工作内存     | 执行引擎将结果负值给线程本地变量  |
| store  | 工作内存     | 存储到主存,给write备用            |
| write  | 主内存       | 写变量                            |



## dubbo 和 long 64位的数据类型加volatile后的原子性

<img src="https://tva1.sinaimg.cn/large/008i3skNly1grz87f1zewj31o50u0to9.jpg" alt="image-20210629164014263" style="zoom:150%;" />



## Linux的lock指令和JVM的lock指令



~~~shell
## 查看 lock
man 2 lock

SYNOPSIS
       Unimplemented system calls.

DESCRIPTION
       These system calls are not implemented in the Linux kernel.

## 查看 futex
man 2 futex

NAME
       futex - fast user-space locking

DESCRIPTION
       The futex() system call provides a method for waiting until a certain condition becomes true.  It is typically
       used as a blocking construct in the context of shared-memory synchronization.  When using futexes, the  major‐
       ity  of  the synchronization operations are performed in user space.  A user-space program employs the futex()
       system call only when it is likely that the program has to block for a longer time until the condition becomes
       true.   Other  futex() operations can be used to wake any processes or threads waiting for a particular condi‐
       tion.

       A futex is a 32-bit value—referred to below as a futex word—whose address is supplied to  the  futex()  system
       call.   (Futexes  are  32  bits in size on all platforms, including 64-bit systems.)  All futex operations are
       governed by this value.  In order to share a futex between processes, the futex  is  placed  in  a  region  of
       shared memory, created using (for example) mmap(2) or shmat(2).  (Thus, the futex word may have different vir‐
       tual addresses in different processes, but these addresses all refer to the same location in physical memory.)
       In  a  multithreaded  program,  it  is  sufficient  to place the futex word in a global variable shared by all
       threads.

       When executing a futex operation that requests to block a thread, the kernel will block only if the futex word
       has  the  value that the calling thread supplied (as one of the arguments of the futex() call) as the expected
       value of the futex word.  The loading of the futex word's  value,  the  comparison  of  that  value  with  the
       expected  value,  and  the  actual blocking will happen atomically and will be totally ordered with respect to
       concurrent operations performed by other threads on the same futex word.  Thus, the futex word is used to con‐
       nect  the  synchronization in user space with the implementation of blocking by the kernel.  Analogously to an
       atomic compare-and-exchange operation that potentially changes shared memory,  blocking  via  a  futex  is  an
       atomic compare-and-block operation.

       One  use  of futexes is for implementing locks.  The state of the lock (i.e., acquired or not acquired) can be
       represented as an atomically accessed flag in shared memory.  In the uncontended case, a thread can access  or
       modify  the  lock  state  with  atomic  instructions,  for example atomically changing it from not acquired to
       acquired using an atomic compare-and-exchange instruction.  (Such instructions are performed entirely in  user
       mode,  and  the  kernel  maintains  no  information about the lock state.)  On the other hand, a thread may be
       unable to acquire a lock because it is already acquired by another thread.  It then may pass the  lock's  flag
       as  a  futex word and the value representing the acquired state as the expected value to a futex() wait opera‐
       tion.  This futex() operation will block if and only if the lock is still acquired (i.e.,  the  value  in  the
       futex word still matches the "acquired state").  When releasing the lock, a thread has to first reset the lock
       state to not acquired and then execute a futex operation that wakes threads blocked on the lock flag used as a
       futex word (this can be further optimized to avoid unnecessary wake-ups).  See futex(7) for more detail on how
       to use futexes.

       Besides the basic wait and wake-up futex functionality, there are further futex operations aimed at supporting
       more complex use cases.

       Note  that no explicit initialization or destruction is necessary to use futexes; the kernel maintains a futex
       (i.e., the kernel-internal implementation artifact) only while operations such as FUTEX_WAIT, described below,
       are being performed on a particular futex word.
~~~



linux 没有实现lock指令,提供了 futex - fast user-space locking



## [字节码相关](./src/main/java/com/fake/jvm/bytecode/README.md )

