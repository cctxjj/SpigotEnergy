package de.ctxj.spigotEnergy.objects.abstr;

import de.ctxj.spigotEnergy.SpigotEnergy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Generator extends EnergyTransferItem {

    static String generatorPath = "generators.";

    public Generator(Block block, int maxEnergy, EnergyItem direction, int transferRate) {
        super(block, maxEnergy, direction, transferRate);
    }

    public static void addGenerator(Generator generator) {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int n = 0;
        while(config.contains(generatorPath + "." + n + ".position.world")) {
            if(!Objects.requireNonNull(config.getString(generatorPath + "." + n + ".position.world")).equalsIgnoreCase("inactive_item")) {
                n++;
            } else {
                return;
            }
        }
        config.set(generatorPath + "." + n + ".position.world", Objects.requireNonNull(generator.getBlock().getLocation().getWorld()).getName());
        config.set(generatorPath + "." + n + ".position.x", generator.getBlock().getLocation().getX());
        config.set(generatorPath + "." + n + ".position.y", generator.getBlock().getLocation().getY());
        config.set(generatorPath + "." + n + ".position.z", generator.getBlock().getLocation().getZ());

        if(generator.getDirection() != null) {
            config.set(generatorPath + "." + n + ".direction.world", Objects.requireNonNull(generator.getDirection().getBlock().getLocation().getWorld()).getName());
            config.set(generatorPath + "." + n + ".direction.x", generator.getDirection().getBlock().getLocation().getX());
            config.set(generatorPath + "." + n + ".direction.y", generator.getDirection().getBlock().getLocation().getY());
            config.set(generatorPath + "." + n + ".direction.z", generator.getDirection().getBlock().getLocation().getZ());
        }

        config.set(generatorPath + "." + n + ".maxEnergy", generator.getMaxEnergy());
        config.set(generatorPath + "." + n + ".transferRate", generator.getTransferRate());

        SpigotEnergy.getEnergyItemManager().save();

    }


    public static void removeGenerator(Generator generator) {
        int number = getCurrentGeneratorNum(generator);
        if (number == -1) return;
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();

        while(config.contains(generatorPath + "." + (number + 1) + ".position.world") && !Objects.requireNonNull(config.getString(generatorPath + "." + (number + 1) + ".position.world")).equalsIgnoreCase("inactive_item")) {
            int num = number + 1;
            Bukkit.getConsoleSender().sendMessage("Removing " + num + " generator");
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

            int maxEnergy = config.getInt(generatorPath + "." + num + ".maxEnergy");
            int transferRate = config.getInt(generatorPath + "." + num + ".transferRate");

            assert world != null;
            config.set(generatorPath + "." + number + ".position.world", world.getName());
            config.set(generatorPath + "." + number + ".position.x", x);
            config.set(generatorPath + "." + number + ".position.y", y);
            config.set(generatorPath + "." + number + ".position.z", z);

            config.set(generatorPath + "." + number + ".maxEnergy", maxEnergy);
            config.set(generatorPath + "." + number + ".transferRate", transferRate);
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

    //TODO next: Adapt to all items
    protected static HashMap<Generator, Block> initializeGenerators() {
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
            if(config.contains(generatorPath + "." + num + ".direction.world")) {
                World dirWorld = Bukkit.getWorld(Objects.requireNonNull(config.getString(generatorPath + "." + num + ".direction.world")));
                int dirX = config.getInt(generatorPath + "." + num + ".direction.x");
                int dirY = config.getInt(generatorPath + "." + num + ".direction.y");
                int dirZ = config.getInt(generatorPath + "." + num + ".direction.z");
                output = dirWorld.getBlockAt(dirX, dirY, dirZ);
            }

            int maxEnergy = config.getInt(generatorPath + "." + num + ".maxEnergy");
            int transferRate = config.getInt(generatorPath + "." + num + ".transferRate");

            generatorReference.get().put(new Generator(block, maxEnergy, null, transferRate), output);
            num++;
        }

        return generatorReference.get();
    }
}
