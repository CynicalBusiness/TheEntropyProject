package me.capit.tep.async;

import me.capit.tep.TEPPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AsyncEventRunner extends BukkitRunnable{
    public volatile TEPPlugin plugin;

    public AsyncEventRunner(TEPPlugin plugin){
        this.plugin = plugin;
    }

    private volatile int tickInSecond;

    @Override
    public void run(){

    }

}
