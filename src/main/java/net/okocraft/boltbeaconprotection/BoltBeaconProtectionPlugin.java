package net.okocraft.boltbeaconprotection;

import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.popcraft.bolt.BoltPlugin;

public class BoltBeaconProtectionPlugin extends JavaPlugin {

    private BeaconListener beaconListener;

    @Override
    public void onEnable() {
        beaconListener = new BeaconListener(JavaPlugin.getPlugin(BoltPlugin.class));
        getServer().getPluginManager().registerEvents(beaconListener, this);
    }

    @Override
    public void onDisable() {
        if (beaconListener != null) {
            HandlerList.unregisterAll(beaconListener);
        }
    }
}
