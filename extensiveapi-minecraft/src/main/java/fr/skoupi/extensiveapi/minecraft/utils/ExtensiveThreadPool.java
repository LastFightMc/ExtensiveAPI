package fr.skoupi.extensiveapi.minecraft.utils;

/*  ModuleScheduler
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ExtensiveThreadPool {

	public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2, r -> new Thread(r, "ModuleTask:" + r.getClass().getName()));
	public static final ScheduledExecutorService RUNNABLE_EXECUTOR = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() * 2, r -> new Thread(r, "ModuleRunnable:" + r.getClass().getName()));

	/**
	 * Shutdown the executor services.
	 */
	public static void shutdown ()
	{
		EXECUTOR_SERVICE.shutdown();
		RUNNABLE_EXECUTOR.shutdown();
	}

	/**
	 * Shutdown the executor service and runnable executor service.
	 */
	public static void shutdownNow ()
	{
		EXECUTOR_SERVICE.shutdownNow();
		RUNNABLE_EXECUTOR.shutdownNow();
	}
}
