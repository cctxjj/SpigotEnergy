package de.ctxj.spigotEnergy.objects.abstr;

import de.ctxj.spigotEnergy.SpigotEnergy;
import de.ctxj.spigotEnergy.holographs.Holograph;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Storage extends EnergyTransferItem {
    
    static String storagePath = "storages.";

    private final String name;

    private final Holograph holograph;
    
    public Storage(Block block, int maxEnergy, EnergyItem direction, int transferRate, String name) {
        super(block, maxEnergy, direction, transferRate);
        this.name = name;
        this.holograph = new Holograph(block.getLocation().add(0, 1, 0), name + "§8§l| §a§l" + getEnergy());
    }

    protected Storage(Block block, int maxEnergy, EnergyItem direction, int transferRate, int energy, String name) {
        super(block, maxEnergy, direction, transferRate);
        this.name = name;
        setEnergy(energy);
        this.holograph = new Holograph(block.getLocation().add(0, 1, 0), name + "§8§l| §a§l" + getEnergy());
    }

    public void cfgUpdateEnergy() {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        config.set(storagePath + "." + getCurrentStorageNum(this) + ".energy", this.getEnergy());
        SpigotEnergy.getEnergyItemManager().save();
    }

    public void cfgUpdateDirection() {
        if(this.getDirection() != null) {
            FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
            int n = getCurrentStorageNum(this);
            config.set(storagePath + "." + n + ".direction.world", Objects.requireNonNull(this.getDirection().getBlock().getLocation().getWorld()).getName());
            config.set(storagePath + "." + n + ".direction.x", this.getDirection().getBlock().getLocation().getX());
            config.set(storagePath + "." + n + ".direction.y", this.getDirection().getBlock().getLocation().getY());
            config.set(storagePath + "." + n + ".direction.z", this.getDirection().getBlock().getLocation().getZ());
            SpigotEnergy.getEnergyItemManager().save();
        }
    }

    public void cfgRegister() {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int n = 0;
        while(config.contains(storagePath + "." + n + ".position.world")) {
            if(Objects.requireNonNull(config.getString(storagePath + "." + n + ".position.world")).equalsIgnoreCase("inactive_item")) {
                break;
            } else {
                n++;
            }
        }

        config.set(storagePath + "." + n + ".position.world", Objects.requireNonNull(this.getBlock().getLocation().getWorld()).getName());
        config.set(storagePath + "." + n + ".position.x", this.getBlock().getLocation().getX());
        config.set(storagePath + "." + n + ".position.y", this.getBlock().getLocation().getY());
        config.set(storagePath + "." + n + ".position.z", this.getBlock().getLocation().getZ());

        if(this.getDirection() != null) {
            config.set(storagePath + "." + n + ".direction.world", Objects.requireNonNull(this.getDirection().getBlock().getLocation().getWorld()).getName());
            config.set(storagePath + "." + n + ".direction.x", this.getDirection().getBlock().getLocation().getX());
            config.set(storagePath + "." + n + ".direction.y", this.getDirection().getBlock().getLocation().getY());
            config.set(storagePath + "." + n + ".direction.z", this.getDirection().getBlock().getLocation().getZ());
        }

        config.set(storagePath + "." + n + ".name", name);
        config.set(storagePath + "." + n + ".maxEnergy", this.getMaxEnergy());
        config.set(storagePath + "." + n + ".energy", this.getEnergy());
        config.set(storagePath + "." + n + ".transferRate", this.getTransferRate());

        SpigotEnergy.getEnergyItemManager().save();
    }


    public void cfgRemove() {
        int number = getCurrentStorageNum(this);
        if (number == -1) return;
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        config.set(storagePath + "." + number + ".position.world", "inactive_item");
        while(config.contains(storagePath + "." + (number + 1) + ".position.world") && !Objects.requireNonNull(config.getString(storagePath + "." + (number + 1) + ".position.world")).equalsIgnoreCase("inactive_item")) {
            int num = number + 1;
            World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(storagePath + "." + num + ".position.world")));
            int x = config.getInt(storagePath + "." + num + ".position.x");
            int y = config.getInt(storagePath + "." + num + ".position.y");
            int z = config.getInt(storagePath + "." + num + ".position.z");

            if(config.contains(storagePath + "." + num + ".direction.world")) {
                World dirWorld = Bukkit.getWorld(Objects.requireNonNull(config.getString(storagePath + "." + num + ".direction.world")));
                int dirX = config.getInt(storagePath + "." + num + ".direction.x");
                int dirY = config.getInt(storagePath + "." + num + ".direction.y");
                int dirZ = config.getInt(storagePath + "." + num + ".direction.z");

                config.set(storagePath + "." + number + ".direction.world", dirWorld.getName());
                config.set(storagePath + "." + number + ".direction.x", dirX);
                config.set(storagePath + "." + number + ".direction.y", dirY);
                config.set(storagePath + "." + number + ".direction.z", dirZ);
            }

            String name = config.getString(storagePath + "."+ num + ".name");
            int maxEnergy = config.getInt(storagePath + "." + num + ".maxEnergy");
            int energy = config.getInt(storagePath + "." + num + ".energy");
            int transferRate = config.getInt(storagePath + "." + num + ".transferRate");

            assert world != null;
            config.set(storagePath + "." + number + ".position.world", world.getName());
            config.set(storagePath + "." + number + ".position.x", x);
            config.set(storagePath + "." + number + ".position.y", y);
            config.set(storagePath + "." + number + ".position.z", z);

            config.set(storagePath + "." + number + ".name", name);
            config.set(storagePath + "." + number + ".maxEnergy", maxEnergy);
            config.set(storagePath + "." + number + ".energy", energy);
            config.set(storagePath + "." + number + ".transferRate", transferRate);

            config.set(storagePath + "." + num + ".position.world", "inactive_item");
            number++;
        }
        SpigotEnergy.getEnergyItemManager().save();
    }

    public static int getCurrentStorageNum(Storage Storage) {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int num = 0;
        while(config.contains(storagePath + "." + num + ".position.world")) {
            if(Storage.getBlock().getWorld() == Bukkit.getWorld(Objects.requireNonNull(config.getString(storagePath + "." + num + ".position.world")))
                    && Storage.getBlock().getX() == config.getInt(storagePath + "." + num + ".position.x")
                    && Storage.getBlock().getY() == config.getInt(storagePath + "." + num + ".position.y")
                    && Storage.getBlock().getZ() == config.getInt(storagePath + "." + num + ".position.z")) {
                return num;
            }
            num++;
        }
        return -1;
    }

    public static HashMap<Storage, Block> initializeStorages() {
        AtomicReference<HashMap<Storage, Block>> StorageReference = new AtomicReference<>(new HashMap<>());
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int num = 0;
        while(config.contains(storagePath + "." + num + ".") && !Objects.requireNonNull(config.getString(storagePath + "." + num + ".position.world")).equalsIgnoreCase("inactive_item")) {
            World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(storagePath + "." + num + ".position.world")));
            int x = config.getInt(storagePath + "." + num + ".position.x");
            int y = config.getInt(storagePath + "." + num + ".position.y");
            int z = config.getInt(storagePath + "." + num + ".position.z");
            assert world != null;
            Block block = world.getBlockAt(x, y, z);

            Block output = null;
            if(config.contains(storagePath + "." + num + ".direction.world")) {
                World dirWorld = Bukkit.getWorld(Objects.requireNonNull(config.getString(storagePath + "." + num + ".direction.world")));
                int dirX = config.getInt(storagePath + "." + num + ".direction.x");
                int dirY = config.getInt(storagePath + "." + num + ".direction.y");
                int dirZ = config.getInt(storagePath + "." + num + ".direction.z");
                output = dirWorld.getBlockAt(dirX, dirY, dirZ);
            }

            String name = config.getString(storagePath + "." + num + ".name");
            int maxEnergy = config.getInt(storagePath + "." + num + ".maxEnergy");
            int energy = config.getInt(storagePath + "." + num + ".energy");
            int transferRate = config.getInt(storagePath + "." + num + ".transferRate");

            StorageReference.get().put(new Storage(block, maxEnergy, null, transferRate, energy, name), output);
            num++;
        }

        return StorageReference.get();
    }

    @Override
    public void setEnergy(int energy) {
        super.setEnergy(energy);
        holograph.setText(name + "§8§l| §a§l" + getEnergy());
    }

    public String getName() {
        return name;
    }
}
