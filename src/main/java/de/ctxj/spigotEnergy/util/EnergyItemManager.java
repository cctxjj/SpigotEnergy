package de.ctxj.spigotEnergy.util;

import de.ctxj.spigotEnergy.SpigotEnergy;
import de.ctxj.spigotEnergy.objects.abstr.*;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class EnergyItemManager extends FileManager {

    String cablePath = "items.generators";
    String storagePath = "items.generators";
    String consumerPath = "items.generators";

    public ArrayList<EnergyItem> activeItems = new ArrayList<>();
    public ArrayList<EnergyItem> activeTransferItems = new ArrayList<>();

    public EnergyItemManager() {
        super(SpigotEnergy.getInstance().getItemFilePath());
    }

    public void initialize() {
        HashMap<Generator, Block> generators = Generator.initializeGenerators();
        HashMap<Cable, Block> cables = Cable.initializeCables();
        HashMap<Storage, Block> storages = Storage.initializeStorages();
        HashMap<Consumer, Block> consumers = Consumer.initializeConsumers();

        Set<Cable> cableKeyset = cables.keySet();
        Set<Storage> storageKeyset = storages.keySet();
        Set<Consumer> consumerKeyset = consumers.keySet();

        ArrayList<EnergyItem> items = new ArrayList<>();

        items.addAll(cableKeyset);
        items.addAll(storageKeyset);
        items.addAll(consumerKeyset);

        generators.forEach((generator, dirBlock) -> {
            items.forEach(item -> {
                if(item.getBlock() == dirBlock) {
                    generator.setDirection(item);
                }
            });
        });

        cables.forEach((cable, dirBlock) -> {
            items.forEach(item -> {
                if(item.getBlock() == dirBlock) {
                    cable.setDirection(item);
                }
            });
        });

        storages.forEach((storage, dirBlock) -> {
            items.forEach(item -> {
                if(item.getBlock() == dirBlock) {
                    storage.setDirection(item);
                }
            });
        });

        items.addAll(generators.keySet());

        this.activeItems = items;
        for(EnergyItem item : activeItems) {
            if(item instanceof EnergyTransferItem) {
                this.activeTransferItems.add(item);
            }
        }
    }

    public void registerEnergyItem(EnergyItem item) {
        item.cfgRegister();
        this.activeItems.add(item);
        if(item instanceof EnergyTransferItem) {
            this.activeTransferItems.add(item);
        }
    }

    public void deleteEnergyItem(EnergyItem item) {
        item.cfgRemove();
        this.activeItems.remove(item);
        if(item instanceof EnergyTransferItem) {
            this.activeTransferItems.remove(item);
        }
    }
}
