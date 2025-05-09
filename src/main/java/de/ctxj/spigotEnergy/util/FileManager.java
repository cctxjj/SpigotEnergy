package de.ctxj.spigotEnergy.util;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public abstract class FileManager {

    private final String path;
    private final File file;
    private final FileConfiguration fileConfiguration;

    public FileManager(String path) {
        this.path = path;
        file = new File(path);
        try {
            boolean newFile = file.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    public void save() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPath() {
        return path;
    }


    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public File getFile() {
        return file;
    }


}