package net.okocraft.boltbeaconprotection;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.popcraft.bolt.BoltPlugin;

public class BoltBeaconProtectionPlugin extends JavaPlugin {

    private BeaconListener beaconListener;

    @Override
    public void onEnable() {
        this.beaconListener = new BeaconListener(JavaPlugin.getPlugin(BoltPlugin.class));
        this.getServer().getPluginManager().registerEvents(this.beaconListener, this);
    }

    @Override
    public void onDisable() {
        if (this.beaconListener != null) {
            HandlerList.unregisterAll(this.beaconListener);
        }
    }
}
