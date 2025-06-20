package de.ctxj.spigotEnergy.objects.concr.test;

import de.ctxj.spigotEnergy.objects.abstr.Cable;
import de.ctxj.spigotEnergy.objects.abstr.EnergyItem;
import org.bukkit.block.Block;

public class BasicCable extends Cable {

    public BasicCable(Block block, EnergyItem direction) {
        super(block, 10, direction, 2);
    }
}
