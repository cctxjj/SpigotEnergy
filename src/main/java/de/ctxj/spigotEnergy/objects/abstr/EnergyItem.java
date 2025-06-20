package de.ctxj.spigotEnergy.objects.abstr;

import org.bukkit.block.Block;


public abstract class EnergyItem {

    private final Block block;
    private int energy;
    private final int maxEnergy;

    public EnergyItem(Block block, int maxEnergy) {
        this.block = block;
        this.maxEnergy = maxEnergy;
        energy = 0;
    }

    public EnergyItem(Block block, int maxEnergy, int energy) {
        this.block = block;
        this.maxEnergy = maxEnergy;
        this.energy = energy;
    }

    public abstract void cfgRegister();
    public abstract void cfgRemove();
    public abstract void cfgUpdateEnergy();

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
}
