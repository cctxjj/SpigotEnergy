package de.ctxj.spigotEnergy.objects.concr.storages;

import de.ctxj.spigotEnergy.objects.abstr.Storage;
import org.bukkit.block.Block;

public class StorageType2 extends Storage {
    public StorageType2(Block block) {
        super(block, 30, null, 30, "§f§lBATTERIE STUFE 2");
    }
}
