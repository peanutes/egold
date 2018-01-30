package com.zfhy.egold.common.util;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by LAI on 2017/9/28.
 */
public class ThreadUtil {
    //通过ThreadPoolExecutor的代理类来对线程池的管理
    private static volatile ThreadPollProxy mThreadPollProxy;
    //单列对象
    public static ThreadPollProxy getThreadPollProxy(){
        if (Objects.isNull(mThreadPollProxy)) {
            synchronized (ThreadPollProxy.class) {
                if (Objects.isNull(mThreadPollProxy)) {
                    mThreadPollProxy = new ThreadPollProxy(1, Runtime.getRuntime().availableProcessors(), 1000);
                }
            }

        }
        return mThreadPollProxy;
    }
    //通过ThreadPoolExecutor的代理类来对线程池的管理
    public static class ThreadPollProxy{
        private ThreadPoolExecutor poolExecutor;
        private int corePoolSize;
        private int maximumPoolSize;
        private long keepAliveTime;

        private ThreadPollProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
            this.corePoolSize = corePoolSize;
            this.maximumPoolSize = maximumPoolSize;
            this.keepAliveTime = keepAliveTime;
        }
        //对外提供一个执行任务的方法
        public void execute(Runnable r){
            if(poolExecutor==null||poolExecutor.isShutdown()){
                poolExecutor=new ThreadPoolExecutor(
                        //核心线程数量
                        corePoolSize,
                        //最大线程数量
                        maximumPoolSize,
                        //当线程空闲时，保持活跃的时间
                        keepAliveTime,
                        //时间单元 ，毫秒级
                        TimeUnit.MILLISECONDS,
                        //线程任务队列
                        new LinkedBlockingQueue<Runnable>());
            }
            poolExecutor.execute(r);
        }
    }

}
