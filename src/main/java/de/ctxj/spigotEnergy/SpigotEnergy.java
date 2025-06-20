package de.ctxj.spigotEnergy;

import de.ctxj.spigotEnergy.holographs.Holograph;
import de.ctxj.spigotEnergy.holographs.HolographListener;
import de.ctxj.spigotEnergy.listeners.BasicItemsListener;
import de.ctxj.spigotEnergy.listeners.EnergyItemStackListener;
import de.ctxj.spigotEnergy.listeners.TestListener;
import de.ctxj.spigotEnergy.objects.abstr.Consumer;
import de.ctxj.spigotEnergy.objects.abstr.EnergyItem;
import de.ctxj.spigotEnergy.objects.abstr.EnergyTransferItem;
import de.ctxj.spigotEnergy.objects.abstr.Generator;
import de.ctxj.spigotEnergy.util.EnergyItemManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.entity.ItemMergeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.atomic.AtomicInteger;

public final class SpigotEnergy extends JavaPlugin {


    public static ItemStack defaultConsumeItem = new ItemStack(Material.DIAMOND, 1);
    public static ItemStack defaultEditItem = new ItemStack(Material.STICK);
    private static SpigotEnergy instance;
    private static EnergyItemManager energyItemManager;

    //Test
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        setupConfig();
        setupCommands();
        setupListeners();
        energyItemManager = new EnergyItemManager();
        energyItemManager.initialize();

        ItemMeta meta = defaultEditItem.getItemMeta();
        assert meta != null;
        meta.setDisplayName("§e§lENERGIE-EDITOR");
        defaultEditItem.setItemMeta(meta);

        AtomicInteger configSafeCounter = new AtomicInteger();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            //autosave config (every 5 mins)
            if(configSafeCounter.get() >= 300) {
                for(EnergyItem item : energyItemManager.activeItems) {
                    item.cfgUpdateEnergy();
                    if(item instanceof EnergyTransferItem) {
                        ((EnergyTransferItem) item).cfgUpdateDirection();
                    }
                }
                configSafeCounter.set(0);
            } else {
                configSafeCounter.getAndIncrement();
            }

            for(EnergyItem item : energyItemManager.activeItems) {
                if(item instanceof Consumer) {
                    ((Consumer)item).consume();
                } else if(item instanceof EnergyTransferItem) {
                    ((EnergyTransferItem) item).transferEnergy();
                    if(item instanceof Generator) {
                        ((Generator)item).generate();
                    }

                    //TODO: remove debugging-line
                    Bukkit.getConsoleSender().sendMessage(item.toString() + " | E=" + item.getEnergy());
                }
            }
        }, 20, 40);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for(EnergyItem item : energyItemManager.activeItems) {
            item.cfgUpdateEnergy();
            if(item instanceof EnergyTransferItem) {
                ((EnergyTransferItem) item).cfgUpdateDirection();
            }
        }
        for(Holograph h : Holograph.holographs) {
            h.remove();
        }
    }

    public static SpigotEnergy getInstance() {
        return instance;
    }

    public void setupCommands() {
        Bukkit.getPluginCommand("energyitem").setExecutor(new de.ctxj.spigotEnergy.cmds.EnergyItemStackCommand());
    }

    public void setupListeners() {
        PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents(new BasicItemsListener(), this);
        manager.registerEvents(new EnergyItemStackListener(), this);
        manager.registerEvents(new TestListener(), this);
        manager.registerEvents(new HolographListener(), this);
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
