package fr.skoupi.extensiveapi.minecraft.tasks;


import fr.skoupi.extensiveapi.minecraft.ExtensiveCore;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

/**
 * Splits manipulating with large about of items in a list
 * into smaller pieces
 */
public abstract class ChunkedTask {

    /**
     * How many ticks should we wait before processing the next bulk amount?
     */
    @Setter
    private int waitPeriodTicks = 20;

    /**
     * How many items should we process at once?
     */
    private final int processAmount;

    /*
     * The current index where we are processing at, right now
     */
    @Getter
    @Setter(AccessLevel.PROTECTED)
    private int currentIndex = 0;

    /*
     * Private flag to prevent dupe executions and cancel running tasks
     */
    @Getter
    private boolean processing = false;
    private boolean firstLaunch = false;

    @Getter
    private static JavaPlugin plugin = ExtensiveCore.getInstance();

    protected boolean logActions = true;

    /**
     * Create a new task that will process the given amount of times on each run
     * (see {@link #waitPeriodTicks}) and wait for 1 second between each time
     *
     * @param processAmount
     */
    public ChunkedTask(int processAmount) {
        this.processAmount = processAmount;
    }

    /**
     * Create a new task that will process the given amount of times on each run
     * and wait the given amount of ticks between each time
     *
     * @param processAmount
     * @param waitPeriodTicks
     */
    public ChunkedTask(int processAmount, int waitPeriodTicks) {
        this.processAmount = processAmount;
        this.waitPeriodTicks = waitPeriodTicks;
    }

    public ChunkedTask(int processAmount, int waitPeriodTicks, JavaPlugin pl) {
        this.processAmount = processAmount;
        this.waitPeriodTicks = waitPeriodTicks;
        plugin = pl;
    }

    /**
     * Start the chain, will run several sync tasks until done
     */
    public final void startChain() {

        if (!this.firstLaunch) {
            this.processing = true;

            this.firstLaunch = true;
        }

        runLater(() -> {

            // Cancelled prematurely
            if (!this.processing) {
                this.onFinish(false);
                this.firstLaunch = false;

                return;
            }

            final long now = System.currentTimeMillis();

            boolean finished = false;
            int processed = 0;

            for (int i = this.currentIndex; i < this.currentIndex + this.processAmount; i++) {
                if (!this.canContinue(i)) {
                    finished = true;

                    break;
                }

                processed++;

                try {
                    this.onProcess(i);

                } catch (Throwable t) {
                    getPlugin().getLogger().severe(t.getMessage());
                    getPlugin().getLogger().severe( "Error in " + this + " processing index " + processed);
                    this.processing = false;
                    this.firstLaunch = false;

                    this.onFinish(false);
                    return;
                }
            }

            if ((processed > 0 || !finished) && logActions)
                getPlugin().getLogger().info(this.getProcessMessage(now, processed));

            if (!finished) {
                this.currentIndex += this.processAmount;

                runLaterAsync(this.waitPeriodTicks, this::startChain);

            } else {
                this.processing = false;
                this.firstLaunch = false;

                this.onFinish(true);
            }
        });
    }

    /**
     * Attempts to cancel this running task, throwing error if it is not running (use {@link #isProcessing()}
     */
    public final void cancel() {
        if(!this.processing)
            getPlugin().getLogger().severe("Chunked task is not running: " + this);

        this.processing = false;
    }

    /**
     * Called when we process a single item
     *
     * @param index
     */
    protected abstract void onProcess(int index) throws Throwable;

    /**
     * Return if the task may execute the next index
     *
     * @param index
     * @return true if can continue
     */
    protected abstract boolean canContinue(int index);

    /**
     * Get the message to send to the console on each progress, or null if no message
     *
     * @param initialTime
     * @param processed
     * @return
     */
    protected String getProcessMessage(long initialTime, int processed) {
        return "Processed " + String.format("%,d", processed) + " " + this.getLabel() + ". Took " + (System.currentTimeMillis() - initialTime) + " ms";
    }

    /**
     * Called when the processing is finished
     *
     * @param gracefully true if natural end, false if {@link #cancel()} used
     */
    protected void onFinish(boolean gracefully) {
        this.onFinish();
    }

    /**
     * @see #onFinish(boolean)
     * @deprecated it is prefered to call {@link #onFinish(boolean)} instead
     */
    @Deprecated
    protected void onFinish() {
    }

    /**
     * Get the label for the process message
     * "blocks" by default
     *
     * @return
     */
    protected String getLabel() {
        return "blocks";
    }

    // ------------------------------------------------------------------------------------------------------------
    // Scheduling
    // ------------------------------------------------------------------------------------------------------------

    /**
     * Runs the task if the plugin is enabled correctly
     *
     * @param task the task
     * @return the task or null
     */
    public static <T extends Runnable> BukkitTask runLater(final T task) {
        return runLater(1, task);
    }

    /**
     * Runs the task even if the plugin is disabled for some reason.
     *
     * @param delayTicks
     * @param task
     * @return the task or null
     */
    public static BukkitTask runLater(final int delayTicks, Runnable task) {
        final BukkitScheduler scheduler = Bukkit.getScheduler();

        try {
            return runIfDisabled(task) ? null : delayTicks == 0 ? task instanceof BukkitRunnable ? ((BukkitRunnable) task).runTask(plugin) : scheduler.runTask(plugin, task) : task instanceof BukkitRunnable ? ((BukkitRunnable) task).runTaskLater(plugin, delayTicks) : scheduler.runTaskLater(plugin, task, delayTicks);
        } catch (final NoSuchMethodError err) {

            return runIfDisabled(task) ? null
                    : delayTicks == 0
                    ? task instanceof BukkitRunnable ? ((BukkitRunnable) task).runTask(plugin) : getTaskFromId(scheduler.scheduleSyncDelayedTask(plugin, task))
                    : task instanceof BukkitRunnable ? ((BukkitRunnable) task).runTaskLater(plugin, delayTicks) : getTaskFromId(scheduler.scheduleSyncDelayedTask(plugin, task, delayTicks));
        }
    }

    // ------------------------------------------------------------------------------------------------------------
    // Bukkit scheduling
    // ------------------------------------------------------------------------------------------------------------

    /**
     * Runs the task async even if the plugin is disabled for some reason.
     *
     * @param delayTicks
     * @param task
     * @return the task or null
     */
    public static BukkitTask runLaterAsync(final int delayTicks, Runnable task) {
        final BukkitScheduler scheduler = Bukkit.getScheduler();

        try {
            return runIfDisabled(task) ? null : delayTicks == 0 ? task instanceof BukkitRunnable ? ((BukkitRunnable) task).runTaskAsynchronously(plugin) : scheduler.runTaskAsynchronously(plugin, task) : task instanceof BukkitRunnable ? ((BukkitRunnable) task).runTaskLaterAsynchronously(plugin, delayTicks) : scheduler.runTaskLaterAsynchronously(plugin, task, delayTicks);

        } catch (final NoSuchMethodError err) {
            return runIfDisabled(task) ? null
                    : delayTicks == 0
                    ? getTaskFromId(scheduler.scheduleAsyncDelayedTask(plugin, task))
                    : getTaskFromId(scheduler.scheduleAsyncDelayedTask(plugin, task, delayTicks));
        }
    }

    /*
     * A compatibility method that converts the given task id into a bukkit task
     */
    private static BukkitTask getTaskFromId(int taskId) {

        for (final BukkitTask task : Bukkit.getScheduler().getPendingTasks())
            if (task.getTaskId() == taskId)
                return task;

        return null;
    }

    // Check our plugin instance if it's enabled
    // In case it is disabled, just runs the task and returns true
    // Otherwise we return false and the task will be run correctly in Bukkit scheduler
    // This is fail-safe to critical save-on-exit operations in case our plugin is improperly reloaded (PlugMan) or malfunctions
    private static boolean runIfDisabled(final Runnable run) {
        if (!plugin.isEnabled()) {
            run.run();

            return true;
        }

        return false;
    }

}