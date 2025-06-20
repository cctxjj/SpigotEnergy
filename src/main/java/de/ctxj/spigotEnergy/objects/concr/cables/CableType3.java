package de.ctxj.spigotEnergy.objects.concr.cables;

import de.ctxj.spigotEnergy.objects.abstr.Cable;
import org.bukkit.block.Block;

public class CableType3 extends Cable {
    public CableType3(Block block) {
        super(block, 30, null, 30);
    }
}
