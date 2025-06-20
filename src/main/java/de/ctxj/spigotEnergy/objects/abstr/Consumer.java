package de.ctxj.spigotEnergy.objects.abstr;

import de.ctxj.spigotEnergy.SpigotEnergy;
import de.ctxj.spigotEnergy.events.ConsumerTriggerEvent;
import de.ctxj.spigotEnergy.holographs.Holograph;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class Consumer extends EnergyItem {
   
    static String consumerPath = "consumers.";

    private final String name;

    private final Holograph holograph;
   
    public Consumer(Block block, int maxEnergy, String name) {
        super(block, maxEnergy);
        this.name = name;
        this.holograph = new Holograph(block.getLocation().add(0, 1, 0), name + "§8§l| §a§l" + getEnergy());
    }

    protected Consumer(Block block, int maxEnergy, int energy, String name) {
        super(block, maxEnergy);
        this.name = name;
        setEnergy(energy);
        this.holograph = new Holograph(block.getLocation().add(0, 1, 0), name + "§8§l| §a§l" + getEnergy());
    }

    public void consume() {
        ConsumerTriggerEvent newEvent = new ConsumerTriggerEvent(this);
        Bukkit.getPluginManager().callEvent(newEvent);
    }


    public void cfgUpdateEnergy() {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        config.set(consumerPath + "." + getCurrentConsumerNum(this) + ".energy", this.getEnergy());
        SpigotEnergy.getEnergyItemManager().save();
    }

    @Override
    public void cfgRegister() {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int n = 0;
        while(config.contains(consumerPath + "." + n + ".position.world")) {
            if(Objects.requireNonNull(config.getString(consumerPath + "." + n + ".position.world")).equalsIgnoreCase("inactive_item")) {
                break;
            } else {
                n++;
            }
        }

        config.set(consumerPath + "." + n + ".position.world", Objects.requireNonNull(this.getBlock().getLocation().getWorld()).getName());
        config.set(consumerPath + "." + n + ".position.x", this.getBlock().getLocation().getX());
        config.set(consumerPath + "." + n + ".position.y", this.getBlock().getLocation().getY());
        config.set(consumerPath + "." + n + ".position.z", this.getBlock().getLocation().getZ());

        config.set(consumerPath + "." + n + ".name", name);
        config.set(consumerPath + "." + n + ".maxEnergy", this.getMaxEnergy());
        config.set(consumerPath + "." + n + ".energy", this.getEnergy());

        SpigotEnergy.getEnergyItemManager().save();
    }

    @Override
    public void cfgRemove() {
        int number = getCurrentConsumerNum(this);
        if (number == -1) return;
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        config.set(consumerPath + "." + number + ".position.world", "inactive_item");
        while(config.contains(consumerPath + "." + (number + 1) + ".position.world") && !Objects.requireNonNull(config.getString(consumerPath + "." + (number + 1) + ".position.world")).equalsIgnoreCase("inactive_item")) {
            int num = number + 1;
            World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(consumerPath + "." + num + ".position.world")));
            int x = config.getInt(consumerPath + "." + num + ".position.x");
            int y = config.getInt(consumerPath + "." + num + ".position.y");
            int z = config.getInt(consumerPath + "." + num + ".position.z");

            if(config.contains(consumerPath + "." + num + ".direction.world")) {
                World dirWorld = Bukkit.getWorld(Objects.requireNonNull(config.getString(consumerPath + "." + num + ".direction.world")));
                int dirX = config.getInt(consumerPath + "." + num + ".direction.x");
                int dirY = config.getInt(consumerPath + "." + num + ".direction.y");
                int dirZ = config.getInt(consumerPath + "." + num + ".direction.z");

                config.set(consumerPath + "." + number + ".direction.world", dirWorld.getName());
                config.set(consumerPath + "." + number + ".direction.x", dirX);
                config.set(consumerPath + "." + number + ".direction.y", dirY);
                config.set(consumerPath + "." + number + ".direction.z", dirZ);
            }

            String name = config.getString(consumerPath + "." + num + ".name");
            int maxEnergy = config.getInt(consumerPath + "." + num + ".maxEnergy");
            int energy = config.getInt(consumerPath + "." + num + ".energy");
            int transferRate = config.getInt(consumerPath + "." + num + ".transferRate");

            assert world != null;
            config.set(consumerPath + "." + number + ".position.world", world.getName());
            config.set(consumerPath + "." + number + ".position.x", x);
            config.set(consumerPath + "." + number + ".position.y", y);
            config.set(consumerPath + "." + number + ".position.z", z);

            config.set(consumerPath + "." + number + ".name", name);
            config.set(consumerPath + "." + number + ".maxEnergy", maxEnergy);
            config.set(consumerPath + "." + number + ".energy", energy);
            config.set(consumerPath + "." + number + ".transferRate", transferRate);

            config.set(consumerPath + "." + num + ".position.world", "inactive_item");
            number++;
        }
        SpigotEnergy.getEnergyItemManager().save();

    }

    public static int getCurrentConsumerNum(Consumer Consumer) {
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int num = 0;
        while(config.contains(consumerPath + "." + num + ".position.world")) {
            if(Consumer.getBlock().getWorld() == Bukkit.getWorld(Objects.requireNonNull(config.getString(consumerPath + "." + num + ".position.world")))
                    && Consumer.getBlock().getX() == config.getInt(consumerPath + "." + num + ".position.x")
                    && Consumer.getBlock().getY() == config.getInt(consumerPath + "." + num + ".position.y")
                    && Consumer.getBlock().getZ() == config.getInt(consumerPath + "." + num + ".position.z")) {
                return num;
            }
            num++;
        }
        return -1;
    }

    public static HashMap<Consumer, Block> initializeConsumers() {
        AtomicReference<HashMap<Consumer, Block>> ConsumerReference = new AtomicReference<>(new HashMap<>());
        FileConfiguration config = SpigotEnergy.getEnergyItemManager().getFileConfiguration();
        int num = 0;
        while(config.contains(consumerPath + "." + num + ".") && !Objects.requireNonNull(config.getString(consumerPath + "." + num + ".position.world")).equalsIgnoreCase("inactive_item")) {
            World world = Bukkit.getWorld(Objects.requireNonNull(config.getString(consumerPath + "." + num + ".position.world")));
            int x = config.getInt(consumerPath + "." + num + ".position.x");
            int y = config.getInt(consumerPath + "." + num + ".position.y");
            int z = config.getInt(consumerPath + "." + num + ".position.z");
            assert world != null;
            Block block = world.getBlockAt(x, y, z);

            Block output = null;
            if(config.contains(consumerPath + "." + num + ".direction.world")) {
                World dirWorld = Bukkit.getWorld(Objects.requireNonNull(config.getString(consumerPath + "." + num + ".direction.world")));
                int dirX = config.getInt(consumerPath + "." + num + ".direction.x");
                int dirY = config.getInt(consumerPath + "." + num + ".direction.y");
                int dirZ = config.getInt(consumerPath + "." + num + ".direction.z");
                output = dirWorld.getBlockAt(dirX, dirY, dirZ);
            }

            String name = config.getString(consumerPath + "." + num + ".name");
            int maxEnergy = config.getInt(consumerPath + "." + num + ".maxEnergy");
            int energy = config.getInt(consumerPath + "." + num + ".energy");

            ConsumerReference.get().put(new Consumer(block, maxEnergy, energy, name), output);
            num++;
        }

        return ConsumerReference.get();
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
