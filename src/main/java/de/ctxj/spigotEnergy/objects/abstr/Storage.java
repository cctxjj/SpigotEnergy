package de.ctxj.spigotEnergy.objects.abstr;

import org.bukkit.block.Block;

public class Storage extends EnergyTransferItem {


    public Storage(Block block, int maxEnergy, EnergyItem direction) {
        super(ItemType.STORAGE, block, maxEnergy, direction);
    }

    @Override
    public void savetoConfig() {

    }
}
