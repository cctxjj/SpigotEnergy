package de.ctxj.spigotEnergy.objects.abstr;

import org.bukkit.block.Block;

public abstract class Cable extends EnergyTransferItem {
    public Cable(Block block, int maxEnergy, EnergyItem direction, int transferRate) {
        super(block, maxEnergy, direction, transferRate);
    }

}
