package de.ctxj.spigotEnergy.listeners;

import de.ctxj.spigotEnergy.SpigotEnergy;
import de.ctxj.spigotEnergy.objects.abstr.EnergyItem;
import de.ctxj.spigotEnergy.objects.abstr.EnergyTransferItem;
import de.ctxj.spigotEnergy.objects.abstr.Generator;
import de.ctxj.spigotEnergy.objects.concr.BasicCable;
import de.ctxj.spigotEnergy.objects.concr.BasicConsumer;
import de.ctxj.spigotEnergy.objects.concr.BasicGenerator;
import de.ctxj.spigotEnergy.objects.concr.BasicStorage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashMap;

public class BasicItemsListener implements Listener {
    //TestListener

    @EventHandler
    public void basicGenerator(BlockPlaceEvent event) {
        if(event.getBlock().getType() == Material.GOLD_BLOCK) {
            SpigotEnergy.getEnergyItemManager().registerEnergyItem(new BasicGenerator(event.getBlock(), null));
        }
        else if(event.getBlock().getType() == Material.IRON_BLOCK) {
            SpigotEnergy.getEnergyItemManager().registerEnergyItem(new BasicCable(event.getBlock(), null));
        }
        else if(event.getBlock().getType() == Material.EMERALD_BLOCK) {
            SpigotEnergy.getEnergyItemManager().registerEnergyItem(new BasicStorage(event.getBlock(), null));
        }
        else if(event.getBlock().getType() == Material.OBSIDIAN) {
            SpigotEnergy.getEnergyItemManager().registerEnergyItem(new BasicConsumer(event.getBlock()));
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

    HashMap<Player, EnergyTransferItem> matching = new HashMap<>();

    @EventHandler
    public void onLeftClick(PlayerInteractEvent event) {
        if(event.getClickedBlock() == null) return;
        if(event.getClickedBlock().getType() == Material.GOLD_BLOCK
                || event.getClickedBlock().getType() == Material.IRON_BLOCK
                || event.getClickedBlock().getType() == Material.EMERALD_BLOCK) {
            if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                for(EnergyItem item : SpigotEnergy.getEnergyItemManager().activeItems) {
                    if(item.getBlock().equals(event.getClickedBlock())) {
                        if(matching.containsKey(event.getPlayer())) {
                            if(!(matching.get(event.getPlayer()).equals(item))) {
                                matching.get(event.getPlayer()).setDirection(item);
                                matching.remove(event.getPlayer());
                            }
                        } else if (item instanceof EnergyTransferItem) {
                            matching.put(event.getPlayer(), (EnergyTransferItem) item);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent event) {
        if(event.getBlock().getType() == Material.GOLD_BLOCK
        || event.getBlock().getType() == Material.IRON_BLOCK
        || event.getBlock().getType() == Material.EMERALD_BLOCK
                || event.getBlock().getType() == Material.OBSIDIAN) {
                for(EnergyItem item : SpigotEnergy.getEnergyItemManager().activeItems) {
                    if(item.getBlock().equals(event.getBlock())) {
                        SpigotEnergy.getEnergyItemManager().deleteEnergyItem(item);
                        return;
                    }
                }
        }
    }
}
