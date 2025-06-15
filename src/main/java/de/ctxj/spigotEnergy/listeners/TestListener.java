package de.ctxj.spigotEnergy.listeners;

import de.ctxj.spigotEnergy.events.EnergyItemUseEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TestListener implements Listener {

    @EventHandler
    public void onTestEvent(EnergyItemUseEvent event) {
        // This is a placeholder for a test event handler.
        // You can implement your test logic here.
        System.out.println("Test event triggered: " + event.getEventName());
    }
    //TODO: remove this
}
