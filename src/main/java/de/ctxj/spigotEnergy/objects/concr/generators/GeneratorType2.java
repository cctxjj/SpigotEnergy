package de.ctxj.spigotEnergy.objects.concr.generators;

import de.ctxj.spigotEnergy.SpigotEnergy;
import de.ctxj.spigotEnergy.objects.abstr.Generator;
import org.bukkit.block.Block;

public class GeneratorType2 extends Generator {

    public GeneratorType2(Block block) {
        super(block, 6000, null, 20, SpigotEnergy.defaultConsumeItem, 20, "§f§lGENERATOR STUFE 2");
    }
}
