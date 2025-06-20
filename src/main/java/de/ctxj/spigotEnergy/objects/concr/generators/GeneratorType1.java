package de.ctxj.spigotEnergy.objects.concr.generators;

import de.ctxj.spigotEnergy.SpigotEnergy;
import de.ctxj.spigotEnergy.objects.abstr.Generator;
import org.bukkit.block.Block;

public class GeneratorType1 extends Generator {

    public GeneratorType1(Block block) {
        super(block, 4000, null, 10, SpigotEnergy.defaultConsumeItem, 10, "§f§lGENERATOR STUFE 1");
    }
}
