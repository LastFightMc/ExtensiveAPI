package fr.skoupi.extensiveapi.minecraft.utils;

/*  ModuleScheduler
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.concurrent.*;

@Getter
public class ExtensiveThreadPool {

    private static @Getter ExecutorService executorService;
    private static @Getter ScheduledExecutorService runnableExecutor;

    public static void startingPools(ConfigurationSection threadingSection) {

        final ThreadPoolConfiguration conf = new ThreadPoolConfiguration(threadingSection);
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(conf.getThreadName()).build();

        ArrayBlockingQueue<Runnable> executorQueue = new ArrayBlockingQueue<>(conf.getQueueSize(), conf.isFair());

        if (conf.getMaximumPoolSize() == -1)
            executorService = new ThreadPoolExecutor(conf.getMinimumPoolSize(), Integer.MAX_VALUE,
                    conf.getKeepAliveTime(), TimeUnit.MILLISECONDS, executorQueue, threadFactory);
        else
            executorService = new ThreadPoolExecutor(conf.getMinimumPoolSize(), conf.getMaximumPoolSize(),
                    conf.getKeepAliveTime(), TimeUnit.MILLISECONDS, executorQueue, threadFactory);

        runnableExecutor = new ScheduledThreadPoolExecutor(conf.getMinimumPoolSize(), threadFactory);
    }

    /**
     * Shutdown the executor services.
     */
    public static void shutdown() {
        if (executorService != null)
            executorService.shutdown();
        if (runnableExecutor != null)
            runnableExecutor.shutdown();
    }

    /**
     * Shutdown the executor service and runnable executor service.
     */
    public static void shutdownNow() {
        if (executorService != null)
            executorService.shutdownNow();
        if (runnableExecutor != null)
            runnableExecutor.shutdownNow();
    }

    @Getter
    private static class ThreadPoolConfiguration {

        private final int minimumPoolSize;
        private final int maximumPoolSize;
        private final long keepAliveTime;

        private final int queueSize;
        private final boolean fair;

        private final String threadName;

        public ThreadPoolConfiguration(ConfigurationSection section) {
            ConfigurationSection poolSection = section.getConfigurationSection("pool");
            if (poolSection == null)
                throw new RuntimeException("Pool section is missing");

            minimumPoolSize = poolSection.getInt("minimum", 2);
            maximumPoolSize = poolSection.getInt("maximum", 10);

            keepAliveTime = poolSection.getLong("keep_alive", 5000);

            ConfigurationSection queueSection = section.getConfigurationSection("queue");
            if (queueSection == null)
                throw new RuntimeException("Queue section is missing");

            queueSize = queueSection.getInt("size", 100);
            fair = queueSection.getString("order", "FIFO").equalsIgnoreCase("FIFO");

            threadName = section.getString("name", "ExtensiveAPI-Thread-%d");
        }
    }

}
