package de.ctxj.spigotEnergy.events;

import de.ctxj.spigotEnergy.objects.abstr.Consumer;
import de.ctxj.spigotEnergy.objects.abstr.EnergyItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ConsumerTriggerEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Consumer consumer;
    private boolean cancelled;

    public ConsumerTriggerEvent(Consumer consumer) {
        this.consumer = consumer;
        this.cancelled = false;
    }

    public Consumer getConsumer() {
        return consumer;
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