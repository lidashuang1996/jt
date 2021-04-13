package com.lidashuang.jt;

import com.lidashuang.jt.jt808.Jt808T0;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 注册中心
 * @author lidashuang
 * @version 1.0
 */
public final class JtRegistry {

    /** 消息核心 */
    private static final Map<Integer, JtMessage> MESSAGE_CORE = new Hashtable<>();

    /** 处理核心 */
    private static final Map<Integer, JtActuator> ACTUATOR_CORE = new Hashtable<>();

    /** 处理线程池 */
    private static ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(0,
            Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new DefaultThreadFactory());

    static {
        /* 设置默认的消息核心 */
        MESSAGE_CORE.put(null, new Jt808T0());
        /* 设置默认的处理器核心 */
        ACTUATOR_CORE.put(null, (context, message) -> System.out.println(message));
    }

    /**
     * 获取消息的核心
     * @param key 消息的状态
     * @return 获取消息的核心
     */
    public static JtMessage getMessageCore(Integer key) {
        JtMessage message = MESSAGE_CORE.get(key);
        if (message == null) {
            message = MESSAGE_CORE.get(null);
        }
        return message;
    }

    /**
     * 获取消息的核心列表
     * @return 消息的核心列表
     */
    public static Map<Integer, JtMessage> getMessageCore() {
        return MESSAGE_CORE;
    }

    /**
     * 获取消息的处理器
     * @param key 消息的状态
     * @return 消息的处理器
     */
    public static JtActuator getActuatorCore(Integer key) {
        JtActuator actuator = ACTUATOR_CORE.get(key);
        if (actuator == null) {
            actuator = ACTUATOR_CORE.get(null);
        }
        return actuator;
    }

    /**
     * 获取消息的处理器列表
     * @return 消息的处理器列表
     */
    public static Map<Integer, JtActuator> getActuatorCore() {
        return ACTUATOR_CORE;
    }

    /**
     * 设置线程池
     * @param threadPoolExecutor 线程池对象
     */
    public static void setThreadPool(ThreadPoolExecutor threadPoolExecutor) {
        shutdownThreadPool();
        THREAD_POOL = threadPoolExecutor;
    }

    /**
     * 关闭线程池
     */
    public static void shutdownThreadPool() {
        if (THREAD_POOL != null) {
            THREAD_POOL.shutdown();
        }
    }

    /**
     * 提交任务到线程池
     */
    public static void executeThreadPool(Runnable runnable) {
        if (THREAD_POOL == null) {
            throw new RuntimeException("线程池为空，无法提交任务到线程池中...");
        } else {
            THREAD_POOL.execute(runnable);
        }
    }

    /**
     * The default thread factory
     */
    public static class DefaultThreadFactory implements ThreadFactory {
        private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        public DefaultThreadFactory() {
            final SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "actuator-pool-" + POOL_NUMBER.getAndIncrement() + "-thread-";
        }

        @Override
        public Thread newThread(Runnable r) {
            final Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }


}
