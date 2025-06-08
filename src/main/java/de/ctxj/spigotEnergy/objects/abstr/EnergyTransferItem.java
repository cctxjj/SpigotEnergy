package de.ctxj.spigotEnergy.objects.abstr;

import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class EnergyTransferItem extends EnergyItem {

    private final int transferRate;
    private EnergyItem direction;

    public EnergyTransferItem(Block block, int maxEnergy, EnergyItem direction, int transferRate) {
        super(block, maxEnergy);
        this.transferRate = transferRate;
        this.direction = direction;


    }

    //return boolean whether the transfer was successful.
    public boolean transferEnergy() {
        if(direction != null) {
            return false;
        }
        //Todo: add method for transfering
        return true;
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
