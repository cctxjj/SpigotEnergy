package de.ctxj.spigotEnergy.objects.abstr;

import org.bukkit.block.Block;

public class Consumer extends EnergyItem {
    public Consumer(Block block, int maxEnergy) {
        super(ItemType.CONSUMER, block, maxEnergy);
    }

    @Override
    public void savetoConfig() {

    }
}
