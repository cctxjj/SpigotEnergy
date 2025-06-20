package de.ctxj.spigotEnergy.objects.abstr;

import de.ctxj.spigotEnergy.SpigotEnergy;
import de.ctxj.spigotEnergy.holographs.Holograph;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Generator extends EnergyTransferItem {

    static String generatorPath = "generators.";
    private final ItemStack consumeItem;
    private final int outputRate;
    private final Inventory inventory;

    private final String name;

    private final Holograph holograph;

    public Generator(Block block, int maxEnergy, EnergyItem direction, int transferRate, ItemStack consumeItem, int outputRate, String name) {
        super(block, maxEnergy, direction, transferRate);
        this.consumeItem = consumeItem;
        this.outputRate = outputRate;
        this.name = name;
        inventory = Bukkit.createInventory(null, 9, name);
        this.holograph = new Holograph(block.getLocation().add(0, 1, 0), name + "§8§l| §a§l" + getEnergy());
    }

    protected Generator(Block block, int maxEnergy, EnergyItem direction, int transferRate, ItemStack consumeItem, int outputRate, int energy, String name) {
        super(block, maxEnergy, direction, transferRate);
        this.consumeItem = consumeItem;
        this.outputRate = outputRate;
        this.setEnergy(energy);
        this.name = name;
        inventory = Bukkit.createInventory(null, 9, name);
        this.holograph = new Holograph(block.getLocation().add(0, 1, 0), name + "§8§l| §a§l" + getEnergy());
    }
    //return if generation was successful
    public boolean generate() {
        if(getEnergy() + outputRate > getMaxEnergy()) {
            return false;
        }
        if(inventory.contains(consumeItem.getType())) {
            for(ItemStack item : inventory.getContents()) {
                if(item == null) {
                    continue;
                }
                if(item.getType() == Material.AIR) {
                    continue;
                }
                ItemStack cloned = item.clone();
                cloned.setAmount(consumeItem.getAmount());
                if(cloned.equals(consumeItem) && item.getAmount() >= consumeItem.getAmount()) {
                    item.setAmount(item.getAmount() - consumeItem.getAmount());
                    setEnergy(getEnergy() + outputRate);
                    return true;
                }
            }
        }
        return false;
    }

    public void cfgUpdateEnergy() {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        config.set(generatorPath + "." + getCurrentGeneratorNum(this) + ".energy", this.getEnergy());
        SpigotEnergy.getEnergyItemManager().save();
    }

    public void cfgUpdateDirection() {
        if(this.getDirection() != null) {
            FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
            int n = getCurrentGeneratorNum(this);
            config.set(generatorPath + "." + n + ".direction.world", Objects.requireNonNull(this.getDirection().getBlock().getLocation().getWorld()).getName());
            config.set(generatorPath + "." + n + ".direction.x", this.getDirection().getBlock().getLocation().getX());
            config.set(generatorPath + "." + n + ".direction.y", this.getDirection().getBlock().getLocation().getY());
            config.set(generatorPath + "." + n + ".direction.z", this.getDirection().getBlock().getLocation().getZ());
            SpigotEnergy.getEnergyItemManager().save();
        }
    }

    public void cfgRegister() {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int n = 0;
        while(config.contains(generatorPath + "." + n + ".position.world")) {
            if(Objects.requireNonNull(config.getString(generatorPath + "." + n + ".position.world")).equalsIgnoreCase("inactive_item")) {
                break;
            } else {
                n++;
            }
        }

        config.set(generatorPath + "." + n + ".position.world", Objects.requireNonNull(this.getBlock().getLocation().getWorld()).getName());
        config.set(generatorPath + "." + n + ".position.x", this.getBlock().getLocation().getX());
        config.set(generatorPath + "." + n + ".position.y", this.getBlock().getLocation().getY());
        config.set(generatorPath + "." + n + ".position.z", this.getBlock().getLocation().getZ());

        if(this.getDirection() != null) {
            config.set(generatorPath + "." + n + ".direction.world", Objects.requireNonNull(this.getDirection().getBlock().getLocation().getWorld()).getName());
            config.set(generatorPath + "." + n + ".direction.x", this.getDirection().getBlock().getLocation().getX());
            config.set(generatorPath + "." + n + ".direction.y", this.getDirection().getBlock().getLocation().getY());
            config.set(generatorPath + "." + n + ".direction.z", this.getDirection().getBlock().getLocation().getZ());
        }

        config.set(generatorPath + "." + n + ".name", this.getName());
        config.set(generatorPath + "." + n + ".maxEnergy", this.getMaxEnergy());
        config.set(generatorPath + "." + n + ".transferRate", this.getTransferRate());
        config.set(generatorPath + "." + n + ".outputRate", this.outputRate);
        config.set(generatorPath + "." + n + ".consumeItem", this.consumeItem);
        config.set(generatorPath + "." + n + ".energy", this.getEnergy());

        SpigotEnergy.getEnergyItemManager().save();
    }


    public void cfgRemove() {
        int number = getCurrentGeneratorNum(this);
        if (number == -1) return;
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        config.set(generatorPath + "." + number + ".position.world", "inactive_item");
        while(config.contains(generatorPath + "." + (number + 1) + ".position.world") && !Objects.requireNonNull(config.getString(generatorPath + "." + (number + 1) + ".position.world")).equalsIgnoreCase("inactive_item")) {
            int num = number + 1;

            World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(generatorPath + "." + num + ".position.world")));
            int x = config.getInt(generatorPath + "." + num + ".position.x");
            int y = config.getInt(generatorPath + "." + num + ".position.y");
            int z = config.getInt(generatorPath + "." + num + ".position.z");

            if(config.contains(generatorPath + "." + num + ".direction.world")) {
                World dirWorld = Bukkit.getWorld(Objects.requireNonNull(config.getString(generatorPath + "." + num + ".direction.world")));
                int dirX = config.getInt(generatorPath + "." + num + ".direction.x");
                int dirY = config.getInt(generatorPath + "." + num + ".direction.y");
                int dirZ = config.getInt(generatorPath + "." + num + ".direction.z");

                config.set(generatorPath + "." + number + ".direction.world", dirWorld.getName());
                config.set(generatorPath + "." + number + ".direction.x", dirX);
                config.set(generatorPath + "." + number + ".direction.y", dirY);
                config.set(generatorPath + "." + number + ".direction.z", dirZ);
            }

            String name = config.getString(generatorPath + "." + num + ".name");
            int maxEnergy = config.getInt(generatorPath + "." + num + ".maxEnergy");
            int energy = config.getInt(generatorPath + "." + num + ".energy");
            int transferRate = config.getInt(generatorPath + "." + num + ".transferRate");
            int outputRate = config.getInt(generatorPath + "." + num + ".outputRate");
            ItemStack consumeItem = config.getItemStack(generatorPath + "." + num + ".consumeItem");

            assert world != null;
            config.set(generatorPath + "." + number + ".position.world", world.getName());
            config.set(generatorPath + "." + number + ".position.x", x);
            config.set(generatorPath + "." + number + ".position.y", y);
            config.set(generatorPath + "." + number + ".position.z", z);


            config.set(generatorPath + "." + number + ".name", name);
            config.set(generatorPath + "." + number + ".maxEnergy", maxEnergy);
            config.set(generatorPath + "." + number + ".energy", energy);
            config.set(generatorPath + "." + number + ".transferRate", transferRate);
            config.set(generatorPath + "." + number + ".consumeItem", consumeItem);
            config.set(generatorPath + "." + number + ".outputRate", outputRate);

            config.set(generatorPath + "." + num + ".position.world", "inactive_item");
            number++;
        }
        SpigotEnergy.getEnergyItemManager().save();

    }

    public static int getCurrentGeneratorNum(Generator generator) {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int num = 0;
        while(config.contains(generatorPath + "." + num + ".position.world")) {
            if(generator.getBlock().getWorld() == Bukkit.getWorld(Objects.requireNonNull(config.getString(generatorPath + "." + num + ".position.world")))
                    && generator.getBlock().getX() == config.getInt(generatorPath + "." + num + ".position.x")
                    && generator.getBlock().getY() == config.getInt(generatorPath + "." + num + ".position.y")
                    && generator.getBlock().getZ() == config.getInt(generatorPath + "." + num + ".position.z")) {
                return num;
            }
            num++;
        }
        return -1;
    }

    public static HashMap<Generator, Block> initializeGenerators() {
        AtomicReference<HashMap<Generator, Block>> generatorReference = new AtomicReference<>(new HashMap<>());
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int num = 0;
        while(config.contains(generatorPath + "." + num + ".") && !Objects.requireNonNull(config.getString(generatorPath + "." + num + ".position.world")).equalsIgnoreCase("inactive_item")) {
            World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(generatorPath + "." + num + ".position.world")));
            int x = config.getInt(generatorPath + "." + num + ".position.x");
            int y = config.getInt(generatorPath + "." + num + ".position.y");
            int z = config.getInt(generatorPath + "." + num + ".position.z");
            assert world != null;
            Block block = world.getBlockAt(x, y, z);

            Block output = null;
            if(config.contains(generatorPath + "." + num + ".direction.")) {

                World dirWorld = Bukkit.getWorld(Objects.requireNonNull(config.getString(generatorPath + "." + num + ".direction.world")));
                int dirX = config.getInt(generatorPath + "." + num + ".direction.x");
                int dirY = config.getInt(generatorPath + "." + num + ".direction.y");
                int dirZ = config.getInt(generatorPath + "." + num + ".direction.z");
                assert dirWorld != null;
                output = dirWorld.getBlockAt(dirX, dirY, dirZ);
            }

            String name = config.getString(generatorPath + "." + num + ".name");
            int maxEnergy = config.getInt(generatorPath + "." + num + ".maxEnergy");
            int energy = config.getInt(generatorPath + "." + num + ".energy");
            int transferRate = config.getInt(generatorPath + "." + num + ".transferRate");
            int outputRate = config.getInt(generatorPath + "." + num + ".outputRate");
            ItemStack consumeItem = config.getItemStack(generatorPath + "." + num + ".consumeItem");

            generatorReference.get().put(new Generator(block, maxEnergy, null, transferRate, consumeItem, outputRate, energy, name), output);
            num++;
        }
        return generatorReference.get();
    }

    @Override
    public void setEnergy(int energy) {
        super.setEnergy(energy);
        holograph.setText(name + "§8§l| §a§l" + getEnergy());
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ItemStack getConsumeItem() {
        return consumeItem;
    }

    public int getOutputRate() {
        return outputRate;
    }

    public String getName() {
        return name;
    }

    public Holograph getHolograph() {
        return holograph;
    }
}
