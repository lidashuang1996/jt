package com.lidashuang.jt;

import com.lidashuang.jt.jt808.Jt808T0;
import com.lidashuang.jt.message.JttMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 注册中心
 * @author lidashuang
 * @version 1.0
 */
public final class JtRegistry {

    public static final String SYNC = "SYNC";
    public static final String ASYNC = "ASYNC";

    private static final String DEFAULT_NAME = "__DEFAULT__";

    /** sync async // 同步 异步 */
    private static String MODE = ASYNC;

    /** 消息核心 */
    private static final Map<String, JttMessage> MESSAGE_CORE = new HashMap<>();

    /** 处理核心 */
    private static final Map<String, JtActuator> ACTUATOR_CORE = new HashMap<>();

    /** 处理线程池 */
    private static ThreadPoolExecutor THREAD_POOL = new ThreadPoolExecutor(5,
            20, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new DefaultThreadFactory());

    static {
        /* 设置默认的消息核心 */
        MESSAGE_CORE.put(DEFAULT_NAME, new Jt808T0());
        /* 设置默认的处理器核心 */
        ACTUATOR_CORE.put(DEFAULT_NAME, new JtGlobalDefaultActuator());
    }

    /**
     * 获取消息的核心
     * @param key 消息的状态
     * @return 获取消息的核心
     */
    public static JttMessage getMessageCore(int key) {
        JttMessage message = MESSAGE_CORE.get(String.valueOf(key));
        if (message == null) {
            message = MESSAGE_CORE.get(DEFAULT_NAME);
        }
        return message;
    }

    /**
     * 获取消息的处理器
     * @param key 消息的状态
     * @return 消息的处理器
     */
    public static JtActuator getActuatorCore(int key) {
        JtActuator actuator = ACTUATOR_CORE.get(String.valueOf(key));
        if (actuator == null) {
            actuator = ACTUATOR_CORE.get(DEFAULT_NAME);
        }
        return actuator;
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
            THREAD_POOL = null;
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
     * 设置同步还是异步执行
     * @param mode 方式
     */
    public static void setMode(String mode) {
        if (mode != null) {
            MODE = ASYNC.equals(mode.toUpperCase()) ? ASYNC : SYNC;
            if (SYNC.equals(MODE)) {
                shutdownThreadPool();
            } else {
                THREAD_POOL = new ThreadPoolExecutor(5,
                        20, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new DefaultThreadFactory());
            }
        }
    }

    /**
     * 获取方式
     * @return 同步还是异步执行的方式
     */
    public static String getMode() {
        return MODE;
    }

    public synchronized static void registerJtMessage(int key, JttMessage value) {
        MESSAGE_CORE.put(String.valueOf(key), value);
    }

    public synchronized static void registerActuator(int key, JtActuator value) {
        ACTUATOR_CORE.put(String.valueOf(key), value);
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
