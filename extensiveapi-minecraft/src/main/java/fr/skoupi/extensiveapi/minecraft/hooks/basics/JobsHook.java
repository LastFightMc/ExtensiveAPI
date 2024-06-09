package fr.skoupi.extensiveapi.minecraft.hooks.basics;

/*  JobsHook
 *  By: vSKAH <vskahhh@gmail.com>

 * Created with IntelliJ IDEA
 * For the project ExtensiveAPI
 */


import com.gamingmesh.jobs.Jobs;
import fr.skoupi.extensiveapi.minecraft.hooks.AbstractHook;

public class JobsHook extends AbstractHook<Jobs> {

    private Jobs jobs;

    public JobsHook() {
        super("Jobs", "com.gamingmesh.jobs.Jobs");
    }

    @Override
    public boolean registerHook() {
        if(!pluginEnabled()) return false;
        this.jobs = Jobs.getInstance();
        return true;
    }

    @Override
    public Jobs getHook() {
        return jobs;
    }
}
