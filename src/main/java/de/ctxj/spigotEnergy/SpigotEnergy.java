package de.ctxj.spigotEnergy;

import de.ctxj.spigotEnergy.listeners.BasicGeneratorListener;
import de.ctxj.spigotEnergy.objects.abstr.EnergyItem;
import de.ctxj.spigotEnergy.objects.abstr.EnergyTransferItem;
import de.ctxj.spigotEnergy.objects.abstr.Generator;
import de.ctxj.spigotEnergy.util.EnergyItemManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class SpigotEnergy extends JavaPlugin {

    private static SpigotEnergy instance;
    private static EnergyItemManager energyItemManager;


    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        setupConfig();
        setupCommands();
        setupListeners();
        energyItemManager = new EnergyItemManager();
        energyItemManager.initialize();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            for(EnergyItem item : energyItemManager.activeItems) {
                Bukkit.getConsoleSender().sendMessage(item.toString());
                if(item instanceof EnergyTransferItem) {
                    ((EnergyTransferItem) item).transferEnergy();
                    if(item instanceof Generator) {
                        ((Generator)item).generate();
                    }
                }
            }
        }, 20, 40);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(EnergyItem item : energyItemManager.activeItems) {
            item.cfgUpdateEnergy();
        }
    }

    public static SpigotEnergy getInstance() {
        return instance;
    }

    public void setupCommands() {

    }

    public void setupListeners() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new BasicGeneratorListener(), this);
    }

    public void setupConfig() {
        getConfig().options().copyDefaults(true);
        getConfig().addDefault("itemFile", "plugins/SpigotEnergy/items.yml");
        saveConfig();
    }

    public static EnergyItemManager getEnergyItemManager() {
        return energyItemManager;
    }

    public String getItemFilePath() {
        return getConfig().getString("itemFile");
    }
}
