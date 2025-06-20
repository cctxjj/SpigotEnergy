package de.ctxj.spigotEnergy.objects.concr.test;

import de.ctxj.spigotEnergy.objects.abstr.EnergyItem;
import de.ctxj.spigotEnergy.objects.abstr.Generator;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

public class BasicGenerator extends Generator {
    public BasicGenerator(Block block, EnergyItem direction) {
        super(block, 200, direction, 10, new ItemStack(Material.GOLD_INGOT), 5, "§fSimpler Generator");
    }
}
