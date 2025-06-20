package de.ctxj.spigotEnergy.objects.concr.storages;

import de.ctxj.spigotEnergy.objects.abstr.EnergyItem;
import de.ctxj.spigotEnergy.objects.abstr.Storage;
import org.bukkit.block.Block;

public class StorageType1 extends Storage {
    public StorageType1(Block block) {
        super(block, 15, null, 15, "§f§lBATTERIE STUFE 1");
    }
}
