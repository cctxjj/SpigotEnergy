package de.ctxj.spigotEnergy.objects.abstr;

import de.ctxj.spigotEnergy.SpigotEnergy;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Cable extends EnergyTransferItem {
    
    static String cablePath = "cables";
    
    public Cable(Block block, int maxEnergy, EnergyItem direction, int transferRate) {
        super(block, maxEnergy, direction, transferRate);
    }

    protected Cable(Block block, int maxEnergy, EnergyItem direction, int transferRate, int energy) {
        super(block, maxEnergy, direction, transferRate);
        setEnergy(energy);
    }


    public void cfgUpdateEnergy() {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        config.set(cablePath + "." + getCurrentCableNum(this) + ".energy", this.getEnergy());
        SpigotEnergy.getEnergyItemManager().save();
    }

    public void cfgUpdateDirection() {
        if(this.getDirection() != null) {
            FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
            int n = getCurrentCableNum(this);
            config.set(cablePath + "." + n + ".direction.world", Objects.requireNonNull(this.getDirection().getBlock().getLocation().getWorld()).getName());
            config.set(cablePath + "." + n + ".direction.x", this.getDirection().getBlock().getLocation().getX());
            config.set(cablePath + "." + n + ".direction.y", this.getDirection().getBlock().getLocation().getY());
            config.set(cablePath + "." + n + ".direction.z", this.getDirection().getBlock().getLocation().getZ());
            SpigotEnergy.getEnergyItemManager().save();
        }
    }

    public void cfgRegister() {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int n = 0;

        while(config.contains(cablePath + "." + n + ".position.world")) {
            if(Objects.requireNonNull(config.getString(cablePath + "." + n + ".position.world")).equalsIgnoreCase("inactive_item")) {
                break;
            } else {
                n++;
            }
        }

        config.set(cablePath + "." + n + ".position.world", Objects.requireNonNull(this.getBlock().getLocation().getWorld()).getName());
        config.set(cablePath + "." + n + ".position.x", this.getBlock().getLocation().getX());
        config.set(cablePath + "." + n + ".position.y", this.getBlock().getLocation().getY());
        config.set(cablePath + "." + n + ".position.z", this.getBlock().getLocation().getZ());

        if(this.getDirection() != null) {
            config.set(cablePath + "." + n + ".direction.world", Objects.requireNonNull(this.getDirection().getBlock().getLocation().getWorld()).getName());
            config.set(cablePath + "." + n + ".direction.x", this.getDirection().getBlock().getLocation().getX());
            config.set(cablePath + "." + n + ".direction.y", this.getDirection().getBlock().getLocation().getY());
            config.set(cablePath + "." + n + ".direction.z", this.getDirection().getBlock().getLocation().getZ());
        }


        config.set(cablePath + "." + n + ".maxEnergy", this.getMaxEnergy());
        config.set(cablePath + "." + n + ".energy", this.getEnergy());
        config.set(cablePath + "." + n + ".transferRate", this.getTransferRate());

        SpigotEnergy.getEnergyItemManager().save();
    }


    public void cfgRemove() {
        int number = getCurrentCableNum(this);
        if (number == -1) return;
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        config.set(cablePath + "." + number + ".position.world", "inactive_item");
        while(config.contains(cablePath + "." + (number + 1) + ".position.world") && !Objects.requireNonNull(config.getString(cablePath + "." + (number + 1) + ".position.world")).equalsIgnoreCase("inactive_item")) {
            int num = number + 1;
            World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(cablePath + "." + num + ".position.world")));
            int x = config.getInt(cablePath + "." + num + ".position.x");
            int y = config.getInt(cablePath + "." + num + ".position.y");
            int z = config.getInt(cablePath + "." + num + ".position.z");

            if(config.contains(cablePath + "." + num + ".direction.world")) {
                World dirWorld = Bukkit.getWorld(Objects.requireNonNull(config.getString(cablePath + "." + num + ".direction.world")));
                int dirX = config.getInt(cablePath + "." + num + ".direction.x");
                int dirY = config.getInt(cablePath + "." + num + ".direction.y");
                int dirZ = config.getInt(cablePath + "." + num + ".direction.z");

                config.set(cablePath + "." + number + ".direction.world", dirWorld.getName());
                config.set(cablePath + "." + number + ".direction.x", dirX);
                config.set(cablePath + "." + number + ".direction.y", dirY);
                config.set(cablePath + "." + number + ".direction.z", dirZ);
            }

            int maxEnergy = config.getInt(cablePath + "." + num + ".maxEnergy");
            int energy = config.getInt(cablePath + "." + num + ".energy");
            int transferRate = config.getInt(cablePath + "." + num + ".transferRate");

            assert world != null;
            config.set(cablePath + "." + number + ".position.world", world.getName());
            config.set(cablePath + "." + number + ".position.x", x);
            config.set(cablePath + "." + number + ".position.y", y);
            config.set(cablePath + "." + number + ".position.z", z);

            config.set(cablePath + "." + number + ".maxEnergy", maxEnergy);
            config.set(cablePath + "." + number + ".energy", energy);
            config.set(cablePath + "." + number + ".transferRate", transferRate);

            config.set(cablePath + "." + num + ".position.world", "inactive_item");
            number++;
        }
        SpigotEnergy.getEnergyItemManager().save();

    }

    public static int getCurrentCableNum(Cable cable) {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int num = 0;
        while(config.contains(cablePath + "." + num + ".position.world")) {
            if(cable.getBlock().getWorld() == Bukkit.getWorld(Objects.requireNonNull(config.getString(cablePath + "." + num + ".position.world")))
                    && cable.getBlock().getX() == config.getInt(cablePath + "." + num + ".position.x")
                    && cable.getBlock().getY() == config.getInt(cablePath + "." + num + ".position.y")
                    && cable.getBlock().getZ() == config.getInt(cablePath + "." + num + ".position.z")) {
                return num;
            }
            num++;
        }
        return -1;
    }

    //TODO next: Adapt to all items
    public static HashMap<Cable, Block> initializeCables() {
        AtomicReference<HashMap<Cable, Block>> cableReference = new AtomicReference<>(new HashMap<>());
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int num = 0;
        while(config.contains(cablePath + "." + num + ".") && !Objects.requireNonNull(config.getString(cablePath + "." + num + ".position.world")).equalsIgnoreCase("inactive_item")) {
            World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(cablePath + "." + num + ".position.world")));
            int x = config.getInt(cablePath + "." + num + ".position.x");
            int y = config.getInt(cablePath + "." + num + ".position.y");
            int z = config.getInt(cablePath + "." + num + ".position.z");
            assert world != null;
            Block block = world.getBlockAt(x, y, z);

            Block output = null;
            if(config.contains(cablePath + "." + num + ".direction.world")) {
                World dirWorld = Bukkit.getWorld(Objects.requireNonNull(config.getString(cablePath + "." + num + ".direction.world")));
                int dirX = config.getInt(cablePath + "." + num + ".direction.x");
                int dirY = config.getInt(cablePath + "." + num + ".direction.y");
                int dirZ = config.getInt(cablePath + "." + num + ".direction.z");
                output = dirWorld.getBlockAt(dirX, dirY, dirZ);
            }

            int maxEnergy = config.getInt(cablePath + "." + num + ".maxEnergy");
            int energy = config.getInt(cablePath + "." + num + ".energy");
            int transferRate = config.getInt(cablePath + "." + num + ".transferRate");

            cableReference.get().put(new Cable(block, maxEnergy, null, transferRate, energy), output);
            num++;
        }

        return cableReference.get();
    }

}
