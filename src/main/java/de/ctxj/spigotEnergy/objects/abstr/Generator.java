package de.ctxj.spigotEnergy.objects.abstr;

import org.bukkit.block.Block;

public class Generator extends EnergyTransferItem {

    public Generator(Block block, int maxEnergy, EnergyItem direction) {
        super(block, maxEnergy, direction);
    }
}
