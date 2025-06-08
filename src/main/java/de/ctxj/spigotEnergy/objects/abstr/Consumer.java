package de.ctxj.spigotEnergy.objects.abstr;

import org.bukkit.block.Block;

public abstract class Consumer extends EnergyItem {
    public Consumer(Block block, int maxEnergy) {
        super(block, maxEnergy);
    }


}
