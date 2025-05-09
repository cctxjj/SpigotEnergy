package de.ctxj.spigotEnergy.objects.abstr;

import org.bukkit.block.Block;

import java.util.ArrayList;

public abstract class EnergyTransferItem extends EnergyItem {

    public static ArrayList<EnergyTransferItem> energyTransferItems = new ArrayList<>();

    private int transferRate;
    private EnergyItem direction;

    public EnergyTransferItem(Block block, int maxEnergy, EnergyItem direction) {
        super(block, maxEnergy);
        this.direction = direction;
        energyTransferItems.add(this);
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
