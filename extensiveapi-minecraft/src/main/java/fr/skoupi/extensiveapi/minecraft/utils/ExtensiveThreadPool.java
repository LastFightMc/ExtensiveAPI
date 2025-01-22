package fr.skoupi.extensiveapi.minecraft.utils;

/*  ModuleScheduler
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.concurrent.*;

@Getter
public class ExtensiveThreadPool {

    private ExecutorService executorService;
    private ScheduledExecutorService runnableExecutor;

    public void startingPools(ConfigurationSection threadingSection) {
        final var threadPoolConfiguration = new ThreadPoolConfiguration(threadingSection);
        startingPools(threadPoolConfiguration);
    }

    public void startingPools(ThreadPoolConfiguration threadPoolConfiguration) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(threadPoolConfiguration.getThreadName()).build();

        ArrayBlockingQueue<Runnable> executorQueue = new ArrayBlockingQueue<>(threadPoolConfiguration.getQueueSize(), threadPoolConfiguration.isFair());

        if (threadPoolConfiguration.getMaximumPoolSize() == -1)
            executorService = new ThreadPoolExecutor(threadPoolConfiguration.getMinimumPoolSize(), Integer.MAX_VALUE,
                    threadPoolConfiguration.getKeepAliveTime(), TimeUnit.MILLISECONDS, executorQueue, threadFactory);
        else
            executorService = new ThreadPoolExecutor(threadPoolConfiguration.getMinimumPoolSize(), threadPoolConfiguration.getMaximumPoolSize(),
                    threadPoolConfiguration.getKeepAliveTime(), TimeUnit.MILLISECONDS, executorQueue, threadFactory);

        runnableExecutor = new ScheduledThreadPoolExecutor(threadPoolConfiguration.getMinimumPoolSize(), threadFactory);
    }

    /**
     * Shutdown the executor services.
     */
    public void shutdown() {
        if (executorService != null)
            executorService.shutdown();
        if (runnableExecutor != null)
            runnableExecutor.shutdown();
    }

    /**
     * Shutdown the executor service and runnable executor service.
     */
    public void shutdownNow() {
        if (executorService != null)
            executorService.shutdownNow();
        if (runnableExecutor != null)
            runnableExecutor.shutdownNow();
    }

    @Getter
    @AllArgsConstructor
    public static class ThreadPoolConfiguration {

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
