package net.okocraft.boltbeaconprotection;

import io.papermc.paper.event.player.PlayerChangeBeaconEffectEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.Beacon;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.jspecify.annotations.NullMarked;
import org.popcraft.bolt.BoltPlugin;

import java.util.Locale;
import java.util.UUID;

@NullMarked
public class BeaconListener implements Listener {

    private final BoltPlugin bolt;

    public BeaconListener(BoltPlugin bolt) {
        this.bolt = bolt;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onChangeBeaconEffect(PlayerChangeBeaconEffectEvent event) {
        var block = event.getBeacon();
        var player = event.getPlayer();

        if (!(block.getState() instanceof Beacon beacon) || !player.hasPermission("boltbeaconprotection.autoprotect")) {
            return;
        }

        int protectedBlocks = this.protectBase(beacon, player.getUniqueId());

        if (protectedBlocks < 1) {
            return;
        }

        var message = Component.text();

        if (player.locale().getLanguage().equals(Locale.JAPANESE.getLanguage())) {
            message.append(Component.text(protectedBlocks + "個", NamedTextColor.AQUA))
                    .append(Component.text("のビーコンの土台ブロックを保護しました。", NamedTextColor.GRAY));
        } else {
            message.append(Component.text("Protected ", NamedTextColor.GRAY))
                    .append(Component.text(protectedBlocks + " base blocks", NamedTextColor.AQUA))
                    .append(Component.text(" of the beacon.", NamedTextColor.GRAY));
        }

        player.sendMessage(message.build());
    }

    // Copied from BeaconBlockEntity#updateBase
    private int protectBase(Beacon beacon, UUID uuid) {
        World world = beacon.getWorld();
        int x = beacon.getX();
        int y = beacon.getY();
        int z = beacon.getZ();

        int count = 0;

        for (int i1 = 1; i1 <= beacon.getTier(); i1++) {
            int j1 = y - i1;

            if (j1 < world.getMinHeight()) {
                break;
            }

            for (int k1 = x - i1; k1 <= x + i1; ++k1) {
                for (int l1 = z - i1; l1 <= z + i1; ++l1) {
                    var block = world.getBlockAt(k1, j1, l1);

                    if (!Tag.BEACON_BASE_BLOCKS.isTagged(block.getType()) || this.bolt.isProtected(block)) {
                        continue;
                    }

                    this.bolt.saveProtection(this.bolt.createProtection(block, uuid, this.bolt.getDefaultProtectionType()));
                    count++;
                }
            }
        }

        return count;
    }
}
