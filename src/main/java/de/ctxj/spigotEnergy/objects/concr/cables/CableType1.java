package de.ctxj.spigotEnergy.objects.concr.cables;

import de.ctxj.spigotEnergy.objects.abstr.Cable;
import de.ctxj.spigotEnergy.objects.abstr.EnergyItem;
import org.bukkit.block.Block;

public class CableType1 extends Cable {
    public CableType1(Block block) {
        super(block, 10, null, 10);
    }
}
