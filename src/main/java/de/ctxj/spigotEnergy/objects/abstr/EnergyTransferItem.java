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
        if(getEnergy() < transferRate) {
            return false;
        }
        if(direction.getEnergy()+transferRate <=direction.getMaxEnergy()) {
            direction.setEnergy(direction.getEnergy()+transferRate);
            setEnergy(getEnergy()-transferRate);
            return true;
        }
        return false;
    }

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
