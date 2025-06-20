package de.ctxj.spigotEnergy.objects.concr.cables;

import de.ctxj.spigotEnergy.objects.abstr.Cable;
import org.bukkit.block.Block;

public class CableType2 extends Cable {
    public CableType2(Block block) {
        super(block, 20, null, 20);
    }
}
