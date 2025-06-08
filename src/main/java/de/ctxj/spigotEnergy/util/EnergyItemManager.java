package de.ctxj.spigotEnergy.util;

import de.ctxj.spigotEnergy.SpigotEnergy;
import de.ctxj.spigotEnergy.objects.abstr.Generator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class EnergyItemManager extends FileManager {

    String cablePath = "items.generators";
    String storagePath = "items.generators";
    String consumerPath = "items.generators";

    public EnergyItemManager() {
        super(SpigotEnergy.getInstance().getItemFilePath());
    }

    public void initialize() {

    }





    public void addCable() {

    }

    public void removeCable() {

    }

    private void initializeCables() {

    }


    public void addStorage() {

    }

    public void removeStorage() {

    }

    private void initializeStorages() {

    }


    public void addConsumer() {

    }

    public void removeConsumer() {

    }

    private void initializeConsumers() {

    }
}
