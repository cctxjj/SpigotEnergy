package de.ctxj.spigotEnergy.holographs;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class HolographListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity().isCustomNameVisible() && event.getEntity() instanceof ArmorStand) {
            event.setCancelled(true);
        }
    }
}
