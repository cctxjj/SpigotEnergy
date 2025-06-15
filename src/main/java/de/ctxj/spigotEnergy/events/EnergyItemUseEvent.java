package de.ctxj.spigotEnergy.events;

import de.ctxj.spigotEnergy.objects.abstr.EnergyItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class EnergyItemUseEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final EnergyItemStack energyItemStack;
    private boolean cancelled;

    public EnergyItemUseEvent(Player player, EnergyItemStack energyItemStack) {
        this.player = player;
        this.energyItemStack = energyItemStack;
        this.cancelled = false;
    }

    public Player getPlayer() {
        return player;
    }

    public EnergyItemStack getEnergyItemStack() {
        return energyItemStack;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}