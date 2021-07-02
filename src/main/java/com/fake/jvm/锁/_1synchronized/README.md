# synchronized

## synchronized 在字节码层面的实现

java文件内容:

~~~java
package com.fake.jvm.锁._1synchronized.code;

/**
 * @author by catface
 * @date 2021/7/1 3:52 下午
 */
public class SynchronizedDemo {

    private static final Object LOCK = new Object();
    private int num;
    private int count = 0;

    public void setUseNothing(int num) {
        this.num = num;
        count++;
    }

    public synchronized void setUseSynMethod(int num) {
        this.num = num;
        count++;
    }

    public void setUseSynBlock(int num) {
        synchronized (LOCK) {
            this.num = num;
            count++;
        }
    }

    public int getNum() {
        return num;
    }

    public int getCount() {
        return count;
    }

}

~~~

字节反编译内容入下:

* setUseNothing方法

基础信息:

![image-20210702152338304](https://tva1.sinaimg.cn/large/008i3skNly1gs2mungoc8j317s0bstak.jpg)

Java虚拟机指令序列:

~~~shell
 0 aload_0
 1 iload_1
 2 putfield #3 <com/fake/jvm/锁/_1synchronized/code/SynchronizedDemo.num>
 5 aload_0
 6 dup
 7 getfield #2 <com/fake/jvm/锁/_1synchronized/code/SynchronizedDemo.count>
10 iconst_1
11 iadd
12 putfield #2 <com/fake/jvm/锁/_1synchronized/code/SynchronizedDemo.count>
15 return
~~~

* setUseSyncMethod

基础信息:

与无synchronized的方法的差异在访问标志位上,setUseNoting的访问标志位是 **0x0001(public)** ,加synchronized后,为**0x0021(public synchronized)**

![image-20210702152615376](https://tva1.sinaimg.cn/large/008i3skNly1gs2mxbxlilj315a0bwgnj.jpg)

Java虚拟机指令序列:

与setUseNothing无差异

~~~shell
 0 aload_0
 1 iload_1
 2 putfield #3 <com/fake/jvm/锁/_1synchronized/code/SynchronizedDemo.num>
 5 aload_0
 6 dup
 7 getfield #2 <com/fake/jvm/锁/_1synchronized/code/SynchronizedDemo.count>
10 iconst_1
11 iadd
12 putfield #2 <com/fake/jvm/锁/_1synchronized/code/SynchronizedDemo.count>
15 return
~~~

* setUseSyncBlock

基础信息,与setUseNothing无差异

![image-20210702153339883](https://tva1.sinaimg.cn/large/008i3skNly1gs2n52248kj312y0co769.jpg)

Java虚拟机指令序列:

会发现在第5行有 **monitorenter**  ,在第22行有**monitorexit**,synchronized语句块是通过在虚拟机指令序列中嵌入 monitor的相关操作来实现的.

~~~shell
 0 getstatic #4 <com/fake/jvm/锁/_1synchronized/code/SynchronizedDemo.LOCK>
 3 dup
 4 astore_2
 5 monitorenter
 6 aload_0
 7 iload_1
 8 putfield #3 <com/fake/jvm/锁/_1synchronized/code/SynchronizedDemo.num>
11 aload_0
12 dup
13 getfield #2 <com/fake/jvm/锁/_1synchronized/code/SynchronizedDemo.count>
16 iconst_1
17 iadd
18 putfield #2 <com/fake/jvm/锁/_1synchronized/code/SynchronizedDemo.count>
21 aload_2
22 monitorexit
23 goto 31 (+8)
26 astore_3
27 aload_2
28 monitorexit
29 aload_3
30 athrow
31 return
~~~

## synchronized 在字节码层面的实现

​ synchronized 在不同的虚拟和不同的操作系统上的实现会有差异,以Oracle 的HotSpot 1.8 Linux版本为例,是通过调用Linux操作系统的futex()
来实现的,futex在linux操作系统中,是通过一种特殊的信号量mutex(互斥信号量来实现)
,synchronized膨胀为重量级锁时,如果当前线程无法获得锁被挂起,会产生系统中断,对当前线程做现场保护,并将其加入到阻塞队列,等待操作系统重新调度.所谓的"重",是指损失在产生系统中断,内核调用,以及操作系统对进程(
jvm线程在linux系统中是通过linux的进程实现的)的调度上的CPU执行时间.

​ Linux 的futex()系统调用描述入下(部分截取):

~~~shell
## 可以通过 man 2 futex 获取该系统调用的使用手册.
NAME
       futex - fast user-space locking

SYNOPSIS
       #include <linux/futex.h>
       #include <sys/time.h>

       int futex(int *uaddr, int futex_op, int val,
                 const struct timespec *timeout,   /* or: uint32_t val2 */
                 int *uaddr2, int val3);

       Note: There is no glibc wrapper for this system call; see NOTES.

DESCRIPTION
       The  futex()  system call provides a method for waiting until a certain condition becomes true.  It is typically used as a blocking construct in the context
       of shared-memory synchronization.  When using futexes, the majority of the synchronization operations are performed in user  space.   A  user-space  program
       employs  the  futex()  system  call  only when it is likely that the program has to block for a longer time until the condition becomes true.  Other futex()
       operations can be used to wake any processes or threads waiting for a particular condition.

       A futex is a 32-bit value—referred to below as a futex word—whose address is supplied to the futex() system call.  (Futexes are 32 bits in size on all plat‐
       forms,  including  64-bit systems.)  All futex operations are governed by this value.  In order to share a futex between processes, the futex is placed in a
       region of shared memory, created using (for example) mmap(2) or shmat(2).  (Thus, the futex word may have different  virtual  addresses  in  different  pro‐
       cesses,  but  these addresses all refer to the same location in physical memory.)  In a multithreaded program, it is sufficient to place the futex word in a
       global variable shared by all threads.

       When executing a futex operation that requests to block a thread, the kernel will block only if the futex word has the value that the  calling  thread  sup‐
       plied  (as one of the arguments of the futex() call) as the expected value of the futex word.  The loading of the futex word's value, the comparison of that
       value with the expected value, and the actual blocking will happen atomically and will be totally ordered with respect to concurrent operations performed by
       other  threads on the same futex word.  Thus, the futex word is used to connect the synchronization in user space with the implementation of blocking by the
       kernel.  Analogously to an atomic compare-and-exchange operation that potentially changes shared memory, blocking via a futex is an atomic compare-and-block
       operation.
~~~

## synchronized 在硬件层面的实现

操作系统层面的系统调用,比如操作信号量,都需要硬件层面支持原子操作,特别是涉及到多核CPU的情况下.CPU硬件厂商一般都会实现CPU指令集中的**lock**和**compxchg**
两条汇编指令.下图展示了x86架构下的部分指令,可以通过锁总线的方式来实现硬件层面的原子性.

![img](https://tva1.sinaimg.cn/large/008i3skNly1gs2o4bx1xnj313e0u0n29.jpg)





