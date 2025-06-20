package de.ctxj.spigotEnergy.objects.concr.storages;

import de.ctxj.spigotEnergy.objects.abstr.Storage;
import org.bukkit.block.Block;

public class StorageType3 extends Storage {
    public StorageType3(Block block) {
        super(block, 40, null, 40, "§f§lBATTERIE STUFE 3");
    }
}
