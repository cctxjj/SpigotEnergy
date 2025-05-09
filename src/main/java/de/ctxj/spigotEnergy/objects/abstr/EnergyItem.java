package de.ctxj.spigotEnergy.objects.abstr;

import org.bukkit.block.Block;

import java.util.ArrayList;

public abstract class EnergyItem {

    public static ArrayList<EnergyItem> energyItems = new ArrayList<>();
    public static ArrayList<Block> energyItemBlocks = new ArrayList<>();

    private Block block;
    private int energy;
    private final int maxEnergy;

    public EnergyItem(Block block, int maxEnergy) {
        this.block = block;
        if(block != null) {
            energyItemBlocks.add(block);
        }
        this.maxEnergy = maxEnergy;
        energy = 0;
        energyItems.add(this);
    }
    //TODO: Fill in
    public void safeToConfig() {

    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public int getEnergy() {
        return energy;
    }

    public int getMaxEnergy() {
        return maxEnergy;
    }

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        if(this.block != null) {
            energyItemBlocks.remove(this.block);
        }
        this.block = block;
        if(block != null) {
            energyItemBlocks.add(block);
        }
    }
}
