package de.ctxj.spigotEnergy.listeners;

import de.ctxj.spigotEnergy.SpigotEnergy;
import de.ctxj.spigotEnergy.events.EnergyItemUseEvent;
import de.ctxj.spigotEnergy.objects.abstr.EnergyItem;
import de.ctxj.spigotEnergy.objects.abstr.EnergyItemStack;
import de.ctxj.spigotEnergy.objects.abstr.Storage;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class EnergyItemStackListener implements Listener {

    @EventHandler
    public void onItemInteract(org.bukkit.event.player.PlayerInteractEvent event) {
        if (event.getClickedBlock() == null || event.getItem() == null) return;

        Player player = event.getPlayer();
        ItemStack itemStack = event.getItem();


        if (!EnergyItemStack.isEnergyItemStack(itemStack)) return;


        Block clickedBlock = event.getClickedBlock();
        Storage storage = null;
        for (EnergyItem energyItem : SpigotEnergy.getEnergyItemManager().activeItems) {
            if (energyItem instanceof Storage && energyItem.getBlock().equals(clickedBlock)) {
                storage = (Storage) energyItem;
                break;
            }
        }
        if (storage == null) {
            try {
                EnergyItemStack item = new EnergyItemStack(event.getItem());
                EnergyItemUseEvent newEvent = new EnergyItemUseEvent(event.getPlayer(), item);
                Bukkit.getPluginManager().callEvent(newEvent);
            } catch (Exception ignored) {}
            return;
        }

        EnergyItemStack energyItemStack = new EnergyItemStack(itemStack);


        int transferAmount = Math.min(10, storage.getTransferRate()); // Beispielwert, kann angepasst werden


        if (event.getAction() == org.bukkit.event.block.Action.LEFT_CLICK_BLOCK) {
            if (energyItemStack.getEnergy() < transferAmount) {
                transferAmount = energyItemStack.getEnergy();
            }
            int transferableEnergy = Math.min(transferAmount, storage.getMaxEnergy() - storage.getEnergy());
            if (transferableEnergy > 0) {
                energyItemStack.setEnergy(energyItemStack.getEnergy() - transferableEnergy);
                storage.setEnergy(storage.getEnergy() + transferableEnergy);
                storage.cfgUpdateEnergy();
                player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new net.md_5.bungee.api.chat.TextComponent("§c-" + transferableEnergy));
            } else {
                player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new net.md_5.bungee.api.chat.TextComponent("§cSpeicher voll!"));
            }
        }

        if (event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK) {
            int transferableEnergy = Math.min(transferAmount, storage.getEnergy());
            if (transferableEnergy > 0) {
                energyItemStack.setEnergy(energyItemStack.getEnergy() + transferableEnergy);
                storage.setEnergy(storage.getEnergy() - transferableEnergy);
                storage.cfgUpdateEnergy();
                player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new net.md_5.bungee.api.chat.TextComponent("§a+" + transferableEnergy));
            } else {
                player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new net.md_5.bungee.api.chat.TextComponent("§cKeine Energie im Speicher!"));
            }
        }
    }

}
