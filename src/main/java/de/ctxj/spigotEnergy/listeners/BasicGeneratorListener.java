package de.ctxj.spigotEnergy.listeners;

import de.ctxj.spigotEnergy.SpigotEnergy;
import de.ctxj.spigotEnergy.objects.abstr.EnergyItem;
import de.ctxj.spigotEnergy.objects.abstr.Generator;
import de.ctxj.spigotEnergy.objects.concr.BasicGenerator;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BasicGeneratorListener implements Listener {

    @EventHandler
    public void basicGenerator(BlockPlaceEvent event) {
        if(event.getBlock().getType() == Material.GOLD_BLOCK) {
            SpigotEnergy.getEnergyItemManager().registerEnergyItem(new BasicGenerator(event.getBlock(), null));
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null) return;
        if(event.getClickedBlock().getType() == Material.GOLD_BLOCK) {
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                for(EnergyItem item : SpigotEnergy.getEnergyItemManager().activeItems) {
                    if(item.getBlock().equals(event.getClickedBlock())) {
                        if(item instanceof Generator) {
                            event.setCancelled(true);
                            event.getPlayer().openInventory(((Generator) item).getInventory());
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent event) {
        if(event.getBlock().getType() == Material.GOLD_BLOCK) {
                for(EnergyItem item : SpigotEnergy.getEnergyItemManager().activeItems) {
                    if(item.getBlock().equals(event.getBlock())) {
                        SpigotEnergy.getEnergyItemManager().deleteEnergyItem(item);
                        return;
                    }
                }
        }
    }
}
