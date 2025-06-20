package de.ctxj.spigotEnergy.objects.concr.test;

import de.ctxj.spigotEnergy.objects.abstr.EnergyItem;
import de.ctxj.spigotEnergy.objects.abstr.Storage;
import org.bukkit.block.Block;

public class BasicStorage extends Storage {
    public BasicStorage(Block block, EnergyItem direction) {
        super(block, 7000, direction, 2, "§fSimple Batterie");
    }
}
