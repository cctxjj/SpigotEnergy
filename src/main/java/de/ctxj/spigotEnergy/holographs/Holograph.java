package de.ctxj.spigotEnergy.holographs;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import java.util.ArrayList;
import java.util.Objects;

public class Holograph {

    public static ArrayList<Holograph> holographs = new ArrayList<>();

    private final ArmorStand armorStand;
    private final String text;

    public Holograph(Location location, String text) {
        this.text = text;
        this.armorStand = Objects.requireNonNull(location.getWorld()).spawn(location, ArmorStand.class);
        armorStand.setGravity(false);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(text);
        armorStand.setVisible(false);
        holographs.add(this);
    }

    public void remove() {
        armorStand.remove();
        holographs.remove(this);
    }

    public void setText(String newText) {
        armorStand.setCustomName(newText);
    }

    public void teleport(Location location)  {
        armorStand.teleport(location);
    }

}
