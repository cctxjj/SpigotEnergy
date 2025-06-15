package de.ctxj.spigotEnergy.objects.abstr;

import de.ctxj.spigotEnergy.SpigotEnergy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EnergyItemStack {

    static String itemStackPath = "validEnergyItems.";

    private final ItemStack item;
    private int energy;

    public EnergyItemStack(ItemStack item) {
        this.item = item;
        this.energy = getEnergyFromItem(item);
        if (energy < 0) {
            throw new IllegalArgumentException("Given ItemStack is not a valid EnergyItemStack.");
        }
        updateLore();
    }

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        updateLore();
    }

    public void addEnergy(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Energy addition must be positive.");
        }
        this.energy += amount;
        updateLore();
    }

    public void removeEnergy(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Energy subtraction must be positive.");
        }
        if (this.energy < amount) {
            throw new IllegalStateException("Unsufficient energy to subtract " + amount + ".");
        }
        this.energy -= amount;
        updateLore();
    }

    public ItemStack getItem() {
        return item;
    }

    private void updateLore() {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            List<String> lore = meta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            } else {
                lore.removeIf(String::isEmpty);
                lore.removeIf(line -> ChatColor.stripColor(line).startsWith("Energy:"));
            }
            List<String> newLore = new ArrayList<>();
            newLore.add(""); // Leerzeile vor der Energy-Zeile
            newLore.add(ChatColor.GREEN + "Energy: " + energy);
            newLore.add(""); // Leerzeile nach der Energy-Zeile
            newLore.addAll(lore); // Bestehende Lore anhängen
            meta.setLore(newLore);
            item.setItemMeta(meta);
        }
    }

    public static boolean isEnergyItemStack(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        ArrayList<String> names = (ArrayList<String>) config.getStringList(itemStackPath + "." + item.getType().name());
        return names.contains(meta.getDisplayName());
    }

    public static int getEnergyFromItem(ItemStack item) {
        if (!isEnergyItemStack(item)) return -1;
        ItemMeta meta = item.getItemMeta();
        if (meta == null || meta.getLore() == null) return 0;
        for (String line : meta.getLore()) {
            if (ChatColor.stripColor(line).startsWith("Energy:")) {
                return Integer.parseInt(ChatColor.stripColor(line).split(": ")[1]);
            }
        }
        return 0;
    }

    public static void addEnergyItemToConfig(ItemStack item) {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        /*
        if(!Objects.requireNonNull(item.getItemMeta()).hasDisplayName()) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RED + item.getType().name());
            item.setItemMeta(meta);
        }
         */

        ArrayList<String> names = new ArrayList<>(Collections.singletonList(item.getItemMeta().getDisplayName()));
        if(config.contains(itemStackPath + "." + item.getType().name())) {
            names.addAll(config.getStringList(itemStackPath + "." + item.getType().name()));
        }
        config.set(itemStackPath + "." + item.getType().name(), names);
        SpigotEnergy.getEnergyItemManager().save();
    }

    public static void removeEnergyItemFromConfig(ItemStack item) {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        /*
        if(!Objects.requireNonNull(item.getItemMeta()).hasDisplayName()) {
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ChatColor.RED + item.getType().name());
            item.setItemMeta(meta);
        }
         */

        if(config.contains(itemStackPath + "." + item.getType().name())) {
            ArrayList<String> names = (ArrayList<String>) config.getStringList(itemStackPath + "." + item.getType().name());
            if(!names.contains(item.getItemMeta().getDisplayName())) {
                throw new NullPointerException("ItemStack not found in config: " + item.getType().name());
            }
            names.remove(item.getItemMeta().getDisplayName());
            config.set(itemStackPath + "." + item.getType().name(), names);
            SpigotEnergy.getEnergyItemManager().save();
        } else {
            throw new NullPointerException("ItemStack not found in config: " + item.getType().name());
        }
    }
}