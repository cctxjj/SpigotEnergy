package de.ctxj.spigotEnergy.objects.concr.consumers;

import de.ctxj.spigotEnergy.objects.abstr.Consumer;
import org.bukkit.block.Block;

public class ConsumerType1 extends Consumer {
    public ConsumerType1(Block block) {
        super(block, 1000, "§f§lVERBRAUCHER STUFE 1");
    }
}
