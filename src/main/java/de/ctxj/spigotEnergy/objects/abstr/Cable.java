package de.ctxj.spigotEnergy.objects.abstr;

import org.bukkit.block.Block;

public class Cable extends EnergyTransferItem {
    public Cable(Block block, int maxEnergy, EnergyItem direction) {
        super(block, maxEnergy, direction);
    }

}
