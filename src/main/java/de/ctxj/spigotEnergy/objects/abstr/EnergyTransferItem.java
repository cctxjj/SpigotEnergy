package de.ctxj.spigotEnergy.objects.abstr;

import org.bukkit.block.Block;

public abstract class EnergyTransferItem extends EnergyItem {

    private final int transferRate;
    private EnergyItem direction;

    public EnergyTransferItem(Block block, int maxEnergy, EnergyItem direction, int transferRate) {
        super(block, maxEnergy);
        this.transferRate = transferRate;
        this.direction = direction;
    }

    public boolean transferEnergy() {
        if(direction == null) {
            return false;
        }
        if(getEnergy() <= 0) {
            if(getEnergy() < 0) {
                setEnergy(0);
            }
            return false;
        }
        if(direction.getEnergy()+Math.min(transferRate, getEnergy()) <= direction.getMaxEnergy()) {
            direction.setEnergy(direction.getEnergy()+Math.min(transferRate, getEnergy()));
            setEnergy(Math.max(0, getEnergy()-transferRate));
            return true;
        }
        return false;
    }

    public abstract void cfgUpdateDirection();

    public int getTransferRate() {
        return transferRate;
    }

    public void setDirection(EnergyItem direction) {
        this.direction = direction;
    }

    public EnergyItem getDirection() {
        return direction;
    }
}
