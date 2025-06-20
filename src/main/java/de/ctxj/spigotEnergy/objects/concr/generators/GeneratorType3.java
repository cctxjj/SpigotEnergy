package de.ctxj.spigotEnergy.objects.concr.generators;

import de.ctxj.spigotEnergy.SpigotEnergy;
import de.ctxj.spigotEnergy.objects.abstr.Generator;
import org.bukkit.block.Block;

public class GeneratorType3 extends Generator {

    public GeneratorType3(Block block) {
        super(block, 10000, null, 30, SpigotEnergy.defaultConsumeItem, 30, "§f§lGENERATOR STUFE 3");
    }
}
