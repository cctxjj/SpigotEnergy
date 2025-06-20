package de.ctxj.spigotEnergy.objects.concr.consumers;

import de.ctxj.spigotEnergy.objects.abstr.Consumer;
import org.bukkit.block.Block;

public class ConsumerType2 extends Consumer {
    public ConsumerType2(Block block) {
        super(block, 2000, "§f§lVERBRAUCHER STUFE 2");
    }
}
