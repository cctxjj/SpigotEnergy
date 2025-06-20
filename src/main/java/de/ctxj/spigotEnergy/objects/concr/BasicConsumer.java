package de.ctxj.spigotEnergy.objects.concr;

import de.ctxj.spigotEnergy.objects.abstr.Consumer;
import org.bukkit.block.Block;

public class BasicConsumer extends Consumer {

    public BasicConsumer(Block block) {
        super(block, 700, "§fSimpler Verbraucher");
    }
}
